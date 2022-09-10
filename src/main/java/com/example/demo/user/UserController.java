package com.example.demo.user;

import com.example.demo.validator.UserDataValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final UserDataValidator userDataValidator;

    @GetMapping("/register")
    public String registration(Model model) {
        model.addAttribute("title", "Registration");
        model.addAttribute("message", "Register for using note service");
        return "register";
    }


    @PostMapping("/register")
    public String registrationUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        userDataValidator.validate(user,result);

        String error;
        if(result.hasErrors()){
//            if(result.hasFieldErrors("username")) {
//                error = "Someone already has that username";
//                model.addAttribute("error", error);
//            }
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
