package com.holoyolo.app.accBookHistory.service;

import java.util.List;
import java.util.Map;

public interface AccBookHistoryService {
	public AccBookHistoryVO test(AccBookHistoryVO vo);
	
	//api호출 후 저장
	public int insertAcc(AccBookHistoryVO vo);
	
	//db에 들어있는 데이터 중 가장 최근 날짜
	public String getLatestPayDate(String id);
	
	//선택한 날짜와 해당 회원의 소비내역 불러옴
	public List<AccBookHistoryVO> getAccHistory(AccBookHistoryVO vo);
	
	//해당 날짜 총 소비금액
	public int getSumPrice(AccBookHistoryVO vo);
	
	//해당 날짜 총 소득금액
	public int getSumInputPrice(AccBookHistoryVO vo);
	
	//해당 월 총 소비금액
	public int getMonthPrice(AccBookHistoryVO vo);
	
	//거래내역 삭제
	public int deleteHistory(AccBookHistoryVO vo);
	

	//마이페이지 차트용 데이터(멤버아이디와 가계부 결제방식 필요)
	public List<AccBookHistoryVO> selectChartData(AccBookHistoryVO vo);
	
}
