package com.holoyolo.app.reply.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.reply.mapper.ReplyMapper;
import com.holoyolo.app.reply.service.ReplyService;
import com.holoyolo.app.reply.service.ReplyVO;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	ReplyMapper replyMapper;

	@Override
	public List<ReplyVO> upperReplyList(ReplyVO vo) {
		List<ReplyVO> setList = new ArrayList<ReplyVO>();
		setList = replyMapper.upperReplyList(vo);

		return setList;
	}

	@Override
	public String deleteReply(ReplyVO vo) {

		String result = "";

		int set = replyMapper.deleteReply(vo);
		if (set == 0) {
			result = "err";
		} else if (set == 1) {
			result = "댓글이 삭제되었습니다";
		}

		return result;

	}

	@Override
	public int insertReply(ReplyVO vo) {
		System.out.println(vo);
		return replyMapper.insertReply(vo);
	}

	@Override
	public List<ReplyVO> memberReplyList() {
		return replyMapper.memberReplyList();
	}

	@Override
	public List<ReplyVO> searchReplyPage(JSONObject req) {
		int start = (int) req.get("start");
		int end = (int) req.get("end");
		 
		
		ReplyVO vo = new ReplyVO();
		vo.setBoardId((int) req.get("boardId"));
		vo.setUpperReplyId(0);
		List<ReplyVO> allList = upperReplyList(vo);
		return allList.subList(start, Math.min(end+1, allList.size()));
	}

	@Override
	public int getTotalReplyRecords(JSONObject req) {
		ReplyVO vo = new ReplyVO();
		vo.setBoardId((int) req.get("boardId"));
		return replyMapper.getTotalReplyRecords(vo);
	}

	@Override
	public String updateReply(ReplyVO vo) {
		String result = "";
		int set = replyMapper.updateReply(vo);
		if (set == 0) {
			result = "err";
		} else if (set == 1) {
			result = "댓글이 수정되었습니다";
		}

		return result;

	}

	@Override
	public ReplyVO upperReplySearch(int replyId) {
		ReplyVO vo = new ReplyVO();
		vo = replyMapper.upperReplySearch(replyId);

		return vo;
	}
}
