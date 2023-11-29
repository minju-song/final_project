package com.holoyolo.app.board.service;

import java.util.List;

import org.json.simple.JSONObject;

public interface BoardService {
	
	
	public List<BoardVO> BoardList();
	
	public BoardVO selectBoard(BoardVO vo);
	
	public int insertBoard(JSONObject json, String userId);
	
	public int updateBoard(BoardVO vo);
	
	public int deleteBoard(BoardVO vo);
	
}
