package com.example.demo.note;

import com.example.demo.auth.CustomUserDetails;
import com.example.demo.note.dto.NoteService;
import com.example.demo.note.entity.Note;
import com.example.demo.user.entity.User;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/note")
@Controller
@PropertySource("classpath:validation.properties")
public record NoteController(NoteService noteService, Environment env) {
    @GetMapping("/list")
    public String getNote(Authentication authentication, Model model) {
        List<Note> allUsersNote = noteService.getAllUsersNote(getUserFromAuthentication(authentication));
        int sizeAllUsersNote = allUsersNote.size();
        if (sizeAllUsersNote != 0){
            model.addAttribute("notes", noteService.parseNoteContentToSimpleView(allUsersNote));
        } else {
            model.addAttribute("notes", allUsersNote);
        }
        model.addAttribute("countNotes", sizeAllUsersNote);
        return "note_list";
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
            model.addAttribute("message", env.getProperty("Size.note"));
            return "any_error";
        }
    }

    @GetMapping("/create")
    public String addNote() {
        return "note_create";
    }

    @PostMapping("/delete")
    public String deleteNote(@RequestParam Map<String, String> map, HttpServletResponse resp) {
        try {
            noteService.deleteById(map);
            return "redirect:/note/list";
        } catch (Exception e){
            e.printStackTrace();
            return "any_error";
        }
    }

    @GetMapping("/edit")
    public String getEditPage(@RequestParam Map<String, String> map, Model model) {
        try {
            UUID id = UUID.fromString(map.get("id"));
            if(!noteService.existNote(id)){
                throw new Exception("Note " + map.get("id") + " does not exist!");
            }
            model.addAttribute("note", noteService.getNoteById(id));
            return "note_update";
        } catch (Exception e) {
            e.printStackTrace();
            return "any_error";
        }
    }

    @PostMapping("/edit")
    public String changeNote(@RequestParam Map<String, String> map) {
        try {
            noteService.update(map);
            return "redirect:/note/list";
        } catch (Exception e) {
            e.printStackTrace();
            return "any_error";
        }
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
        public String getReadPage(@PathVariable String noteId, Authentication authentication, Model model) {
            try{
                Note note = noteService.getNoteById(UUID.fromString(noteId));
                User userAuth = getUserFromAuthentication(authentication);
                if (note.getUser().getId().equals(userAuth.getId())){
                    model.addAttribute("note", noteService.parseNoteContentToHtml(note));
                    return "note_read";
                } else {
                    throw new Exception("User "
                            + userAuth.getId().toString()
                            + " not owner of Note "
                            + noteId);
                }
            } catch (Exception e){
                System.err.println(e.getMessage());
                return "any_error";
            }
        }

        private User getUserFromAuthentication(Authentication authentication) {
            return new User(((CustomUserDetails) authentication.getPrincipal()).getId());
        }
    }