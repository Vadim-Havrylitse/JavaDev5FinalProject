package com.example.demo.validator;

import com.example.demo.user.User;
import com.example.demo.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:validation.properties")
public class UserDataValidator implements Validator {

    private final UserService userService;
    private final Environment env;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @SuppressWarnings({"ConstantConditions", "NullableProblems"})
    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        String errorCode = HttpStatus.BAD_REQUEST.toString();
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", errorCode, env.getProperty("NotEmpty"));
        if (user.getUsername().length() < 5 || user.getUsername().length() > 50) {
            errors.rejectValue("username", errorCode,env.getProperty("Size.user.username"));
        }
        if (userService.getByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", errorCode, env.getProperty("Duplicate.user.username"));
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", errorCode, env.getProperty("NotEmpty"));
        if (user.getPassword().length() < 8 || user.getPassword().length() > 100) {
            errors.rejectValue("password", errorCode,  env.getProperty("Size.user.password"));
        }
    }
}
