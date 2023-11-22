package com.holoyolo.app.memberFinanceInfo.service;

public interface MemberFinanceInfoService {

	

		// 단건
		public MemberFinanceInfoVO selectMemberFinanceInfo(MemberFinanceInfoVO vo);

		// 등록
		public int insertMemberFinance(MemberFinanceInfoVO vo);
}
