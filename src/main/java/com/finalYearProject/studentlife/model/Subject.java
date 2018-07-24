package com.finalYearProject.studentlife.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.finalYearProject.studentlife.repository.UserRepository;

@Entity
public class Subject implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long subjectId;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<Exam> exam;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<Assignment> assignment;

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<Attendance> attendance;
	
	private String subjectName;
	
	private Double subjectGradeGoal;
	
	private Double caCompletedWorth; //The total value of all the CA the student has done so far. (not the result they got but what the CA is worth) for example assignment 1 is worth 20% 
	//assignment two is 30%, attendance is 10% and the exam is 40%. If the student did assignment 1 and 2 their CAworth would be 50%
	
	private Double subjectResults; //the grade they have so far in the subject (all their current results for all CA added together) 
	
	//Maximum remaining marks it's possible to achieve. 
	//For example the student currently has 50% of the subject marks
	//the max number of marks it's possible for them to get in the left over exams/assignments is 20%
	private Double maxSubRemMarks;
	
	private Double marksNeededToReachGoal; 
	
	private String isGoalPossible; //set to true but changes to false if it's not possile for the student to reach their grade goal.
	
	private Double highestPossibleGrade; //Changes when each CA is input. It's calculated by taking the students current results plus the total worth of the remaining CA.
	
	private Double gradePrediction;
	
	private String isGoalAchieved;
	
	private Double averageSubjectGrade; //this is the average of averageAttendanceGrade + averageAssignmentGrade + averageExamGade divided by 3(or 2 or 1 if the subject doesn't have all types of CAs
	private Double averageAssignmentGrade;
	private Double averageExamGrade;
	private Double averageAttendanceGrade;

//	public int getAverageAssignmentGrade;
	
	public Subject(Long subjectId, List<Exam> exam, List<Assignment> assignment, List<Attendance> attendance,
			String subjectName, Double subjectGradeGoal, Double caCompletedWorth, Double subjectResults,
			Double maxSubRemMarks, Double marksNeededToReachGoal, String isGoalPossible, Double highestPossibleGrade,
			Double gradePrediction, String isGoalAchieved, Double averageSubjectGrade, Double averageAssignmentGrade,  Double averageExamGrade, Double averageAttendanceGrade) {
		super();
		this.subjectId = subjectId;
		this.exam = exam;
		this.assignment = assignment;
		this.attendance = attendance;
		this.subjectName = subjectName;
		this.subjectGradeGoal = subjectGradeGoal;
		this.caCompletedWorth = caCompletedWorth;
		this.subjectResults = subjectResults;
		this.maxSubRemMarks = maxSubRemMarks;
		this.marksNeededToReachGoal = marksNeededToReachGoal;
		this.isGoalPossible = isGoalPossible;
		this.highestPossibleGrade = highestPossibleGrade;
		this.gradePrediction = gradePrediction;
		this.isGoalAchieved = isGoalAchieved;
		this.averageSubjectGrade = averageSubjectGrade;
		this.averageAssignmentGrade = averageAssignmentGrade;
		this.averageExamGrade = averageExamGrade;
		this.averageAttendanceGrade = averageAttendanceGrade;
	}

	
	
	
	public Double getAverageAssignmentGrade() {
		return averageAssignmentGrade;
	}




	public void setAverageAssignmentGrade(Double averageAssignmentGrade) {
		this.averageAssignmentGrade = averageAssignmentGrade;
	}




	public Double getAverageExamGrade() {
		return averageExamGrade;
	}




	public void setAverageExamGrade(Double averageExamGrade) {
		this.averageExamGrade = averageExamGrade;
	}




	public Double getAverageAttendanceGrade() {
		return averageAttendanceGrade;
	}




	public void setAverageAttendanceGrade(Double averageAttendanceGrade) {
		this.averageAttendanceGrade = averageAttendanceGrade;
	}




	public Double getAverageSubjectGrade() {
		return averageSubjectGrade;
	}




	public void setAverageSubjectGrade(Double averageSubjectGrade) {
		this.averageSubjectGrade = averageSubjectGrade;
	}




	public Double getGradePrediction() {
		return gradePrediction;
	}

	public void setGradePrediction(Double gradePrediction) {
		this.gradePrediction = gradePrediction;
	}

	public String getIsGoalAchieved() {
		return isGoalAchieved;
	}

	public void setIsGoalAchieved(String isGoalAchieved) {
		this.isGoalAchieved = isGoalAchieved;
	}

	public Double getCaCompletedWorth() {
		return caCompletedWorth;
	}

	public void setCaCompletedWorth(Double caCompletedWorth) {
		this.caCompletedWorth = caCompletedWorth;
	}

	public Double getMarksNeededToReachGoal() {
		return marksNeededToReachGoal;
	}

	public void setMarksNeededToReachGoal(Double marksNeededToReachGoal) {
		this.marksNeededToReachGoal = marksNeededToReachGoal;
	}

	public String getIsGoalPossible() {
		return isGoalPossible;
	}

	public void setIsGoalPossible(String isGoalPossible) {
		this.isGoalPossible = isGoalPossible;
	}

	public Double getHighestPossibleGrade() {
		return highestPossibleGrade;
	}

	public void setHighestPossibleGrade(Double highestPossibleGrade) {
		this.highestPossibleGrade = highestPossibleGrade;
	}

	public List<Exam> getExam() {
		if (exam==null)
			exam = new ArrayList<Exam>();
		
		return exam;
	}

	public List<Assignment> getAssignment() {
		if (assignment==null)
			assignment = new ArrayList<Assignment>();
		
		return assignment;
	}


	public List<Attendance> getAttendance() {
		if (attendance==null)
			attendance = new ArrayList<Attendance>();
		return attendance;
	}

	public void setAttendance(List<Attendance> attendance) {
		this.attendance = attendance;
	}

	public Subject(Long subjectId, List<Exam> exam, List<Assignment> assignment, List<Attendance> attendance,
			String subjectName, Double subjectGradeGoal, Double caCompletedWorth, Double subjectResults,
			Double maxSubRemMarks, Double marksNeededToReachGoal, String isGoalPossible, Double highestPossibleGrade) {
		super();
		this.subjectId = subjectId;
		this.exam = exam;
		this.assignment = assignment;
		this.attendance = attendance;
		this.subjectName = subjectName;
		this.subjectGradeGoal = subjectGradeGoal;
		this.caCompletedWorth = caCompletedWorth;
		this.subjectResults = subjectResults;
		this.maxSubRemMarks = maxSubRemMarks;
		this.marksNeededToReachGoal = marksNeededToReachGoal;
		this.isGoalPossible = isGoalPossible;
		this.highestPossibleGrade = highestPossibleGrade;
	}

	public Subject() {
		super();
		this.subjectId = subjectId;
		this.exam = exam;
		this.attendance = attendance;
		this.assignment = assignment;
		this.subjectName = subjectName;
		this.subjectGradeGoal = subjectGradeGoal;
		this.subjectResults = subjectResults;
		this.maxSubRemMarks = maxSubRemMarks;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public void setExam(List<Exam> exam) {
		this.exam = exam;
	}

	public void setAssignment(List<Assignment> assignment) {
		this.assignment = assignment;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Double getSubjectGradeGoal() {
		return subjectGradeGoal;
	}

	public void setSubjectGradeGoal(Double subjectGradeGoal) {
		this.subjectGradeGoal = subjectGradeGoal;
	}

	public Double getSubjectResults() {
		return subjectResults;
	}

	public void setSubjectResults(Double subjectResults) {
		this.subjectResults = subjectResults;
	}

	public Double getMaxSubRemMarks() {
		return maxSubRemMarks;
	}

	public void setMaxSubRemMarks(Double maxSubRemMarks) {
		this.maxSubRemMarks = maxSubRemMarks;
	}

	public void add(UserRepository userR) {
		// TODO Auto-generated method stub
		
	}
	
	public void addExam(Exam exam){

		getExam().add(exam);
	}

	
	public void addAssignment(Assignment assignment){

		getAssignment().add(assignment);
	}

	public void addAttendance(Attendance attendance) {
		getAttendance().add(attendance);
		
	}
	


}
