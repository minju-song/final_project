package com.holoyolo.app.boardLike.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.boardLike.service.BoardLikeService;
import com.holoyolo.app.boardLike.service.BoardLikeVO;

@Controller
public class BoardLikeController {
	
	BoardLikeService boardLikeService;

	@GetMapping("/likeCheck")
	@ResponseBody
	public int likeCheck(@AuthenticationPrincipal PrincipalDetails prd, @RequestBody BoardLikeVO req) {
		
		req.setMemberId(prd.getUsername());
		boardLikeService.checkLike(req);
		
		
		return 0 ;
	}
}
