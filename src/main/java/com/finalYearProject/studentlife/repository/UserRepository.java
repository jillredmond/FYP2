package com.finalYearProject.studentlife.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUsernameAndPassword(String username, String password);
	User findByUsername(String username);
	User findByEmailAddress(String emailAddress);
	//User add(Subject subject);
	//void add(Subject subject);

	void save(org.apache.catalina.User user);

}
