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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.apache.jasper.tagplugins.jstl.core.If;
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
import com.google.api.services.calendar.Calendar.Events;
import com.finalYearProject.studentlife.dto.AssignmentDto2;
import com.finalYearProject.studentlife.dto.CalendarDTO;
import com.finalYearProject.studentlife.dto.DayDto;
import com.finalYearProject.studentlife.dto.ExamDto2;
import com.finalYearProject.studentlife.dto.TimetableClassDto;
import com.finalYearProject.studentlife.dto.UserDto;
import com.finalYearProject.studentlife.model.Assignment;
import com.finalYearProject.studentlife.model.Event;
import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.Note;
import com.finalYearProject.studentlife.model.Semester;
import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.TimetableClass;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.AssignmentRepository;
import com.finalYearProject.studentlife.repository.EventRepository;
import com.finalYearProject.studentlife.repository.ExamRepository;
import com.finalYearProject.studentlife.repository.NoteRepository;
import com.finalYearProject.studentlife.repository.SemesterRepository;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.TimetableClassRepository;
import com.finalYearProject.studentlife.repository.UserRepository;

@Controller

public class MainController {

	@Autowired
	SemesterRepository semesterRepository;
	@Autowired
	TimetableClassRepository classRepository;
	@Autowired
	EventRepository eventRepository;
	@Autowired
	ExamRepository examRepository;
	@Autowired
	AssignmentRepository assignmentRepository;
	@Autowired
	NoteRepository noteRepository;

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
			String academicYear = "";
			String semester = "";

			// set month + year to now

			int year = Calendar.getInstance().get(Calendar.YEAR);

			int month = Calendar.getInstance().get(Calendar.MONTH);

			int day = Calendar.getInstance().get(Calendar.DATE);
/*			System.out.println("Year :" + year);
			System.out.println("Month  :" + month);
			System.out.println("Day :" + day);*/

			// String stringMonth = getMonthForInt(month);

			if (month > 6)// if its currently in the second half of the year, make it semester 1 of
							// current academic year
			{
				semester = "1";
				academicYear = year + "/" + (year + 1);
			} else // else make it semester 2 of current academic year
			{
				semester = "2";
				academicYear = (year - 1) + "/" + year;
			}

			ArrayList<Event> events = new ArrayList<Event>();
			ArrayList<Assignment> assignments = new ArrayList<Assignment>();
			ArrayList<Exam> exams = new ArrayList<Exam>();

			SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
			System.out.print("Current date " + currentDate);

			for (Event event : user.getEvent()) {
				long diff = 0;
				long difference = 0;
				try {
					Date date1 = myFormat.parse(currentDate);
					Date date2 = myFormat.parse(event.getDate());
					diff = date2.getTime() - date1.getTime();
					System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
					difference = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				System.out.println("Date : " + event.getDate());
				System.out.println(event.getTitle());

				System.out.println("Diff: " + difference);

				if (difference <= event.getReminder() && difference > -1) {
					event.setReminder(safeLongToInt(difference));
					events.add(event);
				}

			}
			for (Subject subject : user.getSubject()) {
				for (Exam exam : subject.getExam()) {

					long diff = 0;
					long difference = 0;
					try {
						Date date1 = myFormat.parse(currentDate);
						Date date2 = myFormat.parse(exam.getDate());
						diff = date2.getTime() - date1.getTime();
						System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
						difference = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					} catch (ParseException e) {
						e.printStackTrace();
					}

					System.out.println("Date : " + exam.getDate());
					System.out.println(exam.getExamTitle());

					System.out.println("Diff: " + difference);

					if (difference <= exam.getReminder() && difference > -1) {
						exam.setReminder(safeLongToInt(difference));
						exams.add(exam);
					}

				}
				for (Assignment assignment : subject.getAssignment()) {

					long diff = 0;
					long difference = 0;
					try {
						Date date1 = myFormat.parse(currentDate);
						Date date2 = myFormat.parse(assignment.getDate());
						diff = date2.getTime() - date1.getTime();
						System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
						difference = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					} catch (ParseException e) {
						e.printStackTrace();
					}

					System.out.println("Date : " + assignment.getDate());
					System.out.println(assignment.getAssignmentTitle());

					System.out.println("Diff: " + difference);

					if (difference <= assignment.getReminder() && difference > -1) {
						assignment.setReminder(safeLongToInt(difference));
						assignments.add(assignment);
					}

				}

			}

			/*
			 * String inputString1 = "23 01 1997"; String inputString2 = "27 04 1997";
			 * 
			 * try { Date date1 = myFormat.parse(inputString1); Date date2 =
			 * myFormat.parse(inputString2); long diff = date2.getTime() - date1.getTime();
			 * System.out.println ("Days: " + TimeUnit.DAYS.convert(diff,
			 * TimeUnit.MILLISECONDS)); } catch (ParseException e) { e.printStackTrace(); }
			 */
			
			ArrayList<Note> notes = new ArrayList<Note>();
			
			for(Note note : user.getNote())
			{
				notes.add(note);
			}
			
			System.out.println(notes.toString());
			

			List<Semester> semesters = user.getSemester();

			List<Subject> subjects = user.getSubject();
			
			//Note note = new Note();

			model.addAttribute("notes", notes);
			//model.addAttribute("note", note);
			model.addAttribute("Note", new Note()); 
			model.addAttribute("assignments", assignments);
			model.addAttribute("exams", exams);
			model.addAttribute("events", events);
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



	@GetMapping("/calendar/{id}/{select}")
	public String calendar(@PathVariable(value = "id") String id, Model model, Principal principal,
			@PathVariable(value = "select") String select) {

		User user = userR.findByEmailAddress(principal.getName());

		String month = "Error";
		int year = Integer.valueOf(id.substring(id.length() - 4));
		String firstDay = "DayError";

		int currentYear = Calendar.getInstance().get(Calendar.YEAR);

		System.out.println("CURRENT YEAR: " + currentYear);
		Calendar cal = Calendar.getInstance();

		int monthNum = Integer.valueOf(firstTwo(id));

		// set month + year to now
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

		} // -----------------

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

		// ------------
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
		// ------------

		ArrayList<CalendarDTO> calendarDtos = new ArrayList<CalendarDTO>();

		ArrayList<DayDto> dayDtos = new ArrayList<DayDto>();
		ArrayList<String> activeDays = new ArrayList<String>();
		ArrayList<ExamDto2> exams = new ArrayList<ExamDto2>();
		ArrayList<AssignmentDto2> assignments = new ArrayList<AssignmentDto2>();
		ArrayList<Event> events = new ArrayList<Event>();

		// add to active days array
		for (Subject subject : user.getSubject()) {

			for (Exam exam : subject.getExam()) {// if there's any exams on this day, make it an 'active day'

				Date date = new Date();

				// String upToNCharacters = exam.getDate().substring(0,
				// Math.min(exam.getDate().length(), 4));

				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date = sdf2.parse(exam.getDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (date.getMonth() + 1 == monthNum && Integer
						.parseInt(exam.getDate().substring(0, Math.min(exam.getDate().length(), 4))) == year) {
					activeDays.add(String.valueOf(date.getDate()));
				}

			}
			for (Assignment assignment : subject.getAssignment()) {// if there's any assignemtns on this day, make it an
																	// 'active day'

				Date date = new Date();
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date = sdf2.parse(assignment.getDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (date.getMonth() + 1 == monthNum && Integer.parseInt(
						assignment.getDate().substring(0, Math.min(assignment.getDate().length(), 4))) == year) {
					activeDays.add(String.valueOf(date.getDate()));
				}
			}
		}
		for (Event event : user.getEvent()) {
			Date date = new Date();
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = sdf2.parse(event.getDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (date.getMonth() + 1 == monthNum
					&& Integer.parseInt(event.getDate().substring(0, Math.min(event.getDate().length(), 4))) == year) {
				activeDays.add(String.valueOf(date.getDate()));
			}
		}

		ArrayList<String> list = new ArrayList<String>();

		boolean active = false;
		for (String day : days) {
			active = false;

			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);

			if (day.equals(String.valueOf(today.getTime().getDate())) && monthNum == (today.getTime().getMonth() + 1)
					&& year == currentYear) {
				String link = "<li><span data-toggle=\"tooltip\" title=\"Today!\" class=\"today\"><a class=\"a2\" href=\"/calendar/"
						+ String.format("%02d", monthNum) + year + "/" + String.format("%02d", Integer.parseInt(day))
						+ " \">" + day + "</a></span></li>";

				list.add(link);
				active = true;
			}
			boolean dup = false;
			for (String aDay : activeDays) {

				if (day.equals(aDay) && !(day.equals(String.valueOf(today.getTime().getDate()))
						&& monthNum == (today.getTime().getMonth() + 1))) {

					if (dup == false) {

						System.out.println("TEST");

						String link = "<li><span class=\"active\"><a class=\"a2\" href=\"/calendar/"
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
				// String link = "<li><span onclick=\"openForm('"+ day +"')>" + day +
				// "</span></li>";

				if (isNumeric(day)) {

					// makes html for days that are not 'active'. This contains the url for whatever
					// day it is
					String link = "<li><span><a class=\"a1\" href=\"/calendar/" + String.format("%02d", monthNum) + year
							+ "/" + String.format("%02d", Integer.parseInt(day)) + " \">" + day + "</a></span></li>";

					list.add(link);
				} else {
					String link = "<li>" + day + "</li>";
					list.add(link);
				}
			}

		}

		for (String s : list) {
			System.out.println(s);
		}

		String addEvent = "";

		if (!select.equals("00")) {// if a day has been selected, make exam/assignment/event appear below calendar
			addEvent = "<a href=\"javascript:openForm(' " + select + "');\">Add Event</a> on " + month + " " + select;
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

					System.out.println("0000000000");
					System.out.println("Date.getyear = " + date.getYear() + " year: " + year);
					System.out.println("0000000000");

					if (date.getDate() == Integer.parseInt(select) && date.getMonth() + 1 == monthNum && Integer
							.parseInt(exam.getDate().substring(0, Math.min(exam.getDate().length(), 4))) == year) {
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

					if (date.getDate() == Integer.parseInt(select) && date.getMonth() + 1 == monthNum
							&& Integer.parseInt(assignment.getDate().substring(0,
									Math.min(assignment.getDate().length(), 4))) == year) {
						assignments.add(assignmentDto);
					}
					System.out.println(date.getDate() + Integer.parseInt(select));

				}
			}
			for (Event event : user.getEvent()) {

				Date date = new Date();

				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

				try {
					date = sdf2.parse(event.getDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (date.getDate() == Integer.parseInt(select) && date.getMonth() + 1 == monthNum && Integer
						.parseInt(event.getDate().substring(0, Math.min(event.getDate().length(), 4))) == year) {
					events.add(event);
				}
				System.out.println(date.getDate() + Integer.parseInt(select));
			}

		}

		int selectedDay = Integer.parseInt(select);
		Event event = new Event();

		model.addAttribute("events", events);
		model.addAttribute("event", event);
		model.addAttribute("selectedDay", selectedDay);
		model.addAttribute("exams", exams);
		model.addAttribute("assignments", assignments);
		model.addAttribute("activeDays", activeDays);
		model.addAttribute("activeDays", activeDays);
		model.addAttribute("items", calendarDtos);
		model.addAttribute("days", days);
		model.addAttribute("next", next);
		model.addAttribute("list", list);
		model.addAttribute("addEvent", addEvent);
		model.addAttribute("prev", prev);
		model.addAttribute("year", year);
		model.addAttribute("yearNext", yearNext);
		model.addAttribute("yearPrev", yearPrev);
		model.addAttribute("month", month);
		model.addAttribute("monthNum", String.format("%02d", monthNum));

		return "calendar";
	}

	@PostMapping("/calendar/{id}/{select}")
	public String calendarAddEvent(@PathVariable(value = "id") String id, Model model, Principal principal,
			@PathVariable(value = "select") String select, @ModelAttribute Event event) {

		System.out.println(event.getDate());
		System.out.println(event.getDescription());
		System.out.println(event.getReminder());
		System.out.println(event.getTitle());

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);

		String month = id.substring(0, Math.min(id.length(), 2));// get month from url
		String year = id.substring(id.length() - 4); // get year from url
		String date = year + "-" + month + "-" + select;
		System.out.println(date);

		event.setDate(date);
		user.addEvent(event);
		eventRepository.save(event);
		userR.save(user);

		String url = "redirect:/calendar/" + id + "/" + select;

		return url;
	}

	@PostMapping("/calendar/{id}/{select}/{eventId}")
	public String calendarDeleteEvent(@PathVariable(value = "id") String id, Model model, Principal principal,
			@PathVariable(value = "select") String select, @PathVariable(value = "eventId") String eventId) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);

		List<Event> list = user.getEvent();
		Iterator<Event> itr = list.iterator();

		while (itr.hasNext()) {
			Event s = itr.next();

			if (s.getEventId() == Long.parseLong(eventId)) {

				System.out.println("================================");
				System.out.println("================================");
				System.out.println("================================");

				itr.remove();
			}
		}

		System.out.println(list.toString());
		user.setEvent(list);
		userR.save(user);

		eventRepository.delete(Long.parseLong(eventId));

		String url = "redirect:/calendar/" + id + "/" + select;

		return url;
	}

	@PostMapping("/eventDeleteHome/{eventId}")//Delete reminder for an event
	public String homeDeleteEvent(Model model, @PathVariable(value = "eventId") String eventId) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);

		Event event = eventRepository.findOne(Long.parseLong(eventId));
		event.setReminder(-1);
		eventRepository.save(event);

		String url = "redirect:/";

		return url;
	}
	
	@PostMapping("/examDeleteHome/{examId}")//Delete reminder for an exam
	public String homeDeleteExam(Model model, @PathVariable(value = "examId") String examId) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);

		Exam exam = examRepository.findOne(Long.parseLong(examId));
		exam.setReminder(-1);
		examRepository.save(exam);

		String url = "redirect:/";

		return url;
	}
	
	@PostMapping("/assignmentDeleteHome/{assignmentId}")//Delete reminder for an assignment
	public String homeDeleteAssignment(Model model, @PathVariable(value = "assignmentId") String assignmentId) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);

		Assignment assignment =  assignmentRepository.findOne(Long.parseLong(assignmentId));
		assignment.setReminder(-1);
		assignmentRepository.save(assignment);

		String url = "redirect:/";

		return url;
	}
	
	@PostMapping("/noteDeleteHome/{id}")//Delete note
	public String homeDeleteNote(Model model, @PathVariable(value = "id") String id) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);

		Note note =  noteRepository.findOne(Long.parseLong(id));

		
		
		List<Note> list = user.getNote();
		Iterator<Note> itr = list.iterator();



		

		while (itr.hasNext()) {
			Note s = itr.next();

			if(s.getNoteId() == Long.parseLong(id)) {
				itr.remove();
			}
		}

		
		user.setNote(list);
		userR.save(user);

		noteRepository.delete(Long.parseLong(id));

		
		
		
		
		//noteRepository.delete(note);

		String url = "redirect:/";

		return url;
	}

	// ------------------------timetable-----------------------------------------------------
	@GetMapping("/timetable/{id}/{semId}") // This method displays timetable.
	public String timetable(@PathVariable(value = "id") String id, Model model,
			@PathVariable(value = "semId") String semId) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

		String email = loggedInUser.getName();

		User user = userR.findByEmailAddress(email);

		TimetableClassDto dto = new TimetableClassDto();
		TimetableClass timetableClass = new TimetableClass();
		List<Subject> subjects = user.getSubject();
		List<Semester> semesters = user.getSemester();

		Semester semester = new Semester();

		for (Semester sem : semesters) {// get semester object with 'semId'
			if (sem.getSemesterId() == Long.parseLong(semId)) {
				semester = sem;
			}
		}

		List<TimetableClass> classes = semester.getTimetableClass();

		ArrayList<String> monday = new ArrayList<String>();
		ArrayList<String> tuesday = new ArrayList<String>();
		ArrayList<String> wednesday = new ArrayList<String>();
		ArrayList<String> thursday = new ArrayList<String>();
		ArrayList<String> friday = new ArrayList<String>();
		ArrayList<String> saturday = new ArrayList<String>();
		ArrayList<String> sunday = new ArrayList<String>();

		String day = "m";
		boolean finished = false;

		while (finished == false) {// will loop 7 times, for 7 days
			ArrayList<String> html = new ArrayList<String>();

			int num = 8;
			for (int i = 0; i < 13; i++) {// This for loop loops 13 times, once for each timeslot/cell
				boolean found = false;
				String timeCode = day + num;

				for (TimetableClass t : classes)// check to see if a class exits in this timeslot/cell
				{
					if (t.getCode().equals(timeCode)) {
						found = true;
						html.add("<td class=\"tg-s268\" onclick=\"openRemoveForm('" + timeCode + "','"
								+ t.getSubjectName() + "')\">" + t.getSubjectName() + "</td>");
						// make html for cell with a class
					}
				}

				if (!found) {
					html.add("<td class=\"tg-s268\" onclick=\"openForm('" + timeCode + "')\"></td>");// make html for
																										// cell with no
																										// class
					/* <td class="tg-s268" onclick="openForm('m8')"></td> */
					/* <td class="tg-s268" onclick="openForm('m9')"></td> */
				}
				num++;
			} // end for loop

			if (day.equals("sun")) {
				sunday = html;
				finished = true;
			}
			if (day.equals("s")) {
				saturday = html;
				day = "sun";
			}
			if (day.equals("f")) {
				friday = html;
				day = "s";
			}
			if (day.equals("th")) {
				thursday = html;
				day = "f";
			}
			if (day.equals("w")) {
				wednesday = html;
				day = "th";
			}
			if (day.equals("t")) {
				tuesday = html;
				day = "w";
			}
			if (day.equals("m")) {
				monday = html;
				day = "t";
			}

		}

		model.addAttribute("monday", monday);
		model.addAttribute("tuesday", tuesday);
		model.addAttribute("wednesday", wednesday);
		model.addAttribute("thursday", thursday);
		model.addAttribute("friday", friday);
		model.addAttribute("saturday", saturday);
		model.addAttribute("sunday", sunday);
		model.addAttribute("semester", semester);
		model.addAttribute("semesters", semesters);
		model.addAttribute("subjects", subjects);
		model.addAttribute("timetableClass", timetableClass);
		model.addAttribute("timetableClassDto", dto);

		return "timetable";

	}

	@PostMapping("/timetable/{id}/{semId}") // add class to timetable post method
	public String timetablePost(@ModelAttribute TimetableClassDto dto, @PathVariable(value = "id") String id,
			Model model, @PathVariable(value = "semId") String semId) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);

		List<Subject> subjects = user.getSubject();

		TimetableClass ttClass = new TimetableClass();
		ttClass.setCode(dto.getCode());

		for (Subject subject : subjects) {
			if (dto.getSubjectName().equals(subject.getSubjectName())) {
				ttClass.setSubjectName(subject.getSubjectName());
				ttClass.setSubjectId(String.valueOf(subject.getSubjectId()));
			}

		}

		Semester semester = semesterRepository.findOne(Long.parseLong(semId));
		semester.addTimetableClass(ttClass);

		semesterRepository.save(semester);

		String url = "redirect:/timetable/00/" + semId;

		return url;

	}

	@PostMapping("/timetable/remove/{semId}") // remove class from timetable post method
	public String timetablePostRemoveClass(@ModelAttribute TimetableClassDto dto, String id, Model model,
			@PathVariable(value = "semId") String semId) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);

		List<Subject> subjects = user.getSubject();

		System.out.println("=======================");
		System.out.println(dto.getCode() + " " + dto.getSubjectName());
		System.out.println("=======================");

		// Semester semester = semesterRepository.findOne(Long.parseLong(semId));
		List<Semester> semesters = user.getSemester();
		Semester semester = new Semester();
		for (Semester sem : semesters) {
			if (sem.getSemesterId() == Long.parseLong(semId)) {
				semester = sem;

				System.out.println(
						"TESTTESTESTTESTTESTESTTESTTESTESTTESTTESTESTTESTTESTESTTESTTESTESTTESTTESTESTTESTTESTEST");
			}
		}

		List<TimetableClass> list = semester.getTimetableClass();
		Iterator<TimetableClass> itr = list.iterator();
		/*
		 * for(TimetableClass cls : list) {
		 * if(cls.getSubjectName().equals(dto.getSubjectName())) { cls.remove(); }
		 */
		// }

		long id2 = -1;

		while (itr.hasNext()) {
			TimetableClass s = itr.next();

			if (s.getSubjectName().equals(dto.getSubjectName()) && s.getCode().equals(dto.getCode())) {
				classRepository.delete(s.getTimetableClassId());
				id2 = s.getTimetableClassId();
				itr.remove();
			}
		}

		semester.setTimetableClass(list);

		semesterRepository.save(semester);

		classRepository.delete(id2);

		String url = "redirect:/timetable/00/" + semId;

		return url;

	}

	// -----------------------------------------------------------------------------
	
	@GetMapping("/addNote")
	public String addSubject(Model model) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);


		model.addAttribute("user", user);
		model.addAttribute("Note", new Note()); 

		return "addNote";
	}
	
	@GetMapping("/editNote/{id}")
	public String viewNote(Model model, @PathVariable(value = "id") String id) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);

		Note note = noteRepository.findOne(Long.parseLong(id));
		
		System.out.println("Note title " + note.getTitle());

		model.addAttribute("user", user);
		model.addAttribute("Note", new Note()); 
		model.addAttribute("note", note); 

		return "editNote";
	}
	
	@PostMapping("/editNote/{id}")//edit note
	public String editNote( Model model, @ModelAttribute Note Note,  @PathVariable(value = "id") String id) {


		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
	//	User user = userR.findByEmailAddress(email);

		Note note = noteRepository.findOne(Long.parseLong(id));

		note.setTitle(Note.getTitle());
		note.setDescription(Note.getDescription());
	//	user.addNote(note);
		noteRepository.save(note);
		//userR.save(user);

		String url = "redirect:/";

		return url;
	}
	
	@PostMapping("/addNote")//Add note to notebook
	public String addNote( Model model, @ModelAttribute Note note) {


		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);
		
		int y = Calendar.getInstance().get(Calendar.YEAR);
		int m = Calendar.getInstance().get(Calendar.MONTH)+1;
		int d = Calendar.getInstance().get(Calendar.DATE);

		String month = String.format("%02d", m);
		String year = String.valueOf(y);
		String date = year + "-" + month + "-" + String.format("%02d", d);
		System.out.println(date);

		note.setDate(date);
		user.addNote(note);
		noteRepository.save(note);
		userR.save(user);

		String url = "redirect:/";

		return url;
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

	// for checking if a String is numeric
	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	// convert long to int
	public static int safeLongToInt(long l) {
		if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
		}
		return (int) l;
	}
}
