package com.finalYearProject.studentlife.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Contact implements Serializable{
	
	
	

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long contactId;
	
	private String name;
	private String details;
	private String email;

	

	public Contact() {
		
	}



	public Long getConactId() {
		return contactId;
	}



	public void setConactId(Long conactId) {
		this.contactId = conactId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDetails() {
		return details;
	}



	public void setDetails(String details) {
		this.details = details;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long eventId;

	

}