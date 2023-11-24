package com.holoyolo.app.memberFinanceInfo.service;

import java.util.Map;

public interface MemberFinanceInfoService {
	// 금융정보 조회
	public MemberFinanceInfoVO selectMemberFinanceInfo(MemberFinanceInfoVO vo);

	// 등록 + 수정
	public MemberFinanceInfoVO insertOrUpdateFinanceInfo(MemberFinanceInfoVO vo);
	
	//회원카드정보조회
	public Map<String, String> getCardInfo();
}
