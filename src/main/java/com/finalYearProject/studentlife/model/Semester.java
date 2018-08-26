package com.finalYearProject.studentlife.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

public class Semester implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long semesterId;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<Subject> subject;
	
	private String semesterName;
	
	

}
