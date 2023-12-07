package com.holoyolo.app.accBookHistory.mapper;


import java.util.List;

import com.holoyolo.app.accBookHistory.service.AccBookHistoryVO;

public interface AccBookHistoryMapper {
	public AccBookHistoryVO test(AccBookHistoryVO vo);
	
	//api호출 후 저장
	public int insertAcc(AccBookHistoryVO vo);
	
	//db에 들어있는 데이터 중 가장 최근 날짜
	public String getLatestPayDate(AccBookHistoryVO vo);
	
	//선택한 날짜와 해당 회원의 소비내역 불러옴
	public List<AccBookHistoryVO> getAccHistory(AccBookHistoryVO vo);
	
	//해당 날짜 총 소비금액
	public int getSumPrice(AccBookHistoryVO vo);
	
	//해당 날짜 총 수입금액
	public int getSumInputPrice(AccBookHistoryVO vo);
	
	//해당 월 총 소비금액
	public int getMonthPrice(AccBookHistoryVO vo);
	
	//거래내역 삭제
	public int deleteHistory(AccBookHistoryVO vo);
	
	//방금 넣은 거래내역 아이디 얻기
	public int selectCurrent(AccBookHistoryVO vo);
}
