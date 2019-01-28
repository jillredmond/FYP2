package com.finalYearProject.studentlife.dto;

public class ExamDto {
	
	private Long examId;
	private String examTitle;
	private Double examGradeWorth;
	private Double examGradeAchieved;
	
	private int reminder;
	private String date;
	
	public int getReminder() {
		return reminder;
	}



	public void setReminder(int reminder) {
		this.reminder = reminder;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
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



	public Double getExamGradeAchieved() {
		return examGradeAchieved;
	}



	public void setExamGradeAchieved(Double examGradeAchieved) {
		this.examGradeAchieved = examGradeAchieved;
	}



	public ExamDto() {
		super();
	}

}
