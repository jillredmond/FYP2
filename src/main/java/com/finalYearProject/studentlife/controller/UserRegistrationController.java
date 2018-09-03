package com.finalYearProject.studentlife.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.service.UserRegistrationDto;
import com.finalYearProject.studentlife.service.UserService;
//This controller is mapped to “/registration” URI. 
//We use the UserRegistrationDto to process and validate the user registration form and inject it using the @ModelAttribute("user") annotation.
//When the form is submitted it’s automatically validated and errors are available in the BindingResult. 
//Next, we check if a user doesn’t already exist with the submitted email. 
//If the form has any errors, we return to the registration page. Otherwise we redirect and inform the user the registration procedure is complete.
@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
	
	 @Autowired
	    private UserService userService;

	    @ModelAttribute("user")
	    public UserRegistrationDto userRegistrationDto() {
	        return new UserRegistrationDto();
	    }

	    @GetMapping
	    public String showRegistrationForm(Model model) {
	        return "registration";
	    }

	    @PostMapping
	    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto, 
	                                      BindingResult result, Model model){
	    	String info = null;
	        User existing = userService.findByEmailAddress(userDto.getEmailAddress());
	        if (existing != null){
	            result.rejectValue("email", null, "There is already an account registered with that email");
	        }

	        if (result.hasErrors()){
	            return "registration";
	        }

	        userService.save(userDto);
	        //this next bit of code displays the details of the user that just registered in the template userProfile1
	        model.addAttribute("emailAddress", userDto.getEmailAddress());
	        model.addAttribute("firstName", userDto.getFirstName());
	        model.addAttribute("surname", userDto.getSurname());
	        model.addAttribute("age", userDto.getAge());
	        model.addAttribute("gender", userDto.getGender());
	        model.addAttribute("dob", userDto.getDob());
	       // return "redirect:/registration?success";
	        return "login";
	        
	    }


}
