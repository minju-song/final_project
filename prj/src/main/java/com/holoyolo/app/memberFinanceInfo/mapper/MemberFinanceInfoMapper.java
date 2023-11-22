package com.holoyolo.app.memberFinanceInfo.mapper;

import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

public interface MemberFinanceInfoMapper {
	
	// 단건
	public MemberFinanceInfoVO selectMemberFinanceInfo(MemberFinanceInfoVO vo);

	// 등록
	public int insertMemberFinance(MemberFinanceInfoVO vo);
}
