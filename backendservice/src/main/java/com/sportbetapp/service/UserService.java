package com.sportbetapp.service;


import java.util.List;

import com.sportbetapp.domain.Outcome;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.CreateWagerDto;
import com.sportbetapp.dto.UserDto;
import com.sportbetapp.exception.NotEnoughBalanceException;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    User save(User user);

    User findByUsername(String username);

    void registerUser(UserDto user);

    User obtainCurrentPrincipleUser();

    void updateUserInfo(UserDto user);

    UserDto convertToUserDto(User user);

    void makeWager(CreateWagerDto wagerDto) throws NotEnoughBalanceException;

    List<User> findAllByOutcome(Outcome outcome);

}