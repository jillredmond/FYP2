package com.finalYearProject.studentlife.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalYearProject.studentlife.service.UserDetails;
import com.finalYearProject.studentlife.service.UserRegistrationDto;
import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.UserRepository;
@Controller

public class MainController {

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    
    }
    
    @Autowired
    UserRepository userR;
    @Autowired //added this in
	private SubjectRepository subjectRepository;
    
    @GetMapping
    public String currentUser(@ModelAttribute("userx") @Valid UserRegistrationDto userDto, BindingResult result, Model model) {
    	
    	Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
    	if (loggedInUser != null)
    	{
        String email = loggedInUser.getName(); 
        		
        User user = userR.findByEmailAddress(email);
        String firstname = user.getFirstName();
        String surname = user.getSurname();
        
        

		
	
        
        List<Subject> subjects = user.getSubject(); 
        
        model.addAttribute("subjects", subjects);
        model.addAttribute("emailAddress", email);
        model.addAttribute("firstName", firstname);
        model.addAttribute("surname", surname);
        model.addAttribute("user", user);
    	}
    
    	return "userProfile1";

    }
   
    
    
    
//    public static String getCurrentUserLogin(@ModelAttribute("userx") @Valid UserRegistrationDto userDto, BindingResult result,Model model) {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        org.springframework.security.core.Authentication authentication = securityContext.getAuthentication();
//        String userName = null;
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof UserDetails) {
//                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
//                userName = springSecurityUser.getUsername();
//            } else if (authentication.getPrincipal() instanceof String) {
//                userName = (String) authentication.getPrincipal();
//            }
//        }
//        model.addAttribute("username", userName);
//        return "userProfile1";
//    }

    
    @GetMapping("/addSubject")
    public String addSubject() {
    	return "addSubject";
    }
    
    
    @GetMapping("/calendar")
    public String calendar() {
    	return "calendar";
    }
    
    
    
    

} 
