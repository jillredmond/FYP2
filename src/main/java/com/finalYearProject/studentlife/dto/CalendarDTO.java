package com.finalYearProject.studentlife.dto;

public class CalendarDTO {
	
	private String day;
	private String id;
	private boolean exam;
	private boolean assignment;
	
	public CalendarDTO() {
		super();
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isExam() {
		return exam;
	}

	public void setExam(boolean exam) {
		this.exam = exam;
	}

	public boolean isAssignment() {
		return assignment;
	}

	public void setAssignment(boolean assignment) {
		this.assignment = assignment;
	}
	
	

}
