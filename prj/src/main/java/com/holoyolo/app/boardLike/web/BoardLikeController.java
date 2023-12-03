package com.holoyolo.app.boardLike.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.boardLike.service.BoardLikeService;
import com.holoyolo.app.boardLike.service.BoardLikeVO;

@Controller
public class BoardLikeController {
	
	BoardLikeService boardLikeService;

	//좋아요
		@RequestMapping(value = "/likeCheck", method = RequestMethod.POST)
		@ResponseBody
		public Map<String, Object> likecheck(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody BoardLikeVO vo) {
			System.out.println(vo);
			vo.setMemberId(principalDetails.getUsername());
			boardLikeService.checkLike(vo);
			Map<String, Object> result = new HashMap<>();
			return result;
		}
}
