package com.epam.training.sportsbetting.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.epam.training.sportsbetting.domain.type.Currency;
import com.epam.training.sportsbetting.domain.user.User;

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
    private User player;

    @ManyToOne
    private OutcomeOdd outcomeOdd;

    private BigDecimal amount;

    private Currency currency;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime creationTime;

    private boolean isProcessed;

    private boolean isWinner;

    @Override
    public String toString() {
        return "Wager{" +
                "amount=" + amount +
                ", isWinner=" + isWinner +
                '}';
    }
}
