package com.holoyolo.app.memberFinanceInfo.mapper;

import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

public interface MemberFinanceInfoMapper {
	
	//회원카드정보조회
	public MemberFinanceInfoVO getCardInfo(MemberFinanceInfoVO vo);
}
