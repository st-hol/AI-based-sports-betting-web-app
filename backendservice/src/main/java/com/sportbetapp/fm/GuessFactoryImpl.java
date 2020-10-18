package com.sportbetapp.fm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sportbetapp.domain.betting.PlayerSide;
import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.betting.guess.Guess;
import com.sportbetapp.domain.betting.guess.GuessBothWillScore;
import com.sportbetapp.domain.betting.guess.GuessGoals;
import com.sportbetapp.domain.betting.guess.GuessScore;
import com.sportbetapp.domain.betting.guess.GuessWinner;
import com.sportbetapp.domain.type.BetType;
import com.sportbetapp.domain.type.GoalsDirection;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.dto.betting.CreateWagerDto;
import com.sportbetapp.exception.NotExistingGuessException;
import com.sportbetapp.repository.betting.PlayerSideRepository;
import com.sportbetapp.service.betting.BetService;
import com.sportbetapp.service.betting.PlayerSideService;
import com.sportbetapp.service.betting.SportEventService;
import com.sportbetapp.service.betting.WagerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GuessFactoryImpl extends GuessFactory {

    private static final String GUESS_NOT_EXIST_MESSAGE = "There is no such dish";

    @Autowired
    private WagerService wagerService;
    @Autowired
    private SportEventService sportEventService;
    @Autowired
    private BetService betService;
    @Autowired
    private PlayerSideService playerSideService;


    public Guess prepareCertainWagerGuess(final CreateWagerDto createWagerDto) throws NotExistingGuessException {
        Guess base = createBase(createWagerDto);
        log.info("Factory: Preparing new Guess: {}", base);
        return base;
    }

    private Guess createBase(final CreateWagerDto createWagerDto) throws NotExistingGuessException {
        switch (createWagerDto.getBetType()) {
            case WINNER:
                GuessWinner guessWinner = new GuessWinner();
                populateCommonGuessFields(createWagerDto, guessWinner);
                //guess specific
                guessWinner.setPlayerSide(playerSideService
                        .findByNameAndSportType(createWagerDto.getWinnerPlayerSideName(),
                                SportType.valueOf(createWagerDto.getSportType())));
                return guessWinner;
            case EXACT_GAME_SCORE:
                GuessScore guessScore = new GuessScore();
                populateCommonGuessFields(createWagerDto, guessScore);
                //guess specific
                guessScore.setHomeTeamScore(createWagerDto.getHomeTeamScore());
                guessScore.setAwayTeamScore(createWagerDto.getAwayTeamScore());
                return guessScore;
            case BOTH_WILL_SCORE_AT_LEAST_BY_ONE_HIT:
                GuessBothWillScore guessBothWillScore = new GuessBothWillScore();
                populateCommonGuessFields(createWagerDto, guessBothWillScore);
                //guess specific
                guessBothWillScore.setBothScoredAtLeastOne(createWagerDto.getBothScoredAtLeastOne());
                return guessBothWillScore;
            case BOTH_WILL_NOT_SCORE_ANY_GOALS:
                GuessBothWillScore guessBothNotScoreAny = new GuessBothWillScore();
                populateCommonGuessFields(createWagerDto, guessBothNotScoreAny);
                //guess specific
                guessBothNotScoreAny.setBothScoredAtLeastOne(createWagerDto.getBothNotScoredAnyGoals());
                return guessBothNotScoreAny;
            case GOALS_BY_TEAM:
                GuessGoals guessGoalsByTeam = new GuessGoals();
                populateCommonGuessFields(createWagerDto, guessGoalsByTeam);
                //guess specific
                guessGoalsByTeam.setGoalsDirection(GoalsDirection.SCORED);
                guessGoalsByTeam.setTeamName(createWagerDto.getTeamNameThatScoredCertainNumOfGoals());
                guessGoalsByTeam.setGoalsCount(createWagerDto.getNumGoalsScoredByCertainTeam());
                return guessGoalsByTeam;
            case MISSES_BY_TEAM:
                GuessGoals guessMissesByTeam = new GuessGoals();
                populateCommonGuessFields(createWagerDto, guessMissesByTeam);
                //guess specific
                guessMissesByTeam.setGoalsDirection(GoalsDirection.MISSED);
                guessMissesByTeam.setTeamName(createWagerDto.getTeamNameThatMissedCertainNumOfGoals());
                guessMissesByTeam.setGoalsCount(createWagerDto.getNumGoalsMissedByCertainTeam());
                return guessMissesByTeam;
            case GOALS_MORE_THAN:
                GuessGoals guessGoalsMoreThanNum = new GuessGoals();
                populateCommonGuessFields(createWagerDto, guessGoalsMoreThanNum);
                //guess specific
                guessGoalsMoreThanNum.setGoalsDirection(GoalsDirection.MORE_THAN);
                guessGoalsMoreThanNum.setTeamName(createWagerDto.getTeamNameThatScoredGoalsMoreThanCertainNum());
                guessGoalsMoreThanNum.setGoalsCount(createWagerDto.getGoalsScoredMoreThanCertainNumByTeam());
                return guessGoalsMoreThanNum;
            case MISSES_MORE_THAN:
                GuessGoals guessGoalsLessThanNum = new GuessGoals();
                populateCommonGuessFields(createWagerDto, guessGoalsLessThanNum);
                //guess specific
                guessGoalsLessThanNum.setGoalsDirection(GoalsDirection.MORE_THAN);
                guessGoalsLessThanNum.setTeamName(createWagerDto.getTeamNameThatMissedGoalsMoreThanCertainNum());
                guessGoalsLessThanNum.setGoalsCount(createWagerDto.getGoalsMissedMoreThanCertainNumByTeam());
                return guessGoalsLessThanNum;
            default:
                throw new NotExistingGuessException(GUESS_NOT_EXIST_MESSAGE);
        }
    }

    private void populateCommonGuessFields(final CreateWagerDto createWagerDto, final Guess guess) {
        guess.setBet(betService.findByBetType(createWagerDto.getBetType()));
        guess.setSportEvent(sportEventService.findById(createWagerDto.getSportEventId()));
    }


}
