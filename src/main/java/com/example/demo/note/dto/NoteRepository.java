package com.example.demo.note.dto;

import com.example.demo.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {

    @Query("FROM Note u WHERE u.userId = :id")
    List<Note> getNotesByUserId(@Param("id") UUID userId);

    Note findNoteById(UUID id);
}
