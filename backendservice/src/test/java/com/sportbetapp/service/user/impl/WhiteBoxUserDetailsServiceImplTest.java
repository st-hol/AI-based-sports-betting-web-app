package com.sportbetapp.service.user.impl;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sportbetapp.domain.user.role.Role;
import com.sportbetapp.repository.user.UserRepository;


/**
 * White Box Testing is a software testing method in which the
 * internal structure/ design/ implementation of the item being
 * tested is known to the tester.
 *
 * @author Stanislav_Holovachuk
 */
@RunWith(MockitoJUnitRunner.class)
public class WhiteBoxUserDetailsServiceImplTest {

    private static final String NAME_GMAIL_COM = "name@gmail.com";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl serviceUnderTest;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Captor
    private ArgumentCaptor<String> usernameCaptor;

    @Test
    public void loadUserByUsername_captureUsernameParameter() {
        when(userRepository.findByEmail(usernameCaptor.capture()))
                .thenReturn(prepareUser("123"));

        serviceUnderTest.loadUserByUsername(NAME_GMAIL_COM);

        assertThat(usernameCaptor.getValue(), is(NAME_GMAIL_COM));
        verify(userRepository).findByEmail(NAME_GMAIL_COM);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void loadUserByUsername_throwNotFound() {
        //given
        when(userRepository.findByEmail(NAME_GMAIL_COM))
                .thenReturn(null);

        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(NAME_GMAIL_COM);

        //when
        serviceUnderTest.loadUserByUsername(NAME_GMAIL_COM);
    }

    @Test
    public void loadUserByUsername_proceedNormally() {
        when(userRepository.findByEmail(NAME_GMAIL_COM))
                .thenReturn(prepareUser("123"));

        User actual =
                (User) serviceUnderTest.loadUserByUsername(NAME_GMAIL_COM);

        assertThat(actual, is(notNullValue()));
    }


    @Test
    public void withoutPassword_loadUserByUsername_throwNotFound() {
        //given
        when(userRepository.findByEmail(NAME_GMAIL_COM))
                .thenReturn(prepareUser(null));

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Cannot pass null or empty values to constructor");

        //when
        serviceUnderTest.loadUserByUsername(NAME_GMAIL_COM);
    }

    @Test
    public void withoutUsername_loadUserByUsername_throwNotFound() {
        //given
        when(userRepository.findByEmail(NAME_GMAIL_COM))
                .thenReturn(prepareUser(null));

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Cannot pass null or empty values to constructor");

        //when
        serviceUnderTest.loadUserByUsername(NAME_GMAIL_COM);
    }

    private com.sportbetapp.domain.user.User prepareUser(String pass) {
        return new com.sportbetapp.domain.user.User() {
            {
                setId(0L);
                setName("Name");
                setPassword(pass);
                setEmail(NAME_GMAIL_COM);
                setRoles(Set.of(prepareRole(0L, "ADMIN"), prepareRole(1L, "CLIENT")));
            }
        };
    }

    private Role prepareRole(long i, String name) {
        return new Role() {{
            setId(i);
            setName(name);
        }};
    }
}
