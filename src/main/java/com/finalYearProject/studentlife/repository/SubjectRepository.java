package com.finalYearProject.studentlife.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalYearProject.studentlife.model.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

	Subject findBySubjectName(String subjectName);
	
	
	
	Subject findBySubjectId(long subjectId);
	//Subject findBySubjectName(String title);

	//Subject findBySubjectId(Long subjectId);

}
