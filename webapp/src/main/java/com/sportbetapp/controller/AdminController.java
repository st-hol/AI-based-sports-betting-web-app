package com.sportbetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sportbetapp.dto.OutcomeOddDto;
import com.sportbetapp.dto.UserDto;
import com.sportbetapp.dto.ProcessResultDto;
import com.sportbetapp.dto.SportEventDto;
import com.sportbetapp.service.PopulateDataRestService;
import com.sportbetapp.service.SportEventService;
import com.sportbetapp.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SportEventService sportEventService;
    @Autowired
    private UserService userService;
    @Autowired
    private PopulateDataRestService populateDataRestService;


    @PostMapping("/event")
    public ResponseEntity<SportEventDto> createSportEvent(@RequestBody SportEventDto sportEventDto) {
        return new ResponseEntity<>(populateDataRestService.populateSportEvent(sportEventDto), HttpStatus.CREATED);
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<UserDto> findUser(@PathVariable Long id) {
        UserDto userDto = userService.convertToUserDto(userService.findById(id));
        return new ResponseEntity<>(userDto, HttpStatus.FOUND);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<SportEventDto> findEvent(@PathVariable Long id) {
        SportEventDto sportEventDto = populateDataRestService.toSportEventDto(sportEventService.findById(id));
        return new ResponseEntity<>(sportEventDto, HttpStatus.FOUND);
    }

    @PostMapping("/outcomeOdd")
    public ResponseEntity<OutcomeOddDto> addOddToOutcome(@RequestBody OutcomeOddDto outcomeOddDto) {
        return new ResponseEntity<>(populateDataRestService.populateOutcomeOddToOutcome(outcomeOddDto),
                HttpStatus.CREATED);
    }

    @PutMapping("/result")
    public ResponseEntity<ProcessResultDto> processResult(@RequestBody ProcessResultDto processResultDto) {
        return new ResponseEntity<>(populateDataRestService.processResult(processResultDto), HttpStatus.OK);
    }
}
