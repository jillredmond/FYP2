package com.finalYearProject.studentlife.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.ExamRepository;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.UserRepository;
import com.finalYearProject.studentlife.service.UserRegistrationDto;

@Controller
//@RequestMapping("/addexam")
public class AddExamController {
	
	@Autowired
	private ExamRepository examRepository;
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired 
	private UserRepository userRepository;
	
	@ModelAttribute("exam")
	public Exam exam() {
		return new Exam();
	}

	
	@GetMapping("/addexam")
	public String showExamForm(Model model) {
		
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();   
    
		User user = userRepository.findByEmailAddress(email);
		
		ArrayList<String> subjects = new ArrayList<String>();
		
		for(Subject sub:user.getSubject())
		{
			subjects.add(sub.getSubjectName());
		}
		
		
		
		
	//	model.addAttribute("date", new Exam().getDate());
	//	model.addAttribute("exam", new Exam()); 
		model.addAttribute("subjects", subjects);
		
		return "addExam";

	}
	
	@PostMapping("/addExam") 
	public String addNewExam(@ModelAttribute("exam") @Valid @RequestBody Exam exam, BindingResult result, Model model) {
		
		
		
		examRepository.save(exam);
		model.addAttribute("examTitle", exam.getExamTitle());
		model.addAttribute("examGradeWorth", exam.getExamGradeWorth());
		model.addAttribute("subject", "");
		
		String subjectName = exam.getSubject();
		Subject subject = subjectRepository.findBySubjectName(subjectName);
		subject.addExam(exam);
		subjectRepository.save(subject);
		
		return "redirect:/allSubjects";
		
	
	}
}
