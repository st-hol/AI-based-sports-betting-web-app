package com.epam.training.sportsbetting.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.epam.training.sportsbetting.domain.user.User;
import com.epam.training.sportsbetting.dto.PlayerDto;
import com.epam.training.sportsbetting.service.UserService;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("squid:S2068")
@Slf4j
@Component
public class UserValidator implements Validator {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_CONFIRM = "passwordConfirm";

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w\\d._-]+@[\\w\\d.-]+\\.[\\w\\d]{2,6}$");

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PlayerDto user = (PlayerDto) o;

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
        log.error("{}", errors);
    }

}
