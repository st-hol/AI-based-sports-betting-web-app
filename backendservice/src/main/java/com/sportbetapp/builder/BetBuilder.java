
package com.sportbetapp.builder;


import java.util.List;

import com.sportbetapp.domain.Bet;
import com.sportbetapp.domain.Outcome;
import com.sportbetapp.domain.SportEvent;


public class BetBuilder {

    private SportEvent sportEvent;
    private String description;
    private Bet.BetType type;
    private List<Outcome> outcomes;

    private BetListBuilder betListBuilder;

    public BetBuilder(BetListBuilder betListBuilder) {
        this.betListBuilder = betListBuilder;
    }

    public BetBuilder setSportEvent(SportEvent sportEvent) {
        this.sportEvent = sportEvent;
        return this;

    }

    public BetBuilder setDescription(String description) {
        this.description = description;
        return this;

    }

    public BetBuilder setType(Bet.BetType type) {
        this.type = type;
        return this;

    }

    public BetBuilder setOutcomes(List<Outcome> outcomes) {
        this.outcomes = outcomes;
        return this;

    }

    public BetBuilder setBetListBuilder(BetListBuilder betListBuilder) {
        this.betListBuilder = betListBuilder;
        return this;
    }

    private Bet build() {
        Bet bet = new Bet();
        bet.setSportEvent(sportEvent);
        bet.setDescription(description);
        bet.setType(type);
        bet.setOutcomes(outcomes);
        return bet;
    }

    public BetListBuilder addBetToList() {
        Bet bet = build();
        this.betListBuilder.addBet(bet);
        return this.betListBuilder;
    }

}
