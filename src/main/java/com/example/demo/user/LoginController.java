package com.example.demo.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/")
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false, defaultValue = "false") Boolean error,
                        @RequestParam(name = "logout", required = false, defaultValue = "false") String logout,
                        Model model) {
        log.info("LOGIN GET");

        model.addAttribute("title", "Login");
        model.addAttribute("message", "To start using login or create an account");
        model.addAttribute("error", error);

        if (error != null) log.info("You login or password not corrected, check it and try again");
        if (logout != null) log.info("You have been logged out");
        //return login url

        return "login";
    }
}