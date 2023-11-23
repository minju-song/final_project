package com.holoyolo.app.memberFinanceInfo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.memberFinanceInfo.mapper.MemberFinanceInfoMapper;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

@RestController
public class MemberFinanceInfoRestController {

	@Autowired
	MemberFinanceInfoMapper memberFinanceInfoMapper;

	@PostMapping("/addFinanceInfo")
	public String handleFormData(@RequestBody MemberFinanceInfoVO vo,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {

		System.out.println("은행명:" + vo.getBankname() + "계좌번호 : " + vo.getAccount());

		MemberFinanceInfoVO userFinanceVO = new MemberFinanceInfoVO();
		userFinanceVO.setBankname(vo.getBankname());
		userFinanceVO.setMemberId(vo.getMemberId());
		userFinanceVO.setAccount(vo.getAccount());
		memberFinanceInfoMapper.insertOrUpdateFinanceInfo(userFinanceVO);

		return "redirect:/member/myHolopay";
	}

}
