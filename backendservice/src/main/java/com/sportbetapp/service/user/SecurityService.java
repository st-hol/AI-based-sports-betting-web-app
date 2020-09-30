package com.sportbetapp.service.user;


import java.util.Set;

import com.sportbetapp.domain.user.role.Role;


public interface SecurityService {
    String findLoggedInUsername();

    void autoLoginAfterReg(String username, String password);

    Set<Role> getLoggedUserRoles();
}
