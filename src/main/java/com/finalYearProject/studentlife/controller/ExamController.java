package com.finalYearProject.studentlife.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import com.finalYearProject.studentlife.dto.AttendanceDto;
import com.finalYearProject.studentlife.dto.ExamDto;
import com.finalYearProject.studentlife.model.Attendance;
import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.ExamRepository;

//@RestController
@Controller
public class ExamController {
	
	@Autowired
	ExamRepository examRepository;
	
	@RequestMapping(value = "/editexam/{examId}", method = RequestMethod.GET)
	public String editExam(@PathVariable(value = "examId")Long examId,Model model) {
		
		ExamDto dto = new ExamDto();
		model.addAttribute("ExamId",examId);
		model.addAttribute("ExamDto", dto);
				
		
		return "editExam";
	}
	
	@PostMapping("editExam")
	public String editExam(ModelMap map, @ModelAttribute ExamDto dto, BindingResult result) {
		
		Exam exam = examRepository.findOne(dto.getExamId());
		
		if(dto.getExamTitle()!=null) {
			exam.setExamTitle(dto.getExamTitle());
		}
		if(dto.getExamGradeAchieved()!=null) {
			exam.setExamGradeAchieved(dto.getExamGradeAchieved());
			
		}
		
		if(dto.getExamGradeWorth()!=null) {
			exam.setExamGradeWorth(dto.getExamGradeWorth());
		}
		examRepository.save(exam);
		
		return "redirect:/allSubjects";
		
	}
	
	
	
	
	
//	@RequestMapping(value = "/editExam/{examId}", method = RequestMethod.GET)
//	public String editExam(@PathVariable String examId) {
//		System.out.println(examId + "TESTING EXAM ID !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//		return "allSubjects"; 
//		
//	}
//	
//	@RequestMapping(value="/exam/{examId}", method=RequestMethod.GET)
//		public String EditExam(@ModelAttribute("exam") @Valid @PathVariable Long examId, BindingResult result, Model model) {
//		Exam exam = examRepository.findOne(examId);
//		model.addAttribute("exam",exam);
//		return "editExam";
//	}
//	
//	
//	
	
	
	
	
	
	
 
//	//@RequestMapping(value = "/editExam/{examId}", method = { RequestMethod.GET})
//	@RequestMapping(value = "/exam/{examId}", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
//	public String editExam(@ModelAttribute("exam") @PathVariable(value = "examId")Long examId, @RequestBody Exam exam,Model model, BindingResult result) {
//				
//		System.out.println(examId + "******************************");
//			 	examRepository.findOne(examId);
//				model.addAttribute("examTitle", exam.getExamTitle());
//				model.addAttribute("examGradeWorth", exam.getExamGradeWorth());
//				model.addAttribute("examGradeAchieved", exam.getExamGradeAchieved());
//			 
//			 
//			exam.setExamTitle(exam.getExamTitle());
//			 exam.setExamGradeWorth(exam.getExamGradeWorth());
//			 exam.setExamGradeAchieved(exam.getExamGradeAchieved());
//		
//			  examRepository.save(exam);
//		
//		return "editExam";
//	}
	
	
	
	
	
	
//	@RequestMapping(value = "/editExam/{examId}", method = { RequestMethod.POST, RequestMethod.PUT})
//	public String editExam(@ModelAttribute("exam") @PathVariable(value = "examId")Long examId, @RequestBody Exam exam,Model model, BindingResult result) {
//				
//		System.out.println(examId + "******************************");
//			 	examRepository.findOne(examId);
//				model.addAttribute("examTitle", exam.getExamTitle());
//				model.addAttribute("examGradeWorth", exam.getExamGradeWorth());
//				model.addAttribute("examGradeAchieved", exam.getExamGradeAchieved());
//			 
//			 
//			exam.setExamTitle(exam.getExamTitle());
//			 exam.setExamGradeWorth(exam.getExamGradeWorth());
//			 exam.setExamGradeAchieved(exam.getExamGradeAchieved());
//		
//			  examRepository.save(exam);
//		
//		return "editExam";
//	}
	
	
	
	
	
	
//	 @PutMapping("/exams/{id}")
//	 public ResponseEntity<Exam> updateExam(@PathVariable(value = "id")Long examId,
//			 @Valid @RequestBody Exam examDetails){
//		 Exam exam = examRepository.findOne(examId);
//		 if(exam == null) {
//			 return ResponseEntity.notFound().build();
//		 }
//		 exam.setExamTitle(examDetails.getExamTitle());
//		 exam.setExamGradeWorth(examDetails.getExamGradeWorth());
//		 exam.setExamGradeRequired(examDetails.getExamGradeRequired());
//		 exam.setExamGradeAchieved(examDetails.getExamGradeAchieved());
//	
//		 Exam updatedExam = examRepository.save(exam);
//		 return ResponseEntity.ok(updatedExam);
//	 }
	 
	
	
	
	
	//Get All Exams
	@GetMapping("/exams")
	public List<Exam> getAllExams(){
		return examRepository.findAll();
	}
	
	//Create (POST) a new exam
	 @PostMapping("/createExam")
	 public Exam createExam(@Valid @RequestBody Exam exam) {
		 return examRepository.save(exam);
	 }
	 
	 //Get a single exam
	 @GetMapping("/exams{id}")
	 public ResponseEntity<Exam> getExamById(@PathVariable(value = "id")Long examId){
		 Exam exam = examRepository.findOne(examId);
		 if(exam == null) {
			 return ResponseEntity.notFound().build();
		 }
		 return ResponseEntity.ok().body(exam);
	 }
	 
	// Update a exam 
		 @PutMapping("/exams/{id}")
		 public ResponseEntity<Exam> updateExam(@PathVariable(value = "id")Long examId,
				 @Valid @RequestBody Exam examDetails){
			 Exam exam = examRepository.findOne(examId);
			 if(exam == null) {
				 return ResponseEntity.notFound().build();
			 }
			 exam.setExamTitle(examDetails.getExamTitle());
			 exam.setExamGradeWorth(examDetails.getExamGradeWorth());
			 exam.setExamGradeRequired(examDetails.getExamGradeRequired());
			 exam.setExamGradeAchieved(examDetails.getExamGradeAchieved());
		
			 Exam updatedExam = examRepository.save(exam);
			 return ResponseEntity.ok(updatedExam);
		 }
		 
		//Delete a exam
		 @DeleteMapping("/exams/{id}")
		 public ResponseEntity<Exam> deleteExam(@PathVariable(value = "id") Long examId){
			 Exam exam = examRepository.findOne(examId);
			 if(exam == null) {
				 return ResponseEntity.notFound().build();
			 }
			 examRepository.delete(exam);
			 return ResponseEntity.ok().build();
		 }
		 
		 
	

}
