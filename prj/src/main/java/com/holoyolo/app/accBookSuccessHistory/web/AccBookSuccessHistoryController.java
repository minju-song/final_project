package com.holoyolo.app.accBookSuccessHistory.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.accBookSuccessHistory.service.AccBookSuccessHistoryService;
import com.holoyolo.app.accBookSuccessHistory.service.AccBookSuccessHistoryVO;
import com.holoyolo.app.auth.PrincipalDetails;

@Controller
public class AccBookSuccessHistoryController {
	
	@Autowired
	AccBookSuccessHistoryService accBookSuccessHistoryService;
	
	//성공여부가져오기
	@GetMapping("getSuccess")
	@ResponseBody
	public String getSuccessByDay(@AuthenticationPrincipal PrincipalDetails principalDetails, 
			                      AccBookSuccessHistoryVO vo) {
		vo.setMemberId(principalDetails.getUsername());

		String result = accBookSuccessHistoryService.getSuccessByDay(vo);

		return result;
	}
	
	//성공기록가져오기
	@GetMapping("/getRecord")
	@ResponseBody
	public Map<String, Object> getRecord(@AuthenticationPrincipal PrincipalDetails principalDetails, 
			                      AccBookSuccessHistoryVO vo) {
		Map<String, Object> map = new HashMap<>();
		vo.setMemberId(principalDetails.getUsername());
		
		vo = accBookSuccessHistoryService.getSuccessRecord(vo);
		map.put("record", vo);
		return map;
	}
	
}
