package com.epam.training.sportsbetting.domain.user;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import org.springframework.format.annotation.DateTimeFormat;

import com.epam.training.sportsbetting.domain.Wager;
import com.epam.training.sportsbetting.domain.type.Currency;
import com.epam.training.sportsbetting.domain.user.role.Role;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Player extends User {
    private String name;
    private Integer accountNumber;
    private BigDecimal balance;
    private Currency currency;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Wager> wagers;

    @PrePersist
    void preInsert() {
        if (this.roles.isEmpty()) {
            Role role = new Role();
            role.setId(1L);
            role.setName("PLAYER");
            roles.add(role);
        }
    }
}
