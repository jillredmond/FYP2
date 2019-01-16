package com.finalYearProject.studentlife.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TimetableClass {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long semesterId;
	
	String subjectId;
	String code;
	String subjectName;
	
	
	public TimetableClass(Long semesterId, String subjectId, String code, String subjectName) {
		super();
		this.semesterId = semesterId;
		this.subjectId = subjectId;
		this.code = code;
		this.subjectName = subjectName;
	}


	public TimetableClass() {
		super();
	}


	public Long getSemesterId() {
		return semesterId;
	}


	public void setSemesterId(Long semesterId) {
		this.semesterId = semesterId;
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