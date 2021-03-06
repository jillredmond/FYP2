package com.finalYearProject.studentlife.controller;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.finalYearProject.studentlife.dto.ExamDto;
import com.finalYearProject.studentlife.model.Assignment;
import com.finalYearProject.studentlife.model.Attendance;
import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.Semester;
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
		
		return "redirect:/allSubjects";

	}
	
	@RequestMapping(value = "/editassignment/{assignmentId}", method = RequestMethod.GET)
	public String editAssignment(@PathVariable(value = "assignmentId")Long assignmentId,Model model) {
		
		AssignmentDto dto = new AssignmentDto();
		

		Assignment assignment = assignmentRepository.findOne(assignmentId);
		
		
		dto.setAssignmentGradeWorth(assignment.getAssignmentGradeWorth());
		dto.setAssignmentGradeAchieved(assignment.getAssignmentGradeAchieved());
		dto.setAssignmentTitle(assignment.getAssignmentTitle());
		dto.setDate(assignment.getDate());
		dto.setReminder(assignment.getReminder());
		
		
		
		model.addAttribute("ass", assignment);
		model.addAttribute("AssignmentId", assignmentId);
		model.addAttribute("AssignmentDto", dto);
		
		return "editAssignment";
	}
	
	@PostMapping("editAssignment")
	public String editAssignment(ModelMap map, @ModelAttribute AssignmentDto dto, BindingResult result) {
		
		Assignment assignment = assignmentRepository.findOne(dto.getAssignmentId());
		
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();   
    
		User user = userRepository.findByEmailAddress(email);
	
		
		long subId = 0;
		for(Subject subject : user.getSubject())
		{
			for(Assignment e : subject.getAssignment())
			{
				if(dto.getAssignmentId() == e.getAssignmentId())
				{
					subId = subject.getSubjectId();
				}
			}
			
		}
		

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
		
		assignment.setDate(dto.getDate());
		assignment.setReminder(dto.getReminder());
		
		
		assignmentRepository.save(assignment);
		
		return "redirect:/viewSubject" + subId;
		
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
			 
			 
			 
			 
				@GetMapping("/addAssignment/{id}")
				public String assForm(Model model, @PathVariable(value = "id")Long id) {
					
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

					
					return "addAssignment2";
				}
				
				
				@PostMapping("/addAssignment/{id}")
				public String ass2(@ModelAttribute("assignment") @Valid @RequestBody Assignment assignment , BindingResult result, Model model, @PathVariable(value = "id")Long id) {
					
					
					
					assignmentRepository.save(assignment);
					model.addAttribute("examTitle", assignment.getAssignmentTitle());
					model.addAttribute("examGradeWorth", assignment.getAssignmentTitle());
					model.addAttribute("subject", "");
					
					Subject subject = subjectRepository.findBySubjectId(id);
					subject.addAssignment(assignment);
					subjectRepository.save(subject);
					
					return "redirect:/viewSubject" + id;
					
				
				}
				
				
				@PostMapping("/assignmentDelete/{assignmentId}/{subId}")//Delete assignment
				public String deleteAssignment(Model model, @PathVariable(value = "assignmentId") String assignmentId,  @PathVariable(value = "subId") String subId) {

					Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
					String email = loggedInUser.getName();
					User user = userRepository.findByEmailAddress(email);
					Subject subject= subjectRepository.findOne(Long.parseLong(subId));
					
					List<Assignment> list = subject.getAssignment();//we need to delete the assignment from subject first, then we can delete
					Iterator<Assignment> itr = list.iterator();

					while (itr.hasNext()) {
						Assignment s = itr.next();

						if (s.getAssignmentId() == Long.parseLong(assignmentId)) {

				

							itr.remove();
						}
					}
					
					subject.setAssignment(list);
					subjectRepository.save(subject);
					
			
					
					assignmentRepository.delete(Long.parseLong(assignmentId));

					String url = "redirect:/viewSubject" + subId;

					return url;
				}
			 
			 
			 
			 
}
