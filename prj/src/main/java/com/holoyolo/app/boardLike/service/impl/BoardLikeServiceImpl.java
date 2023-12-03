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
	public String checkLike(BoardLikeVO vo) {
		String result = "";
		int check = boardLikeMapper.checkLike(vo);
		System.out.println("check : " + check);
		if (check == 0) {
			addLike(vo);
			result = "추가";
		} else if (check == 1) {
			cancelLike(vo);
			result = "삭제";
		}

		System.out.println("result : " + result);
		return result;
	}

	@Override
	public int addLike(BoardLikeVO vo) {

		return boardLikeMapper.addLike(vo);
	}

	@Override
	public int cancelLike(BoardLikeVO vo) {

		return boardLikeMapper.cancelLike(vo);
	}

	@Override
	public BoardVO boardLikeCount(BoardVO vo) {
		vo.setLikeCount(boardLikeMapper.boardLikeCount(vo));
		return vo;
	}

}
