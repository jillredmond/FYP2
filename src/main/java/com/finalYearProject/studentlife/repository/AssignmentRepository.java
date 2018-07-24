package com.finalYearProject.studentlife.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalYearProject.studentlife.model.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

}
