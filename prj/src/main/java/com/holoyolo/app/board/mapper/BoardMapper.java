package com.holoyolo.app.board.mapper;

import java.util.List;

import com.holoyolo.app.board.service.BoardVO;

public interface BoardMapper {
	public List<BoardVO> BoardList();

	public BoardVO selectBoard(BoardVO vo);

	public int insertBoard(BoardVO vo);

	public int updateBoard(BoardVO vo);

	public int deleteBoard(BoardVO vo);
}
