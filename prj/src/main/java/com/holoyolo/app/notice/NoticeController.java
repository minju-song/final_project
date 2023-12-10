package com.holoyolo.app.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
			@RequestParam("imageFiles") MultipartFile[] imageFiles,
			@RequestParam("attachmentFiles") MultipartFile[] attachmentFiles) {

		List<AttachmentVO> imgList = attachmentService.uploadFiles(imageFiles, "notice");
		List<AttachmentVO> attachList = attachmentService.uploadFiles(attachmentFiles, "noticeAttach");
		boardVO.setMemberId(principalDetails.getUsername());
		boardService.insertNotice(boardVO, imgList, attachList);
		return "redirect:/cs/help/notice";
	}

	// 조회

	// 상세보기
	@GetMapping("/cs/help/notice/view")
	public String boardInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, int boardId, Model mo) {
		AttachmentVO attachmentVO = new AttachmentVO();

		String loginId = "not found";
		if (principalDetails != null) {
			loginId = principalDetails.getUsername();
		}
		boardService.addView(boardId);

		BoardVO vo = boardService.selectBoard(boardId);
		attachmentVO.setPostId(boardId);
		attachmentVO.setMenuType("AA6");

		Map<String, List<AttachmentVO>> returnMap = attachmentService.getCSAttachmentList(attachmentVO);
		System.out.println(returnMap);
		mo.addAttribute("menu", "cs");
		mo.addAttribute("boardVO", vo);
		mo.addAttribute("loginId", loginId);
		mo.addAttribute("noticeImg", returnMap.get("imgList"));
		mo.addAttribute("noticeAttach", returnMap.get("attachList"));

		return "user/cs/noticeview";

	}

	@GetMapping("/cs/help/notice/update")
	public String updateView(@AuthenticationPrincipal PrincipalDetails principalDetails, AttachmentVO attachmentVO,
			int boardId, Model mo) {
		String loginId = "not found";
		if (principalDetails != null) {
			loginId = principalDetails.getUsername();
		}

		Map<String, List<AttachmentVO>> returnMap = attachmentService.getCSAttachmentList(attachmentVO);

		BoardVO board = boardService.selectBoard(boardId);
		mo.addAttribute("menu", "cs");
		mo.addAttribute("board", board);
		mo.addAttribute("noticeImg", returnMap.get("imgList"));
		mo.addAttribute("noticeAttach", returnMap.get("attachList"));

		return "user/cs/noticeUpdate";
	}

}
