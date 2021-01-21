package com.sportbetapp.controller.common;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sportbetapp.domain.user.User;
import com.sportbetapp.domain.user.role.Role;
import com.sportbetapp.dto.user.UserDto;
import com.sportbetapp.service.betting.BetTypeService;
import com.sportbetapp.service.scheduling.UpcomingEventToPredictService;
import com.sportbetapp.service.user.RoleService;
import com.sportbetapp.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CommonController {

    @Autowired
    private UpcomingEventToPredictService upcomingEventToPredictService;
    @Autowired
    private BetTypeService betTypeService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    @GetMapping({"/", "/welcome"})
    public String la(Model model) {
        model.addAttribute("upcomingEvents", upcomingEventToPredictService.findTop3Upcoming());
        model.addAttribute("betTypes", betTypeService.getAllBetTypes());
        model.addAttribute("userForm", new UserDto());
        return "common/landing";
    }

    @GetMapping({"/default-home"})
    public String defaultHome(Principal principal) {
        if (principal == null) {
            log.info("No user is authorized. Can't go to personal cabinet.");
            return "redirect:/";
        }

        User loggedInUser = userService.findByUsername(principal.getName());
        List<Role> userRoles = roleService.findAllByUser(loggedInUser);

        Role adminRole = roleService.obtainRoleByName("ADMIN");
        Role clientRole = roleService.obtainRoleByName("USER");

        if (userRoles.contains(adminRole)) {
            return "redirect:/admin/home";
        } else if (userRoles.contains(clientRole)) {
            return "redirect:/user/home";
        } else {
            return "redirect:/";
        }

    }
}
