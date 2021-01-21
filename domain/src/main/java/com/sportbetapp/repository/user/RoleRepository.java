package com.sportbetapp.repository.user;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sportbetapp.domain.user.User;
import com.sportbetapp.domain.user.role.Role;


@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    List<Role> findAllByUsers(User user);

    Role findByName(String nameRole);
}
