package com.finalYearProject.studentlife.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "emailAddress"))
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	//@NotBlank
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<Subject> subject;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<Semester> semester;

	@OneToMany(fetch=FetchType.LAZY,orphanRemoval = true,  cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
	public List<Event> event;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_userId", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))

    public Collection<Role> roles;
	
	public List<Subject> getSubject() {
		if (subject==null)
			subject = new ArrayList<Subject>();
		
		return subject;
	}

	
	
	public List<Semester> getSemester() {
		
		if (semester==null)
			semester = new ArrayList<Semester>();
		
		
		return semester;
	}

	public void setSemester(List<Semester> semester) {
		this.semester = semester;
	}

	
	
	


	public List<Event> getEvent() {
		if(event==null)
		{
			event = new ArrayList<Event>();
		}
		return event;
	}



	public void setEvent(List<Event> event) {
		this.event = event;
	}






	private String username;
	
	//@NotBlank
	private String password;

	//@NotBlank
	private String firstName;
	
	//@NotBlank
	private String surname;
	
	//@NotBlank
	private int age;
	
	//@NotBlank
	private double height;
	
	//@NotBlank
	private double weight;
	
	//@NotBlank
	private String emailAddress;
	
	//@NotBlank
	private String gender;
	
	//@NotBlank
	private String dob;
	
	Boolean studentStatus;
	
	public User() {
    }
	
//    public User(String firstName, String surname, String emailAddress, String password) {
//        this.firstName = firstName;
//        this.surname = surname;
//        this.emailAddress = emailAddress;
//        this.password = password;
//    }
//    
//    public User(String firstName, String surname, String emailAddress, String password, Collection<Role> roles) {
//        this.firstName = firstName;
//        this.surname = surname;
//        this.emailAddress = emailAddress;
//        this.password = password;
//        this.roles = roles;
//    }
//
	public User(Long userId, List<Subject> subject, Collection<Role> roles, String username, String password,
			String firstName, String surname, int age, double height, double weight, String emailAddress, String gender,
			String dob, Boolean studentStatus) {
		super();
		this.userId = userId;
		this.subject = subject;
		this.roles = roles;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.surname = surname;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.emailAddress = emailAddress;
		this.gender = gender;
		this.dob = dob;
		this.studentStatus = studentStatus;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

//	public List<Subject> getSubject() {
//		return subject;
//	}

	public void setSubject(List<Subject> subject) {
		this.subject = subject;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Boolean getStudentStatus() {
		return studentStatus;
	}

	public void setStudentStatus(Boolean studentStatus) {
		this.studentStatus = studentStatus;
	} 
	
	@Override
	public String toString() {
		return "User [userId=" + userId + ", subject=" + subject + ", roles=" + roles + ", username=" + username
				+ ", password=" + password + ", firstName=" + firstName + ", surname=" + surname + ", age=" + age
				+ ", height=" + height + ", weight=" + weight + ", emailAddress=" + emailAddress + ", gender=" + gender
				+ ", dob=" + dob + ", studentStatus=" + studentStatus + "]";
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
//	public void addSubject(Subject subjects) {
//		subject.add(subjects);
//		
//	}
	
	public void addSubject(Subject Subject){

		getSubject().add(Subject);
	}
	
	public void addSemester(Semester semester){

		getSemester().add(semester);
	}

	public void addEvent(Event event){

		getEvent().add(event);
	}
	
	

}
