package com.sportbetapp.validator;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.user.UserDto;
import com.sportbetapp.service.user.UserService;
import com.sportbetapp.util.Utils;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("squid:S2068")
@Slf4j
@Component
public class UserValidator implements Validator {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_CONFIRM = "passwordConfirm";
    private static final String BIRTH = "birth";

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w\\d._-]+@[\\w\\d.-]+\\.[\\w\\d]{2,6}$");
    private static final int MAX_AGE = 130;
    private static final int ZERO = 0;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserDto user = (UserDto) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, EMAIL, "NotEmpty");
        if (user.getEmail().length() < 2 || user.getEmail().length() > 32) {
            errors.rejectValue(EMAIL, "Size.userForm.username");
        }
        if (user.getEmail() != null && !EMAIL_REGEX.matcher(user.getEmail()).matches()) {
            errors.rejectValue(EMAIL, "Username.email.invalid");
        }
        if (userService.findByUsername(user.getEmail()) != null) {
            errors.rejectValue(EMAIL, "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PASSWORD, "NotEmpty");
        if (user.getPassword().length() < 2 || user.getPassword().length() > 32) {
            errors.rejectValue(PASSWORD, "Size.userForm.password");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue(PASSWORD_CONFIRM, "Diff.userForm.passwordConfirm");
        }

        final int userAge = calculateAge(user.getBirth(), LocalDate.now());
        final boolean invalidAge = userAge > MAX_AGE || userAge == ZERO;
        if (Utils.isAfterOrEq(user.getBirth(), LocalDate.now()) || invalidAge) {
            errors.rejectValue(BIRTH, "Incorrect.birth.date");
        }

        log.error("user validation failed: {}", errors);
    }

    private static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return ZERO;
        }
    }

}
