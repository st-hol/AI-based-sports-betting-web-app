package com.sportbetapp.service.user.impl;

import java.math.BigDecimal;
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
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.sportbetapp.domain.betting.PointsTurnoverStatistic;
import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.user.UserDto;
import com.sportbetapp.repository.user.UserRepository;
import com.sportbetapp.service.betting.PointsTurnoverStatisticService;
import com.sportbetapp.service.betting.WagerService;
import com.sportbetapp.service.user.UserService;
import com.sportbetapp.util.math.BigDecimalUtils;

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
    private PointsTurnoverStatisticService pointsTurnoverStatisticService;

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
    public void registerUser(UserDto user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User userToSave = new User();
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
    public void updateUserInfo(UserDto user) {
        User userToUpdate = findById(user.getId());
        BeanUtils.copyProperties(user, userToUpdate, getNullPropertyNames(user));
        save(userToUpdate);
    }

    @Override
    public UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, getNullPropertyNames(user));
        return userDto;
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


    /**
     * @param winnerUser
     * @param wager
     * @return win amount
     */
    @Override
    public BigDecimal addWinAmountToBalance(User winnerUser, Wager wager) {
        BigDecimal betValue = wager.getAmount();
        BigDecimal coefficient = BigDecimal.valueOf(wager.getGuess().getBet().getType().getCoefficient());
        BigDecimal winValue = BigDecimalUtils.multiply(betValue, coefficient);
        BigDecimal newBalance = BigDecimalUtils.add(winValue, winnerUser.getBalance());
        winnerUser.setBalance(newBalance);

        populateStatRecord(wager, winValue);
        this.save(winnerUser);

        return winValue;
    }

    private void populateStatRecord(Wager wager, BigDecimal winValue) {
        PointsTurnoverStatistic statistic = new PointsTurnoverStatistic();
        statistic.setIsWin(true);
        statistic.setCurrency(wager.getCurrency());
        statistic.setAmount(winValue);
        statistic.setWager(wager);
        pointsTurnoverStatisticService.save(statistic);
    }

    @Override
    @Transactional
    public void compensateBalance(Long idWager, User user, BigDecimal amountCompensation) {
        user.setBalance(BigDecimalUtils.add(user.getBalance(), amountCompensation));
        pointsTurnoverStatisticService.deleteByWagerId(idWager);
        this.save(user);
    }


}
