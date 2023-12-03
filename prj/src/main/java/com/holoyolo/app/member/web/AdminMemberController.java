package com.holoyolo.app.member.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

public class AdminMemberController {
	
	@Autowired
	MemberService memberService;
	
	@GetMapping("/member")
	public String selectMemberList(Model model) {
		List<MemberVO> list = memberService.selectMemberAll();
		model.addAttribute("memberList", list);
		return "admin/memberMgt";
	}
}
