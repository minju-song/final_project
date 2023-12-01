package com.holoyolo.app.accBookHistory.web;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.accBookHistory.service.AccBookHistoryService;
import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;
import com.holoyolo.app.accBudget.service.AccBudgetService;
import com.holoyolo.app.accBudget.service.AccBudgetVO;
import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AccBookHistoryController {
	
	private final ApiCall apiCall;
	
	@Autowired 
	AccBookHistoryService accBookHistoryService;
	
	@Autowired 
	MemberFinanceInfoService memberFinanceInfoService;
	
	@Autowired 
	MemberService memberService;
	
	@Autowired
	AccBudgetService accBudgetService;
	
	//가계부 첫 화면
	@GetMapping("/member/accBook")
	public String accBookPage(@AuthenticationPrincipal PrincipalDetails principalDetails,
			                  Model model) {
		//회원의 카드번호 담을 맵
		Map<String, String> map = new HashMap<>();
		//회원의 현재 예산정보 담을 맵
		Map<String, Object> budgetMap = new HashMap<>();
		
		//회원아이디
		String id = principalDetails.getUsername();
		
		//회원카드정보 받아옴
		map = memberFinanceInfoService.getCardInfo(id);

		//만약 카드 등록이 되어있다면 api를 호출, 등록이 되어있지않다면 호출안함
		if(!map.get("카드번호").equals("null")) {			
			apiCall.getPosts(id);		
		}
		
		//가입날짜 가져옴 (필요없으면 지울 예정)
		Date joinDate = memberService.selectJoinDate(id);
		
		//예산정보 맵에 담음
		budgetMap = accBudgetService.getBudgetNow(id);
		
		//예산단위 한글로 바꿔줌
		String budgetUnit = "";
		//예산단위
		if (budgetMap.get("예산단위").equals("YA1")) budgetUnit = "일";
		else if (budgetMap.get("예산단위").equals("YA2")) budgetUnit = "주";
		else if (budgetMap.get("예산단위").equals("YA3")) budgetUnit = "월";
		else budgetUnit = "잘못 입력";
		
		
		//예산금액
		model.addAttribute("accBudgetPrice", budgetMap.get("예산금액"));
		//예산기간
		model.addAttribute("accBudgetUnit", budgetUnit);
		//카드번호
		model.addAttribute("cardNo", map.get("카드번호"));
		//카드회사
		model.addAttribute("cardCompany", map.get("카드회사"));
		//가입날짜
		model.addAttribute("joinDate", joinDate);
		//카드 등록 또는 변경 시 담아올 빈 객체
		model.addAttribute("MemberFinanceInfoVO", new MemberFinanceInfoVO());
		//예산 등록 또는 변경 시 담아올 빈 객체
		model.addAttribute("AccBudgetVO", new AccBudgetVO());
		
		return "user/accBook/accBook";
	}
	
	//해당 날짜 거래내역 가져옴
	@GetMapping("getAb")
	@ResponseBody
	public List<AccBookHistoryVO> getAb(@AuthenticationPrincipal PrincipalDetails principalDetails, 
			                            AccBookHistoryVO vo) {
		//아이디세팅
		vo.setMemberId(principalDetails.getUsername());
		
		//해당 날짜, 회원의 그 날 거래내역 리스트로 받아서 돌려줌
		List<AccBookHistoryVO> list = accBookHistoryService.getAccHistory(vo);

		return list;
	}
	
	//해당 일 총 소비금액
	@GetMapping("getSumPrice")
	@ResponseBody
	public int getSumPrice(@AuthenticationPrincipal PrincipalDetails principalDetails,AccBookHistoryVO vo) {
		vo.setMemberId(principalDetails.getUsername());
		int price = accBookHistoryService.getSumPrice(vo);
		
		return price;
	}
	
	//해당 월 총 소비금액
	@GetMapping("getMonthPrice")
	@ResponseBody
	public int getMonthPrice(@AuthenticationPrincipal PrincipalDetails principalDetails,AccBookHistoryVO vo) {
		vo.setMemberId(principalDetails.getUsername());
		int price = accBookHistoryService.getMonthPrice(vo);
		
		return price;
	}
	
	//거래내역 수기입력
	@PostMapping("insertHistory")
	@ResponseBody
	public int insertAcc(@AuthenticationPrincipal PrincipalDetails principalDetails,AccBookHistoryVO vo) {
		vo.setMemberId(principalDetails.getUsername());
		
		//수기입력은 오늘만 가능
		vo.setPayDate(LocalDate.now());
		
		//해당 거래내역의 아이디로 리턴받고 돌려줌
		int ck = accBookHistoryService.insertAcc(vo);
		if(ck == -1) {
			System.out.println("입력안됨");
		}
		return ck;		
	}
	
	//거래내역삭제
	@GetMapping("delHistory")
	@ResponseBody
	public Map<String, Object> delHistory(AccBookHistoryVO vo) {

		//결과 담을 맵
		Map<String, Object> map = new HashMap<>();
		
		if (accBookHistoryService.deleteHistory(vo) > 0) {
			map.put("result", "success");
		}
		else {
			map.put("result", "fail");
		}
		
		return map;
		
	}
}
