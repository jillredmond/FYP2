package com.finalYearProject.studentlife.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Assignment implements Serializable{
	
	//When a user creates a subject they input the title, grade worth and their grade goal.
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long assignmentId;
	
	private String assignmentTitle;
	
	private String subject;
	private String date;
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	//The percentage of the subject this assignment is worth e.g this assignment is worth 20% of the subject marks
	private Double assignmentGradeWorth; 
	
	//The grade the student is required to get in this exam out of 100 in order to achieve their subject goal.
	private Double assignmentGradeRequired;
	
	//The grade the student got in the assingment (results)
	private Double assignmentGradeAchieved;
	
	
	
	public Assignment() {
		
	}

	public Assignment(Long assignmentId, String assignmentTitle, Double assignmentGradeWorth,
			Double assignmentGradeRequired, Double assignmentGradeAchieved, String subject) {
		super();
		this.assignmentId = assignmentId;
		this.assignmentTitle = assignmentTitle;
		this.assignmentGradeWorth = assignmentGradeWorth;
		this.assignmentGradeRequired = assignmentGradeRequired;
		this.assignmentGradeAchieved = assignmentGradeAchieved;
		this.subject = subject;
	}

	
	
	
	
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getAssignmentTitle() {
		return assignmentTitle;
	}

	public void setAssignmentTitle(String assignmentTitle) {
		this.assignmentTitle = assignmentTitle;
	}

	public Double getAssignmentGradeWorth() {
		return assignmentGradeWorth;
	}

	public void setAssignmentGradeWorth(Double assignmentGradeWorth) {
		this.assignmentGradeWorth = assignmentGradeWorth;
	}

	public Double getAssignmentGradeRequired() {
		return assignmentGradeRequired;
	}

	public void setAssignmentGradeRequired(Double assignmentGradeRequired) {
		this.assignmentGradeRequired = assignmentGradeRequired;
	}

	public Double getAssignmentGradeAchieved() {
		return assignmentGradeAchieved;
	}

	public void setAssignmentGradeAchieved(Double assignmentGradeAchieved) {
		this.assignmentGradeAchieved = assignmentGradeAchieved;
	}

}