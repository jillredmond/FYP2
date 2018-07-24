package com.finalYearProject.studentlife.dto;

public class AttendanceDto {
	
	private Long attendanceId;
	private String attendanceTitle;
	private Double attendanceWorth;
	private Double attendanceAchieved;
	
	public Long getAttendanceId() {
		return attendanceId;
	}
	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
	}
	public String getAttendanceTitle() {
		return attendanceTitle;
	}
	public void setAttendanceTitle(String attendanceTitle) {
		this.attendanceTitle = attendanceTitle;
	}
	public Double getAttendanceWorth() {
		return attendanceWorth;
	}
	public void setAttendanceWorth(Double attendanceWorth) {
		this.attendanceWorth = attendanceWorth;
	}
	public Double getAttendanceAchieved() {
		return attendanceAchieved;
	}
	public void setAttendanceAchieved(Double attendanceAchieved) {
		this.attendanceAchieved = attendanceAchieved;
	}
	
	public AttendanceDto() {
		super();
	}

}
