package com.holoyolo.app.memberFinanceInfo.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.memberFinanceInfo.mapper.MemberFinanceInfoMapper;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoVO;

@Service
public class MemberFinanceInfoServiceImpl implements MemberFinanceInfoService {
	
	@Autowired
	MemberFinanceInfoMapper memberFinanceInfoMapper;

	@Override
	public Map<String, String> getCardInfo() {
		MemberFinanceInfoVO vo = new MemberFinanceInfoVO();
		
		//현재 로그인한 아이디
		vo.setMemberId("testminju@mail.com");
		MemberFinanceInfoVO infoVO = memberFinanceInfoMapper.getCardInfo(vo);
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("카드회사", infoVO.getCardCompany());
		map.put("카드번호", infoVO.getCardNo());
		return map;
	}

	@Override
	public MemberFinanceInfoVO selectMemberFinanceInfo(MemberFinanceInfoVO vo) {
		
		return memberFinanceInfoMapper.selectMemberFinanceInfo(vo);
	}

	@Override
	public MemberFinanceInfoVO insertOrUpdateFinanceInfo(MemberFinanceInfoVO vo) {
		
		return memberFinanceInfoMapper.insertOrUpdateFinanceInfo(vo);
	}
}
