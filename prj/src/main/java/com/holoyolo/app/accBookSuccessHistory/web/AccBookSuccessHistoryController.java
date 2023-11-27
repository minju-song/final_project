package com.holoyolo.app.accBookSuccessHistory.web;

import java.util.Collections;

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
	public String getSuccessByDay(@AuthenticationPrincipal PrincipalDetails principalDetails, AccBookSuccessHistoryVO vo) {
		vo.setMemberId(principalDetails.getUsername());

		String result = accBookSuccessHistoryService.getSuccessByDay(vo);

		return result;
	}
	
}
