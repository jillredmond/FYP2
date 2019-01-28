package com.finalYearProject.studentlife.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Attendance implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long attendanceId;
	
	private String subject;
	
	private String attendanceTitle;
	
	private Double attendanceWorth;
	
	private Double attendanceRequired;
	
	private Double attendanceAchieved;
	private int reminder;
	
	
	public Attendance() {
		
	}


	
	
	

	public int getReminder() {
		return reminder;
	}

	public void setReminder(int reminder) {
		this.reminder = reminder;
	}

	public Long getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public Double getAttendanceRequired() {
		return attendanceRequired;
	}

	public void setAttendanceRequired(Double attendanceRequired) {
		this.attendanceRequired = attendanceRequired;
	}

	public Double getAttendanceAchieved() {
		return attendanceAchieved;
	}

	public void setAttendanceAchieved(Double attendanceAchieved) {
		this.attendanceAchieved = attendanceAchieved;
	}

}
