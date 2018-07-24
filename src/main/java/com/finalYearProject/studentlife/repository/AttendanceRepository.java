package com.finalYearProject.studentlife.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finalYearProject.studentlife.model.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

}

