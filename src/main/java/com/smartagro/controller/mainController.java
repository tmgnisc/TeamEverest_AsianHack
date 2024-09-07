package com.smartagro.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.smartagro.model.User;
import com.smartagro.repository.userRepository;

import jakarta.servlet.http.HttpSession;

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
	
	@PostMapping("login")
	public String login(@ModelAttribute User u, Model model, HttpSession session) {
		if(uRepo.existsByEmailAndPassword(u.getEmail(), DigestUtils.shaHex(u.getPassword()))) {
			String username = uRepo.findUsernameByEmail(u.getEmail());
			if(username!=null) {
				session.setAttribute("username", username);
				model.addAttribute("email", u.getEmail());
				System.out.println(session.getAttribute("email"));
				session.setMaxInactiveInterval(1800);
				
				return ""; 
			}
			
		}
		
		return "";
	
	}
	

}
