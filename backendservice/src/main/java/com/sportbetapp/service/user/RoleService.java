package com.sportbetapp.service.user;


import java.util.List;

import com.sportbetapp.domain.user.User;
import com.sportbetapp.domain.user.role.Role;

public interface RoleService {
    List<Role> findAll();

    Role findById(Long id);

    Role save(Role role);

    List<Role> findAllByUser(User user);
}