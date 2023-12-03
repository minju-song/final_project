package com.holoyolo.app.boardLike.mapper;

import com.holoyolo.app.board.service.BoardVO;
import com.holoyolo.app.boardLike.service.BoardLikeVO;

public interface BoardLikeMapper {
	
	public int checkLike(BoardLikeVO vo);
	
	public int addLike(BoardLikeVO vo);

	public int cancelLike(BoardLikeVO vo);
	
	public int boardLikeCount(BoardVO vo);
}
