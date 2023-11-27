package com.holoyolo.app.memberFinanceInfo.service;

import java.util.Map;

public interface MemberFinanceInfoService {
	// 금융정보 조회
	public MemberFinanceInfoVO selectMemberFinanceInfo(MemberFinanceInfoVO vo);

	// 등록
	public int insertMemberFinance(MemberFinanceInfoVO vo);
	
	//회원카드정보조회
	public Map<String, String> getCardInfo(String id);
	
	//회원금융정보있는지 조회
	public Boolean checkCardSelect();
	
	//회원카드정보등록
	public int insertCard(MemberFinanceInfoVO vo);
	
	//회원카드수정
	public int updateCard(MemberFinanceInfoVO vo);
	
	//회원카드삭제
	public int delcard(MemberFinanceInfoVO vo);
}
