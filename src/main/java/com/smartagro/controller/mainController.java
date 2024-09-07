package com.smartagro.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartagro.model.User;
import com.smartagro.repository.userRepository;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;

@Controller
@MultipartConfig
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
	    public String userRegister(
	            @RequestParam("username") String username,
	            @RequestParam("email") String email,
	            @RequestParam("password") String password,
	            @RequestParam("address") String address,
	            @RequestParam("category") String category,
	            @RequestParam("image") MultipartFile image) {
	        
	        // Implementation for processing the user registration and file upload
	        String hashpwd = DigestUtils.shaHex(password); // Example for password hashing
	        
	        User nu = new User();
	        nu.setUsername(username);
	        nu.setEmail(email);
	        nu.setPassword(hashpwd);
	        nu.setAddress(address);
	        nu.setCategory(category);
	        
	        if (image != null && !image.isEmpty()) {
	            try {
	                File saveDir = new ClassPathResource("static/img").getFile();
	                Path imgPath = Paths.get(saveDir.getAbsolutePath(), image.getOriginalFilename());
	                Files.copy(image.getInputStream(), imgPath, StandardCopyOption.REPLACE_EXISTING);
	                nu.setImage(image.getOriginalFilename());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	         uRepo.save(nu);
	        
	        return "index.html"; // Redirect to avoid form resubmission on refresh
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
