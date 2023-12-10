package com.holoyolo.app.board.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holoyolo.app.attachment.service.AttachmentService;
import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.board.mapper.BoardMapper;
import com.holoyolo.app.board.service.BoardService;
import com.holoyolo.app.board.service.BoardVO;
import com.holoyolo.app.boardLike.service.BoardLikeService;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardMapper boardMapper;

	@Autowired
	BoardLikeService boardLikeService;

	@Autowired
	AttachmentService attachmentService;

	// 게시판 별전체조회
	@Override
	public List<BoardVO> BoardList(String menuType, String search, String searchType) {

		BoardVO vo = new BoardVO();
		vo.setMenuType(menuType);
		List<BoardVO> resultList = new ArrayList<BoardVO>();
		if (search == "") {
			resultList = boardMapper.BoardList(vo);
		} else {
			vo.setSearchWord(search);
			vo.setSearchOption(searchType);
			resultList = boardMapper.searchBoardList(vo);
		}

		return resultList;
	}

	// 단건조회
	@Override
	public BoardVO selectBoard(int boardId) {
		BoardVO vo = new BoardVO();
		vo = boardMapper.selectBoard(boardId);
		vo.setLikeCount(boardLikeService.boardLikeCount(boardId));
		return vo;
	}

	@Override
	public int insertBoard(BoardVO vo) {
		boardMapper.insertBoard(vo);
		return vo.getBoardId();

	}

	@Override
	public int updateBoard(BoardVO vo) {
		System.out.println(vo);
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
		String menuType = (String) req.get("type");
		List<BoardVO> allList = BoardList(menuType, "", "");
		return allList.subList(start, Math.min(end, allList.size()));
	}

	@Override
	public int getTotalBoardRecords(JSONObject req) {

		BoardVO vo = new BoardVO();
		vo.setMenuType((String) req.get("type"));

		return boardMapper.getTotalBoardRecords(vo);
	}

	@Override
	public int addView(int boardId) {
		BoardVO vo = new BoardVO();
		vo.setBoardId(boardId);
		return boardMapper.addView(vo);
	}

	@Override
	public List<BoardVO> recentBoradList(BoardVO vo) {
		return boardMapper.recentBoradList(vo);
	}

	public BoardVO checkBoard(int boardId) {
		BoardVO vo = new BoardVO();
		vo = boardMapper.selectBoard(boardId);
		return vo;
	}

	@Override
	public List<BoardVO> searchBoardSurfPaged(JSONObject req) {
		String search = (String) req.get("search");
		String searchType = (String) req.get("searchType");
		String menuType = (String) req.get("type");
		return BoardList(menuType, search, searchType);
	}

	@Override
	public int getTotalBoardSurfRecords(JSONObject req) {
		BoardVO vo = new BoardVO();
		vo.setMenuType((String) req.get("type"));
		vo.setSearchOption((String) req.get("searchType"));
		vo.setSearchWord((String) req.get("search"));

		return boardMapper.getTotalBoardSurfRecords(vo);

	}

	@Override
	@Transactional
	public int insertNotice(BoardVO boardVO, List<AttachmentVO> imgList, List<AttachmentVO> attachList) {
		boardVO.setMenuType("AA6");
		int result = boardMapper.insertBoard(boardVO);
		for (AttachmentVO vo : imgList) {
			vo.setMenuType("AA6");
			vo.setPostId(boardVO.getBoardId());
			attachmentService.insertAttachment(vo);
		}
		for (AttachmentVO vo : imgList) {
			vo.setMenuType("AA6");
			vo.setPostId(boardVO.getBoardId());
			attachmentService.insertAttachment(vo);
		}

		if (result == 1) {
			return boardVO.getBoardId();
		} else {
			return -1;
		}
	}

	@Override
	@Transactional
	public int updateNotice(BoardVO boardVO, List<AttachmentVO> imgList, List<AttachmentVO> attachList) {
		boardVO.setMenuType("AA6");
		// 기존 첨부파일 삭제
		attachmentService.deletePostAttachment(boardVO);

		// 본문 내용 UPDATE
		int result = boardMapper.updateBoard(boardVO);
		if (imgList != null) {
			for (AttachmentVO vo : imgList) {
				vo.setMenuType("AA6");
				vo.setPostId(boardVO.getBoardId());
				attachmentService.insertAttachment(vo);
			}
		}
		// 이미지 및 첨부파일 새로 등록
		if (attachList != null) {
			for (AttachmentVO vo : attachList) {
				vo.setMenuType("AA6");
				vo.setPostId(boardVO.getBoardId());
				System.out.println(attachList);
				attachmentService.insertAttachment(vo);
			}
		}

		if (result == 1) {
			return boardVO.getBoardId();
		} else {
			return -1;
		}

	}

}
