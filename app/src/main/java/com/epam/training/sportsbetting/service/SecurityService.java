package com.epam.training.sportsbetting.service;


import java.util.Set;

import com.epam.training.sportsbetting.domain.user.role.Role;


public interface SecurityService {
    String findLoggedInUsername();

    void autoLoginAfterReg(String username, String password);

    Set<Role> getLoggedUserRoles();
}
