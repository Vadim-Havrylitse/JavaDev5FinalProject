package com.example.demo.noteController;

import com.example.demo.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/note")
@Controller
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public ModelAndView getNote(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("notes", noteService.getAllNote());
        return modelAndView;
    }

    @PostMapping("/create")
    public void addNote(@RequestParam Map<String,String> map, HttpServletResponse resp){
        noteService.createNote(map,resp);
    }

    @PostMapping("/delete")
    public void deleteNote(@RequestParam Map<String,String> map, HttpServletResponse resp){
        noteService.deleteById(map,resp);
    }

    @PostMapping("/edit")
    public void changeNote(@RequestParam Map<String,String> map, HttpServletResponse resp){
        noteService.update(map,resp);
    }
}
