package com.holoyolo.app.trade.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@GetMapping("/tradeList")
	public String tradeList(Model model) {
		Map<String, Object> map = tradeService.tradeListPage();
		model.addAttribute("tradeList", map);
		System.out.println(map.get(0));
		return "user/trade/tradeList";
	}
	
	@GetMapping("tradePaging")
	@ResponseBody
	public Map<String, Object> tradePaging(TradeVO tradeVO){
		Map<String, Object> map = tradeService.tradePaging(tradeVO);
		return map;
	}
	
	@GetMapping("tradeCnt")
	@ResponseBody
	public Map<String, Object> tradeCnt(TradeVO tradeVO){ 
		Map<String, Object> map = new HashMap<>();
		System.out.println("검색어: " + tradeVO);
		int cnt = tradeService.cntData(tradeVO);
		
		map.put("total", cnt);
		return map;
	}
}
