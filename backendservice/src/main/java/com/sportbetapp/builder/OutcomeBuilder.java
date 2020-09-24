package com.sportbetapp.builder;

import java.util.List;

import com.sportbetapp.domain.Bet;
import com.sportbetapp.domain.Outcome;
import com.sportbetapp.domain.OutcomeOdd;

public class OutcomeBuilder {
    private Bet bet;
    private String description;
    private List<OutcomeOdd> outcomeOdds;

    private OutcomeListBuilder outcomeListBuilder;

    public OutcomeBuilder(OutcomeListBuilder outcomeListBuilder) {
        this.outcomeListBuilder = outcomeListBuilder;
    }

    public OutcomeBuilder setBet(Bet bet) {
        this.bet = bet;
        return this;
    }

    public OutcomeBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public OutcomeBuilder setOutcomeOdds(List<OutcomeOdd> outcomeOdds) {
        this.outcomeOdds = outcomeOdds;
        return this;
    }

    private Outcome build() {
        Outcome outcome = new Outcome();
        outcome.setBet(bet);
        outcome.setDescription(description);
        outcome.setOutcomeOdds(outcomeOdds);
        return outcome;
    }

    public OutcomeListBuilder addOutcomeToList() {
        Outcome outcome = build();
        this.outcomeListBuilder.addOutcome(outcome);
        return this.outcomeListBuilder;
    }
}
