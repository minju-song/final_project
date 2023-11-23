package com.holoyolo.app.accBudget.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.holoyolo.app.accBudget.service.AccBudgetService;
import com.holoyolo.app.accBudget.service.AccBudgetVO;
import com.holoyolo.app.auth.PrincipalDetails;

@Controller
public class AccBudgetController {
	
	@Autowired
	AccBudgetService accBudgetService;
	
	//budgetInsert
	@PostMapping("member/budgetInsert")
	public String budgetInsertProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, AccBudgetVO vo) {
		vo.setMemberId(principalDetails.getUsername());
		int ck = accBudgetService.insertBudget(vo);
		if(ck < 1) {
			System.out.println("입력안됨");
		}
		return "redirect:/member/accBook";
	}
}
