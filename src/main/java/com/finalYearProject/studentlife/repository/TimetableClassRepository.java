package com.finalYearProject.studentlife.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.finalYearProject.studentlife.model.Exam;
import com.finalYearProject.studentlife.model.TimetableClass;

@Repository
public interface TimetableClassRepository extends JpaRepository<TimetableClass, Long>{
	
/*    @Transactional
    Long deleteByTimetableClassId(Long timetableClassId);*/
	
/*	@Modifying
	@Query("delete from Timetable_class t where t.id = ?1")
	void delete(Long entityId);
*/
}
