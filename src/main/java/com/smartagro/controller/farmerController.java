package com.smartagro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.smartagro.model.Notice;
import com.smartagro.repository.newDevicesRepository;
import com.smartagro.repository.noticeRepository;
import com.smartagro.repository.userRepository;

@Controller
public class farmerController {
	@Autowired
	private userRepository uRepo;
	
	@Autowired
	private newDevicesRepository ndRepo;
	
	@Autowired
	private noticeRepository nRepo;
	
	
	@GetMapping("/farmerDash")
	public String farmerDash() {
		return "farmer/farmers.html";
	}
	
	@GetMapping("/faq")
	public String faq() {
		return "farmer/faq.html";
	}
	
	@GetMapping("/farmerNotice")
	public String farmerNotice(Model model) {
		
		List<Notice> notice = nRepo.findAll();
		model.addAttribute("notice", notice);
		return "farmer/farmernotice.html";
	}

	
	@GetMapping("/removeDeviceReq")
	public String removeDevice() {
		return "farmer/removedev.html";
	}
	
	@GetMapping("/raiseComplain")
	public String raiseComplain() {
		return "farmer/complaint.html";
	}
}
