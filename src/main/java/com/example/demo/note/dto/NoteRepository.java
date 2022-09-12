package com.example.demo.note.dto;

import com.example.demo.note.entity.Note;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {

    List<Note> findByUserIs(User user);

    Note findNoteById(UUID id);

    boolean existsById(UUID id);
}
