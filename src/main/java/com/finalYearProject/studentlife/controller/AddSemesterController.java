package com.finalYearProject.studentlife.controller;

import java.util.ArrayList;
import java.util.Calendar;

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
import com.finalYearProject.studentlife.model.TimetableClass;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.SemesterRepository;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.UserRepository;
import com.finalYearProject.studentlife.service.UserRegistrationDto;

@Controller
@RequestMapping("/addsemester")
public class AddSemesterController {

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

		model.addAttribute("semesters", years());

		return "addSemester";
	}

	@PostMapping
		public String addNewSemester(@ModelAttribute("semester") @Valid @RequestBody Semester semester,
				UserRegistrationDto userDto, BindingResult result, Model model) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userRepository.findByEmailAddress(email);

		semester.setSemesterName(semester.getSemesterName() + " Semester " + String.valueOf(semester.getNum()));
		
		String year = semester.getSemesterName().substring(0, Math.min(semester.getSemesterName().length(), 4));//This gets the first 4 characters of semester name, IE the year
		
		
		semester.setAcademicYear(Integer.parseInt(year));
		
		
		for(Semester sem : user.getSemester())
		{
			if(sem.getSemesterName().equals(semester.getSemesterName())){
			

				return "redirect:/addsemester?error";//If semester already exists, return to form with error message.
			}
		}
		
		
/*		if(semester.getTimetableClass()==null)
		{
			semester.setTimetableClass(new ArrayList<>(semester.getTimetableClass()));
		}
		*/
			semesterRepository.save(semester);
			


			user.addSemester(semester);
			userRepository.save(user);
			
			for(Semester sem : user.getSemester())
			{
				if(sem.getSemesterName().equals(semester.getSemesterName()))
				{
					return "redirect:/sem/" + sem.getSemesterId();
				}
			}
			
			
			
			
			
			return "redirect:/";

		}
	
	//this method makes a list of years for drop down box in add semester form
	public ArrayList<String> years(){
		ArrayList<String> years = new ArrayList<String>();
		int year = 2000;
		for (int i = 0; i < 30; i++) {
			years.add(String.valueOf(year) + "/" + String.valueOf(year + 1));

			year = year + 1;
		}
		
		return years;
	}

}
