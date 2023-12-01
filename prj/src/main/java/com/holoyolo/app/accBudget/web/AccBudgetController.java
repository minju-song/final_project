package com.holoyolo.app.accBudget.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
		boolean ck = accBudgetService.insertBudget(vo);	
		return "redirect:/member/accBook";		
	}
	
	//예산 수정
	@PostMapping("member/budgetUpdate")
	public String budgetUpdateProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, AccBudgetVO vo) {
		//회원아이디 조회
		String id = principalDetails.getUsername();
		vo.setMemberId(id);
		accBudgetService.updateBudget(vo);
		
		
		return "redirect:/member/accBook";
	}
	
	//예산삭제
	@GetMapping("budgetDelete")
	@ResponseBody
	public Map<String, Object> budgetDelete(@AuthenticationPrincipal PrincipalDetails principalDetails){

		//결과맵
		Map<String, Object> map = new HashMap<>();
		
		accBudgetService.deleteBudget(principalDetails.getUsername());		
			map.put("result", "success");
		
		return map;

	}
}
