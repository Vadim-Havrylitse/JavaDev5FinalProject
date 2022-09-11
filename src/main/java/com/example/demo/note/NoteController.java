package com.example.demo.note;

import com.example.demo.auth.CustomUserDetails;
import com.example.demo.note.dto.NoteService;
import com.example.demo.user.entity.User;
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

@RequestMapping("/note")
@Controller
public record NoteController(NoteService noteService) {
    @GetMapping("/list")
    public ModelAndView getNote(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("note_list");
        modelAndView.addObject("notes", noteService.getAllUsersNote(getUserFromAuthentication(authentication)));
        return modelAndView;
    }

    @PostMapping("/create")
    public void addNote(@RequestParam Map<String, String> map, HttpServletResponse resp, Authentication authentication) {
        UUID activeUserId = getUserFromAuthentication(authentication).getId();
        map.put("userId", String.valueOf(activeUserId));
        noteService.createNote(map, resp);
    }

    @GetMapping("/create")
    public String addNote() {
        return "note_create";
    }

    @PostMapping("/delete")
    public void deleteNote(@RequestParam Map<String, String> map, HttpServletResponse resp) {
        noteService.deleteById(map, resp);
    }

    @GetMapping("/edit")
    public ModelAndView getEditPage(@RequestParam Map<String, String> map) {
        ModelAndView modelAndView = new ModelAndView("note_update");
        UUID id = UUID.fromString(map.get("id"));
        modelAndView.addObject("note", noteService.getNoteById(id));
        return modelAndView;
    }

    @PostMapping("/edit")
    public void changeNote(@RequestParam Map<String, String> map, HttpServletResponse resp) {
        noteService.update(map, resp);
    }

    @PostMapping("/copyLink")
    public void copyLink(@RequestParam Map<String, String> map, HttpServletRequest req, HttpServletResponse resp) {
        noteService.copyLink(map, req, resp);
    }

    @GetMapping("/share/error")
    public String getShareError() {
        return "share_error";
    }

    @GetMapping("/share/*")
    public ModelAndView getShareNote(HttpServletRequest req, HttpServletResponse resp) {
        return noteService.getSharePage(req, resp);
    }

    private User getUserFromAuthentication(Authentication authentication) {
        return new User(((CustomUserDetails) authentication.getPrincipal()).getId());
    }
}