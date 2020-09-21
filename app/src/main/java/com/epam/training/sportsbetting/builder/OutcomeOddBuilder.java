package com.epam.training.sportsbetting.builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.epam.training.sportsbetting.domain.Outcome;
import com.epam.training.sportsbetting.domain.OutcomeOdd;

public class OutcomeOddBuilder {
    private BigDecimal value;
    private Outcome outcome;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    private OutcomeOddListBuilder outcomeOddListBuilder;

    public OutcomeOddBuilder(OutcomeOddListBuilder outcomeOddListBuilder) {
        this.outcomeOddListBuilder = outcomeOddListBuilder;
    }

    public OutcomeOddBuilder setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    public OutcomeOddBuilder setOutcome(Outcome outcome) {
        this.outcome = outcome;
        return this;
    }

    public OutcomeOddBuilder setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public OutcomeOddBuilder setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
        return this;
    }

    public OutcomeOddBuilder setOutcomeOddListBuilder(OutcomeOddListBuilder outcomeOddListBuilder) {
        this.outcomeOddListBuilder = outcomeOddListBuilder;
        return this;
    }

    private OutcomeOdd createOutcomeOdd() {
        OutcomeOdd outcomeOdd = new OutcomeOdd();
        outcomeOdd.setOutcome(outcome);
        outcomeOdd.setValidFrom(validFrom);
        outcomeOdd.setValidUntil(validUntil);
        outcomeOdd.setValue(value);
        return outcomeOdd;
    }

    public OutcomeOddListBuilder addOutcomeOddToList() {
        OutcomeOdd outcomeOdd = createOutcomeOdd();
        this.outcomeOddListBuilder.addOutcomeOdd(outcomeOdd);
        return this.outcomeOddListBuilder;
    }
}
