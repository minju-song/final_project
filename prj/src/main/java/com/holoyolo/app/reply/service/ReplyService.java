package com.holoyolo.app.reply.service;

import java.util.List;

import org.json.simple.JSONObject;

public interface ReplyService {
	
	

//게시물 댓글삭제
	public String deleteReply(ReplyVO vo);

//게시물 댓글등록
	public int insertReply(ReplyVO vo);

//회원별 댓글 단 게시물 목록
	public List<ReplyVO> memberReplyList();

	public List<ReplyVO> searchReplyPage(JSONObject req);

	public int getTotalReplyRecords(JSONObject req);

	public String updateReply(ReplyVO vo);

	public ReplyVO upperReplySearch(int replyId);
	// 게시물 상위댓글목록
	

	List<ReplyVO> upperReplyList(ReplyVO vo);

	List<ReplyVO> rowReplyList(ReplyVO vo);

}
