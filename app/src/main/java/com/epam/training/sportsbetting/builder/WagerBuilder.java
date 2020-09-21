
package com.epam.training.sportsbetting.builder;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.epam.training.sportsbetting.domain.OutcomeOdd;
import com.epam.training.sportsbetting.domain.Wager;
import com.epam.training.sportsbetting.domain.type.Currency;
import com.epam.training.sportsbetting.domain.user.Player;


public class WagerBuilder {

    private Player player;
    private OutcomeOdd outcomeOdd;
    private BigDecimal amount;
    private Currency currency;
    private LocalDateTime timestampCreated;
    private boolean processed;
    private boolean win;

    public WagerBuilder setPlayer(Player player) {
        this.player = player;
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
