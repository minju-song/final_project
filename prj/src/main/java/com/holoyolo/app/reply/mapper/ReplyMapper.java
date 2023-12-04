package com.holoyolo.app.reply.mapper;

import java.util.List;

import org.json.simple.JSONObject;

import com.holoyolo.app.reply.service.ReplyVO;

public interface ReplyMapper {
	// 게시물 댓글목록
	public List<ReplyVO> ReplyList(int boardId);

	// 게시물 댓글삭제
	public int deleteReply(ReplyVO vo);

	// 게시물 댓글등록
	public int insertReply(ReplyVO vo);

	// 회원별 댓글 단 게시물 목록
	public List<ReplyVO> memberReplyList();
	
	// 페이징된 내역 조회
		public List<ReplyVO> searchReplyPaged(JSONObject req);

		// 전체 레코드 수 조회
		public int getTotalReplyRecords(JSONObject req);
}