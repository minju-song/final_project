package com.holoyolo.app.boardLike.service;

public interface BoardLikeService {

	public String checkLike(BoardLikeVO vo);

	public int boardLikeCount(int board_id);

	public int addLike(BoardLikeVO vo);

	public int cancelLike(BoardLikeVO vo);

}
