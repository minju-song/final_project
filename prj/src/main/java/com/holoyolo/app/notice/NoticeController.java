package com.holoyolo.app.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.holoyolo.app.attachment.service.AttachmentService;
import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.board.service.BoardService;
import com.holoyolo.app.board.service.BoardVO;
import com.holoyolo.app.heart.service.HeartVO;
import com.holoyolo.app.trade.service.TradeVO;

@Controller
public class NoticeController {

	@Autowired
	BoardService boardService;

	@Autowired
	AttachmentService attachmentService;

	// 공지사항 리스트 페이지
	@GetMapping("/cs/help/notice")
	public String noticeList(Model mo) {
		mo.addAttribute("boardType", "공지사항");
		mo.addAttribute("menu", "cs");
		return "user/cs/noticeList";
	}

//공지사항 등록 페이지 
	@GetMapping("/cs/help/notice/Insert")
	public String insertNoticePage(Model mo, BoardVO boardVO) {
		mo.addAttribute("boardType", "공지사항");
		mo.addAttribute("menu", "cs");
		return "user/cs/noticeinsert";
	}

	// 등록
	@PostMapping("/notice/insert")
	public String tradeInsertProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, BoardVO boardVO,
			@RequestPart MultipartFile[] uploadFiles) {
		List<AttachmentVO> imgList = attachmentService.uploadFiles(uploadFiles, "notice");
		boardVO.setMemberId(principalDetails.getUsername());
		boardService.insertNotice(boardVO, imgList);
		return "redirect:/cs/help/notice";
	}

	// 조회

	// 상세보기
	@GetMapping("/cs/help/notice/view")
	public String boardInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, int boardId, Model mo,
			AttachmentVO attachmentVO) {

		String loginId = "not found";
		if (principalDetails != null) {
			loginId = principalDetails.getUsername();
		}
		BoardVO vo = boardService.selectBoard(boardId);
		attachmentVO.setPostId(boardId);
		attachmentVO.setMenuType("AA6");
		mo.addAttribute("menu", "cs");
		mo.addAttribute("boardVO", vo);
		mo.addAttribute("loginId", loginId);
		mo.addAttribute("noticeImg", attachmentService.getAttachmentList(attachmentVO));
		System.out.println(mo);
		return "user/cs/noticeview";

	}

}
