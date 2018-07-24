package com.finalYearProject.studentlife.dto;

public class AssignmentDto {
	
	private Long assignmentId;
	private String assignmentTitle;
	private Double assignmentGradeAchieved;
	private Double assignmentGradeWorth;
	
	
	
	
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
