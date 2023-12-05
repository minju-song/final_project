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
	public Map<String, Object> loadReply(@RequestBody JSONObject reqJson) {

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
	public void insertReply(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody ReplyVO vo) {
		//	upperReplyId = 0 이면 일반 아니면 대댓글
		vo.setMemberId(principalDetails.getUsername());
			replyService.insertReply(vo);
		

	}
}
