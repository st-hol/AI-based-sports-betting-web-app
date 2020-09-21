package com.epam.training.sportsbetting.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.epam.training.sportsbetting.builder.BetListBuilder;
import com.epam.training.sportsbetting.builder.OutcomeListBuilder;
import com.epam.training.sportsbetting.builder.OutcomeOddListBuilder;
import com.epam.training.sportsbetting.builder.SportEventBuilder;
import com.epam.training.sportsbetting.domain.Bet;
import com.epam.training.sportsbetting.domain.Outcome;
import com.epam.training.sportsbetting.domain.OutcomeOdd;
import com.epam.training.sportsbetting.domain.SportEvent;
import com.epam.training.sportsbetting.service.BetService;
import com.epam.training.sportsbetting.service.OutcomeOddService;
import com.epam.training.sportsbetting.service.OutcomeService;
import com.epam.training.sportsbetting.service.ResultService;
import com.epam.training.sportsbetting.service.SportEventService;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class TestDataPopulatingBean {

    @Autowired
    private SportEventService sportEventService;
    @Autowired
    private OutcomeOddService outcomeOddService;
    @Autowired
    private OutcomeService outcomeService;
    @Autowired
    private BetService betService;
    @Autowired
    private ResultService resultService;

    private static final int MAX_ODD = 10;

    private List<SportEvent> sportEvents;
    private List<OutcomeOdd> outcomeOdds;

    @PostConstruct
    public void init() {
        outcomeOdds = new ArrayList<>();
        populateSportEvents();
        log.info("Test data initialized...");
    }

    private void clearDatabaseBeforeLaunch() {
        resultService.deleteAll();
        sportEventService.deleteAll();
    }

    /**
     * generating EVENT
     */
    private void populateSportEvents() {
        clearDatabaseBeforeLaunch();
        sportEvents = new ArrayList<>();

        SportEvent event = new SportEventBuilder()
                .setTitle("Arsenal vs Chelsea")
                .setStartDate(LocalDateTime.of(2016, 2, 3, 0, 0, 0))
                .setEndDate(LocalDateTime.of(2016, 2, 5, 0, 0, 0))
                .buildFootballSportEvent();

        sportEvents.add(event);
        sportEvents.forEach(sportEventService::save);

        event.setBets(populateFootballBets(event));
    }

    /**
     * generating BETS for certain EVENT
     */
    private List<Bet> populateFootballBets(SportEvent sportEvent) {

        List<Bet> bets = new BetListBuilder().addList()
                .addBet()
                .setSportEvent(sportEvent)
                .setDescription("Oliver Giroud score")
                .setType(Bet.BetType.PLAYERS_SCORE).addBetToList()
                .addBet()
                .setSportEvent(sportEvent)
                .setDescription("number of scored goals")
                .setType(Bet.BetType.GOALS).addBetToList()
                .addBet()
                .setSportEvent(sportEvent)
                .setDescription("winner")
                .setType(Bet.BetType.WINNER).addBetToList()
                .buildList();

        bets.forEach(betService::save);

        List<Outcome> possibleOutc = populateOutcomesByDescriptions(bets.get(0), Lists.newArrayList("1", "2"));
        bets.get(0).setOutcomes(possibleOutc);

        possibleOutc = populateOutcomesByDescriptions(bets.get(1), Lists.newArrayList("0", "3"));
        bets.get(1).setOutcomes(possibleOutc);

        possibleOutc = populateOutcomesByDescriptions(bets.get(2), Lists.newArrayList("Arsenal", "Chelsea"));
        bets.get(2).setOutcomes(possibleOutc);

        return bets;
    }

    /**
     * generating OUTCOMES for certain BET
     */
    private List<Outcome> populateOutcomesByDescriptions(Bet bet, List<String> outcomeDescriptions) {
        OutcomeListBuilder builder = new OutcomeListBuilder().addList();
        outcomeDescriptions.forEach(outcomeDescription -> builder.addOutcome()
                .setBet(bet)
                .setDescription(outcomeDescription)
                .addOutcomeToList());
        List<Outcome> possibleOutcomes = builder.buildList();
        possibleOutcomes.forEach(outcomeService::save);
        possibleOutcomes.forEach(outcome -> outcome.setOutcomeOdds(populateRandomOutcomeOdds()));
        assignOutcomeToEachOdd(possibleOutcomes);
        return possibleOutcomes;
    }

    /**
     * cyclic dependency...Outcome has OutcomeOdd and vice versa
     */
    private void assignOutcomeToEachOdd(List<Outcome> outcomes) {
        outcomes.forEach(outcome -> outcome.getOutcomeOdds()
                .forEach(outcomeOdd -> {
                    outcomeOdd.setOutcome(outcome);
                    outcomeOddService.save(outcomeOdd);
                }));
    }

    /**
     * generating OUTCOME_ODD for certain OUTCOME
     */
    private List<OutcomeOdd> populateRandomOutcomeOdds() {
        List<OutcomeOdd> odds = new OutcomeOddListBuilder().addList()
                .addOutcomeOdd()
                .setValidFrom(LocalDateTime.now())
                .setValidUntil(LocalDateTime.now().plusDays(1))
                .setValue(BigDecimal.valueOf(new Random().nextInt(MAX_ODD)))
                .addOutcomeOddToList()
                .buildList();
        odds.forEach(outcomeOddService::save);
        outcomeOdds.addAll(odds);
        return odds;
    }

}
