package com.holoyolo.app.reply.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.reply.service.ReplyService;
import com.holoyolo.app.reply.service.ReplyVO;

@Controller
public class ReplyController {
	@Autowired
	ReplyService replyService;

	@PostMapping("/loadReply")
	@ResponseBody
	public Map<String, Object> loadReply(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestBody JSONObject reqJson) {

		reqJson.put("memberId", (String) principalDetails.getUsername());
		System.out.println(reqJson);
		List<ReplyVO> resultList = replyService.searchReplyPage(reqJson);
		int totalRecords = replyService.getTotalReplyRecords(reqJson);
		Map<String, Object> result = new HashMap<>();
		result.put("historyList", resultList);
		result.put("totalRecords", totalRecords);
		return result;
	}

	@PostMapping("/insertReply")
	@ResponseBody
	public String insertReply(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestBody ReplyVO vo) {
		
		vo.setMemberId(principalDetails.getUsername());
		System.out.println("===========================");
		System.out.println("vo : " + vo);
		replyService.insertReply(vo);
		
		return "/loadReply";
	}
}
