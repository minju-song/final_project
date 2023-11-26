package com.holoyolo.app.holopayHistory.web;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import com.holoyolo.app.holopayHistory.service.HolopayReqVO;
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
		MemberFinanceInfoVO financeVO = new MemberFinanceInfoVO();
		financeVO.setMemberId(memberId);
		financeVO = memberFinanceInfoService.selectMemberFinanceInfo(financeVO);
		if (financeVO.getUseYn() == null) {
			mo.addAttribute("amount", '0');
		} else {
			mo.addAttribute("financeVO", financeVO);
		}
		mo.addAttribute("memberInfo", memberVO);
		mo.addAttribute("subMenu", "memberInfo");
		return "user/mypage/myholopay";
	}

	@RequestMapping(value = "/apireq", method = RequestMethod.POST)
	@ResponseBody
	public void apireq(@RequestBody JSONObject tram) {
		
	
		
		
		
	
		
		
//		HolopayReqVO apiRequest = new HolopayReqVO(holoreq);
//		
//		System.out.println(apiRequest);
		
//		int reqPrice = Integer.parseInt(reqVO.getTram());
//		//ajax실행 > 충전금액 요청
//		HolopayReqVO reqInfo = new HolopayReqVO(reqPrice);

		// 회원id 기반으로 금융정보 로드//고정

		// api 요청

		// 응답 후 데이터 반환

		return;
	}

}
