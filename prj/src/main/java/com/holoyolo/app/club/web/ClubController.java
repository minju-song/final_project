package com.holoyolo.app.club.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;

@Controller
public class ClubController {
	
	@Autowired
	ClubService clubService;
	
	@GetMapping("/admin/club")
	public String selectClubList(Model model) {
		List<ClubVO> list = clubService.selectClubAll();
		model.addAttribute("clubList", list);
		return "admin/clubMgt";
	}
}
