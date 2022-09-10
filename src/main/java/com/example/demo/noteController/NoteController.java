package com.example.demo.noteController;

import com.example.demo.auth.CustomUserDetails;
import com.example.demo.note.Note;
import com.example.demo.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/note")
@Controller
public class NoteController {
    private final NoteService noteService;
    @GetMapping("/list")
    public ModelAndView getNote(HttpServletRequest req){
        ModelAndView modelAndView = new ModelAndView("note_list");
        modelAndView.addObject("notes", noteService.getAllNote());
        return modelAndView;
    }

    @PostMapping("/create")
    public void addNote(@RequestParam Map<String,String> map, HttpServletResponse resp, Authentication authentication){
        map.put("userId", ((CustomUserDetails)authentication.getPrincipal()).getId().toString());
        noteService.createNote(map,resp);
    }

    @GetMapping("/create")
    public ModelAndView addNote(){
        ModelAndView modelAndView = new ModelAndView("note_create");
        return modelAndView;
    }

    @PostMapping("/delete")
    public void deleteNote(@RequestParam Map<String,String> map, HttpServletResponse resp){
        noteService.deleteById(map,resp);
    }

    @GetMapping("/edit")
    public ModelAndView getEditPage(@RequestParam Map<String,String> map){
        ModelAndView modelAndView = new ModelAndView("note_update");
        UUID id = UUID.fromString(map.get("id"));
        modelAndView.addObject("note",noteService.getNoteById(id));
        return modelAndView;
    }

    @PostMapping("/edit")
    public void changeNote(@RequestParam Map<String,String> map, HttpServletResponse resp){
        noteService.update(map,resp);
    }

    @PostMapping("/copyLink")
    public void copyLink(@RequestParam Map<String,String> map,HttpServletRequest req,HttpServletResponse resp){
        noteService.copyLink(map,req,resp);
    }

    @GetMapping("/share/error")
    public ModelAndView getShareError(){
        ModelAndView modelAndView = new ModelAndView("share_error");
        return modelAndView;
    }

    @GetMapping("/share/*")
    public ModelAndView getShareNote(HttpServletRequest req, HttpServletResponse resp){
        return noteService.getSharePage(req,resp);
    }
}