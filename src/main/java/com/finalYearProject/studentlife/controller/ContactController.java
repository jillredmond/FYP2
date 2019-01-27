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
import com.finalYearProject.studentlife.model.Contact;
import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.Note;
import com.finalYearProject.studentlife.model.Semester;
import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.TimetableClass;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.ContactRepository;
import com.finalYearProject.studentlife.repository.SemesterRepository;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.TimetableClassRepository;
import com.finalYearProject.studentlife.repository.UserRepository;

@Controller
public class ContactController {
	@Autowired
	UserRepository userR;
	@Autowired
	ContactRepository contactRepository;

	@GetMapping("/contact")
	public String contact(Model model) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		if (loggedInUser != null) {
			String email = loggedInUser.getName();
			Semester semester = new Semester();
			User user = userR.findByEmailAddress(email);
			
			//this for each loops makes sure that the semester which the user is trying to view belongs to that user


			
			
			List<Contact> contacts = user.getContact();
			Contact contact = new Contact();
	

			model.addAttribute("contacts", contacts);
			model.addAttribute("contact", contact);
			model.addAttribute("user", user);
		}

		
		return "contact";
		
	}

	

	@PostMapping("/contact/{id}")//add contact
	public String addContact( Model model, @ModelAttribute Contact contact,  @PathVariable(value = "id") String id) {


		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userR.findByEmailAddress(email);
		
		contactRepository.save(contact);
		user.addContact(contact);
		userR.save(user);

		String url = "redirect:/contact";

		return url;
	}
	
	
	@GetMapping("/editContact")
	public String editContact(Model model) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		if (loggedInUser != null) {
			String email = loggedInUser.getName();
			Semester semester = new Semester();
			User user = userR.findByEmailAddress(email);
			
			//this for each loops makes sure that the semester which the user is trying to view belongs to that user


			
			
			List<Contact> contacts = user.getContact();
			Contact contact = new Contact();
	

			model.addAttribute("contacts", contacts);
			model.addAttribute("contact", contact);
			model.addAttribute("user", user);
		}

		
		return "editContact";
		
	}
	
	@PostMapping("/editContact/{id}")//edit note
	public String editNote( Model model, @ModelAttribute Contact contact,  @PathVariable(value = "id") String id) {


		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
	//	User user = userR.findByEmailAddress(email);

		Contact c = contactRepository.findOne(Long.parseLong(id));
		c.setDetails(contact.getDetails());
		c.setEmail(contact.getEmail());
		c.setName(contact.getName());
	//	user.addNote(note);
		contactRepository.save(c);
	

		String url = "redirect:/contact";

		return url;
	}



}
