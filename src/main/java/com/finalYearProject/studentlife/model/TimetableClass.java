package com.finalYearProject.studentlife.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TimetableClass {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long timetableClassId;
	
	String subjectId;
	String code;
	String subjectName;
	
	



	public TimetableClass() {
		super();
	}




	public Long getTimetableClassId() {
		return timetableClassId;
	}




	public void setTimetableClassId(Long timetableClassId) {
		this.timetableClassId = timetableClassId;
	}




	public String getSubjectId() {
		return subjectId;
	}


	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getSubjectName() {
		return subjectName;
	}


	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	
	
	
}