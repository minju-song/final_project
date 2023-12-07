package com.holoyolo.app.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.board.service.BoardService;

@Controller
public class NoticeController {

	@Autowired
	BoardService boardService;

	// 공지사항 리스트 페이지
	@GetMapping("/cs/help/notice")
	public String noticeList(Model mo) {
		mo.addAttribute("boardType", "공지사항");
		return "user/cs/noticeList";
	}

}
