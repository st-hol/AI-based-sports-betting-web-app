package com.sportbetapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// todo
// scheduler and global params - and mq
// ui -
// email
// integration with payment system - need or not?
// remove orphan guess, - need fix or not?
