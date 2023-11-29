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

	@Override
	public List<BoardVO> BoardList() {

		return boardMapper.BoardList();
	}

	@Override
	public BoardVO selectBoard(BoardVO vo) {

		return boardMapper.selectBoard(vo);
	}

	@Override
	public int insertBoard(JSONObject req, String userId) {
		System.out.println(req);
		System.out.println(userId);
		
		BoardVO vo = new BoardVO();
		vo.setContent((String)req.get("content"));
		vo.setTitle((String)req.get("title"));
		vo.setMenuType((String) req.get("menuType"));
		vo.setMemberId(userId);
		
		return  boardMapper.insertBoard(vo);
	}

	@Override
	public int updateBoard(BoardVO vo) {

		return boardMapper.updateBoard(vo);
	}

	@Override
	public int deleteBoard(BoardVO vo) {

		return boardMapper.deleteBoard(vo);
	}

}
