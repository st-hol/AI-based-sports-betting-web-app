package com.sportbetapp.domain.betting;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.sportbetapp.domain.betting.guess.Guess;
import com.sportbetapp.domain.type.Currency;
import com.sportbetapp.domain.type.OutcomeType;
import com.sportbetapp.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Wager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Guess guess;

    private BigDecimal amount;

    private Currency currency;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime creationTime;

    private OutcomeType outcomeType;

}
