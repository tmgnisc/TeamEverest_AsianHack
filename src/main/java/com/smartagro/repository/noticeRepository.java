package com.smartagro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartagro.model.Notice;

public interface noticeRepository extends JpaRepository<Notice, Integer>{
	
	

}
