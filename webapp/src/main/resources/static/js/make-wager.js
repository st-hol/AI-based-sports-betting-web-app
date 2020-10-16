const betTypeToInputBlockIdsMap = {
    'WINNER': 'inputWinner',
    'EXACT_GAME_SCORE': 'inputScores',
    'BOTH_WILL_SCORE_AT_LEAST_BY_ONE_HIT': 'inputBothScoredAtLeastOne',
    'BOTH_WILL_NOT_SCORE_ANY_GOALS': 'inputBothNotScoredAnyGoals',
    'GOALS_BY_TEAM': 'inputGoalsByTeam',
    'MISSES_BY_TEAM': 'inputMissesByTeam',
    'GOALS_MORE_THAN': 'inputGoalsByTeamMoreThan',
    'MISSES_MORE_THAN': 'inputMissesByTeamMoreThan'
};

const inputIdsList = [
    'winnerPlayerSideName',

    'homeTeamScore',
    'awayTeamScore',

    'bothScoredAtLeastOne1',
    'bothScoredAtLeastOne2',

    'bothNotScoredAnyGoals1',
    'bothNotScoredAnyGoals2',


    'teamNameThatScoredCertainNumOfGoals',
    'numGoalsScoredByCertainTeam',

    'teamNameThatMissedCertainNumOfGoals',
    'numGoalsMissedByCertainTeam',

    'teamNameThatScoredGoalsMoreThanCertainNum',
    'goalsScoredMoreThanCertainNumByTeam',

    'teamNameThatMissedGoalsMoreThanCertainNum',
    'goalsMissedMoreThanCertainNumByTeam'
];

$(document).ready(function () {
    $('#displayAfterBetChosen').css({'display': 'none'});
    disableAllInputs(betTypeToInputBlockIdsMap);
    const chooseBet = $('#chooseBet');
    if (chooseBet.val() !== "") {
        processSelectChange(null);
    }
    $(chooseBet.on('change', function (event) {
        processSelectChange(event);
    }));

});

function disableAllInputs(betTypeToInputBlockIdsMap) {
    $.each(betTypeToInputBlockIdsMap, function (key, value) {
        $('#'.concat(value)).css({'display': 'none'});
    });
}

function processSelectChange(event) {
    const chooseBet = $('#chooseBet').val();

    if (chooseBet && chooseBet !== "") {
        makeTheInputActiveDisableOthers(chooseBet, betTypeToInputBlockIdsMap, inputIdsList);

        if (event) {
            event.preventDefault();
        }
    }
}

function makeTheInputActiveDisableOthers(newActive, betTypeToInputBlockIdsMap, inputIdsList) {
    clearAllInputs(inputIdsList);
    const activeId = betTypeToInputBlockIdsMap[newActive];
    updateBetId(betTypeToInputBlockIdsMap, activeId);

    $.each(betTypeToInputBlockIdsMap, function (key, value) {
        if (value === activeId) {
            $('#'.concat(activeId)).css({'display': 'block'});
        } else {
            $('#'.concat(value)).css({'display': 'none'});
        }
    });
}

function getKeyByValue(inputMap, value) {
    for (var prop in inputMap) {
        if (inputMap.hasOwnProperty(prop)) {
            if (inputMap[prop] === value)
                return prop;
        }
    }
}

function clearAllInputs(inputIdsList) {
    $.each(inputIdsList, function (ind, el) {
        $('#'.concat(el)).val('');
    });
}

function updateBetId(betTypeToInputBlockIdsMap, newActiveId) {
    $('#betType').val(getKeyByValue(betTypeToInputBlockIdsMap, newActiveId));
}
