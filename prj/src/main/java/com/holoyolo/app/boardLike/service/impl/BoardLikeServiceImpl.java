package com.holoyolo.app.boardLike.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.board.service.BoardVO;
import com.holoyolo.app.boardLike.mapper.BoardLikeMapper;
import com.holoyolo.app.boardLike.service.BoardLikeService;
import com.holoyolo.app.boardLike.service.BoardLikeVO;

@Service
public class BoardLikeServiceImpl implements BoardLikeService {

	@Autowired
	BoardLikeMapper boardLikeMapper;

	@Override
	public BoardLikeVO checkLike(BoardLikeVO vo) {
System.out.println(vo);
		return null;
	}
	
	@Override
	public int addLike() {
	
		return 0;
	}

	@Override
	public int cancelLike() {

		return 0;
	}

	@Override
	public BoardVO boardLikeCount(BoardVO vo) {
		vo.setLikeCount(boardLikeMapper.boardLikeCount(vo));
		return vo;
	}

	

}
