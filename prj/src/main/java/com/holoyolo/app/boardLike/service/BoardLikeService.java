package com.holoyolo.app.boardLike.service;

import com.holoyolo.app.board.service.BoardVO;

public interface BoardLikeService {

	public String checkLike(BoardLikeVO vo);

	public BoardVO boardLikeCount(BoardVO vo);

	public int addLike(BoardLikeVO vo);

	public int cancelLike(BoardLikeVO vo);

}
