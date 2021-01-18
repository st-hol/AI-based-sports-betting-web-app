package com.sportbetapp.service.betting.impl;


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
import com.sportbetapp.repository.user.UserRepository;
import com.sportbetapp.service.betting.WagerService;
import com.sportbetapp.service.user.impl.UserServiceImpl;

class UserServiceImplTest {

    private static final String EMAIL = "email@test.ua";

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WagerService wagerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByUsername() {
        User expected = new User();
        expected.setId(1L);
        expected.setEmail(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(expected);

        assertThat(userService.findByUsername(EMAIL).getEmail(), is(EMAIL));
        assertThat(userService.findByUsername(EMAIL).getId(), is(1L));
    }

}
