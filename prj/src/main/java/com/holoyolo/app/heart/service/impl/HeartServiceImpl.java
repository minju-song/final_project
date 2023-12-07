package com.holoyolo.app.heart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.heart.mapper.HeartMapper;
import com.holoyolo.app.heart.service.HeartService;
import com.holoyolo.app.heart.service.HeartVO;

@Service
public class HeartServiceImpl implements HeartService {
	
	@Autowired
	HeartMapper heartMapper;
	
	//하트 수
	@Override
	public HeartVO getHeartCount(HeartVO heartVO) {
		return heartMapper.selectHeartCount(heartVO);
	}
	
	//단건조회
	@Override
	public HeartVO getHeart(HeartVO heartVO) {
		return heartMapper.selectHeart(heartVO);
	}
	
	//등록
	@Override
	public int insertHeart(HeartVO heartVO) {
		return heartMapper.insertHeart(heartVO);
	}
	
	//삭제
	@Override
	public int deleteHeart(HeartVO heartVO) {
		return heartMapper.deleteHeart(heartVO);
	}
}
