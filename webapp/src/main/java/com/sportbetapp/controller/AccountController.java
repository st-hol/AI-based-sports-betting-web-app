package com.sportbetapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sportbetapp.dto.UserDto;
import com.sportbetapp.service.SecurityService;
import com.sportbetapp.service.UserService;
import com.sportbetapp.validator.UserValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AccountController {

    @Autowired
    private UserValidator userValidator;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserService userService;

    @GetMapping("/la")
    public String ron(Model model) {
        model.addAttribute("userForm", new UserDto());
        return "common/landing";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserDto());
        return "common/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") UserDto userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("reg. form had errors.");
            return "common/registration";
        }
        userService.registerUser(userForm);
        securityService.autoLoginAfterReg(userForm.getEmail(), userForm.getPasswordConfirm());
        return "redirect:/player/home";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");
        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        return "common/login";
    }

}
