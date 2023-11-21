package com.holoyolo.app.accBookHistory.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.accBookHistory.service.AccBookHistoryService;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;
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
	
	@GetMapping("/member/accBook")
	public String test(Model model) {
		apiCall.getPosts();
		
		Map<String, String> map = new HashMap<>();
		map = memberFinanceInfoService.getCardInfo();
//        Iterator<String> keys = map.keySet().iterator();
//        while (keys.hasNext()){
//            String key = keys.next();
//            model.addAttribute(key, map.get(key));
//        }
		
		model.addAttribute("cardNo", map.get("카드번호"));
		model.addAttribute("cardCompany", map.get("카드회사"));
		
		return "member/accBook/accBook";
	}
	
	@GetMapping("/api")
	public void api() {
		
		apiCall.getPosts();
		
	}
}
