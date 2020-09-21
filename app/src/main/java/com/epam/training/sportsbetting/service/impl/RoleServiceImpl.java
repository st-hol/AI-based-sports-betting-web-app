package com.epam.training.sportsbetting.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.training.sportsbetting.domain.user.User;
import com.epam.training.sportsbetting.domain.user.role.Role;
import com.epam.training.sportsbetting.repository.RoleRepository;
import com.epam.training.sportsbetting.service.RoleService;
import com.google.common.collect.Lists;



@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return Lists.newArrayList(roleRepository.findAll());
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role save(Role complaint) {
        return roleRepository.save(complaint);
    }

    @Autowired
    public void setComplaintRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public List<Role> findAllByUser(User user) {
        return roleRepository.findAllByUsers(user);
    }
}