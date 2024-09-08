package com.smartagro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartagro.model.Complains;

public interface complainRepository extends JpaRepository<Complains, Integer>{
	
	List<Complains> findBySuggestionIsNull();
	
	List<Complains> findBySuggestionIsNotNull();
	
	


}
