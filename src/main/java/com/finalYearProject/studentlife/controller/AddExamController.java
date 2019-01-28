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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalYearProject.studentlife.model.Assignment;
import com.finalYearProject.studentlife.model.Attendance;
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
		
		int max = 100;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	//	model.addAttribute("date", new Exam().getDate());
	//	model.addAttribute("exam", new Exam()); 
		model.addAttribute("subjects", subjects);
		model.addAttribute("max", max);
		
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
	
	
	@GetMapping("/addExam/{id}")
	public String examForm(Model model, @PathVariable(value = "id")Long id) {
		
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();   
    
		User user = userRepository.findByEmailAddress(email);
		
		Subject subject = subjectRepository.findOne(id);
		

		double check = 0;
		double max = 0;
		//Check to make sure the grade worth isn't over 100========================================================================================
		for(Exam exam : subject.getExam())
		{
			max = max + exam.getExamGradeWorth();
		}
		
		for(Assignment ass : subject.getAssignment())
		{
			max = max + ass.getAssignmentGradeWorth();
		}
		
		for(Attendance a : subject.getAttendance())
		{
			max = max + a.getAttendanceWorth();
		}
		

		String message="  "+  max + "/100 percentage has been assigned.";
		
		String message2="			<span class=\"alert alert-warning\" role=\"alert\"> " + max+"/100 percentage has been assigned." + "</span>";

		if(max > 99)
		{
			check = 0;
		}
		else
		{
			check = 100-max;
		}
		
		System.out.println(max);
		System.out.println(check);
		
		
		model.addAttribute("message", message2);
		model.addAttribute("max", check);		
		model.addAttribute("subject", subject);


		
		return "addExam2";
	}
	
	
	@PostMapping("/addExam/{id}")
	public String addNewExam2(@ModelAttribute("exam") @Valid @RequestBody Exam exam , BindingResult result, Model model, @PathVariable(value = "id")Long id) {
		
		
		
		examRepository.save(exam);
		model.addAttribute("examTitle", exam.getExamTitle());
		model.addAttribute("examGradeWorth", exam.getExamGradeWorth());
		model.addAttribute("subject", "");
		
		Subject subject = subjectRepository.findBySubjectId(id);
		subject.addExam(exam);
		subjectRepository.save(subject);
		
		return "redirect:/viewSubject" + id;
		
	
	}
	

}
