
package com.sportbetapp.builder;

import java.util.ArrayList;
import java.util.List;

import com.sportbetapp.domain.Outcome;


public class OutcomeListBuilder {

    private List<Outcome> listOfOutcomes;

    public OutcomeListBuilder addList() {
        this.listOfOutcomes = new ArrayList<>();
        return this;
    }

    public OutcomeListBuilder addOutcome(Outcome outcome) {
        listOfOutcomes.add(outcome);
        return this;
    }

    public OutcomeBuilder addOutcome() {
        return new OutcomeBuilder(this);
    }

    public List<Outcome> buildList() {
        return listOfOutcomes;
    }

}
