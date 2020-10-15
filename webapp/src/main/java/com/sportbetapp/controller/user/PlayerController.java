package com.sportbetapp.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sportbetapp.domain.betting.Bet;
import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.dto.betting.CreateWagerDto;
import com.sportbetapp.dto.user.UserDto;
import com.sportbetapp.exception.EventAlreadyStartedException;
import com.sportbetapp.exception.NotEnoughBalanceException;
import com.sportbetapp.service.betting.BetService;
import com.sportbetapp.service.betting.GuessService;
import com.sportbetapp.service.betting.SportEventService;
import com.sportbetapp.service.user.UserService;
import com.sportbetapp.service.betting.WagerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/player")
public class PlayerController {

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
        return "player/home";
    }


    @PutMapping("/home/update-info")
    public String updateUserInfo(UserDto userDto) {
        userService.updateUserInfo(userDto);
        return "redirect:/player/home";
    }

    @GetMapping("/wagers")
    public String listAllWagers(Model model) {
        model.addAttribute("wagers", wagerService.findAllByUser(userService.obtainCurrentPrincipleUser()));
        return "player/wagers";
    }

    @GetMapping("/events")
    public String listAllEvents(Model model) {
        model.addAttribute("events", sportEventService.findAll());
        return "player/events";
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
        return "/player/new-wager";
    }

    @PostMapping("/wager")
    public String createWager(@ModelAttribute("wagerForm") CreateWagerDto wagerDto) throws NotEnoughBalanceException {
        userService.makeWager(wagerDto);
        return "redirect:/player/wagers";
    }

    @DeleteMapping("/wager/{idWager}")
    public String deleteWager(@PathVariable Long idWager) throws EventAlreadyStartedException {
        wagerService.deleteById(idWager);
        return "redirect:/player/wagers";
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public String handleNotEnoughBalanceException(Model model, Exception exception) {
        log.error("Not enough money for wager: {}", exception.getMessage());
        model.addAttribute("notEnoughMoney", true);
        CreateWagerDto requestForm = ((NotEnoughBalanceException) exception).getWagerDto();
        Long betId = requestForm.getBetId();
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
