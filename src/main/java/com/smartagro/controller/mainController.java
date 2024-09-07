package com.smartagro.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@PostMapping("/login")
	public String login(@ModelAttribute User u, Model model, HttpSession session) {
		if(uRepo.existsByUsernameAndPassword(u.getUsername(), DigestUtils.shaHex(u.getPassword()))) {
		

				
				session.setAttribute("username", u.getUsername());
				
				System.out.println(session.getAttribute("username"));
				session.setMaxInactiveInterval(1800);
				
				return "test.html"; 
			}
			
		return "index.html";
		}
	
	
	@GetMapping("/superAdmin")
	public String adminDashboard() {
		
		return "superAdmin/adminDashboard.html";
		
	}
	
	@GetMapping("/addDevice")
	public String addDevice() { 
		return "superAdmin/add.html";
	}
	
	@GetMapping("/addUser")
	public String addUser() {
		return "superAdmin/adduser.html";
	}
	@GetMapping("/viewDevice")
	public String viewDevice() {
		return "superAdmin/map.html";
	}
	
	@GetMapping("/notice")
	public String notice() {
		return "superAdmin/notice.html";
	}
	
	@GetMapping("/removeDevice")
	public String removeDevice() {
		return "superAdmin/removedevice.html";
	}
	
	
	
		
		
	
	

}
