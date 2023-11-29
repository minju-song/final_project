package com.holoyolo.app.memberFinanceInfo.web;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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



import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.memberFinanceInfo.mapper.MemberFinanceInfoMapper;

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

	
	@PostMapping("member/cardInsert")
	public String cardInsertProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, MemberFinanceInfoVO vo) {
		vo.setMemberId(principalDetails.getUsername());
		int ck = memberFinanceInfoService.insertCard(vo);
		if(ck < 1) {
			System.out.println("입력안됨");
		}
		return "redirect:/member/accBook";
	}
	
	@PostMapping("member/cardUpdate")
	public String cardUpdateProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, MemberFinanceInfoVO vo) {
		vo.setMemberId(principalDetails.getUsername());
		int ck = memberFinanceInfoService.updateCard(vo);
		if(ck < 1) {
			System.out.println("입력안됨");
		}
		return "redirect:/member/accBook";
	}
	
	@PostMapping("cardDelete")
	@ResponseBody
	public Map<String, Object> cardDelete(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody String data) throws ParseException {
		System.out.println("실행완료");
		System.out.println(data);
		Map<String, Object> map = new HashMap<>();
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(data);
		System.out.println(jsonObject.get("cardNo"));
		
		
		MemberFinanceInfoVO vo = new MemberFinanceInfoVO();
		vo.setMemberId(principalDetails.getUsername());
		
		if(memberFinanceInfoService.delcard(vo)>0) {
			map.put("result", "success");
		}
		else {
			map.put("result", "fail");
		}
		
		
		return map;
	}
	
	

}
