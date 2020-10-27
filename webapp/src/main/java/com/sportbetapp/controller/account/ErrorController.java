package com.sportbetapp.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/error")
public class ErrorController {

    @GetMapping(value = "/403")
    public String personalCabinet() {
        log.info("Attempt of unlawful action was prevented. Access denied ");
        return "common/error/403";
    }

}
