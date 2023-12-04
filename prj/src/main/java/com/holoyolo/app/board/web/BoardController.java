package com.holoyolo.app.board.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.board.service.BoardService;
import com.holoyolo.app.board.service.BoardVO;
import com.holoyolo.app.boardLike.service.BoardLikeService;

@Controller
public class BoardController {

	@Autowired
	BoardService boardService;

	@Autowired
	BoardLikeService boardLikeService;

	@GetMapping("/infoBoard")
	public String infomationBoard(Model mo, @AuthenticationPrincipal PrincipalDetails prd) {
		// 회원만 보이는 버튼
		String username = "";
		if (prd != null) {
			username = (String) prd.getUsername();
		}
		// 게시물 로드
		List<BoardVO> history = boardService.BoardList("AA2");
		if (history.size() != 0) {
			mo.addAttribute("boardList", history);
		} else {
			mo.addAttribute("boardList", "0");
		}

		mo.addAttribute("likeCount", 0);
		mo.addAttribute("username", username);
		mo.addAttribute("menu", "community");
		return "user/community/boardList";
	}

//글 등록
	@GetMapping("/member/addInfoBoard")
	public String addBoard(@AuthenticationPrincipal PrincipalDetails prd, Model mo) {
		mo.addAttribute("user", prd.getMemberVO());
		mo.addAttribute("menu", "community");
		mo.addAttribute("boardType", "정보공유게시판");
		return "user/community/insertBoard";
	}

	// 글 등록 처리
	@PostMapping("/insertPostreq")
	@ResponseBody
	public String savePost(@AuthenticationPrincipal PrincipalDetails prd, @RequestBody BoardVO vo) {
		String userId = (String) prd.getUsername();
		vo.setMemberId(userId);
		boardService.insertBoard(vo);
		return "/infoBoard";
	}

//페이지 로드
	@RequestMapping(value = "/boardLoad", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> infoBoardLoad(@RequestBody JSONObject req) {
		List<BoardVO> resultList = boardService.searchBoardPaged(req);
		int totalRecords = boardService.getTotalBoardRecords(req);
		Map<String, Object> result = new HashMap<>();
		result.put("historyList", resultList);
		result.put("totalRecords", totalRecords);
		return result;
	}

//상세보기
	@GetMapping("/member/BoardInfo")
	public String boardInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, int boardId, Model mo) {

		String loginId = "not found";
		if (principalDetails != null) {
			loginId = principalDetails.getUsername();
		}
		BoardVO vo = boardService.selectBoard(boardId);
		mo.addAttribute("menu", "community");
		mo.addAttribute("board", vo);
		mo.addAttribute("loginId", loginId);
		return "/user/community/boardView";

	}

	// 수정
	@GetMapping("/member/updateview")
	public String updateView(@AuthenticationPrincipal PrincipalDetails principalDetails, int boardId, Model model) {
		String loginId = "not found";

		if (principalDetails != null) {
			loginId = principalDetails.getUsername();
		}

		BoardVO board = boardService.selectBoard(boardId);
		model.addAttribute("board", board);
		model.addAttribute("loginId", loginId);
		return "/user/community/postUpdate";
	}
}
