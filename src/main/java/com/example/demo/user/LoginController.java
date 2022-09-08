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


    @GetMapping("/")
    public Object greeting() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Your username or password is invalid");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out");
        }
        log.info("I'm rendering the \"login\" page");
        return "login";
    }
}