package com.holoyolo.app.accBudget.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.holoyolo.app.accBookSuccessHistory.service.AccBookSuccessHistoryService;
import com.holoyolo.app.accBudget.service.AccBudgetService;
import com.holoyolo.app.accBudget.service.AccBudgetVO;
import com.holoyolo.app.auth.PrincipalDetails;

@Controller
public class AccBudgetController {
	
	@Autowired
	AccBudgetService accBudgetService;
	
	@Autowired
	AccBookSuccessHistoryService accBookSuccessHistoryService;
	
	//예산 등록
	@PostMapping("member/budgetInsert")
	public String budgetInsertProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, AccBudgetVO vo) {
		//회원아이디 조회
		String id = principalDetails.getUsername();
		vo.setMemberId(id);
		
		//예산 데이터 입력
		int ck = accBudgetService.insertBudget(vo);
		if(ck < 1) {
			System.out.println("입력안됨");
		}
		
		if(accBookSuccessHistoryService.insertSuccess(vo)) {
			System.out.println("입력됨");
		}
		
		return "redirect:/member/accBook";
	}
	
	//예산 수정
	@PostMapping("member/budgetUpdate")
	public String budgetUpdateProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, AccBudgetVO vo) {
		//회원아이디 조회
		String id = principalDetails.getUsername();
		vo.setMemberId(id);
		
		//이전 예산데이터 사용여부 N으로 수정
		if(accBudgetService.updateBudget(vo) > 0) {			
			int ck = accBudgetService.insertBudget(vo);
			//예산 데이터 입력
			if(ck < 1) {
				System.out.println("입력안됨");
			}
			else {
				if(accBookSuccessHistoryService.deleteIng(id) > 0) {
					if(accBookSuccessHistoryService.insertSuccess(vo)) {
						System.out.println("입력됨");
					}
				}
			}
		}
		
		return "redirect:/member/accBook";
	}
}
