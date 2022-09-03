package com.example.demo.note;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public Note createNote(Note note){
        return noteRepository.save(note);
    }

    public Note update(Note note){
        return noteRepository.save(note);
    }

    public void deleteById(UUID id){
        noteRepository.deleteById(id);
    }

    public List<Note> getNotesByUserId(UUID userId){
        return noteRepository.getNotesByUserId(userId);
    }

    public Note getNoteById(UUID id){
        return noteRepository.findNoteById(id);
    }
}
