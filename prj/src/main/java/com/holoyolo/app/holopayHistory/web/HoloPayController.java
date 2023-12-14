package com.holoyolo.app.holopayHistory.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
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
	HoloPayHistoryService holopayHistoryService;

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

		if (financeVO == null) {
			mo.addAttribute("financeVO", null);
		} else {
			mo.addAttribute("financeVO", financeVO);
			// 홀로페이
			
			int holoBalance = holoPayHistoryService.holopayBalance(memberVO);
			mo.addAttribute("payBalance", holoBalance);
		}
		// 홀로페이 내역
		HoloPayHistoryVO holoPayHistoryVO = new HoloPayHistoryVO();
		holoPayHistoryVO.setMemberId(memberId);
		List<HoloPayHistoryVO> history = holoPayHistoryService.holopayHistoryList(holoPayHistoryVO);
		if (history.size() != 0) {
			mo.addAttribute("holopayList", history);
		} else {
			mo.addAttribute("holopayList", "0");
		}

		mo.addAttribute("memberInfo", memberVO);
		mo.addAttribute("menu", "mypage");

		return "user/mypage/myholopay";
	}

	@RequestMapping(value = "/apireq", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> apireq(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestBody JSONObject req, Model mo) {

		String requestTram = (String) req.get("Tram");

		HolopayReqVO apiRequest = new HolopayReqVO(requestTram);
		HoloPayHistoryVO holoPayHistoryVO = new HoloPayHistoryVO();
		// api 호출
		JSONObject apiResult = holopayRechargeApi.getPosts(apiRequest);

		JSONObject header = (JSONObject) apiResult.get("Header");
		String resultStatus = (String) header.get("Rpcd");
		String resultMsg = "";
		System.out.println("==============" + resultStatus + "==============");
		Map<String, Object> returnData = new HashMap<>();

		if (resultStatus.equals("00133")) {
			resultMsg = "계좌의 잔액이 부족합니다.";
			returnData.put("resultMsg", resultMsg);
			returnData.put("resultCode", 5);
		} else if (resultStatus.equals("00000")) {
			try {
				holoPayHistoryVO.setMemberId(principalDetails.getUsername());
				holoPayHistoryVO.setPrice(Integer.parseInt((String) req.get("Tram")));
				String RgsnYmd = ((String) apiResult.get("RgsnYmd")).substring(2);
				holoPayHistoryVO.setHpType("HP1");
				SimpleDateFormat dateformat = new SimpleDateFormat("yyMMdd");
				Date formatset;
				formatset = dateformat.parse(RgsnYmd);
				holoPayHistoryVO.setHpDate(formatset);
				holoPayHistoryService.insertHolopayHistory(holoPayHistoryVO);
				System.out.println(holoPayHistoryVO);
				int checkResultType = holoPayHistoryVO.getAddPayresultType();
				if (checkResultType == 1) {
					resultMsg = (String) req.get("Tram") + "원 충전되었습니다.";
					returnData.put("resultCode", checkResultType);
				} else if (checkResultType == 4) {
					resultMsg = "홀로페이는 1,000,000원 이상 충전할 수 없습니다.";
					returnData.put("resultCode", checkResultType);
				}

			} catch (ParseException e) {
				System.out.println("123");
				e.printStackTrace();
				return null;
			}
		} else {
			resultMsg = "오류가 발생했습니다. 다시 시도해주세요.//";
		}

		returnData.put("resultMsg", resultMsg);
		System.out.println(returnData);
		return returnData;
	}

	@RequestMapping(value = "/withdrawapireq", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> takeOutApi(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestBody JSONObject req, Model mo) {
		// 요청 객체 생성
		MemberFinanceInfoVO memberFinanceInfoVO = new MemberFinanceInfoVO();
		memberFinanceInfoVO.setMemberId((String) principalDetails.getUsername());
		memberFinanceInfoVO = memberFinanceInfoService.selectMemberFinanceInfo(memberFinanceInfoVO);
//지정된 계좌번호가 아니면 강제로 변환 추후 삭제
		if (memberFinanceInfoVO.getAccount() != "3020000009570") {
			memberFinanceInfoVO.setAccount("3020000009570");
		}
		HolopayWithdrawalApiVO apiRequest = new HolopayWithdrawalApiVO((String) req.get("Tram"),
				memberFinanceInfoVO.getAccount());

		// api 호출
		JSONObject apiResult = holopayWithdrawApi.getPosts(apiRequest);
		JSONObject header = (JSONObject) apiResult.get("Header");
		String resultStatus = (String) header.get("Rpcd");

		// 응답 후 데이터 반환
		String resultMsg = "";

		HoloPayHistoryVO holoPayHistoryVO = new HoloPayHistoryVO();
		Map<String, Object> returnData = new HashMap<String, Object>();

		System.out.println("==============" + apiResult + "==============");
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

				int checkResultType = holoPayHistoryVO.getAddPayresultType();
				System.out.println(checkResultType);

				if (checkResultType == 2) {
					resultMsg = (String) req.get("Tram") + "원 인출되었습니다.";
					returnData.put("resultMsg", resultMsg);
					returnData.put("resultCode", checkResultType);
				} else if (checkResultType == 3) {
					resultMsg = "홀로페이 잔액이 부족합니다.";
					returnData.put("resultMsg", resultMsg);
					returnData.put("resultCode", checkResultType);
				}

			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}

		} else {
			resultMsg = "오류가 발생했습니다. 다시 시도해주세요.//";
			returnData.put("resultMsg", resultMsg);

		}
		return returnData;
	}

	@RequestMapping(value = "/loadhistory", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> historySearch(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@RequestBody JSONObject req, Model mo) {
		String term = (String) req.get("term");
		int start = (int) req.get("start");
		int end = (int) req.get("end");

		HoloPayHistoryVO vo = new HoloPayHistoryVO();
		vo.setMemberId(principalDetails.getUsername());

		List<HoloPayHistoryVO> resultHistoryList = holoPayHistoryService.searchPayPaged(term, vo, start, end);
		int totalRecords = holoPayHistoryService.getTotalRecords(term, vo);

		Map<String, Object> result = new HashMap<>();
		result.put("historyList", resultHistoryList);
		result.put("totalRecords", totalRecords);
		return result;

	}

}
