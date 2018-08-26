package com.finalYearProject.studentlife.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.finalYearProject.studentlife.dto.SubjectDto;
import com.finalYearProject.studentlife.dto.UserDto;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.UserRepository;

@RestController
@Controller
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	//Get All Users
		 @GetMapping("/users")
		 public List<User> getAllUsers(){
			 return userRepository.findAll();
		 }
		 //Create a new User
		 @PostMapping("/createUser")
		 public User createUser(@Valid @RequestBody User user) {
			 return userRepository.save(user);
		 }
		 //Get a single user
		 @GetMapping("/users{id}")
		 public ResponseEntity<User> getUserById(@PathVariable(value = "id")Long userId){
			 User user = userRepository.findOne(userId);
			 if(user == null) {
				 return ResponseEntity.notFound().build();
			 }
			 return ResponseEntity.ok().body(user);
		 }
		
		 
		 public ResponseEntity<User> updateUser(@PathVariable(value = "id")Long userId,
				 @Valid @RequestBody User userDetails){
			 User user = userRepository.findOne(userId);
			 if(user == null) {
				 return ResponseEntity.notFound().build();
			 }
			 user.setUsername(userDetails.getUsername());
			 user.setPassword(userDetails.getPassword());
			 user.setFirstName(userDetails.getFirstName());
			 user.setSurname(userDetails.getSurname());
			 user.setAge(userDetails.getAge());
			 user.setHeight(userDetails.getHeight());
			 user.setWeight(userDetails.getWeight());
			 user.setEmailAddress(userDetails.getEmailAddress());
			 user.setGender(userDetails.getGender());
			 user.setDob(userDetails.getDob());
			 
			 User updatedUser = userRepository.save(user);
			 return ResponseEntity.ok(updatedUser);
		 }
		 

		 //Delete a student
		 @DeleteMapping("/users/{id}")
		 public ResponseEntity<User> deleteUser(@PathVariable(value = "id") Long userId){
			 User user = userRepository.findOne(userId);
			 if(user == null) {
				 return ResponseEntity.notFound().build();
			 }
			 userRepository.delete(user);
			 return ResponseEntity.ok().build();
		 }
		 
		 
		 @GetMapping("/students/{id}")
		    public String showForm2(User user) {
		    	return "studentProfile";
		    }
		 
		 @RequestMapping(value = "/edituser/{userId}", method = RequestMethod.GET)
		public String editUser(@PathVariable(value = "userId")Long userId, Model model) {
			UserDto dto = new UserDto();
			model.addAttribute("UserId", userId);
			model.addAttribute("UserDto", dto);	
			
			return "UpdatePofile";
		}
		 
		 @PostMapping("editUser")
		 public String editUser(ModelMap map, @ModelAttribute UserDto dto, BindingResult result) {
			 User user = userRepository.findOne(dto.getUserId());
			 
			 if(dto.getFirstName()!=null) {
				 user.setFirstName(dto.getFirstName());
			 }
			 
			 if(dto.getSurname()!=null) {
				 user.setSurname(dto.getSurname());
			 }
			 
			 if(dto.getAge()!=0) {
				 user.setAge(dto.getAge());
			 }
			 if(dto.getGender()!=null) {
				 user.setGender(dto.getGender());
			 }
			 
			 userRepository.save(user);
			 
			 return  "redirect:/userProfile1";
		 }
		 


			  
		 
}
