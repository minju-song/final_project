package com.holoyolo.app.boardLike.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.boardLike.service.BoardLikeService;
import com.holoyolo.app.boardLike.service.BoardLikeVO;

@Controller
public class BoardLikeController {

	@Autowired
	BoardLikeService boardLikeService;

	// 좋아요
	@PostMapping("/addLike")
	@ResponseBody
	public Map<String, Object> addLikeCheck(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestBody BoardLikeVO vo) {
		vo.setMemberId(principalDetails.getUsername());
	String setResult = 	boardLikeService.checkLike(vo);
		Map<String, Object> result = new HashMap<>();
		result.put("like", vo);
		result.put("resultMsg", setResult);
		return result;
	}
	@PostMapping("/likeCheck")
	@ResponseBody
	public Map<String, Object> likecheck(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestBody BoardLikeVO vo) {
		vo.setMemberId(principalDetails.getUsername());
		String tab = boardLikeService.viewCheck(vo);
	
		Map<String, Object> result = new HashMap<>();
		result.put("searchLike", tab);
		return result;
	}
}
