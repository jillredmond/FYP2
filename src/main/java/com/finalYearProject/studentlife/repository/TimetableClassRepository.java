package com.finalYearProject.studentlife.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalYearProject.studentlife.model.Exam;

@Repository
public interface TimetableClassRepository extends JpaRepository<Exam, Long>{

}
