package com.holoyolo.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {
	
	@GetMapping("/adminDashboard")
	public String adminHome() {
		return "adminIndex";
	}
	
	@GetMapping("/adminReport")
	public String adminReport() {
		return "admin/reportMgt";
	}
	@GetMapping("/adminMember")
	public String adminMember() {
		return "admin/memberMgt";
	}
	@GetMapping("/adminHolopay")
	public String adminHolopay() {
		return "admin/holopayMgt";
	}
	@GetMapping("/adminClub")
	public String adminClub() {
		return "admin/clubMgt";
	}
	@GetMapping("/adminTrade")
	public String adminTrade() {
		return "admin/tradeMgt";
	}
	@GetMapping("/adminCommunity")
	public String adminCommunity() {
		return "admin/communityMgt";
	}
	@GetMapping("/adminNotice")
	public String adminNotice() {
		return "admin/noticeMgt";
	}
}
