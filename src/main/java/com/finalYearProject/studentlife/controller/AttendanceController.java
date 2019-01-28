package com.finalYearProject.studentlife.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.finalYearProject.studentlife.dto.AssignmentDto;
import com.finalYearProject.studentlife.dto.AttendanceDto;
import com.finalYearProject.studentlife.dto.ExamDto;
import com.finalYearProject.studentlife.model.Assignment;
import com.finalYearProject.studentlife.model.Attendance;
import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.AssignmentRepository;
import com.finalYearProject.studentlife.repository.AttendanceRepository;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.UserRepository;

@Controller
public class AttendanceController {
	
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired 
	private UserRepository userRepository;
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	@ModelAttribute("attendance")
	public Attendance attendance() {
		return new Attendance();
	}
	
	@GetMapping("/addattendance")
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
		
	return "addAttendance";
		
	}
	
	@PostMapping("/addAttendance")
	public String addNewAttendance(@ModelAttribute("attendance") @Valid @RequestBody Attendance attendance, BindingResult result, Model model) {
		
		attendanceRepository.save(attendance);
		model.addAttribute("assignmentTitle", attendance.getAttendanceTitle());
		model.addAttribute("assignmentWorth", attendance.getAttendanceWorth());
		model.addAttribute("subject", attendance.getSubject());
		
		String subjectName = attendance.getSubject();
		Subject subject = subjectRepository.findBySubjectName(subjectName);
		subject.addAttendance(attendance);
		subjectRepository.save(subject);
		
	return "userProfile1";

	}
	
	@RequestMapping(value = "/editattendance/{attendanceId}", method = RequestMethod.GET)
	public String editAttendance(@PathVariable(value = "attendanceId")Long attendanceId,Model model) {
		
		AttendanceDto dto = new AttendanceDto();
		
		Attendance attendance = attendanceRepository.findOne(attendanceId);
	
		
		dto.setAttendanceWorth(attendance.getAttendanceWorth());
		dto.setAttendanceAchieved(attendance.getAttendanceAchieved());
		dto.setAttendanceTitle(attendance.getAttendanceTitle());
		
		
		model.addAttribute("ass", attendance);
		model.addAttribute("AttendanceId",attendanceId);
		model.addAttribute("AttendanceDto", dto);
				
		
		return "editAttendance";
	}
	
	@PostMapping("editAttendance")
	public String editAttendance(ModelMap map, @ModelAttribute AttendanceDto dto, BindingResult result) {
		
		Attendance attendance = attendanceRepository.findOne(dto.getAttendanceId());
		System.out.println("=======");
		System.out.println("=======");
		System.out.println(dto.getAttendanceId());
		System.out.println("=======");
		System.out.println("=======");
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();   
    
		User user = userRepository.findByEmailAddress(email);
	
		
		long subId = 0;
		for(Subject subject : user.getSubject())
		{
			for(Attendance e : subject.getAttendance())
			{
				if(dto.getAttendanceId().equals(e.getAttendanceId()))
				{
					subId = subject.getSubjectId();
					
					System.out.println("=======");
					System.out.println("=======");
					System.out.println("TESTESTESTESTESTEST");
					System.out.println("=======");
					System.out.println("=======");

					
				}
			}
			
		}
		

		
		
		
		
		
		
		if(dto.getAttendanceTitle()!=null) {
			attendance.setAttendanceTitle(dto.getAttendanceTitle());
		}
		if(dto.getAttendanceAchieved()!=null) {
			attendance.setAttendanceAchieved(dto.getAttendanceAchieved());
			
		}
		
		if(dto.getAttendanceWorth()!=null) {
			attendance.setAttendanceWorth(dto.getAttendanceWorth());
		}
		attendanceRepository.save(attendance);
		
		
		
		
		return "redirect:/viewSubject" + subId;
		
	}
	
	
	@GetMapping("/addAttendance/{id}")
	public String atForm(Model model, @PathVariable(value = "id")Long id) {
		
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


		
		return "addAttendance2";
	}
	
	
	@PostMapping("/addAttendance/{id}")
	public String adt(@ModelAttribute("attendence") @Valid @RequestBody Attendance atttendance , BindingResult result, Model model, @PathVariable(value = "id")Long id) {
		
		
		
		attendanceRepository.save(atttendance);
/*		model.addAttribute("examTitle", atttendance.getAttendanceTitle());
		model.addAttribute("examGradeWorth", atttendance.getAssignmentTitle());
		model.addAttribute("subject", "");
		*/
		Subject subject = subjectRepository.findBySubjectId(id);
		subject.addAttendance(atttendance);
		subjectRepository.save(subject);
		
		return "redirect:/viewSubject" + id;
		
	
	}
 
	
	@PostMapping("/attendanceDelete/{attendanceId}/{subId}")//Delete exam
	public String deleteAttendance(Model model, @PathVariable(value = "attendanceId") String attendanceId,  @PathVariable(value = "subId") String subId) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userRepository.findByEmailAddress(email);
		Subject subject= subjectRepository.findOne(Long.parseLong(subId));
		
		List<Attendance> list = subject.getAttendance();//we need to delete the subject from user first, then we can delete
		Iterator<Attendance> itr = list.iterator();

		while (itr.hasNext()) {
			Attendance s = itr.next();

			if (s.getAttendanceId() == Long.parseLong(attendanceId)) {

	

				itr.remove();
			}
		}
		
		subject.setAttendance(list);
		subjectRepository.save(subject);
		

		
		attendanceRepository.delete(Long.parseLong(attendanceId));

		String url = "redirect:/viewSubject" + subId;

		return url;
	}
	
	
	
	

}
