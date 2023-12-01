package com.holoyolo.app.trade.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.trade.service.TradeService;
import com.holoyolo.app.trade.service.TradeVO;

@Controller
public class TradeController {
	@Autowired
	TradeService tradeService;

	@GetMapping("/admin/trade")
	public String selectTradeList(Model model) {
		List<TradeVO> list = tradeService.getTradeList();
		model.addAttribute("tradeList", list);
		return "admin/tradeMgt";
	}
	
	//중고거래 페이지 이동
	@GetMapping("/tradeList")
	public String tradeList(Model model) {
		return "user/trade/tradeList";
	}
	
	//중고거래 페이징
	@GetMapping("tradePaging")
	@ResponseBody
	public Map<String, Object> tradePaging(TradeVO tradeVO){
		Map<String, Object> map = tradeService.tradePaging(tradeVO);
		return map;
	}
	
	@GetMapping("member/tradeInsert")
	public String tradeInsert(@AuthenticationPrincipal PrincipalDetails principalDetails,TradeVO tradeVO) {
		tradeVO.setSellerId(principalDetails.getUsername());
		return "user/trade/tradeInsert";
	}
	
	@GetMapping("member/tradeInfo")
	public String tradeInfo(@AuthenticationPrincipal PrincipalDetails principalDetails,TradeVO tradeVO, Model model) {
		System.out.println(tradeVO);
		model.addAttribute("tradeInfo", tradeService.getTrade(tradeVO));
		return "user/trade/tradeInfo";
	}
}
