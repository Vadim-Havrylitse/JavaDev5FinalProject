package com.example.demo.note;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public void createNote(Map<String,String> map, HttpServletResponse resp){
        String name = map.get("name");
        String content = map.get("content");
        Access access = Access.valueOf(map.get("access"));
        String userId = map.get("userId");

        Optional<User> user = userRepository.findById(UUID.fromString(userId));

        Note note = new Note();
        note.setName(name);
        note.setContent(content);
        note.setAccess(access);
        note.setUserId(user.get());

        noteRepository.save(note);

        try {
            resp.sendRedirect("/note");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Map<String,String> map, HttpServletResponse resp){
        String id = map.get("id");
        String name = map.get("name");
        String content = map.get("content");
        Access access = Access.valueOf(map.get("access"));

        Note note = noteRepository.findNoteById(UUID.fromString(id));
        note.setName(name);
        note.setAccess(access);
        note.setContent(content);

        noteRepository.save(note);

        try {
            resp.sendRedirect("/note");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(Map<String,String> map, HttpServletResponse resp){
        String id = map.get("id");
        noteRepository.deleteById(UUID.fromString(id));

        try {
            resp.sendRedirect("/note");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Note> getNotesByUserId(UUID userId){
        return noteRepository.getNotesByUserId(userId);
    }

    public Note getNoteById(UUID id){
        return noteRepository.findNoteById(id);
    }

    public List<Note> getAllNote(){
        return noteRepository.findAll();
    }
}
