package com.holoyolo.app.board.mapper;

import java.util.List;

import com.holoyolo.app.board.service.BoardVO;

public interface BoardMapper {
	public List<BoardVO> BoardList(BoardVO vo);

	public BoardVO selectBoard(int boardId);

	public int insertBoard(BoardVO vo);

	public int updateBoard(BoardVO vo);

	public int deleteBoard(BoardVO vo);

	public int getTotalBoardRecords(BoardVO vo);

	public List<BoardVO> searchBoardPaged(int start, int end);

	public int addView(BoardVO vo);
	
	public List<BoardVO> recentBoradList(BoardVO vo); //메인용 리스트
}
