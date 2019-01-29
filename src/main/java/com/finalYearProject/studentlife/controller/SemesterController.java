package com.finalYearProject.studentlife.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalYearProject.studentlife.service.UserDetails;
import com.finalYearProject.studentlife.service.UserRegistrationDto;
import com.finalYearProject.studentlife.dto.AssignmentDto2;
import com.finalYearProject.studentlife.dto.CalendarDTO;
import com.finalYearProject.studentlife.dto.DayDto;
import com.finalYearProject.studentlife.dto.ExamDto2;
import com.finalYearProject.studentlife.dto.UserDto;
import com.finalYearProject.studentlife.model.Assignment;
import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.Semester;
import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.TimetableClass;
import com.finalYearProject.studentlife.model.Attendance;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.SemesterRepository;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.TimetableClassRepository;
import com.finalYearProject.studentlife.repository.UserRepository;

@Controller
public class SemesterController {

	@Autowired
	UserRepository userRepo;
	@Autowired
	SemesterRepository semesterRepository;

	@Autowired
	TimetableClassRepository timetableClassRepository;

	@Autowired
	UserRepository userR;
	@Autowired // added this in
	private SubjectRepository subjectRepository;

	@PostMapping("/addCurrentSem") // If the user has no semesters, make a semester with the current academic year.
	public String addSemesterForCurrentDate(ModelMap map) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		if (loggedInUser != null) {
			String email = loggedInUser.getName();

			User user = userR.findByEmailAddress(email);
			String firstname = user.getFirstName();
			String surname = user.getSurname();
			long userId = user.getUserId();

			Calendar cal = Calendar.getInstance();
			String academicYear = "";
			String semester = "";
			String name = "";

			// set month + year to now

			int year = Calendar.getInstance().get(Calendar.YEAR);
			int yearA = 0;// first year of academic year. IE '2018' if academic year of 2018/2019
			int month = Calendar.getInstance().get(Calendar.MONTH);
			String stringMonth = getMonthForInt(month);

			if (month > 6)// if its currently in the second half of the year, make it semester 1 of
							// current academic year
			{
				semester = "1";
				academicYear = year + "/" + (year + 1);
				yearA = year;

				name = academicYear + " Semester " + semester;
			} else // else make it semester 2 of current academic year
			{
				semester = "2";
				academicYear = (year - 1) + "/" + year;
				yearA = year - 1;
				name = academicYear + " Semester " + semester;
			}

			Semester semesterObject = new Semester();
			semesterObject.setAcademicYear(yearA);
			semesterObject.setNum(Integer.parseInt(semester));
			semesterObject.setSemesterName(name);

			user.addSemester(semesterObject);

			semesterRepository.save(semesterObject);
			userRepo.save(user);

			for (Semester sem : user.getSemester()) {
				if (sem.getSemesterName().equals(semesterObject.getSemesterName())) {
					return "redirect:/sem/" + sem.getSemesterId();
				}
			}

		}

		return "redirect:/";

	}

	@GetMapping("/sem/{id}")
	public String semester(@PathVariable(value = "id") String id, Model model, Principal principal) {
		// User user = userRepo.findByEmailAddress(principal.getName());

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		if (loggedInUser != null) {
			String email = loggedInUser.getName();
			Semester semester = new Semester();
			User user = userR.findByEmailAddress(email);

			// this for each loops makes sure that the semester which the user is trying to
			// view belongs to that user
			for (Semester sem : user.getSemester()) {
				if (sem.getSemesterId() == Long.parseLong(id)) {
					semester = sem;
				}

			}

			List<Subject> subjects = semester.getSubject();

			ArrayList<Double> assignments = new ArrayList<Double>();
			ArrayList<Double> exams = new ArrayList<Double>();
			ArrayList<Double> attendances = new ArrayList<Double>();
			ArrayList<Double> grades = new ArrayList<Double>();

			double assignmentAverage = 0.0;
			double examAverage = 0.0;
			double attendanceAverage = 0.0;
			double average = 0;
			double sum = 0;
			double semesterGrade = 0;

			for (Subject subject : subjects)// for loop for getting average grades for semester
			{
				for (Assignment assignment : subject.getAssignment()) {
					if (assignment.getAssignmentGradeAchieved() != null) {
						if (assignment.getAssignmentGradeAchieved() != 0) {
							assignments.add(assignment.getAssignmentGradeAchieved());
						}
					}
				}
				sum = 0;
				for (Double d : assignments) {
					sum += d;
				}

				assignmentAverage = sum / assignments.size();

				for (Exam exam : subject.getExam()) {
					if (exam.getExamGradeAchieved() != null) {
						if (exam.getExamGradeAchieved() != 0) {
							exams.add(exam.getExamGradeAchieved());
						}
					}
				}
				sum = 0;
				for (Double d : exams) {
					sum += d;
				}

				examAverage = sum / exams.size();

				for (Attendance attendance : subject.getAttendance()) {
					if (attendance.getAttendanceAchieved() != null) {
						if (attendance.getAttendanceAchieved() != 0) {
							attendances.add(attendance.getAttendanceAchieved());
						}
					}
				}
				sum = 0;
				for (Double d : attendances) {
					sum += d;
				}

				attendanceAverage = sum / exams.size();

			}

/*			for (Subject subject : subjects)// for loop for getting grades for semester
			{

				double assignmentsTotalWorth = 0;
				double assignmentsWorthAchieved = 0;

				double examsTotalWorth = 0;
				double examsWorthAchieved = 0;

				double attendancesTotalWorth = 0;
				double attendancesWorthAchieved = 0;

				double subjectTotalWorth = 0;
				double subjectGradeAchieved = 0;

				for (Assignment assignment : subject.getAssignment()) {//get asignment grades
					if (assignment.getAssignmentGradeAchieved() != null) {
						if (assignment.getAssignmentGradeAchieved() != 0) {
							assignmentsWorthAchieved = assignmentsWorthAchieved
									+ (assignment.getAssignmentGradeAchieved() / 100)
											* assignment.getAssignmentGradeWorth();
							assignmentsTotalWorth = assignmentsTotalWorth + assignment.getAssignmentGradeWorth();
						}
					}
				}

				subjectTotalWorth = subjectTotalWorth + assignmentsTotalWorth;
				subjectGradeAchieved = subjectGradeAchieved + assignmentsWorthAchieved;

				for (Exam exam : subject.getExam()) {//exam grades
					if (exam.getExamGradeAchieved() != null) {
						if (exam.getExamGradeAchieved() != 0) {
							examsWorthAchieved = examsWorthAchieved
									+ (exam.getExamGradeAchieved() / 100) * exam.getExamGradeWorth();
							examsTotalWorth = examsTotalWorth + exam.getExamGradeWorth();
						}
					}
				}

				subjectTotalWorth = subjectTotalWorth + examsTotalWorth;
				subjectGradeAchieved = subjectGradeAchieved + examsWorthAchieved;

				for (Attendance attendance : subject.getAttendance()) {//get attendance grades
					if (attendance.getAttendanceAchieved() != null) {
						if (attendance.getAttendanceAchieved() != 0) {
							attendancesWorthAchieved = assignmentsWorthAchieved
									+ (attendance.getAttendanceAchieved() / 100) * attendance.getAttendanceWorth();
							attendancesTotalWorth = attendancesTotalWorth + attendance.getAttendanceWorth();
						}
					}
				}

				subjectTotalWorth = subjectTotalWorth + attendancesTotalWorth;
				subjectGradeAchieved = subjectGradeAchieved + attendancesWorthAchieved;

				
				if(!Double.isNaN(subjectGradeAchieved / subjectTotalWorth))
				{
					grades.add(subjectGradeAchieved / subjectTotalWorth);

				}
				
				
			}*/
			
			for(Subject s : semester.getSubject())
			{
				if(s.getSubjectResults() != null) {
				if(s.getSubjectResults() > 0)
				{
					grades.add(s.getSubjectResults());
				}
			
				}
			}
			
			
			for(int i = 0;i < grades.size();i++)
			{
			
				System.out.println("==Grades==");
				System.out.println(grades.get(i));
				System.out.println("==========");
			}

			
			
		

			sum = 0;
			for (Double d : grades) {
				sum += d;
			}
			
			if(grades.size() > 0)
			semesterGrade = (sum / grades.size());
			
			//change doubles to 0 if Not a Number 
			if (Double.isNaN(semesterGrade)) {
				semesterGrade = 0.0;
			}
			if (Double.isNaN(assignmentAverage)) {
				assignmentAverage = 0.0;
			}
			if (Double.isNaN(examAverage)) {
				examAverage = 0.0;
			}
			if (Double.isNaN(attendanceAverage)) {
				attendanceAverage = 0.0;
			}
			
			

			model.addAttribute("semesterGrade", semesterGrade);
			model.addAttribute("assignmentAverage", assignmentAverage);
			model.addAttribute("examAverage", examAverage);
			model.addAttribute("attendanceAverage", attendanceAverage);

			model.addAttribute("subjects", subjects);
			model.addAttribute("semester", semester);
			model.addAttribute("user", user);
		}

		return "semesters";

	}

	// convert month number to month name
	String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month;
	}

	// get first 2 characters of string
	public String firstTwo(String str) {
		return str.length() < 2 ? str : str.substring(0, 2);
	}

}
