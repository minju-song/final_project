package com.holoyolo.app.accBookHistory.mapper;


import java.util.List;

import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;

public interface AccBookHistoryMapper {
	public AccBookHistoryVO test(AccBookHistoryVO vo);
	
	//api호출 후 저장
	public int insertAccApi(AccBookHistoryVO vo);
	
	//db에 들어있는 데이터 중 가장 최근 날짜
	public String getLatestPayDate(AccBookHistoryVO vo);
	
	//선택한 날짜와 해당 회원의 소비내역 불러옴
	public List<AccBookHistoryVO> getAccHistory(AccBookHistoryVO vo);
	
	//해당 날짜 총 소비금액
	public int getSumPrice(AccBookHistoryVO vo);
	
}
