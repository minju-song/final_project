package com.holoyolo.app.holopayHistory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

@Controller
public class HoloPayController {

	@Autowired
	MemberFinanceInfoService memberFinanceInfoService;
	
	@Autowired
	MemberService memberService;

	@GetMapping("/member/myHolopay")
	public String holopaypage(@AuthenticationPrincipal PrincipalDetails principalDetails, Model mo) {
		String memberId = principalDetails.getUsername();
		MemberVO memberVO = memberService.selectUser(memberId);
		MemberFinanceInfoVO vo = new MemberFinanceInfoVO();
		vo.setMemberId(memberId);

		MemberFinanceInfoVO memberFinanceInfoVO = memberFinanceInfoService.selectMemberFinanceInfo(vo);
		if (memberFinanceInfoVO == null) {
			mo.addAttribute("amount", 0);
		} else {
			mo.addAttribute("amount", memberFinanceInfoVO.getAccount());
		}
		mo.addAttribute("memberInfo", memberVO);
		mo.addAttribute("subMenu", "memberInfo");
		return "user/mypage/myholopay";
	}

}
