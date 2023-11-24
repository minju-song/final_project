package com.holoyolo.app.accBookSuccessHistory.mapper;

import com.holoyolo.app.accBookSuccessHistory.service.AccBookSuccessHistoryVO;

public interface AccBookSuccessHistoryMapper {
	
	//날짜마다 성공여부가져옴
	public AccBookSuccessHistoryVO getSuccessByDay(AccBookSuccessHistoryVO vo);
}
