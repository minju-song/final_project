package com.holoyolo.app.boardLike.mapper;

import com.holoyolo.app.board.service.BoardVO;
import com.holoyolo.app.boardLike.service.BoardLikeVO;

public interface BoardLikeMapper {
	
	public BoardLikeVO checkLike(BoardLikeVO vo);
	public int addLike();

	public int cancelLike();
	
	public int boardLikeCount(BoardVO vo);
}
