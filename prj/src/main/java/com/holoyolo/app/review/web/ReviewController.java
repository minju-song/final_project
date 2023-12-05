package com.holoyolo.app.review.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubMember.service.ClubMemberService;
import com.holoyolo.app.clubMember.service.ClubMemberVO;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.review.service.ReviewService;
import com.holoyolo.app.review.service.ReviewVO;

@Controller
public class ReviewController {
	
	@Autowired
	ReviewService reviewService;
	
	@Autowired
	ClubService clubService;
	
	@Autowired
	ClubMemberService clubMemberService;
	
	@Autowired
	MemberService memberService;
	
	@GetMapping("/member/club/clubReview")
	public String clubPage(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model,HttpSession session, ClubVO vo) {
		//회원이 가입한 클럽인지 체크한 후 세션에 저장
		ClubMemberVO cmvo = new ClubMemberVO();
		cmvo.setClubId(vo.getClubId());
		cmvo.setMemberId(principalDetails.getUsername());
		cmvo = clubMemberService.checkMyClub(cmvo);
		
		if(cmvo != null) {
			if(cmvo.getStopDate() != null) {
				if(cmvo.getJoinDate() == null) {
					session.setAttribute("check", "재가입승인대기");
				}
				else {					
					session.setAttribute("check", "탈퇴");
				}
			}
			else if (cmvo.getJoinDate() == null) {
				session.setAttribute("check", "승인대기");
			}
			else {				
				session.setAttribute("check", true);
			}
		}
		else {
			session.setAttribute("check", false);
		}
		
		MemberVO memberVO = memberService.selectUser(principalDetails.getUsername());
		
		Map<String, Object> map = new HashMap<>();
		
		map = reviewService.reviewPage(vo.getClubId(), principalDetails.getUsername());
		
		System.out.println(memberVO);
		
		model.addAttribute("user", memberVO);
		model.addAttribute("result", map);	
		model.addAttribute("menu", "club");
		model.addAttribute("subMenu", "review");
		model.addAttribute("userId", principalDetails.getUsername());

		return "user/club/clubReview";
	}
	
	@PostMapping("/member/reviewinsert")
	@ResponseBody
	public Map<String, Object> insertReview(@AuthenticationPrincipal PrincipalDetails principalDetails, ReviewVO vo) {
		Map<String, Object> map = new HashMap<>();
		
		vo.setMemberId(principalDetails.getUsername());
		
		System.out.println("들어온 리뷰"+vo);
		
		if(reviewService.insertReview(vo) > 0) {
			map.put("result", "success");
		}
		else {
			map.put("result", "fail");
		}
		
		map.put("review", vo);
		
		
		return map;
	}
	
	@PostMapping("/member/reviewUpdate")
	@ResponseBody
	public Map<String, Object> updateReview(@AuthenticationPrincipal PrincipalDetails principalDetails, ReviewVO vo) {
		Map<String, Object> map = new HashMap<>();
		System.out.println(vo);
		
		if(reviewService.updateReview(vo) > 0) {
			map.put("result", "success");
		}
		else {
			map.put("result", "fail");
		}
		
		return map;
	}
	
	@GetMapping("/member/reviewDelete")
	@ResponseBody
	public Map<String, Object> deleteReview(ReviewVO vo){
		Map<String, Object> map = new HashMap<>();
		System.out.println(vo);
		
		if(reviewService.deleteReview(vo) > 0) {
			map.put("result", "success");
		}
		else {
			map.put("result", "fail");
		}
		
		return map;
	}
	
}
