package com.sportbetapp.controller.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.sportbetapp.domain.betting.SportEvent;
import com.sportbetapp.domain.betting.Wager;
import com.sportbetapp.domain.technical.Pager;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.betting.CreateWagerDto;
import com.sportbetapp.dto.betting.PointsTurnoverStatisticDto;
import com.sportbetapp.dto.user.UserDto;
import com.sportbetapp.exception.EventAlreadyPredictedException;
import com.sportbetapp.exception.EventAlreadyStartedException;
import com.sportbetapp.exception.NotEnoughBalanceException;
import com.sportbetapp.exception.NotExistingGuessException;
import com.sportbetapp.service.betting.BetService;
import com.sportbetapp.service.betting.GuessService;
import com.sportbetapp.service.betting.PointsTurnoverStatisticService;
import com.sportbetapp.service.betting.SportEventService;
import com.sportbetapp.service.betting.WagerService;
import com.sportbetapp.service.user.UserService;
import com.sportbetapp.validator.wager.MakeNewWagerValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 3;
    private static final int[] PAGE_SIZES = {3, 5, 10, 15, 20, 30};

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
    @Autowired
    private PointsTurnoverStatisticService pointsTurnoverStatisticService;


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

    /**
     * Handles all requests
     *
     * @param pageSize - the size of the page
     * @param page     - the page number
     * @return model and view
     */
    @GetMapping("/wagers")
    public String listAllWagers(Model model,
                                @RequestParam("pageSize") Optional<Integer> pageSize,
                                @RequestParam("page") Optional<Integer> page) {

        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        User user = userService.obtainCurrentPrincipleUser();
        Page<Wager> wagers = wagerService.findAllByUserPageable(user, PageRequest.of(evalPage, evalPageSize));
        Pager pager = new Pager(wagers.getTotalPages(), wagers.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("wagers", wagers);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);
        return "user/wagers";
    }

    @GetMapping("/events")
    public String listAllEvents(Model model,
                                @RequestParam("pageSize") Optional<Integer> pageSize,
                                @RequestParam("page") Optional<Integer> page) {

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<SportEvent> events = sportEventService.findAllPageable(PageRequest.of(evalPage, evalPageSize));
        Pager pager = new Pager(events.getTotalPages(), events.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("events", events);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);
        return "user/events";
    }

    @GetMapping("/make-wager")
    public String formNewWager(Model model,
                               @RequestParam(value = "eventId") Long eventId) {
        model.addAttribute("guessWagerForm", new CreateWagerDto());

        model.addAttribute("bets", betService.populateStandardBetsForNewSportEvent());
        model.addAttribute("event", sportEventService.findById(eventId));
        model.addAttribute("user", userService.obtainCurrentPrincipleUser());

        return "/user/make-wager";
    }

    @PostMapping("/make-wager")
    public String createNewWager(@ModelAttribute("guessWagerForm") CreateWagerDto wagerDto,
                                 BindingResult bindingResult, Model model)
            throws NotEnoughBalanceException, NotExistingGuessException,
            EventAlreadyStartedException, EventAlreadyPredictedException {

        makeNewWagerValidator.validate(wagerDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("createSportEventForm. form had errors.");
            model.addAttribute("bets", betService.populateStandardBetsForNewSportEvent());
            model.addAttribute("event", sportEventService.findById(wagerDto.getSportEventId()));
            model.addAttribute("betType", wagerDto.getBetType());
            model.addAttribute("user", userService.obtainCurrentPrincipleUser());
            return "user/make-wager";
        }
        wagerService.createWagerWithGuess(wagerDto);
        return "redirect:/user/wagers";
    }

    @DeleteMapping("/wager/{idWager}")
    public String deleteWager(@PathVariable Long idWager)
            throws EventAlreadyStartedException, EventAlreadyPredictedException {
        wagerService.deleteWager(idWager);
        return "redirect:/user/wagers";
    }

    @GetMapping("/stats")
    public String listStats(Model model,
                            @RequestParam("pageSize") Optional<Integer> pageSize,
                            @RequestParam("page") Optional<Integer> page,
                            @RequestParam(value = "search") Optional<String> search) {

        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<PointsTurnoverStatisticDto> topUsersSorted =
                pointsTurnoverStatisticService.findTopUsersSortedPageable(search.orElse(""), PageRequest.of(evalPage, evalPageSize));
        Pager pager = new Pager(topUsersSorted.getTotalPages(), topUsersSorted.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("topUsersSorted", topUsersSorted);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);
        return "user/stats";
    }


    @ExceptionHandler(NotEnoughBalanceException.class)
    public String handleNotEnoughBalanceException(Model model, Exception exception) {
        log.error("Not enough money for wager: {}", exception.getMessage());
        model.addAttribute("notEnoughMoney", true);
        CreateWagerDto requestForm = ((NotEnoughBalanceException) exception).getWagerDto();
        Long sportEventId = requestForm.getSportEventId();
        return formNewWager(model, sportEventId);
    }

    @ExceptionHandler(EventAlreadyStartedException.class)
    public String handleEventAlreadyStartedException(Model model, Exception exception) {
        log.error("Event is already started. {}", exception.getMessage());
        model.addAttribute("eventAlreadyStarted", true);
        return listAllWagers(model, Optional.empty(), Optional.empty());
    }

    @ExceptionHandler(EventAlreadyPredictedException.class)
    public String handleEventAlreadyPredictedException(Model model, Exception exception) {
        log.error("Event is already predicted. {}", exception.getMessage());
        model.addAttribute("eventAlreadyPredicted", true);
        return listAllWagers(model, Optional.empty(), Optional.empty());
    }

}
