package com.finalYearProject.studentlife.dto;

public class AssignmentDto {
	
	private Long assignmentId;
	private String assignmentTitle;
	private Double assignmentGradeAchieved;
	private Double assignmentGradeWorth;
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
	public Double getAssignmentGradeWorth() {
		return assignmentGradeWorth;
	}
	public void setAssignmentGradeWorth(Double assignmentGradeWorth) {
		this.assignmentGradeWorth = assignmentGradeWorth;
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
	public Double getAssignmentGradeAchieved() {
		return assignmentGradeAchieved;
	}
	public void setAssignmentGradeAchieved(Double assignmentGradeAchieved) {
		this.assignmentGradeAchieved = assignmentGradeAchieved;
	}
	
	public AssignmentDto() {
		super();
	}
	
	

}
