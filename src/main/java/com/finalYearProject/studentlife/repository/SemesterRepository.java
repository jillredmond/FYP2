package com.finalYearProject.studentlife.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalYearProject.studentlife.model.Semester;
import com.finalYearProject.studentlife.model.Subject;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

	Semester findBySemesterName(String semesterName);
	
	
	
	Semester findBySemesterId(long semesterId);


}
