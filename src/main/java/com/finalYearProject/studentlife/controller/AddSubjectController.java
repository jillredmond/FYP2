package com.finalYearProject.studentlife.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.UserRepository;
import com.finalYearProject.studentlife.service.AddSubjectDto;
import com.finalYearProject.studentlife.service.SubjectService;
import com.finalYearProject.studentlife.service.UserRegistrationDto;

@Controller
@RequestMapping("/addsubject")
public class AddSubjectController {

		
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired 
	private UserRepository userRepository;
	
	
	@ModelAttribute("subject")
	public Subject subject() {
		return new Subject();
	}
	
	@GetMapping
	public String showSubjectForm(Model model) {
		return "addSubject";
	}
	
	@PostMapping
	public String addNewSubject(@ModelAttribute("subject") @Valid @RequestBody Subject subject,UserRegistrationDto userDto, BindingResult result, Model model) {
		
		subjectRepository.save(subject);
		model.addAttribute("subjectName", subject.getSubjectName());
		model.addAttribute("subjectGradeGoal", subject.getSubjectGradeGoal());
		
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();   
    
		User user = userRepository.findByEmailAddress(email);
	
		user.addSubject(subject);
	userRepository.save(user);
	return "redirect:/";

	}
}