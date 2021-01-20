package com.sportbetapp.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sportbetapp.dto.user.UserDto;
import com.sportbetapp.service.betting.BetTypeService;
import com.sportbetapp.service.scheduling.UpcomingEventToPredictService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CommonController {

    @Autowired
    private UpcomingEventToPredictService upcomingEventToPredictService;
    @Autowired
    private BetTypeService betTypeService;


    @GetMapping({"/", "/welcome"})
    public String la(Model model) {
        model.addAttribute("upcomingEvents", upcomingEventToPredictService.findTop3Upcoming());
        model.addAttribute("betTypes", betTypeService.getAllBetTypes());
        model.addAttribute("userForm", new UserDto());
        return "common/landing";
    }
}
