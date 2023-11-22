package com.holoyolo.app.holopayHistory.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

@Controller
public class HoloPayHistoryController {
	@Autowired
	MemberFinanceInfoService MFIS;

	@GetMapping("myHolopay")
	public String holopaypage(Model mo, MemberFinanceInfoVO vo) {

		mo.addAttribute("menu", "mypage");

		MemberFinanceInfoVO MFIV = MFIS.selectMemberFinanceInfo(vo);
		mo.addAttribute("amount", MFIV);
		return "member/mypage/myholopay";
	}
}
