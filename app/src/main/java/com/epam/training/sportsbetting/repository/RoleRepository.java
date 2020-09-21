package com.epam.training.sportsbetting.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.epam.training.sportsbetting.domain.user.User;
import com.epam.training.sportsbetting.domain.user.role.Role;


@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    List<Role> findAllByUsers(User user);
}
