package com.holoyolo.app.member.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

@Controller
public class AdminMemberController {
	
	@Autowired
	MemberService memberService;
	
	@GetMapping("/admin/member")
	public String selectMemberList(Model model) {
		return "/admin/memberMgt";
	}
	
	@GetMapping("/admin/member/list")
	@ResponseBody
	public Map<String, Object> getMemberMapAjax(MemberVO memberVO){
		Map<String, Object> memberMap = new HashMap<>();
		memberMap.put("role", memberVO.getRole());
		memberMap.put("list", memberService.selectMemberAll(memberVO));
		memberMap.put("count", memberService.selectMemberCount(memberVO));
		return memberMap;
	}
		
}
