package com.holoyolo.app.reply.service;

import java.util.List;

import org.json.simple.JSONObject;

public interface ReplyService {
	// 게시물 댓글목록
	public List<ReplyVO> ReplyList(int boardId);

//게시물 댓글삭제
	public int deleteReply(ReplyVO vo);

//게시물 댓글등록
	public int insertReply(ReplyVO vo);

//회원별 댓글 단 게시물 목록
	public List<ReplyVO> memberReplyList();

	List<ReplyVO> searchReplyPage(JSONObject req);

	int getTotalReplyRecords(JSONObject req);
}
