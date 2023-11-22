package com.holoyolo.app.memberFinanceInfo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.memberFinanceInfo.mapper.MemberFinanceInfoMapper;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

@Controller
public class MemberFinanceInfoController {
	
	@Autowired
	MemberFinanceInfoMapper memberFinanceInfoMapper;

	
	@PostMapping("checkFinance")
	@ResponseBody
	 public String checkAmount(Model mo, MemberFinanceInfoVO vo) {
		MemberFinanceInfoVO myFinance = memberFinanceInfoMapper.selectMemberFinanceInfo(vo);
		mo.addAttribute("myFinance",myFinance);
		
		return "redirect:myHolopay";
	}
}
