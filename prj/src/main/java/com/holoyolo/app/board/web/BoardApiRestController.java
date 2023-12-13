package com.holoyolo.app.board.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.holoyolo.app.board.service.BoardService;
import com.holoyolo.app.board.service.BoardVO;
import com.holoyolo.app.editor.PostVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardApiRestController {

	@Autowired
	BoardService boardService;

	@GetMapping("/api/board/{boardId}")
	public BoardVO selectPostInfo(@PathVariable final int boardId) {
		
		boardService.addView(boardId);
		return boardService.selectBoard(boardId);
	}

	@PostMapping("/api/Board/posts")
	public int insertPost(@RequestBody final BoardVO params) {

		return boardService.insertBoard(params);
	}
}