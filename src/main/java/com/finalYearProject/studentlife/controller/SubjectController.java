package com.finalYearProject.studentlife.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.finalYearProject.studentlife.dto.SubjectDto;
import com.finalYearProject.studentlife.model.Assignment;
import com.finalYearProject.studentlife.model.Attendance;
import com.finalYearProject.studentlife.model.Event;
import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.Semester;
import com.finalYearProject.studentlife.model.Subject;
import com.finalYearProject.studentlife.model.User;
import com.finalYearProject.studentlife.repository.AssignmentRepository;
import com.finalYearProject.studentlife.repository.AttendanceRepository;
import com.finalYearProject.studentlife.repository.ExamRepository;
import com.finalYearProject.studentlife.repository.SemesterRepository;
import com.finalYearProject.studentlife.repository.SubjectRepository;
import com.finalYearProject.studentlife.repository.UserRepository;
import com.finalYearProject.studentlife.service.UserRegistrationDto;



@Controller
public class SubjectController {
	
	@Autowired
	SubjectRepository subjectRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ExamRepository examRepository;
	@Autowired
	AssignmentRepository assignmentRepository;
	@Autowired
	AttendanceRepository attendanceRepository;
	@Autowired
	SemesterRepository semesterRepository;
	
	//local varibales
	double allAttendanceTotalResults =0;
	double examsTotalResults =0;
	double assignmentsTotalWorth = 0;
	double assignmentsTotalResults =0;
	double caCompletedWorth =0;
	double subjectResults = 0;
	double examsTotalWorth =0;
	double attendancesTotalWorth =0;
	double remExamMarks =0;
	double remAssignMarks = 0;
	double remAttenMarks = 0;
	double maxSubRemMarks =0;
	double highestPossibleGrade =0;
	double subjectGradeGoal = 0;
	double marksNeededToReachGoal = 0;
	String isGoalPossible;
	String isGoalAchieved;
	String isGradeAboveGoal;
	double averageAssignGrade = 0;
	double averageExamGrade = 0;
	double averageAttenGrade = 0;
	double averageSubjectGrade = 0;
	double allAssignments = 0;
	int assignmentArraySize = 0;
	double allExams = 0;
	double allAttendances = 0;
	int examArraySize = 0;
	int attendanceArraySize = 0;
	double averageAttendanceGrade =0;
	double assignmentGradePrediction = 0;
	double assignmentGradePredictions = 0; //prediction for each individual assignment added together
	
	
//	@GetMapping("/allsubjects")
//	public String getSubjects(Model model) {
//		return "allSubjects";
//	}
	
	@GetMapping("/addSubject")
	public String addSubject(Model model) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userRepository.findByEmailAddress(email);

		List<Semester> semesters = user.getSemester();

		model.addAttribute("user", user);
		model.addAttribute("semesters", semesters);

		return "addSubject";
	}

	
	@PostMapping("/subjectDelete/{subjectId}/{semId}")//Delete subject
	public String homeDeleteEvent(Model model, @PathVariable(value = "subjectId") String subjectId,  @PathVariable(value = "semId") String semId) {

		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();
		User user = userRepository.findByEmailAddress(email);
		Semester semester = semesterRepository.findOne(Long.parseLong(semId));
		
		List<Subject> list = user.getSubject();//we need to delete the subject from user first, then we can delete
		Iterator<Subject> itr = list.iterator();

		while (itr.hasNext()) {
			Subject s = itr.next();

			if (s.getSubjectId() == Long.parseLong(subjectId)) {

	

				itr.remove();
			}
		}
		
		user.setSubject(list);
		userRepository.save(user);
		
		List<Subject> list2 = semester.getSubject();//we need to delete the subject from user first, then we can delete
		Iterator<Subject> itr2 = list2.iterator();

		while (itr2.hasNext()) {
			Subject s = itr2.next();

			if (s.getSubjectId() == Long.parseLong(subjectId)) {

	

				itr2.remove();
			}
		}
		
		semester.setSubject(list2);
		semesterRepository.save(semester);
		
		
		
		
		subjectRepository.delete(Long.parseLong(subjectId));

		String url = "redirect:/sem/" + semId;

		return url;
	}
	
	
	@GetMapping("/allSubjects")
	public String showSubjects(@ModelAttribute("subject") @Valid UserRegistrationDto userDto, BindingResult result, Model model) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();   
		
		assignmentsTotalResults = 0;
		assignmentsTotalWorth =0;
		examsTotalResults = 0;
		allAttendanceTotalResults = 0;
		caCompletedWorth = 0;
		attendancesTotalWorth=0;
		examsTotalWorth =0;
		
		
		User user = userRepository.findByEmailAddress(email);
		
	List<Subject> subjects = user.getSubject(); //Gets list of subjects belonging to the current user
		
	//FIND SUBJECT CACOMPLETEWORTH
		for(int i=0; i<subjects.size(); i++) {
			assignmentsTotalWorth =0;
			examsTotalWorth=0;
			attendancesTotalWorth=0;
			Subject subject = subjects.get(i);
			
			List<Assignment> assignments = subject.getAssignment();
			
			for(int y=0; y<assignments.size(); y++ ) {
				Assignment assignment = assignments.get(y);
				
				ArrayList<Double> assignmentsWorth = new ArrayList<Double>();
				
				if(assignments.get(y).getAssignmentGradeAchieved() != null){ //if the grade achieved is not null (Because we only want the CA worth of subjects that the user has results for)
					assignmentsWorth.add(assignments.get(y).getAssignmentGradeWorth());
					
				}
				for(int q = 0; q < assignmentsWorth.size(); q++) {
					
					assignmentsTotalWorth = assignmentsTotalWorth + assignmentsWorth.get(q);
					
				}
				
				
			}
			List<Exam> exams = subject.getExam();
			
			 for(int y=0; y<exams.size(); y++ ) {
				Exam exam = exams.get(y);
				
				ArrayList<Double> examsWorth = new ArrayList<Double>();
				
				if(exams.get(y).getExamGradeAchieved() !=null) {
					examsWorth.add(exams.get(y).getExamGradeWorth());
				}
				for(int q = 0; q < examsWorth.size(); q++) {
				
					examsTotalWorth = examsTotalWorth + examsWorth.get(q);
				}
			}
			 
				List<Attendance> attendances = subject.getAttendance();
				
				 for(int y=0; y<attendances.size(); y++ ) {
					 Attendance attendance = attendances.get(y);
					
					ArrayList<Double> attendancesWorth = new ArrayList<Double>();
					
					if(attendances.get(y).getAttendanceAchieved() !=null) {
						attendancesWorth.add(attendances.get(y).getAttendanceWorth());
					}
					for(int q = 0; q < attendancesWorth.size(); q++) {
						
						attendancesTotalWorth = attendancesTotalWorth + attendancesWorth.get(q);
					}
				 }
			
			
			caCompletedWorth = assignmentsTotalWorth + examsTotalWorth + attendancesTotalWorth;
			subject.setCaCompletedWorth(caCompletedWorth);
			subjectRepository.save(subject);
		
		}
		
		//FIND SUBJECT RESULTS
		for(int i=0; i<subjects.size(); i++) { //for each subject in the list
			Subject subject = subjects.get(i);
			assignmentsTotalResults =0;
			examsTotalResults=0;
			allAttendanceTotalResults=0;
			List<Assignment> assignments = subject.getAssignment(); //Get the list of assignments belonging to current subject
		
				for(int y=0; y<assignments.size(); y++ ) { //For each assignment in the list 
					Assignment assignment = assignments.get(y);
					ArrayList<Double> assignmentGrades = new ArrayList<Double>(); 
					if(assignments.get(y).getAssignmentGradeAchieved() != null){
						assignmentGrades.add(assignments.get(y).getAssignmentGradeAchieved()); //If the assignment has a value for gradeAchieved add grade achieved to the assignmentGrades list.
					}	
							for(int q = 0; q < assignmentGrades.size(); q++) {
								assignmentsTotalResults = assignmentsTotalResults + assignmentGrades.get(q); 
							}
				}		
					
			List<Exam> exams = subject.getExam();
			
			for(int z=0; z<exams.size(); z++) {
				Exam exam = exams.get(z);

				ArrayList<Double> examGrades = new ArrayList<Double>(); 
				if(exams.get(z).getExamGradeAchieved() !=null) {
					examGrades.add(exams.get(z).getExamGradeAchieved());
				}
				for(int h=0; h < examGrades.size(); h++) {
					examsTotalResults = examsTotalResults + examGrades.get(h);
				}
			}
			
			List<Attendance> attendances = subject.getAttendance();
			
			for(int x=0; x<attendances.size(); x++) {
				Attendance attendance = attendances.get(x);
				ArrayList<Double> attendanceGrades = new ArrayList<Double>();
				if(attendances.get(x).getAttendanceAchieved() !=null) {
					attendanceGrades.add(attendances.get(x).getAttendanceAchieved());
				}
				for(int w=0; w< attendanceGrades.size(); w++) {
					allAttendanceTotalResults = allAttendanceTotalResults + attendanceGrades.get(w);
				}	
			}
			subjectResults =(assignmentsTotalResults + examsTotalResults + allAttendanceTotalResults);
			//Subject results is the current result a user has for a subject
			subject.setSubjectResults(subjectResults);
			subjectRepository.save(subject);

		} // End loop that goes through the subjects CAs that then adds the CAs results together to find the users current results for each subject.
		
		model.addAttribute("subjects", user.getSubject());
		
		//REMAINING POTENTIAL MARKS
		for(int i=0; i<subjects.size(); i++){ //for each subject in the list
			Subject subject = subjects.get(i);
			remExamMarks =0;
			remAssignMarks = 0;
			remAttenMarks = 0;
			List<Assignment> assignments = subject.getAssignment(); //Get the list of assignments belonging to current subject
			
			for(int y=0; y<assignments.size(); y++ ) { //For each assignment in the list 
			Assignment assignment = assignments.get(y);
				ArrayList<Double> assignmentRemMarks = new ArrayList<Double>(); 
				if(assignments.get(y).getAssignmentGradeAchieved()==null){
					assignmentRemMarks.add(assignments.get(y).getAssignmentGradeWorth());
				}
					for(int q = 0; q < assignmentRemMarks.size(); q++) {
				remAssignMarks = remAssignMarks + assignmentRemMarks.get(q);
					}
		
				}
			List<Attendance> attendances = subject.getAttendance();
			for(int x=0; x<attendances.size(); x++) {
				Attendance attendance = attendances.get(x);
				ArrayList<Double> attendanceRemMarks = new ArrayList<Double>();
				if(attendances.get(x).getAttendanceAchieved()==null) {
					attendanceRemMarks.add(attendances.get(x).getAttendanceWorth());
				}
					for(int w=0; w< attendanceRemMarks.size(); w++) {
						remAttenMarks = remAttenMarks + attendanceRemMarks.get(w);	
					}	
				}

			List<Exam> exams = subject.getExam();
			for(int z=0; z<exams.size(); z++) {
				Exam exam = exams.get(z);

				ArrayList<Double> examRemMarks = new ArrayList<Double>(); 
				if(exams.get(z).getExamGradeAchieved()==null) {
					examRemMarks.add(exams.get(z).getExamGradeWorth());
				}
				for(int h=0; h < examRemMarks.size(); h++) {
					remExamMarks = remExamMarks + examRemMarks.get(h);				
				}
				
			}
		maxSubRemMarks =(remExamMarks);		
			subject.setMaxSubRemMarks(maxSubRemMarks + remAttenMarks + remAssignMarks);
			subjectRepository.save(subject);
			
		}	
		
		//HIGHEST POSSIBLE GRADE
	
		for(int i=0; i<subjects.size(); i++){ //for each subject in the list
			Subject subject = subjects.get(i);
			highestPossibleGrade = (subject.getSubjectResults() + subject.getMaxSubRemMarks());

		
		subject.setHighestPossibleGrade(highestPossibleGrade);
		subjectRepository.save(subject);
		}
		
		//MARKS UNTIL GOAL IS REACHED
		for(int i=0; i<subjects.size(); i++){ //for each subject in the list
			Subject subject = subjects.get(i);
			
			if(subject.getSubjectResults() >subject.getSubjectGradeGoal()) {
				isGradeAboveGoal = "Your grade is above your grade goal";
				subject.setIsGradeAboveGoal(isGradeAboveGoal);
				
			}else {
					isGradeAboveGoal = "Your grade is below your grade goal";
					subject.setIsGradeAboveGoal(isGradeAboveGoal);
				}
			
			
			if(subject.getSubjectResults() >= subject.getSubjectGradeGoal()) {				
				marksNeededToReachGoal = 0;
				isGoalAchieved = "Goal Achieved";
				isGoalPossible = ("You have already reached your goal");
				subject.setMarksNeededToReachGoal(marksNeededToReachGoal);
				subject.setIsGoalAchieved(isGoalAchieved);
				subject.setIsGoalPossible(isGoalPossible);
							
			} else if((subject.getSubjectResults() + subject.getMaxSubRemMarks()) < subject.getSubjectGradeGoal() ) {
					isGoalPossible = ("Goal not possible");
					isGoalAchieved = "Not possible to achieved the current goal";
					subject.setIsGoalPossible(isGoalPossible);
					subject.setIsGoalAchieved(isGoalAchieved);
					
			} else {
							marksNeededToReachGoal = (subject.getSubjectGradeGoal() - (subject.getSubjectResults()));
							isGoalPossible = ("Goal is possible");
							isGoalAchieved = "Goal hasn't been achieved yet";
							subject.setMarksNeededToReachGoal(marksNeededToReachGoal);
							subject.setIsGoalPossible(isGoalPossible);
							subject.setIsGoalAchieved(isGoalAchieved);
						}
			

			subjectRepository.save(subject);
					
		}//end for
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	// I decided against using grade prediction as I felt it was way to inaccurate and pointless.	
		
		////////////////////////////////////////////
		//GRADE PREDICTION
		for(int i=0; i<subjects.size(); i++){ //for each subject in the list
			Subject subject = subjects.get(i);
	//		Assignment assignment = (Assignment) subject.getAssignment();
			
			List<Assignment> assignments = subject.getAssignment(); //Get the list of assignments belonging to current subject
			ArrayList<Double> assignmentGradesAchieved = new ArrayList<Double>(); 
			
		//	for(subject.getAssignment())
			
				for(int y=0; y<assignments.size(); y++ ) { //For each assignment in the list 
	//				Assignment assignment = assignments.get(y);
									
					if(assignments.get(y).getAssignmentGradeAchieved()!=null) {
						assignmentGradesAchieved.add(assignments.get(y).getAssignmentGradeAchieved());
						allAssignments =0;
					for(int z=0; z<assignmentGradesAchieved.size(); z++) {

						allAssignments = allAssignments + assignmentGradesAchieved.get(z);
					}
				}
		
		}
				assignmentArraySize = 0;
				assignmentArraySize = (assignmentGradesAchieved.size());	
				averageAssignGrade = (allAssignments / assignmentArraySize);

//				System.out.println("!!!!!!!!!!!!!!!!! allAssignments " + allAssignments);							
//				System.out.println("!!!!!!!!!!!!!!!!! assignmentArraySize " + assignmentArraySize);
//				System.out.println("!!!!!!!!!!!!!!!!!  averageAssignGrade " + averageAssignGrade);
//				System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111111");
//				
				List<Exam> exams = subject.getExam();
				ArrayList<Double> examGradesAchieved = new ArrayList<Double>(); 
				
				for(int y=0; y<exams.size(); y++ ) {
					Exam exam = exams.get(y);
					allExams =0;
					
					if(exams.get(y).getExamGradeAchieved()!=null) {
						examGradesAchieved.add(exams.get(y).getExamGradeAchieved());
						
						for(int z=0; z<examGradesAchieved.size(); z++) {
							allExams = allExams + examGradesAchieved.get(z);
						}
					}
					examArraySize =0;
					examArraySize = (examGradesAchieved.size());
					averageExamGrade = (allExams / examArraySize);
//	
//					System.out.println("!!!!!!!!!!!!!!!!! allExams " + allExams);							
//					System.out.println("!!!!!!!!!!!!!!!!! examArraySize " + examArraySize);
//					System.out.println("!!!!!!!!!!!!!!!!!  averageExamGrade " + averageExamGrade);
//					System.out.println("22222222222222222222222222222222222222222222222222222222222");

					//
					List<Attendance> attendances = subject.getAttendance();
					ArrayList<Double> attendanceGradesAchieved = new ArrayList<Double>(); 
					
					for(int y1=0; y1<attendances.size(); y1++ ) {
						Attendance attendance = attendances.get(y1);
						allAttendances =0;
						
						if(attendances.get(y1).getAttendanceAchieved()!=null) {
							attendanceGradesAchieved.add(attendances.get(y1).getAttendanceAchieved());
							
							for(int z1=0; z1<attendanceGradesAchieved.size(); z1++) {
								allAttendances = allAttendances + attendanceGradesAchieved.get(z1);
							}
						}
					//	attendanceArraySize =0;
						attendanceArraySize = (attendanceGradesAchieved.size());
						averageAttendanceGrade = (allAttendances / attendanceArraySize);
						
					//	subject.setAverageAttendanceGrade(averageAttendanceGrade);
			
						
//						System.out.println("!!!!!!!!!!!!!!!!! allAttendances " + allAttendances);							
//						System.out.println("!!!!!!!!!!!!!!!!! attendanceArraySize " + attendanceArraySize);
//						System.out.println("!!!!!!!!!!!!!!!!!  averageAttendanceGrade " + averageAttendanceGrade);
//						System.out.println("333333333333333333333333333333333333333333333333333333333333333333333");

					}
		//			subjectRepository.save(subject);
				}
				
				List<Assignment> assignments1 = subject.getAssignment();
				ArrayList<Double> assignmentGradePredictionsList = new ArrayList<Double>(); 
				//This list will contain the grade prediction for each assignment belonging to the subject that doesn't have a result yet
				
				for(int t=0; t<assignments1.size(); t++) {				
				
					if(assignments1.get(t).getAssignmentGradeAchieved()==null) {
						assignmentGradePrediction = ( averageAssignGrade / 100) * (assignments1.get(t).getAssignmentGradeWorth()); 
						
					
					}
//					System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
//					System.out.println("??????????????????????????? "  + assignmentGradePrediction);	
				}

				
//				System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
//				System.out.println("??????????????????????????? "  + assignmentGradePrediction);	
//				
				
				
				
				
				
				
				
//		
			//	subjectRepository.save(subject);
		}
		
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		
		
	return "allSubjects";
		
	}
	
	@GetMapping("/subjectSearch")
	public String subjectSearch(ModelMap map) {
		
	ArrayList<String> subjects = new ArrayList<String>();



		map.addAttribute("subjects", subjects);
		return "subjectSearch";

	}
	
	//SUBJECT SEARCH
	@PostMapping("/subjectSearch")
	public String subjectSearchPost(ModelMap map, @ModelAttribute Subject subject, Model model) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String email = loggedInUser.getName();   
			
		User user = userRepository.findByEmailAddress(email);
		
		
		
		List<Subject> subjects = user.getSubject();
		
		if(subject.getSubjectName()!="") {
			
			for (int i = 0; i < subjects.size(); i++) {
			
				if(subjects.get(i).getSubjectName().toLowerCase().contains((subject.getSubjectName().toLowerCase()))) {
					Long subjectId = subjects.get(i).getSubjectId();
					String subjectName = subjects.get(i).getSubjectName();
					model.addAttribute("subjectId", subjectId);
					model.addAttribute("subjectName", subjectName);
					
				}
			}
		}
		
		map.addAttribute("subjects", subjects);
		return "subjectSearch";
		
	}
	
	@RequestMapping(value = "/viewSubject{id}", method = RequestMethod.GET)
	public String subject(@PathVariable(value = "id") Long subjectId, @ModelAttribute Subject subject, Model model) {

		subject = subjectRepository.findBySubjectId(subjectId);
		//		.orElseThrow(() -> new ResourceNotFoundException("Subject", "id", subjectId));

		
		String subjectName = subject.getSubjectName();		
		
		model.addAttribute("subjectId", subjectId);
		model.addAttribute("subject", subject);
		model.addAttribute("subjectName", subjectName);	
		
		return "viewSubject"; 
		
	}
	
	

	@RequestMapping(value = "/editsubject/{subjectId}", method = RequestMethod.GET)
	public String editSubject(@PathVariable(value = "subjectId")Long subjectId, Model model) {
	
		
		SubjectDto dto = new SubjectDto();
		model.addAttribute("SubjectId", subjectId);
		model.addAttribute("SubjectDto", dto);	
		
		return "editSubject";
		
	}
			
	
	
	@PostMapping("editSubject")
	public String editSubject(ModelMap map, @ModelAttribute SubjectDto dto, BindingResult result) {
		
		Subject subject = subjectRepository.findOne(dto.getSubjectId());
		

		if(dto.getSubjectName()!=null) {
			subject.setSubjectName(dto.getSubjectName());
		}
		
		if(dto.getSubjectGradeGoal()!=null) {
			subject.setSubjectGradeGoal(dto.getSubjectGradeGoal());
		}
		
		if(dto.getCaCompletedWorth()!=null) {
			subject.setCaCompletedWorth(dto.getCaCompletedWorth());
		}
		
		if(dto.getSubjectResults()!=null) {
			subject.setSubjectResults(dto.getSubjectResults());
		}
		
		subjectRepository.save(subject);
	
		return "redirect:/allSubjects";

		
	}

	
	
	@GetMapping("/subjects")
	public List<Subject> getAllSubject(){
		return subjectRepository.findAll();
	}


	
	
	
	

	
	// Delete a subject
//		@DeleteMapping("/subjects/{id}")
//		public ResponseEntity<Subject> deleteSubject(@PathVariable(value = "id") Long subjectId) {
//			Subject subject = subjectRepository.findOne(subjectId);
//		    if(subject == null) {
//		        return ResponseEntity.notFound().build();
//		    }
//
//		    subjectRepository.delete(subject);
//		    return ResponseEntity.ok().build();
//		}
		


}
