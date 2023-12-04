package com.holoyolo.app.reply.web;

import java.util.HashMap;
import java.util.Map;

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
			@RequestBody ReplyVO vo) {
	System.out.println(vo);
		Map<String, Object> result = new HashMap<>();
		
		return result;
	}


@PostMapping("/insertReply")
@ResponseBody
	public Map<String, Object> insertReply(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestBody ReplyVO vo) {
	System.out.println(vo);
		Map<String, Object> result = new HashMap<>();
		result.put("searchLike", result);
		return result;
	}
}
