package com.holoyolo.app.board.service.Impl;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.board.mapper.BoardMapper;
import com.holoyolo.app.board.service.BoardService;
import com.holoyolo.app.board.service.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardMapper boardMapper;

	// 게시판 별전체조회
	@Override
	public List<BoardVO> BoardList(String req) {
		BoardVO vo = new BoardVO();
		vo.setMenuType(req);
		return boardMapper.BoardList(vo);
	}

//단건조회
	@Override
	public BoardVO selectBoard(int vo) {

		return boardMapper.selectBoard(vo);
	}

	@Override
	public int insertBoard(JSONObject req, String userId) {
		System.out.println(req);
		System.out.println(userId);

		BoardVO vo = new BoardVO();
		vo.setContent((String) req.get("content"));
		vo.setTitle((String) req.get("title"));
		vo.setMenuType((String) req.get("menuType"));
		vo.setMemberId(userId);

		return boardMapper.insertBoard(vo);
	}

	@Override
	public int updateBoard(BoardVO vo) {

		return boardMapper.updateBoard(vo);
	}

	@Override
	public int deleteBoard(BoardVO vo) {

		return boardMapper.deleteBoard(vo);
	}

	@Override
	public List<BoardVO> searchBoardPaged(JSONObject req) {
		int start = (int) req.get("start");
		int end = (int) req.get("end");
		String menuType = (String)req.get("type");
		System.out.println(menuType);
		List<BoardVO> allList = BoardList(menuType);
		return allList.subList(start, Math.min(end, allList.size()));
	}

	@Override
	public int getTotalBoardRecords(JSONObject req) {
		
		BoardVO vo = new BoardVO();
		vo.setMenuType((String)req.get("type"));
		System.out.println(vo);
		
		
		return boardMapper.getTotalBoardRecords(vo);
	}
}
