package com.finalYearProject.studentlife.model;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Exam implements Serializable{
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long examId;
	
	private String subject;
	
	private String examTitle;
	private int reminder;
	
	public int getReminder() {
		return reminder;
	}





	public void setReminder(int reminder) {
		this.reminder = reminder;
	}



	//The percentage of the module the exam is worth:
	private Double examGradeWorth;
	
	//(this is measured out of 100) What grade the student needs to achieve in the exam
	//to reach their module grade goal.:
	private Double examGradeRequired; //goals
	//pass precentge ? default 40/
	//The grade the student achieved in the exam out of 100. (results)
	private Double examGradeAchieved;
	private String date;
	
	
	public Exam() {
		
	}




	
	public Exam(Long examId, String subject, String examTitle, Double examGradeWorth, Double examGradeRequired,
			Double examGradeAchieved, String date) {
		super();
		this.examId = examId;
		this.subject = subject;
		this.examTitle = examTitle;
		this.examGradeWorth = examGradeWorth;
		this.examGradeRequired = examGradeRequired;
		this.examGradeAchieved = examGradeAchieved;
		this.date = date;
	}





	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getExamId() {
		return examId;
	}

	public void setExamId(Long examId) {
		this.examId = examId;
	}

	public String getExamTitle() {
		return examTitle;
	}

	public void setExamTitle(String examTitle) {
		this.examTitle = examTitle;
	}

	public Double getExamGradeWorth() {
		return examGradeWorth;
	}

	public void setExamGradeWorth(Double examGradeWorth) {
		this.examGradeWorth = examGradeWorth;
	}

	public Double getExamGradeRequired() {
		return examGradeRequired;
	}

	public void setExamGradeRequired(Double examGradeRequired) {
		this.examGradeRequired = examGradeRequired;
	}

	public Double getExamGradeAchieved() {
		return examGradeAchieved;
	}

	public void setExamGradeAchieved(Double examGradeAchieved) {
		this.examGradeAchieved = examGradeAchieved;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}

	
}

