package com.sportbetapp.controller.mobile.rest.api;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sportbetapp.controller.mobile.rest.api.dto.BetCoefficientMobileDto;
import com.sportbetapp.controller.mobile.rest.api.dto.MakeBetMobileDto;
import com.sportbetapp.controller.mobile.rest.api.dto.MobileSportEventDto;
import com.sportbetapp.controller.mobile.rest.api.dto.MobileUserDto;
import com.sportbetapp.controller.mobile.rest.api.dto.MobileWagerDto;
import com.sportbetapp.controller.mobile.rest.api.mapper.MakeBetMapper;
import com.sportbetapp.controller.mobile.rest.api.mapper.SportEventMapper;
import com.sportbetapp.controller.mobile.rest.api.mapper.UserMapper;
import com.sportbetapp.controller.mobile.rest.api.mapper.WagerMapper;
import com.sportbetapp.domain.type.BetType;
import com.sportbetapp.domain.type.SportType;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.betting.CreateWagerDto;
import com.sportbetapp.dto.user.UserDto;
import com.sportbetapp.exception.EventAlreadyPredictedException;
import com.sportbetapp.exception.EventAlreadyStartedException;
import com.sportbetapp.exception.NotEnoughBalanceException;
import com.sportbetapp.exception.NotExistingGuessException;
import com.sportbetapp.service.betting.BetService;
import com.sportbetapp.service.betting.GuessService;
import com.sportbetapp.service.betting.SportEventService;
import com.sportbetapp.service.betting.WagerService;
import com.sportbetapp.service.user.UserService;
import com.sportbetapp.validator.wager.MakeNewWagerValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class MobileRestClientUserController {

    @Autowired
    private MakeNewWagerValidator makeNewWagerValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private WagerService wagerService;
    @Autowired
    private SportEventService sportEventService;
    @Autowired
    private BetService betService;
    @Autowired
    private GuessService guessService;

    @GetMapping("/user/account")
    public ResponseEntity<MobileUserDto> userAccount() {
        User currentUser = userService.obtainCurrentPrincipleUser();
        MobileUserDto dto = UserMapper.toMobileDto(currentUser);
        dto.setNumOfWagers(wagerService.countWagersByUser(currentUser));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/user/update-info")
    public ResponseEntity updateUserInfo(@RequestBody MobileUserDto mobileUserDto) {
        UserDto userDto = UserMapper.toUserDto(mobileUserDto);
        userService.updateUserInfo(userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/bet-coefficients")
    public ResponseEntity<List<BetCoefficientMobileDto>> betCoefficients() {
        List<BetCoefficientMobileDto> list = Stream.of(BetType.values())
                .map(betType -> new BetCoefficientMobileDto(betType.getValue(), betType.getCoefficient()))
                .collect(toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/user/wagers")
    public ResponseEntity<List<MobileWagerDto>> listAllWagers() {
        User user = userService.obtainCurrentPrincipleUser();
        List<MobileWagerDto> wagers = wagerService.findAllByUser(user)
                .stream()
                .map(WagerMapper::toMobileWagerDto)
                .collect(toList());

        return new ResponseEntity<>(wagers, HttpStatus.OK);
    }

    @GetMapping("user/events")
    public ResponseEntity<List<MobileSportEventDto>> listAllEvents() {
        List<MobileSportEventDto> events = sportEventService.findAll()
                .stream()
                .map(SportEventMapper::toSportEventMobileDto)
                .collect(toList());
        return new ResponseEntity<>(events, HttpStatus.OK);
    }


    @PostMapping("/user/make-bet")
    public ResponseEntity createNewWager(@RequestBody MakeBetMobileDto makeBetMobileDto)
            throws EventAlreadyStartedException, EventAlreadyPredictedException,
            NotEnoughBalanceException, NotExistingGuessException {
        CreateWagerDto dto = MakeBetMapper.toMobileDto(makeBetMobileDto);
        dto.setBetType(BetType.EXACT_GAME_SCORE);
        dto.setSportType(SportType.FOOTBALL.getValue());
        wagerService.createWagerWithGuess(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/wager/{idWager}")
    public ResponseEntity deleteWager(@PathVariable Long idWager)
            throws EventAlreadyStartedException, EventAlreadyPredictedException {
        wagerService.deleteWager(idWager);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
