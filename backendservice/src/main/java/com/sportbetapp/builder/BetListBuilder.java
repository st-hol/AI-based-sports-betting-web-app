
package com.sportbetapp.builder;

import java.util.ArrayList;
import java.util.List;

import com.sportbetapp.domain.Bet;


public class BetListBuilder {

    private ArrayList<Bet> listOfBets;

    public BetListBuilder addList() {
        this.listOfBets = new ArrayList<>();
        return this;
    }

    public BetListBuilder addBet(Bet bet) {
        listOfBets.add(bet);
        return this;
    }

    public BetBuilder addBet() {
        return new BetBuilder(this);
    }

    public List<Bet> buildList() {
        return listOfBets;
    }

}
