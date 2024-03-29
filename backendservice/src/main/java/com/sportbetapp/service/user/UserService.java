package com.sportbetapp.service.user;


import java.math.BigDecimal;
import java.util.List;

import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.user.UserDto;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    User save(User user);

    User findByUsername(String username);

    void registerUser(UserDto user);

    User obtainCurrentPrincipleUser();

    void updateUserInfo(UserDto user);

    UserDto convertToUserDto(User user);

    BigDecimal addWinAmountToBalance(User winnerUser, Wager wager);

    void compensateBalance(Long idWager, User user, BigDecimal amountCompensation);
}
