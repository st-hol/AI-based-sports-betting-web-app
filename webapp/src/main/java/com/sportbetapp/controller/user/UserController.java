package com.sportbetapp.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.betting.BetDto;
import com.sportbetapp.dto.betting.CreateWagerDto;
import com.sportbetapp.dto.user.UserDto;
import com.sportbetapp.exception.EventAlreadyStartedException;
import com.sportbetapp.exception.NotEnoughBalanceException;
import com.sportbetapp.service.betting.BetService;
import com.sportbetapp.service.betting.GuessService;
import com.sportbetapp.service.betting.SportEventService;
import com.sportbetapp.service.user.UserService;
import com.sportbetapp.service.betting.WagerService;
import com.sportbetapp.validator.wager.MakeNewWagerValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

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


    @GetMapping({"/home", "/"})
    public String home(Model model) {
        model.addAttribute("user", userService.obtainCurrentPrincipleUser());
        return "user/home";
    }


    @PutMapping("/home/update-info")
    public String updateUserInfo(UserDto userDto) {
        userService.updateUserInfo(userDto);
        return "redirect:/user/home";
    }

    @GetMapping("/events")
    public String listAllEvents(Model model) {
        model.addAttribute("events", sportEventService.findAll());
        return "user/events";
    }

    @GetMapping("/wagers")
    public String listAllWagers(Model model) {
        model.addAttribute("wagers", wagerService.findAllByUser(userService.obtainCurrentPrincipleUser()));
        return "user/wagers";
    }

    @GetMapping("/make-wager")
    public String formWager(Model model,
                            @RequestParam(value = "eventId") Long eventId) {
        model.addAttribute("guessWagerForm", new CreateWagerDto());
        model.addAttribute("bets", betService.populateStandardBetsForNewSportEvent());
        model.addAttribute("event", sportEventService.findById(eventId));
//         model.addAttribute("user", userService.obtainCurrentPrincipleUser());
        model.addAttribute("user", new User());
        return "/user/make-wager";
    }

    @PostMapping("/make-wager")
    public String createNewWager(BindingResult bindingResult,
                                 @ModelAttribute("guessWagerForm") CreateWagerDto wagerDto)
            throws NotEnoughBalanceException {

        makeNewWagerValidator.validate(wagerDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("createSportEventForm. form had errors.");
//            model.addAttribute("listOfTypes", sportEventService.findAllSportTypes());
            return "admin/create-sport-event";
        }
        userService.makeWager(wagerDto);
        return "redirect:/user/wagers";
    }

//    @GetMapping("/bets")
//    public String listAllBetsByEvent(Model model,
//                                     @RequestParam(value = "eventId") Long eventId) {
//        SportEvent sportEvent = sportEventService.findById(eventId);
//        model.addAttribute("event", sportEvent);
//        model.addAttribute("bets", betService.findAllBySportEvent(sportEvent));
//        return "player/bets";
//    }

    @GetMapping("/wager")
    public String formWager(Model model,
                            @RequestParam(value = "betId") Long betId,
                            @RequestParam(value = "eventId") Long eventId) {
        Bet bet = betService.findById(betId);
        model.addAttribute("event", sportEventService.findById(eventId));
        model.addAttribute("bet", bet);
        model.addAttribute("user", userService.obtainCurrentPrincipleUser());
        model.addAttribute("outcomes", guessService.findAllByBet(bet));
        model.addAttribute("wagerForm", new CreateWagerDto());
        return "/user/new-wager";
    }

    @PostMapping("/wager")
    public String createWager(@ModelAttribute("wagerForm") CreateWagerDto wagerDto) throws NotEnoughBalanceException {
        userService.makeWager(wagerDto);
        return "redirect:/user/wagers";
    }

    @DeleteMapping("/wager/{idWager}")
    public String deleteWager(@PathVariable Long idWager) throws EventAlreadyStartedException {
        wagerService.deleteById(idWager);
        return "redirect:/user/wagers";
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public String handleNotEnoughBalanceException(Model model, Exception exception) {
        log.error("Not enough money for wager: {}", exception.getMessage());
        model.addAttribute("notEnoughMoney", true);
        CreateWagerDto requestForm = ((NotEnoughBalanceException) exception).getWagerDto();
//        Long betId = requestForm.getBetId();
        Long betId = 1L;
        Long sportEventId = requestForm.getSportEventId();
        return formWager(model, betId, sportEventId);
    }

    @ExceptionHandler(EventAlreadyStartedException.class)
    public String handleEventAlreadyStartedException(Model model, Exception exception) {
        log.error("Event is already started. Can not delete wager. {}", exception.getMessage());
        model.addAttribute("eventAlreadyStarted", true);
        return listAllWagers(model);
    }

}
