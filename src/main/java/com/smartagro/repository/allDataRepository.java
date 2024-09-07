package com.smartagro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smartagro.model.alldata;

public interface allDataRepository extends JpaRepository<alldata, Integer>{

	 // Custom query to get the most recent data based on the date column
//	@Query("SELECT a FROM alldata a ORDER BY a.date DESC")
//    List<alldata> findTopByOrderByDateDesc();
    
    @Query("SELECT a FROM alldata a ORDER BY a.date DESC LIMIT 1")
    List<alldata> findTopByOrderByDateDesc();
	
}
