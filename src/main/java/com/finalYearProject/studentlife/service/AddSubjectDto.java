package com.finalYearProject.studentlife.service;

import com.finalYearProject.studentlife.repository.UserRepository;

//I use this class to validate the add Subject 
public class AddSubjectDto {
	
		
		private String subjectName;
		
		private Double subjectGradeGoal;
		
		
		private Double maxSubRemMarks;



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


//		public void add(UserRepository userR) {
//			// TODO Auto-generated method stub
//			
//		}

}
