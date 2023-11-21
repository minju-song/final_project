package com.holoyolo.app.memberFinanceInfo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.memberFinanceInfo.mapper.MemberFinanceInfoMapper;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;

@Service
public class MemberFinanceInfoServiceImpl implements MemberFinanceInfoService {
	
	@Autowired
	MemberFinanceInfoMapper memberFinanceInfoMapper;

}
