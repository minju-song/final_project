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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
		
		
		Map<String, Object> map = clubService.getClubPage(vo);
		model.addAttribute("result", map);
		model.addAttribute("userId", principalDetails.getUsername());
		model.addAttribute("ClubBudgetVO", new ClubBudgetVO());
		model.addAttribute("menu", "club");
		model.addAttribute("subMenu", "main");
		return "user/club/clubPage";
	}
	
	
	//클럽생성 페이지이동
	@GetMapping("/member/club/clubInsert")
	public String clubInsertForm(Model model) {
		
		model.addAttribute("ClubVO", new ClubVO());
		return "user/club/clubInsert";
	}
	
	//클럽생성
	@PostMapping("/member/clubInsert")
	public String clubInsert(@AuthenticationPrincipal PrincipalDetails principalDetails, ClubVO vo) throws IllegalStateException, IOException {			
		//프로필사진 업로드 이후 파일명 받아옴
		String fileName = attachmentService.uploadImage(vo.getImg(), "clubProfile");
		System.out.println(fileName);
		//파일명과 리더아이디 설정
		if(fileName == null) {
			vo.setClubProfileImg("club/profile/notking.png");
		}
		else {			
			vo.setClubProfileImg(fileName);
		}
		
		
		 vo.setClubLeader(principalDetails.getUsername());
		 
		 if(clubService.insertClub(vo).equals("success")) {
			 System.out.println("성공");
		 }
		 else {
			 System.out.println("실패");
		 }
		 return "redirect:/member/club/clubPage?clubId="+vo.getClubId();
	}
	
	//클럽 기본정보 수정
	@PostMapping("/member/clubUpdate")
	public String clubUpdate(ClubVO vo) {
		System.out.println("수정 : " + vo);
		
		 if(clubService.updateClubInfo(vo) > 0) {
			 System.out.println("성공");
		 }
		 else {
			 System.out.println("실패");
		 }
		 return "redirect:/member/club/clubPage?clubId="+vo.getClubId();
	}
	
	//클럽이미지수정
	@PostMapping("/member/clubUpdateImg")
	@ResponseBody
	public Map<String, Object> clubInsert(ClubVO vo) throws IllegalStateException, IOException {		
		System.out.println(vo);
		
		Map<String, Object> map = new HashMap<>();
		String fileName = attachmentService.uploadImage(vo.getImg(), "clubProfile");
		
		vo.setClubProfileImg(fileName);
		
		if(clubService.updateClubProfile(vo) > 0) {
			map.put("result", "success");
		}
		else map.put("result", "fail");

		return map;
	}
	
	//클럽 수정 페이지 이동
	@GetMapping("/member/club/clubUpdate")
	public String clubUpdate(Model model, ClubVO paramVO) {
		System.out.println("들어온 값 : "+paramVO);
		ClubVO vo = new ClubVO();
		vo = clubService.getClub(paramVO.getClubId());
		
		model.addAttribute("club", vo);
		return "user/club/clubUpdate";
	}
	
	//클럽장 위임
	@GetMapping("/mandate")
	public String mandate(Model model,ClubVO vo) {
		System.out.println("들어온 클럽 : "+vo);
		clubService.mandateKing(vo);
		
		model.addAttribute("clubname",vo.getClubName());
		return "user/club/mandateComplete";
	}
	
	//클럽삭제
	@GetMapping("/member/clubDelete")
	@ResponseBody
	public String clubDelete(ClubVO vo) {
		System.out.println("삭제 : " + vo);
		
		if(clubService.delectClub(vo) > 0) {
			return "success";
		}
		else {
			return "fail";
		}
		
	}
	

	// 관리자 클럽 리스트
	// 페이지 이동
	@GetMapping("/admin/club")
	public String selectClubList() {
		return "admin/club/clubPage";
		}
	// 처리
	@GetMapping("/admin/club/list")
	@ResponseBody
	public Map<String, Object> getClubListAjax(ClubVO clubVO) {
		Map<String, Object> clubMap = new HashMap<>();
		System.out.println(clubService.selectClubAll(clubVO));
		clubMap.put("list", clubService.selectClubAll(clubVO));
		
		return clubMap;
	}
	
	@GetMapping("/admin/club/detail")
	public String selectClubDetail(ClubVO clubVO, Model model) {
//		Map<String, Object> clubInfo = clubService.getClubDetail(clubVO);
//		model.addAttribute("clubInfo", clubInfo.get("clubInfo"));
		return "admin/club/clubDetail";
	}
		

}
