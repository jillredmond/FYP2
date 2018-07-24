package com.finalYearProject.studentlife.dto;

public class ExamDto {
	
	private Long examId;
	private String examTitle;
	private Double examGradeWorth;
	private Double examGradeAchieved;
	
	
	
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
