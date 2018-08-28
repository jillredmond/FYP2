package com.finalYearProject.studentlife.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

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
import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.UserRepository;
@Controller

public class MainController {

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    
    }
    
    @Autowired
    UserRepository userR;
    @Autowired //added this in
	private SubjectRepository subjectRepository;
    
    @GetMapping
    public String currentUser(@ModelAttribute("userx") @Valid UserRegistrationDto userDto, BindingResult result, Model model) {
    	
    	Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
    	if (loggedInUser != null)
    	{
        String email = loggedInUser.getName(); 
        		
        User user = userR.findByEmailAddress(email);
        String firstname = user.getFirstName();
        String surname = user.getSurname();
        long userId = user.getUserId();
        
/*        System.out.println("===============================");
       System.out.println(user.toString());
       System.out.println("===============================");*/
		
	
        
        List<Subject> subjects = user.getSubject(); 
        
        model.addAttribute("subjects", subjects);
        model.addAttribute("emailAddress", email);
        model.addAttribute("firstName", firstname);
        model.addAttribute("surname", surname);
        model.addAttribute("userIs", userId);
        model.addAttribute("user", user);
    	}
    
    	return "userProfile1";

    }
   
    
    
    
//    public static String getCurrentUserLogin(@ModelAttribute("userx") @Valid UserRegistrationDto userDto, BindingResult result,Model model) {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        org.springframework.security.core.Authentication authentication = securityContext.getAuthentication();
//        String userName = null;
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof UserDetails) {
//                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
//                userName = springSecurityUser.getUsername();
//            } else if (authentication.getPrincipal() instanceof String) {
//                userName = (String) authentication.getPrincipal();
//            }
//        }
//        model.addAttribute("username", userName);
//        return "userProfile1";
//    }

    
    @GetMapping("/addSubject")
    public String addSubject() {
    	return "addSubject";
    }
    

    @GetMapping("/calendar/{id}")
    public String calendar(@PathVariable(value = "id") String id, Model model) {
    	
    	System.out.println("------");
    	System.out.println(id);
    	
    	String month ="Error";
    	int year = Integer.valueOf(id.substring(id.length() - 4));
    	String firstDay = "DayError";
    	Calendar cal = Calendar.getInstance();
   
    	int monthNum = Integer.valueOf(firstTwo(id));
    	
    	 
    	if(id.equals("000000"))
    	{
    		 year = Calendar.getInstance().get(Calendar.YEAR);
    		//String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
    		//String day = Calendar.getInstance().get(Calendar.d)
    		 month = getMonthForInt(Calendar.getInstance().get(Calendar.MONTH));
    				monthNum = Calendar.getInstance().get(Calendar.MONTH) +1;
    		
    		
    		  
    				
    	}
    	else
    	{
    	
    		 cal.set(Calendar.DATE, 1);
		     cal.set(Calendar.MONTH, monthNum-1);
		     cal.set(Calendar.YEAR, year);
 
      	    		 month = getMonthForInt(cal.get(Calendar.MONTH));
        			
      	    		 
      	    		System.out.println("--------------");	 
    			System.out.println(cal.toString());
    				
    	}
    
    	
	     cal.set(Calendar.DAY_OF_MONTH, 1);
	     Date firstDayOfMonth = cal.getTime();  

	     DateFormat sdf = new SimpleDateFormat("EEEEEEEE");  
	     
	     firstDay = sdf.format(firstDayOfMonth);
	     
	     
	     System.out.println("=============================================================");  
	     System.out.println("Month: " + month);  	
	     System.out.println("First Day of Month: " + firstDay);  	
	     System.out.println("============================================================="); 
    	
    	//Because the first day of the month is not necessarily monday, I need to insert blank
    	//Strings into the arraylist of days so the dates match up correctly with day name on the calendar
    	//so if the first day of the month is wednesday, i'll insert 2 blank Strings so that '1' is on wednesday
    	int blanks = 0;
    	
    	if(firstDay.equals("Monday"))
    	{
    		 blanks = 0;
    	}
    	if(firstDay.equals("Tuesday"))
    	{
    		 blanks = 1;
    	}
    	if(firstDay.equals("Wednesday"))
    	{
    		 blanks = 2;
    	}
    	if(firstDay.equals("Thursday"))
    	{
    		 blanks =3;
    	}
    	if(firstDay.equals("Friday"))
    	{
    		 blanks = 4;
    	}
    	if(firstDay.equals("Saturday"))
    	{
    		 blanks = 5;
    	}
    	if(firstDay.equals("Sunday"))
    	{
    		 blanks = 6;
    	}
    	
    	
    	
    	YearMonth yearMonthObject = YearMonth.of(year, Calendar.getInstance().get(Calendar.MONTH));
    	int daysInMonth = yearMonthObject.lengthOfMonth(); 
    	
    	// int monthNum = yearMonthObject.getMonthValue());
    	
    	ArrayList<String> days = new ArrayList<String>();
    	
    	for(int i =0; i < daysInMonth; i++)
    	{
    		days.add(String.valueOf(i+1));
    	}
    	

    	
    	for(int i = 0; i < blanks; i++)
    	{
    
    		days.add(0, " ");
    	}
    	
    	for(String string : days)
    	{
    		System.out.print(string + ", ");
    	}
    	
    	String next = String.format("%02d", monthNum +1);
    	String prev = String.format("%02d", monthNum -1);
    	
    	if(monthNum == 12)
    	{
    		next = String.format("%02d", 1);
    	}
    	if(monthNum == 1)
    	{
    		prev = String.format("%02d", 12);
    	}
    	
    	
    	
    	int yearNext = year;
    	int yearPrev = year;
    	if(monthNum == 12)
    	{
    		yearNext = year +1;
    	}
    	if(monthNum == 1)
    	{
    		yearPrev = year -1;
    	}
    	
    	
    	System.out.println(monthNum + "KKKKKKKKKKKKKKK");
    	model.addAttribute("days", days);
    	model.addAttribute("next", next);
    	model.addAttribute("prev", prev);
    	model.addAttribute("year", year);
    	model.addAttribute("yearNext", yearNext);
    	model.addAttribute("yearPrev", yearPrev);
    	model.addAttribute("month", month);
    	model.addAttribute("monthNum", String.format("%02d", monthNum));
    	
    	return "calendar";
    }
    
    //convert month number to month name
    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
    //get first 2 characters of string
    public String firstTwo(String str) {
        return str.length() < 2 ? str : str.substring(0, 2);
    }
    

} 
