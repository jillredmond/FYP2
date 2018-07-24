package com.finalYearProject.studentlife.dto;

public class SubjectDto {
	
	private Long subjectId;
	private String subjectName;
	private Double subjectGradeGoal;
	private Double caCompletedWorth;
	private Double subjectResults;
	public Long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Double getSubjectGradeGoal() {
		return subjectGradeGoal;
	}
	public void setSubjectGradeGoal(Double subjectGradeGoal) {
		this.subjectGradeGoal = subjectGradeGoal;
	}
	public Double getCaCompletedWorth() {
		return caCompletedWorth;
	}
	public void setCaCompletedWorth(Double caCompletedWorth) {
		this.caCompletedWorth = caCompletedWorth;
	}
	public Double getSubjectResults() {
		return subjectResults;
	}
	public void setSubjectResults(Double subjectResults) {
		this.subjectResults = subjectResults;
	}
	
	public SubjectDto() {
		super();

	}
	
	

}
