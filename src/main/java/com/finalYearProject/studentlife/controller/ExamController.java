package com.finalYearProject.studentlife.controller;

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

import com.finalYearProject.studentlife.dto.AttendanceDto;
import com.finalYearProject.studentlife.dto.ExamDto;
import com.finalYearProject.studentlife.model.Assignment;
import com.finalYearProject.studentlife.model.Attendance;
import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.repository.ExamRepository;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.UserRepository;

//@RestController
@Controller
public class ExamController {
	
	@Autowired
	ExamRepository examRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	SubjectRepository subjectRepository;
	
	@RequestMapping(value = "/editexam/{examId}", method = RequestMethod.GET)
	public String editExam(@PathVariable(value = "examId")Long examId,Model model) {
		
		Exam exam = examRepository.findOne(examId);
		ExamDto dto = new ExamDto();
		
		dto.setExamGradeWorth(exam.getExamGradeWorth());
		dto.setExamGradeAchieved(exam.getExamGradeAchieved());
		dto.setExamTitle(exam.getExamTitle());
		dto.setDate(exam.getDate());
		dto.setReminder(exam.getReminder());
		model.addAttribute("ExamId",examId);
		model.addAttribute("ExamDto", dto);
				
		
		return "editExam";
	}
	
	@PostMapping("editExam")
	public String editExam(ModelMap map, @ModelAttribute ExamDto dto, BindingResult result) {
		
		Exam exam = examRepository.findOne(dto.getExamId());
		
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();   
    
		User user = userRepository.findByEmailAddress(email);
	
		
		long subId = 0;
		for(Subject subject : user.getSubject())
		{
			for(Exam e : subject.getExam())
			{
				if(dto.getExamId() == e.getExamId())
				{
					subId = subject.getSubjectId();
				}
			}
			
		}
		
		
		if(dto.getExamTitle()!=null) {
			exam.setExamTitle(dto.getExamTitle());
		}
		if(dto.getExamGradeAchieved()!=null) {
			exam.setExamGradeAchieved(dto.getExamGradeAchieved());
			
		}
		
		if(dto.getExamGradeWorth()!=null) {
			exam.setExamGradeWorth(dto.getExamGradeWorth());
		}
		
		exam.setDate(dto.getDate());
		exam.setReminder(dto.getReminder());
		examRepository.save(exam);
		
		return "redirect:/viewSubject" + subId;
		
	}
	
	
	@PostMapping("/examDelete/{examId}/{subId}")//Delete exam
	public String deleteExam(Model model, @PathVariable(value = "examId") String examId,  @PathVariable(value = "subId") String subId) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userRepository.findByEmailAddress(email);
		Subject subject= subjectRepository.findOne(Long.parseLong(subId));
		
		List<Exam> list = subject.getExam();//we need to delete the exam from subject first, then we can delete
		Iterator<Exam> itr = list.iterator();

		while (itr.hasNext()) {
			Exam s = itr.next();

			if (s.getExamId() == Long.parseLong(examId)) {

	

				itr.remove();
			}
		}
		
		subject.setExam(list);
		subjectRepository.save(subject);
		

		
		examRepository.delete(Long.parseLong(examId));

		String url = "redirect:/viewSubject" + subId;

		return url;
	}
	
	
		 
		 
	

}
