package com.holoyolo.app.accBookHistory.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.accBookHistory.service.AccBookHistoryService;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;
import com.holoyolo.app.accBudget.service.AccBudgetService;
import com.holoyolo.app.accBudget.service.AccBudgetVO;
import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AccBookHistoryController {
	
	private final ApiCall apiCall;
	
	@Autowired 
	AccBookHistoryService accBookHistoryService;
	
	@Autowired 
	MemberFinanceInfoService memberFinanceInfoService;
	
	@Autowired 
	MemberService memberService;
	
	@Autowired
	AccBudgetService accBudgetService;
	
	@GetMapping("/member/accBook")
	public String test(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model) {
		Map<String, String> map = new HashMap<>();
		Map<String, Object> budgetMap = new HashMap<>();
		
		String id = principalDetails.getUsername();
		
		map = memberFinanceInfoService.getCardInfo(id);
		System.out.println("카드 번호 : "+map.get("카드번호"));
		if(!map.get("카드번호").equals("null")) {			
			apiCall.getPosts(id);		
		}
		
		Date joinDate = memberService.selectJoinDate(id);
		budgetMap = accBudgetService.getBudgetNow(id);
		
		String budgetUnit = "";
		//예산단위
		if (budgetMap.get("예산단위").equals("YA1")) budgetUnit = "일";
		else if (budgetMap.get("예산단위").equals("YA2")) budgetUnit = "주";
		else if (budgetMap.get("예산단위").equals("YA3")) budgetUnit = "월";
		else budgetUnit = "잘못 입력";
		
		
		model.addAttribute("accBudgetPrice", budgetMap.get("예산금액"));
		model.addAttribute("accBudgetUnit", budgetUnit);
		model.addAttribute("cardNo", map.get("카드번호"));
		model.addAttribute("cardCompany", map.get("카드회사"));
		model.addAttribute("joinDate", joinDate);
		model.addAttribute("MemberFinanceInfoVO", new MemberFinanceInfoVO());
		model.addAttribute("AccBudgetVO", new AccBudgetVO());
		
		return "user/accBook/accBook";
	}
	
	@GetMapping("/api")
	public void api() {
		
//		apiCall.getPosts();
		
	}
	
	@GetMapping("getAb")
	@ResponseBody
	public List<AccBookHistoryVO> getAb(@AuthenticationPrincipal PrincipalDetails principalDetails, AccBookHistoryVO vo) {
		vo.setMemberId(principalDetails.getUsername());
		List<AccBookHistoryVO> list = accBookHistoryService.getAccHistory(vo);

		return list;
	}
	
	@GetMapping("getSumPrice")
	@ResponseBody
	public int getSumPrice(@AuthenticationPrincipal PrincipalDetails principalDetails,AccBookHistoryVO vo) {
		vo.setMemberId(principalDetails.getUsername());
		int price = accBookHistoryService.getSumPrice(vo);
		
		return price;
	}
}
