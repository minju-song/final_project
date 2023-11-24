package com.holoyolo.app.memberFinanceInfo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

@Controller
public class MemberFinanceInfoController {

	@Autowired
	MemberFinanceInfoService memberFinanceInfoService;

	@PostMapping("checkFinance")
	@ResponseBody
	public String checkAmount(Model mo, MemberFinanceInfoVO vo) {
		MemberFinanceInfoVO myFinance = memberFinanceInfoService.selectMemberFinanceInfo(vo);
		mo.addAttribute("myFinance", myFinance);

		return "redirect:myHolopay";
	}

	@RequestMapping(value = "/addFinanceInfo", method = RequestMethod.POST)
	@ResponseBody
	public MemberFinanceInfoVO insertOrUpdateFinanceInfo(@RequestBody MemberFinanceInfoVO vo,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {

		MemberFinanceInfoVO userFinanceVO = new MemberFinanceInfoVO();
		userFinanceVO.setBankname(vo.getBankname());
		userFinanceVO.setMemberId(principalDetails.getUsername());
		userFinanceVO.setAccount(vo.getAccount());
		System.out.println(userFinanceVO);
		vo = memberFinanceInfoService.insertOrUpdateFinanceInfo(userFinanceVO);
		return vo;
	}
}
