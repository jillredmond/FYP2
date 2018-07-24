package com.finalYearProject.studentlife.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finalYearProject.studentlife.dto.AssignmentDto;
import com.finalYearProject.studentlife.model.Assignment;
import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.AssignmentRepository;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.UserRepository;

//@RestController
@Controller
public class AssignmentController {
	

	
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired 
	private UserRepository userRepository;
	@Autowired
	private AssignmentRepository assignmentRepository;
	
	@ModelAttribute("assignment")
	public Assignment assignment() {
		return new Assignment();
	}
	
	@GetMapping("/addassignment")
	public String showAssignmentForm(Model model) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();   
    
		User user = userRepository.findByEmailAddress(email);
		
		ArrayList<String> subjects = new ArrayList<String>();
		
		for(Subject sub:user.getSubject())
		{
			subjects.add(sub.getSubjectName());
		}
		model.addAttribute("subjects", subjects);
		
	return "addAssignment";
		
	}
	
	@PostMapping("/addAssignment")
	public String addNewAssignment(@ModelAttribute("assignment") @Valid @RequestBody Assignment assignment, BindingResult result, Model model) {
		
		//subjectdto= new subject dto
		
		assignmentRepository.save(assignment);
		model.addAttribute("assignmentTitle", assignment.getAssignmentTitle());
		model.addAttribute("assignmentGradeWorth", assignment.getAssignmentGradeWorth());
		model.addAttribute("subject", "");
		
		String subjectName = assignment.getSubject();
		Subject subject = subjectRepository.findBySubjectName(subjectName);
		subject.addAssignment(assignment);
		subjectRepository.save(subject);
		
	return "userProfile1";

	}
	
	@RequestMapping(value = "/editassignment/{assignmentId}", method = RequestMethod.GET)
	public String editAssignment(@PathVariable(value = "assignmentId")Long assignmnetId,Model model) {
		
		AssignmentDto dto = new AssignmentDto();
		model.addAttribute("AssignmentId", assignmnetId);
		model.addAttribute("AssignmentDto", dto);
		
		return "editAssignment";
	}
	
	@PostMapping("editAssignment")
	public String editAssignment(ModelMap map, @ModelAttribute AssignmentDto dto, BindingResult result) {
		
		Assignment assignment = assignmentRepository.findOne(dto.getAssignmentId());
		System.out.println("-------------------------------------------------------------------------------------------");
		System.out.println("ASSIGNMENT title" + assignment.getAssignmentTitle() + "grade achieved:" + assignment.getAssignmentGradeAchieved()+ " grade required" +  assignment.getAssignmentGradeRequired()+ "grade worth" + assignment.getAssignmentGradeWorth());
		System.out.println("DTO title" + dto.getAssignmentTitle() +  " dto grade achoeved:" + dto.getAssignmentGradeAchieved()+  "dto grade worth"  + dto.getAssignmentGradeWorth());
		System.out.println("-------------------------------------------------------------------------------------------");
		if(dto.getAssignmentTitle()!=null || dto.getAssignmentTitle().equals("") ) {
			assignment.setAssignmentTitle(dto.getAssignmentTitle());
		}
		if(dto.getAssignmentGradeAchieved()!=null) {
			assignment.setAssignmentGradeAchieved(dto.getAssignmentGradeAchieved());
			
		}
		
		if(dto.getAssignmentGradeWorth()!=null) {
			assignment.setAssignmentGradeWorth(dto.getAssignmentGradeWorth());
		}
		assignmentRepository.save(assignment);
		
		return "redirect:/allSubjects";
		
	}
	
	
	
	
	
	
	
	
	//Get All Assignments
	@GetMapping("/assignments")
	public List<Assignment> getAllAssignments(){
		return assignmentRepository.findAll();
	}
	
	//Create (POST) a new assignment
	 @PostMapping("/createAssignment")
	 public Assignment createAssignment(@Valid @RequestBody Assignment assignment) {
		 return assignmentRepository.save(assignment);
	 }
	 
	 //Get a single assignment
	 @GetMapping("/assignments{id}")
	 public ResponseEntity<Assignment> getAssignmentById(@PathVariable(value = "id")Long assignmentId){
		 Assignment assignment = assignmentRepository.findOne(assignmentId);
		 if(assignment == null) {
			 return ResponseEntity.notFound().build();
		 }
		 return ResponseEntity.ok().body(assignment);
	 }
	 
	// Update a assignment 
			 @PutMapping("/assignments/{id}")
			 public ResponseEntity<Assignment> updateAssignment(@PathVariable(value = "id")Long assignmentId,
					 @Valid @RequestBody Assignment assignmentDetails){
				 Assignment assignment = assignmentRepository.findOne(assignmentId);
				 if(assignment == null) {
					 return ResponseEntity.notFound().build();
				 }
				 assignment.setAssignmentTitle(assignmentDetails.getAssignmentTitle());
				 assignment.setAssignmentGradeWorth(assignmentDetails.getAssignmentGradeWorth());
				 assignment.setAssignmentGradeRequired(assignmentDetails.getAssignmentGradeRequired());
				 assignment.setAssignmentGradeAchieved(assignmentDetails.getAssignmentGradeAchieved());
	
			
				 Assignment updatedAssignment = assignmentRepository.save(assignment);
				 return ResponseEntity.ok(updatedAssignment);
			 }
			 
			//Delete a assignment
			 @DeleteMapping("/assignments/{id}")
			 public ResponseEntity<Assignment> deleteAssignment(@PathVariable(value = "id") Long assignmentId){
				 Assignment assignment = assignmentRepository.findOne(assignmentId);
				 if(assignment == null) {
					 return ResponseEntity.notFound().build();
				 }
				 assignmentRepository.delete(assignment);
				 return ResponseEntity.ok().build();
			 }
			 
			 
			 
			 
			 
}
