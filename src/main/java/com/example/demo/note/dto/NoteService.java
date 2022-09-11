package com.example.demo.note.dto;

import com.example.demo.note.entity.Access;
import com.example.demo.note.entity.Note;
import com.example.demo.user.entity.User;
import com.example.demo.user.dto.UserRepository;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public record NoteService(NoteRepository noteRepository,
                          UserRepository userRepository) {

    public void createNote(Map<String, String> map, HttpServletResponse resp) {
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
            resp.sendRedirect("/note/list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Map<String, String> map, HttpServletResponse resp) {
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
            resp.sendRedirect("/note/list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(Map<String, String> map, HttpServletResponse resp) {
        String id = map.get("id");
        noteRepository.deleteById(UUID.fromString(id));

        try {
            resp.sendRedirect("/note/list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Note getNoteById(UUID id) {
        return noteRepository.findNoteById(id);
    }

    public List<Note> getAllUsersNote(User user) {
        return noteRepository.findByUserIdIs(user);
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

    public void copyLink(Map<String, String> map, HttpServletRequest req, HttpServletResponse resp) {
        String id = map.get("id");
        String url = req.getServerName() + ":" + req.getServerPort() + "/note/share/" + id;

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(url);
        clipboard.setContents(tText, null);

        try {
            resp.sendRedirect("/note/list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ModelAndView getSharePage(HttpServletRequest req, HttpServletResponse resp) {
        String[] split = req.getRequestURI().split("/");
        String id = split[3];
        try {
            Note noteById = noteRepository.findNoteById(UUID.fromString(id));

            if (noteById.getAccess().equals(Access.PRIVATE)) {
                resp.sendRedirect("/note/share/error");
            } else {
                ModelAndView modelAndView = new ModelAndView("share_read");
                modelAndView.addObject("note", parseNoteContentToHtml(noteById));
                return modelAndView;
            }


        } catch (Exception e) {
            try {
                resp.sendRedirect("/note/share/error");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return new ModelAndView("share_error");
    }

}