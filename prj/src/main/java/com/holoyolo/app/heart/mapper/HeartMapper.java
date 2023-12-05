package com.holoyolo.app.heart.mapper;


import com.holoyolo.app.heart.service.HeartVO;

public interface HeartMapper {
	//하트 수
	public HeartVO selectHeartCount(HeartVO heartVO);
}
