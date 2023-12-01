package com.holoyolo.app.board.service;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryVO;

public interface BoardService {

	public List<BoardVO> BoardList(String req);

	public BoardVO selectBoard(int boardId);

	public int insertBoard(BoardVO vo);

	public int updateBoard(BoardVO vo);

	public int deleteBoard(BoardVO vo);

	// 페이징된 내역 조회
	public List<BoardVO> searchBoardPaged(JSONObject req);

	// 전체 레코드 수 조회
	public int getTotalBoardRecords(JSONObject req);
	//조회수 증가
	
	public int addView(int vo);
	
}
