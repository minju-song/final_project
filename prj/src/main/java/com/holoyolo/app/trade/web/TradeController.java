package com.holoyolo.app.trade.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	public String tradeList(TradeVO tradeVO, Model model) {
		List<TradeVO> list = tradeService.tradeList();
		if(list.size() == 0) {
			model.addAttribute("tradeList", "null");
		}else {
			model.addAttribute("tradeList", list);
		}
		return "user/trade/tradeList";
	}
}
