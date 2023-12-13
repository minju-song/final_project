package com.holoyolo.app.trade.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holoyolo.app.attachment.mapper.AttachmentMapper;
import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.trade.mapper.TradeMapper;
import com.holoyolo.app.trade.service.TradeService;
import com.holoyolo.app.trade.service.TradeVO;

@Service
public class TradeServiceImpl implements TradeService {

	@Autowired
	TradeMapper tradeMapper;
	@Autowired
	AttachmentMapper attachmentMapper;
	
	// 거래 단건조회
	@Override
	public TradeVO getTrade(TradeVO tradeVO) {
		return tradeMapper.selectTrade(tradeVO);
	}

	// 거래 등록
	@Override
	@Transactional
	public int insertTrade(TradeVO tradeVO, List<AttachmentVO> imgList) {
		int result = tradeMapper.insertTrade(tradeVO);
		
		for(int i=0; i<imgList.size(); i++) {
			AttachmentVO vo = imgList.get(i);
			vo.setMenuType("AA1");
			vo.setPostId(tradeVO.getTradeId());
			attachmentMapper.insertAttachment(imgList.get(i));
		}
		
		if(result == 1) {
			return tradeVO.getTradeId();
		}
		return -1;
	}

	// 거래 수정
	@Override
	public Map<String, Object> updateTrade(TradeVO tradeVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;
		
		int result = tradeMapper.updateTrade(tradeVO);
		if(result == 1) {
			isSuccessed = true;
		}
		
		map.put("result", isSuccessed);
		map.put("info", tradeVO);
		
		return map;
	}

	// 거래 삭제
	@Override
	public int deleteTrade(TradeVO tradeVO) {
		return tradeMapper.deleteTrade(tradeVO);
	}

	//리스트 페이징
	@Override
	public Map<String, Object> getTradeList(TradeVO tradeVO) {
		Map<String, Object> map = new HashMap<>();
		List<TradeVO> list = tradeMapper.getTradeList(tradeVO);
		
		map.put("total", tradeMapper.cntData(tradeVO));
		map.put("list", list);
		
		return map;
	}

	//조회수 증가
	@Override
	public int updateViews(TradeVO tradeVO) {
		return tradeMapper.updateViews(tradeVO);
	}

	//구매자 수정
	@Override
	public int updateBuyerId(TradeVO tradeVO) {
		return tradeMapper.updateBuyerId(tradeVO);
	}

	@Override
	public int updateTradeImg(TradeVO tradeVO, List<AttachmentVO> imgList) {
		int result = 0;
		if(imgList != null) {
			for(int i=0; i<imgList.size(); i++) {
				AttachmentVO vo = imgList.get(i);
				vo.setMenuType("AA1");
				vo.setPostId(tradeVO.getTradeId());
				result = attachmentMapper.insertAttachment(imgList.get(i));
			}
		}
		return result;
	}

	//포인트, 홀로페이 등록(구매자)
	@Override
	public Map<String, Object> insertPayPoint(MemberVO memberVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSucceed = false;
		
		int result = tradeMapper.insertPayPoint(memberVO);
		if(result == 1) {
			isSucceed = true;
		}
		
		map.put("result", isSucceed);
		map.put("info", memberVO);
		return map;
	}
	
	//포인트, 홀로페이 등록(판매자)
		@Override
		public Map<String, Object> insertPayPointSeller(MemberVO memberVO) {
			Map<String, Object> map = new HashMap<>();
			boolean isSucceed = false;
			
			int result = tradeMapper.insertPayPointSeller(memberVO);
			if(result == 1) {
				isSucceed = true;
			}
			
			map.put("result", isSucceed);
			map.put("info", memberVO);
			return map;
		}

	//마이페이지 전체조회
	@Override
	public List<TradeVO> selectMyTradeList(TradeVO tradeVO) {
		return tradeMapper.selectMyTradeList(tradeVO);
	}

	@Override
	public TradeVO selectTrade2(TradeVO vo) {
		return tradeMapper.selectTrade2(vo);
	}
}