package com.holoyolo.app.reply.service.Impl;

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
	public List<ReplyVO> ReplyList(int boardId) {
		return replyMapper.ReplyList(boardId);
	}

	@Override
	public int deleteReply(ReplyVO vo) {

		return replyMapper.deleteReply(vo);

	}

	@Override
	public int insertReply(ReplyVO vo) {
		return replyMapper.insertReply(vo);

	}

	@Override
	public List<ReplyVO> memberReplyList() {

		return replyMapper.memberReplyList();
	}

	
	@Override
	public List<ReplyVO> searchBoardPage(JSONObject req) {
		int start = (int) req.get("start");
		int end = (int) req.get("end");
//		int boardId = (int) req.get(req);
		List<ReplyVO> allList = ReplyList(33);
		return allList.subList(start, Math.min(end, allList.size()));
	}

	@Override
	public int getTotalBoardRecords(JSONObject req) {

		ReplyVO vo = new ReplyVO();
		vo.setBoardId((int) req.get("boardId"));

		return replyMapper.getTotalReplyRecords(req);
	}
}
