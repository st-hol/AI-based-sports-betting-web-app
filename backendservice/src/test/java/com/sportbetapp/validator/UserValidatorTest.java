package com.sportbetapp.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.user.UserDto;
import com.sportbetapp.service.user.UserService;


/**
 * @author Stanislav_Holovachuk, Olena_Patsevko
 */
@RunWith(MockitoJUnitRunner.class)
public class UserValidatorTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    public void givenCorrectInputData_whenCheckingWithValidator_thenShouldBeNoErrors() {
        //everything to make it valid
        UserDto validUser = new UserDto();
        validUser.setPassword("123");
        validUser.setPasswordConfirm("123");
        validUser.setEmail("testusername@gmail.com");

        Mockito.when(userService.findByUsername("testusername@gmail.com"))
                .thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(validUser, "validUser");
        userValidator.validate(validUser, errors);

        assertFalse(errors.hasErrors());
    }


    @Test
    public void givenInvalidUsername_whenCheckingWithValidator_thenShouldBeUsernameError() {
        //everything to make it valid except the username
        UserDto userWithInvalidUsername = new UserDto();
        userWithInvalidUsername.setPassword("123");
        userWithInvalidUsername.setPasswordConfirm("123");
        userWithInvalidUsername.setEmail("1");

        Mockito.when(userService.findByUsername("1"))
                .thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(userWithInvalidUsername, "userWithInvalidUsername");
        userValidator.validate(userWithInvalidUsername, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("email"));
    }

    @Test
    public void givenExistingUsername_whenCheckingWithValidator_thenShouldBeUsernameError() {
        UserDto userWithExistingUsername = new UserDto();
        userWithExistingUsername.setPassword("123");
        userWithExistingUsername.setPasswordConfirm("123");
        userWithExistingUsername.setEmail("testusername");

        Mockito.when(userService.findByUsername("testusername"))
                .thenReturn(new User());

        Errors errors = new BeanPropertyBindingResult(userWithExistingUsername, "userWithExistingUsername");
        userValidator.validate(userWithExistingUsername, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("email"));
    }

    @Test
    public void givenInvalidPassword_whenCheckingWithValidator_thenShouldBePasswordError() {
        UserDto userWithInvalidPassword = new UserDto();
        userWithInvalidPassword.setPassword("#");
        userWithInvalidPassword.setPasswordConfirm("#");
        userWithInvalidPassword.setEmail("testusername");

        Mockito.when(userService.findByUsername("testusername"))
                .thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(userWithInvalidPassword, "userWithInvalidPassword");
        userValidator.validate(userWithInvalidPassword, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("password"));
    }

    @Test
    public void givenDifferentPasswords_whenCheckingWithValidator_thenShouldBePasswordError() {
        UserDto userWithDifferentPasswords = new UserDto();
        userWithDifferentPasswords.setPassword("password_itself");
        userWithDifferentPasswords.setPasswordConfirm("password_confirmation");
        userWithDifferentPasswords.setEmail("testusername");

        Mockito.when(userService.findByUsername("testusername"))
                .thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(userWithDifferentPasswords, "userWithDifferentPasswords");
        userValidator.validate(userWithDifferentPasswords, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("passwordConfirm"));
    }
}
