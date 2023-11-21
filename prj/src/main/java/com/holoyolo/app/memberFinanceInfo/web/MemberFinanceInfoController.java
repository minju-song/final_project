package com.holoyolo.app.memberFinanceInfo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.holoyolo.app.memberFinanceInfo.mapper.MemberFinanceInfoMapper;

@Controller
public class MemberFinanceInfoController {
	
	@Autowired
	MemberFinanceInfoMapper memberFinanceInfoMapper;

}
