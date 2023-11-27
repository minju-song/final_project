package com.holoyolo.app.club.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;

@Controller
public class ClubController {
	
	@Autowired
	ClubService clubService;
	
	@GetMapping("/clublist")
	public String clubListPage(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model) {
		ClubVO vo = new ClubVO();
		vo.setPage(1);
		List<ClubVO> list = clubService.getAllClubList();
		
		model.addAttribute("list", list);
		return "user/club/clublist";
	}
	
	@GetMapping("clubPaging")
	@ResponseBody
	public List<ClubVO> clubPaging(ClubVO vo) {
		List<ClubVO> list = new ArrayList<>();
		list = clubService.getClubList(vo);
		System.out.println(vo);
		return list;
	}
}
