package com.holoyolo.app.trade.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holoyolo.app.attachment.mapper.AttachmentMapper;
import com.holoyolo.app.attachment.service.AttachmentVO;
import com.holoyolo.app.trade.mapper.TradeMapper;
import com.holoyolo.app.trade.service.TradeService;
import com.holoyolo.app.trade.service.TradeVO;

@Service
public class TradeServiceImpl implements TradeService {

	@Autowired
	TradeMapper tradeMapper;
	@Autowired
	AttachmentMapper attachmentMapper;
	
	// 거래 전체조회
	@Override
	public List<TradeVO> getTradeList() {
		return tradeMapper.selectTradeList();
	}
	
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
	public Map<String, Object> tradePaging(TradeVO tradeVO) {
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


}