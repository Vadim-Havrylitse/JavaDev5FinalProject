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
    public String deleteNote(@RequestParam Map<String, String> map, Authentication authentication) {
        try {
            Note note = noteService.getNoteById(UUID.fromString(map.get("id")));
            User userAuth = getUserFromAuthentication(authentication);
            if (noteService.isUserOwnerNote(userAuth, note)) {
                noteService.deleteById(map);
                return "redirect:/note/list";
            }else {
                return "";
            }
        } catch (Exception e){
            e.printStackTrace();
            return "any_error";
        }
    }

    @GetMapping("/edit")
    public String getEditPage(@RequestParam Map<String, String> map, Model model, Authentication authentication) {
        try {
            UUID noteId = UUID.fromString(map.get("id"));
            if(!noteService.existNote(noteId)){
                throw new Exception("Note " + map.get("id") + " does not exist!");
            }
            Note note = noteService.getNoteById(noteId);
            User userAuth = getUserFromAuthentication(authentication);
            if (noteService.isUserOwnerNote(userAuth, note)){
                model.addAttribute("note", note);
                return "note_update";
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "any_error";
        }
    }

    @PostMapping("/edit")
    public String changeNote(@RequestParam Map<String, String> map, Authentication authentication) {
        try {
            Note note = noteService.getNoteById(UUID.fromString(map.get("id")));
            User userAuth = getUserFromAuthentication(authentication);
            if (noteService.isUserOwnerNote(userAuth, note)){
                noteService.update(map);
                return "redirect:/note/list";
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "any_error";
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
            if (noteService.isUserOwnerNote(userAuth, note)){
                model.addAttribute("note", noteService.parseNoteContentToHtml(note));
                return "note_read";
            } else {
                return "";
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