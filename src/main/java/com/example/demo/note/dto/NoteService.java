package com.example.demo.note.dto;

import com.example.demo.note.entity.Access;
import com.example.demo.note.entity.Note;
import com.example.demo.user.dto.UserRepository;
import com.example.demo.user.entity.User;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public record NoteService(NoteRepository noteRepository,
                          UserRepository userRepository) {

    public void createNote(Map<String, String> map) throws Exception {
        String name = map.get("name");
        String content = map.get("content");
        Access access = Access.valueOf(map.get("access"));
        String userId = map.get("userId");

        Optional<User> user = userRepository.findById(UUID.fromString(userId));

        Note note = new Note();
        note.setName(name);
        note.setContent(content);
        note.setAccess(access);
        note.setUser(user.get());

        if (!isValidNotes(note)){
            throw new Exception("Wrong format of name or content in Note!");
        }

        noteRepository.save(note);
    }

    public void update(Map<String, String> map) {
        String id = map.get("id");
        String name = map.get("name");
        String content = map.get("content");
        Access access = Access.valueOf(map.get("access"));

        Note note = noteRepository.findNoteById(UUID.fromString(id));
        note.setName(name);
        note.setAccess(access);
        note.setContent(content);

        noteRepository.save(note);

    }

    public void deleteById(Map<String, String> map) {
        String id = map.get("id");
        noteRepository.deleteById(UUID.fromString(id));
    }

    public Note getNoteById(UUID id) {
        return noteRepository.findNoteById(id);
    }

    public List<Note> getAllUsersNote(User user) {
        return noteRepository.findByUserIs(user);
    }

    public Note parseNoteContentToHtml(Note note) {
        Parser parser = new Parser.Builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder()
                .softbreak("<br>")
                .build();
        String content = note.getContent();
        Node document = parser.parse(content == null ? "" : content);
        String contentHtml = renderer.render(document);
        note.setContent(contentHtml);
        return note;
    }

    public String getSharePage(String noteId, Model model) {
        try {
            Note note = noteRepository.findNoteById(UUID.fromString(noteId));
            if (note.getAccess().equals(Access.PRIVATE)) {
                return "redirect:/note/share/error";
            } else {
                model.addAttribute("note", parseNoteContentToHtml(note));
                return "share_read";
            }
        } catch (Exception e) {
            return "redirect:/note/share/error";
        }
    }

    public boolean isValidNotes(Note note){
        boolean content1 = note.getContent().length() >= 5;
        boolean content2 = note.getContent().length() <= 10000;
        boolean name1 = note.getName().length() >= 5;
        boolean name2 = note.getName().length() <= 100;
        return content1 && content2 && name1 && name2;
    }

    public List<Note> parseNoteContentToSimpleView(List<Note> notes){
        notes.forEach(note -> {
            String content = note.getContent() == null ? "" : note.getContent();
            if (content.length() > 100){
                content = content.substring(0, 100);
            }
            Parser parser = new Parser.Builder().build();
            TextContentRenderer renderer = TextContentRenderer.builder().stripNewlines(true).build();
            Node document = parser.parse(content);
            note.setContent(renderer.render(document) + "...");
        });
        return notes;
    }

    public boolean existNote(UUID id){
        return noteRepository.existsById(id);
    }

    public boolean isUserOwnerNote(User user, Note note) throws Exception {
        if (note.getUser().getId().equals(user.getId())){
            return true;
        } else {
            throw new Exception("User "
                    + user.getId().toString()
                    + " not owner of Note "
                    + note.getId().toString());
        }
    }

}