package com.holoyolo.app;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.board.service.BoardService;
import com.holoyolo.app.board.service.BoardVO;
import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.trade.service.TradeService;
import com.holoyolo.app.trade.service.TradeVO;

@Controller
public class HomeController {
	
	@Autowired
	TradeService tradeService;
	
	@Autowired
	ClubService clubService;
	
	@Autowired
	BoardService boardService;
	

	@GetMapping("/")
	public String home(Model model) {
		
		// 중고거래 최신글 조회
		TradeVO tvo = new TradeVO();
		tvo.setPage(1);
		Map<String, Object> map = tradeService.getTradeList(tvo);
		List<TradeVO> tradeList = (List<TradeVO>) map.get("list");
		model.addAttribute("tradeList", tradeList);
		
		// 알뜰모임 조회
		ClubVO cvo = new ClubVO();
		cvo.setPage(10);
		List<ClubVO> clubList = clubService.bestClubList(cvo);
		model.addAttribute("clubList", clubList);
		
		// 커뮤니티(정보공유) 조회
		BoardVO bvo = new BoardVO();
		bvo.setMenuType("AA2");
		bvo.setPage(5);
		List<BoardVO> infoBoardList = boardService.recentBoradList(bvo);
		model.addAttribute("infoBoard", infoBoardList);
		
		// 커뮤니티(수다방) 조회
		bvo.setMenuType("AA3");
		List<BoardVO> sudaBoardList = boardService.recentBoradList(bvo);
		model.addAttribute("sudaBoard", sudaBoardList);
		
		return "index";
	}
}
