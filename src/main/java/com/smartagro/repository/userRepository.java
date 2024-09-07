package com.smartagro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartagro.model.User;
import java.util.List;


public interface userRepository extends JpaRepository<User, Integer> {

	List<User> findByEmail(String email);
	
	boolean existsByEmailAndPassword(String email, String password);
	
	String findUsernameByEmail(String email);
	
}
