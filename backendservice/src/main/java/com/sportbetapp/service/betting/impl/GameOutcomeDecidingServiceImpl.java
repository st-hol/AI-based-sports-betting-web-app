package com.sportbetapp.service.betting.impl;

import static com.sportbetapp.domain.type.BetType.BOTH_WILL_NOT_SCORE_ANY_GOALS;
import static com.sportbetapp.domain.type.BetType.BOTH_WILL_SCORE_AT_LEAST_BY_ONE_HIT;
import static com.sportbetapp.domain.type.BetType.EXACT_GAME_SCORE;
import static com.sportbetapp.domain.type.BetType.GOALS_BY_TEAM;
import static com.sportbetapp.domain.type.BetType.GOALS_MORE_THAN;
import static com.sportbetapp.domain.type.BetType.MISSES_BY_TEAM;
import static com.sportbetapp.domain.type.BetType.MISSES_MORE_THAN;
import static com.sportbetapp.domain.type.BetType.WINNER;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import javax.mail.MessagingException;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportbetapp.domain.betting.Result;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.betting.guess.Guess;
import com.sportbetapp.domain.betting.guess.GuessBothWillScore;
import com.sportbetapp.domain.betting.guess.GuessGoals;
import com.sportbetapp.domain.betting.guess.GuessScore;
import com.sportbetapp.domain.betting.guess.GuessWinner;
import com.sportbetapp.domain.predicting.HitScore;
import com.sportbetapp.domain.predicting.PredictionRecord;
import com.sportbetapp.domain.technical.Mail;
import com.sportbetapp.domain.type.BetType;
import com.sportbetapp.domain.type.OutcomeType;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.service.betting.GameOutcomeDecidingService;
import com.sportbetapp.service.betting.GuessService;
import com.sportbetapp.service.betting.ResultService;
import com.sportbetapp.service.betting.SportEventService;
import com.sportbetapp.service.mail.MailService;
import com.sportbetapp.service.predicting.PredictionService;
import com.sportbetapp.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GameOutcomeDecidingServiceImpl implements GameOutcomeDecidingService {

    private Map<BetType, BiPredicate<Guess, Pair<? extends PredictionRecord, ? extends PredictionRecord>>>
            betTypePredicateMap = Map.of(

            WINNER,
            (guess, teamsResults) -> {
                GuessWinner guessWinner = (GuessWinner) guess;
                String guessWinnerName = guessWinner.getPlayerSide().getName();

                PredictionRecord firstTeamRes = teamsResults.getLeft();
                PredictionRecord secondTeamRes = teamsResults.getRight();
                HitScore firstTeamScore = firstTeamRes.getHitScore();
                HitScore secondTeamScore = secondTeamRes.getHitScore();

                String actualWinnerName;
                if (firstTeamScore.getHitsScored() > secondTeamScore.getHitsScored()) {
                    actualWinnerName = firstTeamRes.getPlayerSide().getName();
                } else if (firstTeamScore.getHitsScored() < secondTeamScore.getHitsScored()) {
                    actualWinnerName = secondTeamRes.getPlayerSide().getName();
                } else {
                    actualWinnerName = "";
                }
                return guessWinnerName.equals(actualWinnerName);
            },

            EXACT_GAME_SCORE,
            (guess, teamsResults) -> {
                GuessScore guessScore = (GuessScore) guess;
                int guessFirstScore = guessScore.getHomeTeamScore();
                int guessSecondScore = guessScore.getAwayTeamScore();

                int firstTeam = teamsResults.getLeft().getHitScore().getHitsScored();
                int secondTeam = teamsResults.getLeft().getHitScore().getHitsMissed();

                return guessFirstScore == firstTeam && guessSecondScore == secondTeam;
            },

            BOTH_WILL_SCORE_AT_LEAST_BY_ONE_HIT,
            (guess, teamsResults) -> {
                GuessBothWillScore guessBothWillScore = (GuessBothWillScore) guess;
                boolean guessBothWillScoreBothScoredAtLeastOne = guessBothWillScore.getBothScoredAtLeastOne();

                int firstTeam = teamsResults.getLeft().getHitScore().getHitsScored();
                int secondTeam = teamsResults.getRight().getHitScore().getHitsScored();
                boolean actualBothWillScoreBothScoredAtLeastOne = firstTeam > 1 && secondTeam > 1;

                return guessBothWillScoreBothScoredAtLeastOne == actualBothWillScoreBothScoredAtLeastOne;
            },

            BOTH_WILL_NOT_SCORE_ANY_GOALS,
            (guess, teamsResults) -> {
                GuessBothWillScore guessBothWillScore = (GuessBothWillScore) guess;
                boolean guessBothNotScoredAnyGoals = guessBothWillScore.getBothNotScoredAnyGoals();

                int firstTeam = teamsResults.getLeft().getHitScore().getHitsScored();
                int secondTeam = teamsResults.getRight().getHitScore().getHitsScored();
                boolean actualBothNotScoredAnyGoals = firstTeam == 1 && secondTeam == 1;

                return guessBothNotScoredAnyGoals == actualBothNotScoredAnyGoals;
            },

            GOALS_BY_TEAM,
            (guess, teamsResults) -> {
                GuessGoals guessGoals = (GuessGoals) guess;
                int guessScore = guessGoals.getGoalsCount();
                String guessTeam = guessGoals.getTeamName();

                int actualScore = Stream.of(teamsResults.getLeft(), teamsResults.getRight())
                        .filter(predictionRecord -> predictionRecord.getPlayerSide().getName().equals(guessTeam))
                        .map(predictionRecord -> predictionRecord.getHitScore().getHitsScored())
                        .findFirst().orElse(Integer.MIN_VALUE);
                return guessScore == actualScore;
            },

            MISSES_BY_TEAM,
            (guess, teamsResults) -> {
                GuessGoals guessGoals = (GuessGoals) guess;
                int guessMisses = guessGoals.getGoalsCount();
                String guessTeam = guessGoals.getTeamName();

                int actualMisses = Stream.of(teamsResults.getLeft(), teamsResults.getRight())
                        .filter(predictionRecord -> predictionRecord.getPlayerSide().getName().equals(guessTeam))
                        .map(predictionRecord -> predictionRecord.getHitScore().getHitsMissed())
                        .findFirst().orElse(Integer.MAX_VALUE);
                return guessMisses == actualMisses;
            },

            GOALS_MORE_THAN,
            (guess, teamsResults) -> {
                GuessGoals guessGoals = (GuessGoals) guess;
                int guessScore = guessGoals.getGoalsCount();
                String guessTeam = guessGoals.getTeamName();

                int actualScore = Stream.of(teamsResults.getLeft(), teamsResults.getRight())
                        .filter(predictionRecord -> predictionRecord.getPlayerSide().getName().equals(guessTeam))
                        .map(predictionRecord -> predictionRecord.getHitScore().getHitsScored())
                        .findFirst().orElse(Integer.MIN_VALUE);
                return actualScore > guessScore;
            },

            MISSES_MORE_THAN,
            (guess, teamsResults) -> {
                GuessGoals guessGoals = (GuessGoals) guess;
                int guessMisses = guessGoals.getGoalsCount();
                String guessTeam = guessGoals.getTeamName();

                int actualMisses = Stream.of(teamsResults.getLeft(), teamsResults.getRight())
                        .filter(predictionRecord -> predictionRecord.getPlayerSide().getName().equals(guessTeam))
                        .map(predictionRecord -> predictionRecord.getHitScore().getHitsMissed())
                        .findFirst().orElse(Integer.MAX_VALUE);
                return actualMisses < guessMisses;
            }
                                        );

    @Autowired
    private UserService userService;
    @Autowired
    private ResultService resultService;
    @Autowired
    private SportEventService sportEventService;
    @Autowired
    private PredictionService predictionService;
    @Autowired
    private GuessService guessService;
    @Autowired
    private MailService mailService;


    @Override
    @Transactional
    public void determineResults(SportEvent sportEvent) {

        List<PredictionRecord> predictionRecords = predictionService.findHistoricRecordsByEvent(sportEvent);

        PredictionRecord firstTeamRes = predictionRecords.get(0);
        PredictionRecord secondTeamRes = predictionRecords.get(1);

        Result winnerResult = new Result();
        winnerResult.setSportEvent(sportEventService.findById(sportEvent.getId()));
        Set<Guess> winnerGuesses = new HashSet<>();

        List<Guess> guesses = guessService.findAllByEvent(sportEvent);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        guesses.forEach(guess -> {
            if (betTypePredicateMap.get(guess.getBet().getType()).test(guess, Pair.of(firstTeamRes, secondTeamRes))) {
                guess.getWagers().forEach(wager -> {
                    wager.setOutcomeType(OutcomeType.SUCCESS);
                    User winnerUser = wager.getUser();
                    BigDecimal winAmount = userService.addWinAmountToBalance(winnerUser, wager);
                    executorService.submit(() -> sendMailNotification(wager, winAmount));
                });
                winnerGuesses.add(guess);
            } else {
                guess.getWagers().forEach(wager -> {
                    wager.setOutcomeType(OutcomeType.FAILURE);
                    executorService.submit(() -> sendMailNotification(wager, BigDecimal.ZERO));
                });
            }
        });

        winnerResult.setWinnerGuesses(winnerGuesses);
        resultService.save(winnerResult);

        determineSportEventResult(sportEvent, winnerResult);
    }

    private void determineSportEventResult(SportEvent sportEvent, Result winnerResult) {
        sportEvent.setResult(winnerResult);
        sportEvent.setAlreadyPredicted(true);
        sportEventService.save(sportEvent);
    }

    private void sendMailNotification(Wager wager, BigDecimal winAmount) {
        User currentUser = userService.obtainCurrentPrincipleUser();

        Mail mail = new Mail();
        mail.setFrom("SportBetApp");
        mail.setTo(currentUser.getEmail());
        mail.setSubject("Sending wager result");
        mail.setContent(String.format("Your wager id# %d result is %s.%nYour win amount = %d.",
                wager.getId(), wager.getOutcomeType().getValue(), winAmount.intValue()));
        try {
            mailService.sendMail(mail);
        } catch (MessagingException ex) {
            log.error("Can not send mail {}", ex.getMessage());
        }

    }

}
