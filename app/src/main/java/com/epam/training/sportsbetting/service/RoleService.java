package com.epam.training.sportsbetting.service;


import java.util.List;

import com.epam.training.sportsbetting.domain.user.User;
import com.epam.training.sportsbetting.domain.user.role.Role;

public interface RoleService {
    List<Role> findAll();

    Role findById(Long id);

    Role save(Role role);

    List<Role> findAllByUser(User user);
}