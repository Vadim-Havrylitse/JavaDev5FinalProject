package com.example.demo.note;

import com.example.demo.auth.CustomUserDetails;
import com.example.demo.note.dto.NoteService;
import com.example.demo.note.entity.Note;
import com.example.demo.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RequestMapping("/note")
@Controller
public record NoteController(NoteService noteService) {
    @GetMapping("/list")
    public ModelAndView getNote(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("note_list");
        List<Note> allUsersNote = noteService.getAllUsersNote(getUserFromAuthentication(authentication));
        modelAndView.addObject("countNotes", allUsersNote.size());
        modelAndView.addObject("notes", allUsersNote);
        return modelAndView;
    }

    @PostMapping("/create")
    public String addNote(@RequestParam Map<String, String> map, Model model, Authentication authentication) {
        UUID activeUserId = getUserFromAuthentication(authentication).getId();
        map.put("userId", String.valueOf(activeUserId));
        try {
            noteService.createNote(map);
            return "redirect:/note/list";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Note`s <b>NAME</b> mast have size between 5 and 100 character " +
                    "and <b>CONTENT</b> size — between 5 and 10000 character!");
            return "any_error";
        }
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
    public String copyLink(@RequestParam Map<String, String> map, HttpServletRequest req) {
        try{
            noteService.copyLink(map, req);
            return "redirect:/note/list";
        } catch (Exception e){
            e.printStackTrace();
            return "share_error";
        }

    }

    @GetMapping("/share/error")
    public String getShareError() {
        return "share_error";
    }

    @GetMapping("/share/{noteId}")
    public String getShareNote(@PathVariable String noteId, Model model) {
        return noteService.getSharePage(noteId, model);
    }

    @GetMapping("/read/{noteId}")
    public ModelAndView getReadPage(@PathVariable String noteId, Authentication authentication) {
        try{
            Note note = noteService.getNoteById(UUID.fromString(noteId));
            User userAuth = getUserFromAuthentication(authentication);
            if (Objects.equals(note.getUserId(), userAuth)){
                ModelAndView model = new ModelAndView("note_read");
                model.addObject("note",
                        noteService.parseNoteContentToHtml(note));
                return model;
            } else {
                throw new Exception("User "
                        + userAuth.getId().toString()
                        + " not owner of Note "
                        + noteId);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ModelAndView("any_error");
        }
    }

    private User getUserFromAuthentication(Authentication authentication) {
        return new User(((CustomUserDetails) authentication.getPrincipal()).getId());
    }
}