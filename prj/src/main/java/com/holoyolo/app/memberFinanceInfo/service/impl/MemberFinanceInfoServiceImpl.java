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

	@SuppressWarnings("null")
	@Override
	public Map<String, String> getCardInfo(String id) {
		MemberFinanceInfoVO vo = new MemberFinanceInfoVO();
		
		//현재 로그인한 아이디
		vo.setMemberId(id);
		MemberFinanceInfoVO infoVO = memberFinanceInfoMapper.getCardInfo(vo);
		
		Map<String,String> map = new HashMap<String, String>();
		if(infoVO != null && !infoVO.getCardNo().equals("") && infoVO.getCardNo() != null) {	
			map.put("카드회사", infoVO.getCardCompany());
			map.put("카드번호", infoVO.getCardNo());
		}
		else {
			map.put("카드회사", "null");
			map.put("카드번호", "null");
		}
		
		return map;
	}

	@Override
	public MemberFinanceInfoVO selectMemberFinanceInfo(MemberFinanceInfoVO vo) {
		
		return memberFinanceInfoMapper.selectMemberFinanceInfo(vo);
	}

	@Override   
	public int insertMemberFinance(MemberFinanceInfoVO vo) {
		
		return memberFinanceInfoMapper.insertMemberFinance(vo);
	}

	@Override
	public Boolean checkCardSelect() {
		MemberFinanceInfoVO vo = new MemberFinanceInfoVO();
		//멤버아이디가져오기
		vo.setMemberId("testminju@mail.com");
		vo = memberFinanceInfoMapper.checkCardSelect(vo);
		if(vo != null && !vo.getCardNo().equals("")) {
			return true;
		}
		else {
			return false;
		}
		
	}

	@Override
	public int insertCard(MemberFinanceInfoVO vo) {
		return memberFinanceInfoMapper.insertCard(vo);
	}

	@Override
	public int updateCard(MemberFinanceInfoVO vo) {
		return memberFinanceInfoMapper.updateCard(vo);
	}

	@Override
	public int delcard(MemberFinanceInfoVO vo) {
		return memberFinanceInfoMapper.delcard(vo);
	}
	
	
}
