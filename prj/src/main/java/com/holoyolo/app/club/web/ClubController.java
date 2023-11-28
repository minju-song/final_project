package com.holoyolo.app.club.web;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ResponseBody;


import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubMember.service.ClubMemberService;
import com.holoyolo.app.clubMember.service.ClubMemberVO;

@Controller
public class ClubController {
	
	@Autowired
	ClubService clubService;
	
	@Autowired
	ClubMemberService clubMemberService;
	

	@GetMapping("/admin/club")
	public String selectClubList(Model model) {
		List<ClubVO> list = clubService.selectClubAll();
		model.addAttribute("clubList", list);
		return "admin/clubMgt";
	}

	@GetMapping("/clublist")
	public String clubListPage(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model) {

		//모든 클럽 리스트
		List<ClubVO> list = clubService.getAllClubList();
//		System.out.println("값 "+principalDetails.getUsername());
		//회원이 가입한 클럽리스트	
		if(principalDetails!=null) {
			List<ClubMemberVO> clubJoinlist = clubMemberService.getClubJoin(principalDetails.getUsername());
			
			model.addAttribute("joinlist", clubJoinlist);
			model.addAttribute("userId", principalDetails.getUsername());
		}
		else {
			model.addAttribute("joinlist", "null");
			model.addAttribute("userId","null");
		}
			
	
		
		
		model.addAttribute("list", list);
		return "user/club/clublist";
	}
	
	@GetMapping("clubPaging")
	@ResponseBody
	public Map<String, Object> clubPaging(ClubVO vo) {
		Map<String, Object> map = new HashMap<>();
		
		List<ClubVO> list = new ArrayList<>();
		list = clubService.getClubList(vo);
		
		for(int i=0; i<list.size(); i++) {
			ClubVO temp = new ClubVO();
			temp = list.get(i);
			temp.setJoinCnt(clubMemberService.countMember(list.get(i).getClubId()));
			list.set(i, temp);
		}
		
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
		
		map.put("length", list.size());
		map.put("result", list);
//		System.out.println(vo);
		return map;

	}
	
	@GetMapping("clubCnt")
	@ResponseBody
	public Map<String, Object> clubCnt(ClubVO vo) {
		Map<String, Object> map = new HashMap<>();
		System.out.println("검색어 : "+vo);
		int cnt = clubService.cntData(vo);
		
		map.put("total", cnt);
		return map;
	}
	
	@GetMapping("/member/club/clubPage")
	public String clubPage(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model, ClubVO vo) {
		
		model.addAttribute("clubId", vo.getClubId());
		return "user/club/clubPage";
	}
}
