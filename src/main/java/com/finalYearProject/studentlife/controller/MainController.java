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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.SemesterRepository;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.UserRepository;

@Controller

public class MainController {

	@Autowired
	UserRepository userRepo;
	@Autowired
	SemesterRepository semesterRepository;

	@GetMapping("/login")
	public String login(Model model) {
		return "login";

	}

	@Autowired
	UserRepository userR;
	@Autowired // added this in
	private SubjectRepository subjectRepository;

	@GetMapping
	public String currentUser(@ModelAttribute("userx") @Valid UserRegistrationDto userDto, BindingResult result,
			Model model) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		if (loggedInUser != null) {
			String email = loggedInUser.getName();


			User user = userR.findByEmailAddress(email);
			String firstname = user.getFirstName();
			String surname = user.getSurname();
			long userId = user.getUserId();
			
			Calendar cal = Calendar.getInstance();
			String academicYear ="";
			String semester="";

			//set month + year to now

				int year = Calendar.getInstance().get(Calendar.YEAR);

				int month = Calendar.getInstance().get(Calendar.MONTH);
				String stringMonth = getMonthForInt(month);
			
				if(month > 6)//if its currently in the second half of the year, make it semester 1 of current academic year
				{
					semester = "1";
					academicYear=year + "/" + (year +1);
				}
				else //else make it semester 2 of current academic year
				{
					semester = "2";
					academicYear=(year -1) + "/" + year;
				}
			
			
			
		

			List<Subject> subjects = user.getSubject();
			List<Semester> semesters = user.getSemester();


			model.addAttribute("subjects", subjects);
			model.addAttribute("semesters", semesters);
			model.addAttribute("academicYear", academicYear);
			model.addAttribute("semester", semester);
			model.addAttribute("subjects", subjects);
			model.addAttribute("emailAddress", email);
			model.addAttribute("firstName", firstname);
			model.addAttribute("surname", surname);
			model.addAttribute("userIs", userId);
			model.addAttribute("user", user);
		}

		return "userProfile1";

	}
	
/*	@PostMapping("/addCurrentSem")//If the user has no semesters, make a semester with the current academic year.
	public String addSemesterForCurrentDate(ModelMap map){

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		if (loggedInUser != null) {
			String email = loggedInUser.getName();
			

			
			

			User user = userR.findByEmailAddress(email);
			String firstname = user.getFirstName();
			String surname = user.getSurname();
			long userId = user.getUserId();
			
			Calendar cal = Calendar.getInstance();
			String academicYear ="";
			String semester="";
			String name = "";

			//set month + year to now

				int year = Calendar.getInstance().get(Calendar.YEAR);
				int yearA = 0;//first year of academic year. IE '2018' if academic year of 2018/2019
				int month = Calendar.getInstance().get(Calendar.MONTH);
				String stringMonth = getMonthForInt(month);
			
				if(month > 6)//if its currently in the second half of the year, make it semester 1 of current academic year
				{
					semester = "1";
					academicYear=year + "/" + (year +1);
					yearA = year;
					
					name = academicYear + " Semester " + semester;
				}
				else //else make it semester 2 of current academic year
				{
					semester = "2";
					academicYear=(year -1) + "/" + year;
					yearA = year-1;
					name = academicYear + " Semester " + semester;
				}
			
			
				Semester semesterObject = new Semester();
				semesterObject.setAcademicYear(yearA);
				semesterObject.setNum(Integer.parseInt(semester));
				semesterObject.setSemesterName(name);

				user.addSemester(semesterObject);
				
				semesterRepository.save(semesterObject);
				userRepo.save(user);
		
		}
		

		 return  "redirect:/userProfile1";

	}*/
	/*
	@GetMapping("/sem/{id}")
	public String semester(@PathVariable(value = "id") String id, Model model, Principal principal) {
	//	User user = userRepo.findByEmailAddress(principal.getName());
		



		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		if (loggedInUser != null) {
			String email = loggedInUser.getName();
			Semester semester = new Semester();
			User user = userR.findByEmailAddress(email);
			
			//this for each loops makes sure that the semester which the user is trying to view belongs to that user
			for(Semester sem : user.getSemester())
			{
				if(sem.getSemesterId() == Long.parseLong(id))
				{
					semester = sem;
				}
				
				
			}
			
			List<Subject> subjects = semester.getSubject();
			
	

			model.addAttribute("subjects", subjects);
			model.addAttribute("semester", semester);
			model.addAttribute("user", user);
		}

		
		return "semesters";
		
	}


*/
	@GetMapping("/addSubject")
	public String addSubject(Model model) {
		
		

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);
		
		List<Semester> semesters = user.getSemester();
			
			
		model.addAttribute("user", user);
		model.addAttribute("semesters", semesters);
		
		
		
		return "addSubject";
	}

	@GetMapping("/calendar/{id}/{select}")
	public String calendar(@PathVariable(value = "id") String id, Model model, Principal principal,
			@PathVariable(value = "select") String select) {

		User user = userRepo.findByEmailAddress(principal.getName());

		String month = "Error";
		int year = Integer.valueOf(id.substring(id.length() - 4));
		String firstDay = "DayError";
		Calendar cal = Calendar.getInstance();

		int monthNum = Integer.valueOf(firstTwo(id));

		//set month + year to now
		if (id.equals("000000")) {
			year = Calendar.getInstance().get(Calendar.YEAR);

			month = getMonthForInt(Calendar.getInstance().get(Calendar.MONTH));
			monthNum = Calendar.getInstance().get(Calendar.MONTH) + 1;

		} else {

			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.MONTH, monthNum - 1);
			cal.set(Calendar.YEAR, year);

			month = getMonthForInt(cal.get(Calendar.MONTH));

			System.out.println("--------------");
			System.out.println(cal.toString());

		}//-----------------
		
		
		
		
		cal.set(Calendar.DAY_OF_MONTH, 1);

		Date firstDayOfMonth = cal.getTime();

		// getting month name from no. of month
		DateFormat sdf = new SimpleDateFormat("EEEEEEEE");

		firstDay = sdf.format(firstDayOfMonth);

		System.out.println("=============================================================");
		System.out.println("Month: " + month);
		System.out.println("First Day of Month: " + firstDay);
		System.out.println("=============================================================");

		// Because the first day of the month is not necessarily monday, I need to
		// insert blank
		// Strings into the arraylist of days so the dates match up correctly with day
		// name on the calendar
		// so if the first day of the month is wednesday, i'll insert 2 blank Strings so
		// that '1' is on wednesday
		int blanks = 0;

		if (firstDay.equals("Monday")) {
			blanks = 0;
		}
		if (firstDay.equals("Tuesday")) {
			blanks = 1;
		}
		if (firstDay.equals("Wednesday")) {
			blanks = 2;
		}
		if (firstDay.equals("Thursday")) {
			blanks = 3;
		}
		if (firstDay.equals("Friday")) {
			blanks = 4;
		}
		if (firstDay.equals("Saturday")) {
			blanks = 5;
		}
		if (firstDay.equals("Sunday")) {
			blanks = 6;
		}

		// find out how many days in the month
		YearMonth yearMonthObject = YearMonth.of(year, monthNum);

		int daysInMonth = yearMonthObject.lengthOfMonth();
		
		System.out.println("=====");
		System.out.println(daysInMonth);
		System.out.println("=====");

		// int monthNum = yearMonthObject.getMonthValue());

		ArrayList<String> days = new ArrayList<String>();

		// make arraylist of days (1, 2, 3 .. ) [(1st, 2nd, 3rd ...)]
		for (int i = 0; i < daysInMonth; i++) {
			days.add(String.valueOf(i + 1));
		}

		// insert empty strings into arraylist of days so days match up correctly on
		// calendar
		for (int i = 0; i < blanks; i++) {

			days.add(0, " ");
		}
		
		//------------
		String next = String.format("%02d", monthNum + 1);
		String prev = String.format("%02d", monthNum - 1);


		if (monthNum == 12) {
			next = String.format("%02d", 1);
		}

		if (monthNum == 1) {
			prev = String.format("%02d", 12);
		}

		int yearNext = year;
		int yearPrev = year;

		if (monthNum == 12) {

			yearNext = year + 1;
		}

		if (monthNum == 1) {

			yearPrev = year - 1;
		}
		//------------
		
		
		
		ArrayList<CalendarDTO> calendarDtos = new ArrayList<CalendarDTO>();

		ArrayList<DayDto> dayDtos = new ArrayList<DayDto>();
		ArrayList<String> activeDays = new ArrayList<String>();
		ArrayList<ExamDto2> exams = new ArrayList<ExamDto2>();
		ArrayList<AssignmentDto2> assignments = new ArrayList<AssignmentDto2>();

		
		//add to active days array
		for (Subject subject : user.getSubject()) {

			for (Exam exam : subject.getExam()) {

				Date date = new Date();
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date = sdf2.parse(exam.getDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}
		
				if (date.getMonth()+1 == monthNum) {
					activeDays.add(String.valueOf(date.getDate()));
				}

	
				
			}
			for (Assignment assignment : subject.getAssignment()) {

				Date date = new Date();
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date = sdf2.parse(assignment.getDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}
		
				if (date.getMonth()+1 == monthNum) {
					activeDays.add(String.valueOf(date.getDate()));
				}				
			}
		}


		
		
		ArrayList<String> list = new ArrayList<String>();

		boolean active = false;
		for (String day : days) {
			active = false;

			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);

			
			if (day.equals(String.valueOf(today.getTime().getDate())) && monthNum == (today.getTime().getMonth() + 1)) {
				String link = "<li><span data-toggle=\"tooltip\" title=\"Today!\" class=\"today\">" + day
						+ "</span></li>";
				list.add(link);
				active = true;
			}
			boolean dup = false;
			for (String aDay : activeDays) {
				
				if (day.equals(aDay)) {
	
					if (dup == false) {
						
						System.out.println("TEST");
						
						String link = "<li><span class=\"active\"><a href=\"/calendar/"
								+ String.format("%02d", monthNum) + year + "/"
								+ String.format("%02d", Integer.parseInt(aDay)) + " \">" + day + "</a></span></li>";
						list.add(link);
						active = true;
						dup = true;
					}
				} else {

				}
			}

			if (active == false) {
				String link = "<li>" + day + "</li>";
				list.add(link);
			}

		}

		
		for (String s : list) {
			System.out.println(s);
		}

		if (!select.equals("00")) {

			for (Subject subject : user.getSubject()) {
				for (Exam exam : subject.getExam()) {
					
					ExamDto2 examDto = new ExamDto2();
					examDto.setExamId(exam.getExamId());
					examDto.setExamtitle(exam.getExamTitle());
					examDto.setSubjectId(subject.getSubjectId());
					
					Date date = new Date();

					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

					try {
						date = sdf2.parse(exam.getDate());
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (date.getDate() == Integer.parseInt(select)) {
						exams.add(examDto);
					}
					System.out.println("[===============]");
					System.out.println(date.getDate() + Integer.parseInt(select));

				}
				for (Assignment assignment : subject.getAssignment()) {
					
					AssignmentDto2 assignmentDto = new AssignmentDto2();
					assignmentDto.setAssignentId(assignment.getAssignmentId());
					assignmentDto.setAssignmentTitle(assignment.getAssignmentTitle());
					assignmentDto.setSubjectId(subject.getSubjectId());
					
					Date date = new Date();

					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

					try {
						date = sdf2.parse(assignment.getDate());
					} catch (ParseException e) {
						e.printStackTrace();
					}

					if (date.getDate() == Integer.parseInt(select)) {
						assignments.add(assignmentDto);
					}
					System.out.println(date.getDate() + Integer.parseInt(select));

				}
			}

		}
		
		int selectedDay = Integer.parseInt(select);

		model.addAttribute("selectedDay", selectedDay);
		model.addAttribute("exams", exams);
		model.addAttribute("assignments", assignments);
		model.addAttribute("activeDays", activeDays);
		model.addAttribute("activeDays", activeDays);
		model.addAttribute("items", calendarDtos);
		model.addAttribute("days", days);
		model.addAttribute("next", next);
		model.addAttribute("list", list);
		model.addAttribute("prev", prev);
		model.addAttribute("year", year);
		model.addAttribute("yearNext", yearNext);
		model.addAttribute("yearPrev", yearPrev);
		model.addAttribute("month", month);
		model.addAttribute("monthNum", String.format("%02d", monthNum));

		return "calendar";
	}

	@GetMapping("/timetable/{id}/{semId}")
	public String timetable(@PathVariable(value = "id") String id, Model model, Principal principal,
			@PathVariable(value = "semId") String semId) {
		
		
		
		
		
		return "timetable";
	
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
