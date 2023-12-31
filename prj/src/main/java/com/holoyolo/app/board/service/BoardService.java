package com.holoyolo.app.board.service;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.holopayHistory.service.HoloPayHistoryVO;

public interface BoardService {

	List<BoardVO> BoardList(String menuType, String search, String searchType);

	public BoardVO selectBoard(int boardId);

	public int insertBoard(BoardVO vo);

	public int updateBoard(BoardVO vo);

	public int deleteBoard(BoardVO vo);

	// 페이징된 내역 조회
	public List<BoardVO> searchBoardPaged(JSONObject req);

	// 전체 레코드 수 조회
	public int getTotalBoardRecords(JSONObject req);

	// 조회수 증가

	public int addView(int vo);

	BoardVO checkBoard(int boardId);

	List<BoardVO> searchBoardSurfPaged(JSONObject req);

//검색 결과 수 조회
	int getTotalBoardSurfRecords(JSONObject req);

	public List<BoardVO> recentBoradList(BoardVO vo); // 메인용 리스트

	public int insertNotice(BoardVO boardVO, List<AttachmentVO> imgList, List<AttachmentVO> attachList);

	public int updateNotice(BoardVO boardVO, List<AttachmentVO> imgList, List<AttachmentVO> attachList);

	public List<BoardVO> myBoardList(String memberId);

	List<BoardVO> myReplyList(String menuType, String memberId);

	
	public List<BoardVO> myReplyBoardList(String memberId);
}
