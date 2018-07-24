package com.finalYearProject.studentlife.service;


import com.finalYearProject.studentlife.service.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.finalYearProject.studentlife.model.User;

public interface UserService extends UserDetailsService {

    User findByEmailAddress(String emailAddress);
  //  User findByFirstName(String firstName);

    User save(UserRegistrationDto registration);
}