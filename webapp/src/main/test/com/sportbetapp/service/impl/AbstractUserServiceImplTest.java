package com.sportbetapp.service.impl;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sportbetapp.domain.user.User;
import com.sportbetapp.repository.UserRepository;
import com.sportbetapp.service.OutcomeOddService;
import com.sportbetapp.service.WagerService;

class AbstractUserServiceImplTest {

    private static final String EMAIL = "email@test.ua";

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WagerService wagerService;
    @Mock
    private OutcomeOddService outcomeOddService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findByUsername() {
        User expected = new User();
        expected.setId(1L);
        expected.setEmail(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(expected);

        assertThat(userService.findByUsername(EMAIL).getEmail(), is(EMAIL));
        assertThat(userService.findByUsername(EMAIL).getId(), is(1L));
    }

    @Test
    void registerUser() {
    }

    @Test
    void obtainCurrentPrincipleUser() {
    }

    @Test
    void updatePlayerInfo() {
    }

    @Test
    void convertToPlayerDto() {
    }

    @Test
    void makeWager() {
    }

    @Test
    void findAllByOutcome() {
    }
}