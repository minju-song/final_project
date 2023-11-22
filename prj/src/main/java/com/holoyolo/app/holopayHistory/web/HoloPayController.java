package com.holoyolo.app.holopayHistory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

@Controller
public class HoloPayController {

	@Autowired
	MemberFinanceInfoService memberFinanceInfoService;

	@GetMapping("myHolopay")
	public String holopaypage(Model mo) {

		mo.addAttribute("menu", "mypage");
		MemberFinanceInfoVO vo = new MemberFinanceInfoVO();
		vo.setMemberId("JINU@mail.com");
		MemberFinanceInfoVO MFIV = memberFinanceInfoService.selectMemberFinanceInfo(vo);
		if (MFIV == null) {
			mo.addAttribute("amount", 0);
		} else {
			mo.addAttribute("amount", MFIV.getAccount());
		}
		return "member/mypage/myholopay";
	}

	@GetMapping("/mypageHome")
	public String mypageHome(Model mo) {
		String menu = "mypage";
		mo.addAttribute("menu", menu);
		return "member/mypage/main";
	}
}
