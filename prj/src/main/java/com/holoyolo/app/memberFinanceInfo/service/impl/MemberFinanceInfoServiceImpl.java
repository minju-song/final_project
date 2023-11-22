package com.holoyolo.app.memberFinanceInfo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.memberFinanceInfo.mapper.MemberFinanceInfoMapper;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

@Service
public class MemberFinanceInfoServiceImpl implements MemberFinanceInfoService {
	
	@Autowired
	MemberFinanceInfoMapper MFIM;

	@Override
	public MemberFinanceInfoVO selectMemberFinanceInfo(MemberFinanceInfoVO vo) {
		
		return MFIM.selectMemberFinanceInfo(vo);
	}

	@Override
	public int insertMemberFinance(MemberFinanceInfoVO vo) {
		
		return MFIM.insertMemberFinance(vo);
	}

}
