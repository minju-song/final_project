package com.holoyolo.app.memberFinanceInfo.mapper;

import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

public interface MemberFinanceInfoMapper {

	// 회원카드정보조회
	public MemberFinanceInfoVO getCardInfo(MemberFinanceInfoVO vo);

	// 단건
	public MemberFinanceInfoVO selectMemberFinanceInfo(MemberFinanceInfoVO vo);

	// 등록
	public int insertMemberFinance(MemberFinanceInfoVO vo);
	
	//회원금융정보있는지 조회
	public MemberFinanceInfoVO checkCardSelect(MemberFinanceInfoVO vo);
	
	//회원카드등록
	public int insertCard(MemberFinanceInfoVO vo);
	
	//회원카드수정
	public int updateCard(MemberFinanceInfoVO vo);
	
	//회원카드삭제
	public int delcard(MemberFinanceInfoVO vo);
}
