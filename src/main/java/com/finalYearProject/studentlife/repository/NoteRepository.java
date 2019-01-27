package com.finalYearProject.studentlife.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>{

}
