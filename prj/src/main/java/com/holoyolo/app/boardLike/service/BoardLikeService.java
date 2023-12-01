package com.holoyolo.app.boardLike.service;

import com.holoyolo.app.board.service.BoardVO;

public interface BoardLikeService {
	
	public BoardLikeVO checkLike(BoardLikeVO vo);
	
	public int addLike();
	
	public int cancelLike();
	
	public BoardVO boardLikeCount(BoardVO vo);
	

}
