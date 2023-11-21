package com.holoyolo.app.accBookHistory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.accBookHistory.service.AccBookHistoryService;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AccBookHistoryController {
	
	private final ApiCall apiCall;
	
	@Autowired 
	AccBookHistoryService accBookHistoryService;
	
	@GetMapping("/member/accBook")
	public String test() {
		AccBookHistoryVO vo = new AccBookHistoryVO();
		vo.setAbHistoryId(0);
		System.out.println(accBookHistoryService.test(vo));
		
		return "member/accBook/accBook";
	}
	
	@GetMapping("/api")
	public void api() {
		
		apiCall.getPosts();
	}
}
