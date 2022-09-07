package com.example.demo.user;

import com.example.demo.exception.ImpossibleActionException;
import com.example.demo.validator.UserDataValidator;
import lombok.RequiredArgsConstructor;
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

//    @PostMapping("/register")
//    public String registrationUser(@Valid User user, BindingResult result) {
//        try {
//            if (result.hasErrors()) return "register";
//            userService.create(user);
//            return "redirect:/login";
//        } catch (ImpossibleActionException e) {
//            log.error("User with this username already exist");
//            //return reg url
//            return "register";
//        }
//    }


    @PostMapping("/register")
    public String registrationUser(@ModelAttribute("user") User user, BindingResult result){
        userDataValidator.validate(user,result);

        if(result.hasErrors()){
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
