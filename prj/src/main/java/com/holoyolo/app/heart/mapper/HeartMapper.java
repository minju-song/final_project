package com.holoyolo.app.heart.mapper;


import com.holoyolo.app.heart.service.HeartVO;

public interface HeartMapper {
	//하트 수
	public HeartVO selectHeartCount(HeartVO heartVO);
	
	//단건조회
	public HeartVO selectHeart(HeartVO heartVO);
	
	//등록
	public int insertHeart(HeartVO heartVO);
	
	//삭제
	public int deleteHeart(HeartVO heartVO);
}
