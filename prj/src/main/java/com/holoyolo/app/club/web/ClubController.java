package com.holoyolo.app.club.web;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.attachment.service.AttachmentService;
import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubBudget.service.ClubBudgetService;
import com.holoyolo.app.clubBudget.service.ClubBudgetVO;
import com.holoyolo.app.clubMember.service.ClubMemberService;
import com.holoyolo.app.clubMember.service.ClubMemberVO;
import com.holoyolo.app.clubSuccessHistory.service.ClubSuccessHistoryService;

@Controller
public class ClubController {
	
	@Autowired
	ClubService clubService;
	
	@Autowired
	ClubMemberService clubMemberService;
	
	@Autowired
	ClubSuccessHistoryService clubSuccessHistoryService;
	
	@Autowired
	ClubBudgetService clubBudgetService;
	
	@Autowired
	AttachmentService attachmentService;
	

	@GetMapping("/admin/club")
	public String selectClubList(Model model) {
		List<ClubVO> list = clubService.selectClubAll();
		model.addAttribute("clubList", list);
		return "admin/clubMgt";
		}


	//클럽목록으로 이동
	@GetMapping("/clublist")
	public String clubListPage(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model) {

		String memberId = "";
		if(principalDetails!=null) {
			memberId = principalDetails.getUsername();
		}
		else {
			memberId = "null";
		}
			
		Map<String, Object> map = clubService.clubListPage(memberId);
	
		
		
		model.addAttribute("result", map);
		return "user/club/clublist";
	}
	
	//클럽리스트 페이징
	@GetMapping("clubPaging")
	@ResponseBody
	public Map<String, Object> clubPaging(ClubVO vo) {
		Map<String, Object> map = clubService.clubPaging(vo);
		
		return map;

	}
	
	//클럽페이징을 위한 카운트
	@GetMapping("clubCnt")
	@ResponseBody
	public Map<String, Object> clubCnt(ClubVO vo) {
		Map<String, Object> map = new HashMap<>();
		System.out.println("검색어 : "+vo);
		int cnt = clubService.cntData(vo);
		
		map.put("total", cnt);
		return map;
	}
	
	
	//클럽상세보기 페이지 이동
	@GetMapping("/member/club/clubPage")
	public String clubPage(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model,HttpSession session, ClubVO vo) {

		//회원이 가입한 클럽인지 체크한 후 세션에 저장
		ClubMemberVO cmvo = new ClubMemberVO();
		cmvo.setClubId(vo.getClubId());
		cmvo.setMemberId(principalDetails.getUsername());
		int ck = clubMemberService.checkMyClub(cmvo);
		if(ck == 1) {
			session.setAttribute("check", true);
		}
		else {
			session.setAttribute("check", false);
		}
		
		
		Map<String, Object> map = clubService.getClubPage(vo);
		model.addAttribute("result", map);
		return "user/club/clubPage";
	}
	
	
	@GetMapping("/member/club/clubInsert")
	public String clubInsertForm(Model model) {
		
		model.addAttribute("ClubVO", new ClubVO());
		return "user/club/clubInsert";
	}
	
	@PostMapping("/member/clubInsert")
	@ResponseBody
	public String clubInsert(@AuthenticationPrincipal PrincipalDetails principalDetails, ClubVO vo) throws IllegalStateException, IOException {
		System.out.println("넘어온 객체 : "+vo);
		
		 String fileName = attachmentService.uploadImage(vo.getImg(), "clubProfile");
//		String path = ""
		 vo.setClubProfileImg(fileName);
		 vo.setClubLeader(principalDetails.getUsername());
		 System.out.println("돌아온 파일이름 : " + fileName);
		 
		 if(clubService.insertClub(vo).equals("success")) {
			 System.out.println("성공");
		 }
		 else {
			 System.out.println("실패");
		 }
		 return "redirect:/member/club/clubPage?clubId="+vo.getClubId();
	}
}
