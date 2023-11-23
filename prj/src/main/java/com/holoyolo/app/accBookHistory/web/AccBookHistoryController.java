package com.holoyolo.app.accBookHistory.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.accBookHistory.service.AccBookHistoryService;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;

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
	
	@GetMapping("/member/accBook")
	public String test(Model model) {
		apiCall.getPosts();
		
		Map<String, String> map = new HashMap<>();
		map = memberFinanceInfoService.getCardInfo();
		Date joinDate = memberService.selectJoinDate();
		
		model.addAttribute("cardNo", map.get("카드번호"));
		model.addAttribute("cardCompany", map.get("카드회사"));
		model.addAttribute("joinDate", joinDate);
		
		return "user/accBook/accBook";
	}
	
	@GetMapping("/api")
	public void api() {
		
		apiCall.getPosts();
		
	}
	
	@GetMapping("getAb")
	@ResponseBody
	public List<AccBookHistoryVO> getAb(AccBookHistoryVO vo) {
		System.out.println(">>>>>>>>>>>"+vo.getPayDate().toString());
		List<AccBookHistoryVO> list = accBookHistoryService.getAccHistory(vo);

		return list;
	}
}
