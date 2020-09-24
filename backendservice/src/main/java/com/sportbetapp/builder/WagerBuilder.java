
package com.sportbetapp.builder;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.sportbetapp.domain.OutcomeOdd;
import com.sportbetapp.domain.Wager;
import com.sportbetapp.domain.type.Currency;
import com.sportbetapp.domain.user.User;


public class WagerBuilder {

    private User user;
    private OutcomeOdd outcomeOdd;
    private BigDecimal amount;
    private Currency currency;
    private LocalDateTime timestampCreated;
    private boolean processed;
    private boolean win;

    public WagerBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public WagerBuilder setOutcomeOdd(OutcomeOdd outcomeOdd) {
        this.outcomeOdd = outcomeOdd;
        return this;
    }

    public WagerBuilder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public WagerBuilder setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public WagerBuilder setTimestampCreated(LocalDateTime timestampCreated) {
        this.timestampCreated = timestampCreated;
        return this;
    }

    public WagerBuilder setProcessed(boolean processed) {
        this.processed = processed;
        return this;
    }

    public WagerBuilder setWin(boolean win) {
        this.win = win;
        return this;
    }

    public Wager build() {
        Wager wager = new Wager();
//        wager.setPlayer(player);
        wager.setOutcomeOdd(outcomeOdd);
        wager.setAmount(amount);
        wager.setCurrency(currency);
        wager.setCreationTime(timestampCreated);
        wager.setProcessed(processed);
        wager.setWinner(win);
        return wager;
    }

}
