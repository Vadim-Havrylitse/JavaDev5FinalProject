package com.example.demo.user;

import com.example.demo.user.dto.UserService;
import com.example.demo.user.entity.User;
import com.example.demo.user.validator.UserDataValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping(path = "/users")
public record UserController(UserService userService,
                             UserDataValidator userDataValidator) {

    @GetMapping("/register")
    public String registration(Model model) {
        model.addAttribute("title", "Registration");
        model.addAttribute("message", "Register for using note service");
        return "register";
    }


    @PostMapping("/register")
    public String registrationUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        userDataValidator.validate(user, result);

        String error;
        if (result.hasErrors()) {
            result.getAllErrors().forEach(System.out::println);
            String massage = result.getAllErrors().stream().findFirst().get().getDefaultMessage();

            model.addAttribute("error", massage);
            return "register";
        }
        User user1 = userService.create(user);
        System.out.println("Create user success: " + user1.toString());
        return "redirect:/login";
    }

    @ModelAttribute("user")
    public User defaultUser() {

        return new User();
    }

}
