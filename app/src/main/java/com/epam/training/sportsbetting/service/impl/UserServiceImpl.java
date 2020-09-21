package com.epam.training.sportsbetting.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.epam.training.sportsbetting.domain.Outcome;
import com.epam.training.sportsbetting.domain.OutcomeOdd;
import com.epam.training.sportsbetting.domain.Wager;
import com.epam.training.sportsbetting.domain.user.Player;
import com.epam.training.sportsbetting.domain.user.User;
import com.epam.training.sportsbetting.dto.CreateWagerDto;
import com.epam.training.sportsbetting.dto.PlayerDto;
import com.epam.training.sportsbetting.exception.NotEnoughBalanceException;
import com.epam.training.sportsbetting.repository.UserRepository;
import com.epam.training.sportsbetting.service.OutcomeOddService;
import com.epam.training.sportsbetting.service.UserService;
import com.epam.training.sportsbetting.service.WagerService;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WagerService wagerService;
    @Autowired
    private OutcomeOddService outcomeOddService;


    @Override
    public List<User> findAll() {
        return Lists.newArrayList(userRepository.findAll());
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public void registerUser(PlayerDto user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User userToSave = new Player();
        BeanUtils.copyProperties(user, userToSave);
        userRepository.save(userToSave);
        log.info("user registered");
    }

    @Override
    public User obtainCurrentPrincipleUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername());
    }

    @Override
    public void updatePlayerInfo(PlayerDto user) {
        User userToUpdate = findById(user.getId());
        BeanUtils.copyProperties(user, userToUpdate, getNullPropertyNames(user));
        save(userToUpdate);
    }

    @Override
    public PlayerDto convertToPlayerDto(User user){
        PlayerDto playerDto = new PlayerDto();
        BeanUtils.copyProperties(user, playerDto, getNullPropertyNames(user));
        return playerDto;
    }
    /**
     * return names of null-fields
     *
     * @param source
     * @return
     */
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Override
    public void makeWager(CreateWagerDto wagerDto) throws NotEnoughBalanceException {
        Player currentUser = (Player) obtainCurrentPrincipleUser();
        BigDecimal wagerAmount = wagerDto.getAmount();
        BigDecimal playerBalance = currentUser.getBalance();
        OutcomeOdd outcomeOdd = outcomeOddService.findByOutcome(wagerDto.getOutcome());
        if (checkPlayerHasEnoughMoney(wagerAmount, currentUser)) {
            currentUser.setBalance(playerBalance.subtract(wagerAmount));
            Wager wager = populateWager(wagerAmount, currentUser, outcomeOdd);
            wagerService.save(wager);
        } else {
            throw new NotEnoughBalanceException("Sorry bro. Not enough money.", wagerDto);
        }
    }

    @Override
    public List<User> findAllByOutcome(Outcome outcome) {
        return userRepository.findAllByOutcome(outcome);
    }

    private Wager populateWager(BigDecimal wagerAmount, Player currentUser, OutcomeOdd outcomeOdd) {
        Wager wager = new Wager();
        wager.setPlayer(currentUser);
        wager.setOutcomeOdd(outcomeOdd);
        wager.setCurrency(currentUser.getCurrency());
        wager.setAmount(wagerAmount);
        wager.setCreationTime(LocalDateTime.now());
        return wager;
    }

    private boolean checkPlayerHasEnoughMoney(BigDecimal wagerAmount, Player player) {
        return player.getBalance().compareTo(wagerAmount) >= 0;
    }

}
