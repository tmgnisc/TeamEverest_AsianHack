package com.smartagro.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.smartagro.model.Complains;
import com.smartagro.model.DeviceRequestRemoval;
import com.smartagro.model.Notice;
import com.smartagro.model.User;
import com.smartagro.model.newDevices;
import com.smartagro.repository.DeviceRemoveRepository;
import com.smartagro.repository.complainRepository;
import com.smartagro.repository.newDevicesRepository;
import com.smartagro.repository.noticeRepository;
import com.smartagro.repository.userRepository;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;

@Controller
@MultipartConfig
public class mainController {
	@Autowired
	private userRepository uRepo;
	
	@Autowired
	private newDevicesRepository ndRepo;
	
	@Autowired
	private noticeRepository nRepo;
	
	@Autowired
	private complainRepository cRepo;
	
	@Autowired
	private DeviceRemoveRepository drRepo;
	
	
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
	                System.out.println("Item Picture saved to: " + imgPath);
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
	public String adminDashboard(@ModelAttribute User u, Model model) {
		
		long totalDevices = ndRepo.count();
		
		long totaluserCount = uRepo.count();
		
		List<User> uList = uRepo.findAll();
		
		model.addAttribute("uList", uList);
		model.addAttribute("totalUser",totaluserCount);
		model.addAttribute("totalDevices", totalDevices);
		return "superAdmin/adminDashboard.html";
		
	}
	
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable int id, Model model, HttpSession session) {
		
		User u = uRepo.getById(id);
		model.addAttribute("user",u );
		
		return "editForm.html";
		
	}
	
	@GetMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable int id, Model model) {
		uRepo.deleteById(id);
		
		long totalDevices = ndRepo.count();
		
		long totaluserCount = uRepo.count();
		
		List<User> uList = uRepo.findAll();
		model.addAttribute("uList", uList);
		model.addAttribute("totalUser",totaluserCount);
		model.addAttribute("totalDevices", totalDevices);
		
		return "superAdmin/adminDashboard.html";
		
	}
	
	@GetMapping("/addDevice")
	public String addDevice() { 
		return "superAdmin/add.html";
	}
	
	@PostMapping("/addDeviceData")
	public String addDeviceData(@ModelAttribute newDevices nd) {
		ndRepo.save(nd);
		return "superAdmin/add.html";
	}
	
	@GetMapping("/addUser")
	public String addUser() {
		return "superAdmin/adduser.html";
	}
	
	@GetMapping("/viewDevice")
	public String viewDevice(HttpSession session, Model model) {
		List<newDevices> ndList = ndRepo.findAll();
		session.setAttribute("device",ndList);
		model.addAttribute("device",ndList);
		return "superAdmin/map.html";
	}
	
	@GetMapping("/notice")
	public String notice(Model model) {
		
		List<Notice> notice = nRepo.findAll();
		model.addAttribute("notice", notice);
		return "superAdmin/notice.html";
	}
	
	@PostMapping("/addNotice")
	public String addNotice(@ModelAttribute Notice n, Model model) {
		nRepo.save(n);
		List<Notice> notice = nRepo.findAll();
		model.addAttribute("notice", notice);
		return "superAdmin/notice.html";
	}
	
	
	@GetMapping("/removeDevice")
	public String removeDevice(Model model) {
		
		List<DeviceRequestRemoval>  drr = drRepo.findAll();
		
		model.addAttribute("dr", drr);
		
		return "superAdmin/removedevice.html";
	}
	
	@GetMapping("/deleteDevice/{id}")
	public String deleteDevice(Model model, @PathVariable int id) {
		
		List<DeviceRequestRemoval>  drr = drRepo.findAll();
		
		model.addAttribute("dr", drr);
		
		ndRepo.deleteById(id);
		
		DeviceRequestRemoval dr = drRepo.findById(id)
									.orElseThrow(() -> new IllegalArgumentException("Invalid ID: " + id));
		
		dr.setStatus("removed");
		drRepo.save(dr);
		
		return "superAdmin/removedevice.html";
	}
	
	@GetMapping("/viewComplains")
	public String viewComplains(Model model) {
		List<Complains> cList = cRepo.findBySuggestionIsNull();
		model.addAttribute("cList", cList);
		
		return "superAdmin/viewcomplaint.html";
	}
	
	
	@PostMapping("/submitSuggestion")
	public String submitSuggestion(@RequestParam("complaintId") int complaintId,
	                                @RequestParam("suggestion") String suggestion,
	                                Model model) {
	    // Find the complaint by ID
	    Complains complaint = cRepo.findById(complaintId)
	                               .orElseThrow(() -> new IllegalArgumentException("Invalid complaint ID: " + complaintId));
	    
	    // Update the suggestion field
	    complaint.setSuggestion(suggestion);
	    
	    // Save the updated complaint back to the repository
	    cRepo.save(complaint);
	    
	    // Retrieve the updated list of complaints
	    List<Complains> cList = cRepo.findAll();
	    model.addAttribute("cList", cList);
	    
	    return "superAdmin/viewcomplaint.html";
	}
	
	
	
		
		
	
	

}
