package com.holoyolo.app.heart.service;


public interface HeartService {
	//하트 수
	public HeartVO getHeartCount(HeartVO heartVO);
	
	//단건조회
	public HeartVO getHeart(HeartVO heartVO);
	
	//등록
	public int insertHeart(HeartVO heartVO);
	
	//삭제
	public int deleteHeart(HeartVO heartVO);
}
