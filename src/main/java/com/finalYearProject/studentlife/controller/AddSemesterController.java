package com.finalYearProject.studentlife.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalYearProject.studentlife.model.Semester;
import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.SemesterRepository;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.UserRepository;
import com.finalYearProject.studentlife.service.UserRegistrationDto;

public class AddSemesterController {

	@Controller
	@RequestMapping("/addsemester")
	public class AddSubjectController {

		@Autowired
		private SemesterRepository semesterRepository;
		@Autowired
		private UserRepository userRepository;

		@ModelAttribute("semester")
		public Semester semester() {
			return new Semester();
		}

		@GetMapping
		public String showSemesterForm(Model model) {
			return "addSemester";
		}

		@PostMapping
		public String addNewSemester(@ModelAttribute("semester") @Valid @RequestBody Semester semester,
				UserRegistrationDto userDto, BindingResult result, Model model) {

			semesterRepository.save(semester);
			/*
			 * model.addAttribute("semesterName", semester.getSemesterName());
			 * model.addAttribute("q", semester.getAcademicYear());
			 */

			Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
			String email = loggedInUser.getName();

			User user = userRepository.findByEmailAddress(email);
			

			

			user.addSemester(semester);
			userRepository.save(user);
			return "redirect:/";

		}
	}

}
