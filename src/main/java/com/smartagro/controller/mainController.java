package com.smartagro.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.smartagro.model.User;
import com.smartagro.repository.userRepository;

@Controller
public class mainController {
	@Autowired
	private userRepository uRepo;
	
	
	@GetMapping("/")
	public String adminPage() {
		return "index.html";
	}
	
	@GetMapping("/signup")
	public String signup() {
		return "signup.html";
		
	}
	
	@PostMapping("/register")
	public String userRegister(@ModelAttribute User u) {
		String hashpwd=DigestUtils.shaHex(u.getPassword());
		u.setPassword(hashpwd);
		uRepo.save(u);
		return "index.html";
	}

}
