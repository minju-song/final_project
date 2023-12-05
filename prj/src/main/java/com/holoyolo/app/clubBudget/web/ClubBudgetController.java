package com.holoyolo.app.clubBudget.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.holoyolo.app.clubBudget.service.ClubBudgetService;
import com.holoyolo.app.clubBudget.service.ClubBudgetVO;

@Controller
public class ClubBudgetController {
	
	@Autowired
	ClubBudgetService clubBudgetService;
	
	//클럽예산변경
	@PostMapping("member/club/updateBudget")
	public String updateBudget(ClubBudgetVO vo) {
		System.out.println("실행됨"+vo);
		String result = clubBudgetService.updateClubBudget(vo);
		
		return "redirect:/member/club/clubPage?clubId="+vo.getClubId();
		
	}
}
