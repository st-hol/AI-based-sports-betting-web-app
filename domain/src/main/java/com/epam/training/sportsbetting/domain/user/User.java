package com.epam.training.sportsbetting.domain.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import com.epam.training.sportsbetting.domain.user.role.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String email;
    protected String password;
    @Transient
    protected String passwordConfirm;
    @ManyToMany
    @JoinTable(name = "users_has_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    protected Set<Role> roles = new HashSet<>();
    protected boolean enabled;

}
