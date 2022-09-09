package com.example.demo.noteController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String getMainPage(){
        return "redirect:/note/list";
    }
}
