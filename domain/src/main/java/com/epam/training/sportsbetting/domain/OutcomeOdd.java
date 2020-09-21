package com.epam.training.sportsbetting.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OutcomeOdd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal value;
    @ManyToOne
    private Outcome outcome;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime validFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime validUntil;
    @OneToMany(mappedBy = "outcomeOdd", cascade = CascadeType.ALL)
    private List<Wager> wagers;

    @Override
    public String toString() {
        return "OutcomeOdd{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OutcomeOdd that = (OutcomeOdd) o;
        return Objects.equals(value, that.value) &&
                Objects.equals(outcome, that.outcome) &&
                Objects.equals(validFrom, that.validFrom) &&
                Objects.equals(validUntil, that.validUntil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, outcome, validFrom, validUntil);
    }
}
