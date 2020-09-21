package com.epam.training.sportsbetting.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.epam.training.sportsbetting.builder.SportEventBuilder;
import com.epam.training.sportsbetting.domain.Bet;
import com.epam.training.sportsbetting.domain.Outcome;
import com.epam.training.sportsbetting.domain.OutcomeOdd;
import com.epam.training.sportsbetting.domain.Result;
import com.epam.training.sportsbetting.domain.SportEvent;
import com.epam.training.sportsbetting.domain.Wager;
import com.epam.training.sportsbetting.domain.user.Player;
import com.epam.training.sportsbetting.domain.user.User;
import com.epam.training.sportsbetting.dto.BetDto;
import com.epam.training.sportsbetting.dto.OutcomeDto;
import com.epam.training.sportsbetting.dto.OutcomeOddDto;
import com.epam.training.sportsbetting.dto.ProcessResultDto;
import com.epam.training.sportsbetting.dto.SportEventDto;
import com.epam.training.sportsbetting.service.BetService;
import com.epam.training.sportsbetting.service.OutcomeOddService;
import com.epam.training.sportsbetting.service.OutcomeService;
import com.epam.training.sportsbetting.service.PopulateDataRestService;
import com.epam.training.sportsbetting.service.ResultService;
import com.epam.training.sportsbetting.service.SportEventService;
import com.epam.training.sportsbetting.service.UserService;

@Service
public class PopulateDataRestServiceImpl implements PopulateDataRestService {

    @Autowired
    private OutcomeService outcomeService;
    @Autowired
    private OutcomeOddService outcomeOddService;
    @Autowired
    private BetService betService;
    @Autowired
    private SportEventService sportEventService;
    @Autowired
    private ResultService resultService;
    @Autowired
    private UserService userService;

    @Override
    public SportEventDto populateSportEvent(SportEventDto sportEventDto) {
        SportEvent sportEvent = toSportEvent(sportEventDto);
        sportEventService.save(sportEvent);
        sportEventDto.setId(sportEvent.getId());
        if (!CollectionUtils.isEmpty(sportEventDto.getBets())) {
            sportEventDto.getBets().forEach(bet -> bet.setSportEventId(sportEventDto.getId()));
            sportEventDto.getBets().forEach(this::populateBetToSportEvent);
        }
        return sportEventDto;
    }

    @Override
    public BetDto populateBetToSportEvent(BetDto betDto) {
        Bet bet = toBet(betDto);
        betService.save(bet);
        betDto.setId(bet.getId());
        if (!CollectionUtils.isEmpty(betDto.getOutcomes())) {
            betDto.getOutcomes().forEach(outcomeData -> outcomeData.setBetId(bet.getId()));
            betDto.getOutcomes().forEach(this::populateOutcomeToBet);
        }
        return betDto;
    }

    @Override
    public OutcomeDto populateOutcomeToBet(OutcomeDto outcomeDto) {
        Outcome outcome = toOutcome(outcomeDto);
        outcomeService.save(outcome);
        outcomeDto.setId(outcome.getId());
        if (!CollectionUtils.isEmpty(outcomeDto.getOutcomeOdds())) {
            outcomeDto.getOutcomeOdds().forEach(outcomeOddDto -> outcomeOddDto.setOutcomeId(outcome.getId()));
            outcomeDto.getOutcomeOdds().forEach(this::populateOutcomeOddToOutcome);
        }
        return outcomeDto;
    }

    @Override
    public OutcomeOddDto populateOutcomeOddToOutcome(OutcomeOddDto outcomeOddData) {
        OutcomeOdd outcomeOdd = toOutcomeOdd(outcomeOddData);
        outcomeOddService.save(outcomeOdd);
        outcomeOddData.setId(outcomeOdd.getId());
        return outcomeOddData;
    }

    @Override
    public SportEventDto toSportEventDto(SportEvent sportEvent) {
        SportEventDto sportEventDto = new SportEventDto();
        BeanUtils.copyProperties(sportEvent, sportEventDto);
        return sportEventDto;
    }

    @Override
    @Transactional
    public ProcessResultDto processResult(ProcessResultDto processResultDto) {
        resultService.deleteAllBySportEvent(sportEventService.findById(processResultDto.getSportEventId()));
        Result result = toResult(processResultDto);
        resultService.save(result);
        processResultDto.setId(result.getId());

        SportEvent sportEvent = sportEventService.findById(processResultDto.getSportEventId());

        List<Outcome> winnerOutcomes = processResultDto.getWinnerOutcomes().stream()
                .map(this::toOutcome).collect(Collectors.toList());

        for (Bet bet : sportEvent.getBets()) {
            for (Outcome outcome : bet.getOutcomes()) {
                if (winnerOutcomes.contains(outcome)) {
                    processOutcomes(outcome);
                }
            }
        }
        return processResultDto;
    }

    private void processOutcomes(Outcome outcome) {
        List<User> winners = userService.findAllByOutcome(outcome);
        for (User user : winners) {
            processWinner(user, outcome);
        }
        winners.forEach(userService::save);
    }

    private void processWinner(User user, Outcome outcome) {
        Player player = (Player) user;
        Set<Wager> wagers = player.getWagers();
        for (Wager wager : wagers) {
            if (wager.getOutcomeOdd().getOutcome().equals(outcome)) {
                wager.setWinner(true);
                updatePlayerBalance(player, wager);
            }
            wager.setProcessed(true);
        }
    }

    private void updatePlayerBalance(Player player, Wager wager) {
        player.setBalance(
                player.getBalance()
                        .add(wager.getAmount()
                                .multiply(wager.getOutcomeOdd()
                                        .getValue())));
    }

    private Result toResult(ProcessResultDto processResultDto) {
        Result result = new Result();
        result.setWinnerOutcomes(processResultDto.getWinnerOutcomes().stream()
                .map(this::toOutcome).collect(Collectors.toList()));
        result.setSportEvent(sportEventService.findById(processResultDto.getSportEventId()));
        return result;
    }

    private SportEvent toSportEvent(SportEventDto sportEventDto) {
        SportEventBuilder sportEventBuilder = new SportEventBuilder()
                .setId(sportEventDto.getId())
                .setTitle(sportEventDto.getTitle())
                .setStartDate(sportEventDto.getStartDate())
                .setEndDate(sportEventDto.getEndDate());
        if (sportEventDto.getEventType() == SportEventDto.EventType.FOOTBALL_EVENT) {
            return sportEventBuilder.buildFootballSportEvent();
        } else {
            return sportEventBuilder.buildTennisSportEvent();
        }
    }

    private Bet toBet(BetDto betDto) {
        Bet bet = new Bet();
        BeanUtils.copyProperties(betDto, bet, "outcomes", "sportEventId");
        bet.setSportEvent(sportEventService.findById(betDto.getSportEventId()));
        return bet;
    }

    private Outcome toOutcome(OutcomeDto outcomeDto) {
        Outcome outcome = new Outcome();
        BeanUtils.copyProperties(outcomeDto, outcome, "outcomeOdds", "betId");
        outcome.setBet(betService.findById(outcomeDto.getBetId()));
        return outcome;
    }

    private OutcomeOdd toOutcomeOdd(OutcomeOddDto outcomeOddDto) {
        OutcomeOdd outcomeOdd = new OutcomeOdd();
        BeanUtils.copyProperties(outcomeOddDto, outcomeOdd, "outcomeId");
        outcomeOdd.setOutcome(outcomeService.findById(outcomeOddDto.getOutcomeId()));
        return outcomeOdd;
    }

}
