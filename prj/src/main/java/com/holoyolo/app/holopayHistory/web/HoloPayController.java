package com.holoyolo.app.holopayHistory.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryService;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryVO;
import com.holoyolo.app.holopayHistory.service.api.HolopayReqVO;
import com.holoyolo.app.holopayHistory.service.api.HolopayWithdrawalApiVO;
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

	@Autowired
	HolopayRechargeApi holopayRechargeApi;
	@Autowired
	HolopayWithdrawApi holopayWithdrawApi;

	@Autowired
	HoloPayHistoryService holoPayHistoryService;

	@GetMapping("/member/myHolopay")
	public String holopaypage(@AuthenticationPrincipal PrincipalDetails principalDetails, Model mo) {
		// 유저정보
		String memberId = principalDetails.getUsername();
		MemberVO memberVO = memberService.selectUser(memberId);
		// 계좌정보
		MemberFinanceInfoVO financeVO = new MemberFinanceInfoVO();
		financeVO.setMemberId(memberId);
		financeVO = memberFinanceInfoService.selectMemberFinanceInfo(financeVO);
		if (financeVO.getUseYn() == null) {
			mo.addAttribute("amount", '0');
		} else {
			mo.addAttribute("financeVO", financeVO);
			// 홀로페이
			int holoBalance = holoPayHistoryService.holopayBalance(memberVO);
			mo.addAttribute("payBalance", holoBalance);
		}

		mo.addAttribute("memberInfo", memberVO);
		mo.addAttribute("subMenu", "memberInfo");
		return "user/mypage/myholopay";
	}

	@RequestMapping(value = "/apireq", method = RequestMethod.POST)
	@ResponseBody
	public void apireq(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody JSONObject req,
			Model mo) {

		String requestTram = (String) req.get("Tram");

		HolopayReqVO apiRequest = new HolopayReqVO(requestTram);
		HoloPayHistoryVO holoPayHistoryVO = new HoloPayHistoryVO();
		// api 호출
		JSONObject apiResult = holopayRechargeApi.getPosts(apiRequest);

		JSONObject header = (JSONObject) apiResult.get("Header");
		String resultStatus = (String) header.get("Rpcd");
		String resultMsg = "";

		if ("00133".equals(resultStatus)) {
			resultMsg = "잔액이 부족합니다.";
		}
		if ("00000".equals(resultStatus)) {
			try {
				holoPayHistoryVO.setMemberId(principalDetails.getUsername());
				holoPayHistoryVO.setPrice(Integer.parseInt((String) req.get("Tram")));
				String RgsnYmd = ((String) apiResult.get("RgsnYmd")).substring(2);
				holoPayHistoryVO.setHpType("HP1");
				System.out.println(RgsnYmd);
				SimpleDateFormat dateformat = new SimpleDateFormat("yyMMdd");
				Date formatset;
				formatset = dateformat.parse(RgsnYmd);
				holoPayHistoryVO.setHpDate(formatset);
				holoPayHistoryService.insertHolopayHistory(holoPayHistoryVO);
				resultMsg = (String) req.get("Tram") + "원 충전되었습니다.";
				mo.addAttribute("resultMsg", resultMsg);
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}

	}

	@RequestMapping(value = "/withdrawapireq", method = RequestMethod.POST)
	@ResponseBody
	public void takeOutApi(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody JSONObject req,
			Model mo) {
		// 요청 객체 생성
		MemberFinanceInfoVO memberFinanceInfoVO = new MemberFinanceInfoVO();
		memberFinanceInfoVO.setMemberId((String) principalDetails.getUsername());
		memberFinanceInfoVO = memberFinanceInfoService.selectMemberFinanceInfo(memberFinanceInfoVO);
		HolopayWithdrawalApiVO apiRequest = new HolopayWithdrawalApiVO((String) req.get("Tram"),
				memberFinanceInfoVO.getAccount());

		// api 호출
		JSONObject apiResult = holopayWithdrawApi.getPosts(apiRequest);
		JSONObject header = (JSONObject) apiResult.get("Header");
		String resultStatus = (String) header.get("Rpcd");

		// 응답 후 데이터 반환
		String resultMsg = "";

		HoloPayHistoryVO holoPayHistoryVO = new HoloPayHistoryVO();
		if ("00000".equals(resultStatus)) {

			try {
				Date formatset;
				holoPayHistoryVO.setMemberId((String) principalDetails.getUsername());
				holoPayHistoryVO.setPrice(Integer.parseInt((String) req.get("Tram")));
				holoPayHistoryVO.setHpType("HP2");
				String Tsymd = ((String) header.get("Tsymd")).substring(2);
				SimpleDateFormat dateformat = new SimpleDateFormat("yyMMdd");
				formatset = dateformat.parse(Tsymd);
				holoPayHistoryVO.setHpDate(formatset);
				holoPayHistoryService.insertHolopayHistory(holoPayHistoryVO);
				resultMsg = (String) req.get("Tram") + "원 인출되었습니다.";
				mo.addAttribute("resultMsg", resultMsg);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		} else {
			resultMsg = "오류가 발생했습니다. 다시 시도해주세요.";
			mo.addAttribute("resultMsg", resultMsg);
		}
	}
}
