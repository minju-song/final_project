package com.holoyolo.app.accBookHistory.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.holoyolo.app.accBookHistory.web.ApiCall;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.memberFinanceInfo.service.MemberFinanceInfoService;

@Service
public class ApiJob{
//    private final ApiCall apiCall;
	
	@Autowired
	MemberService memberservice;
	
	@Autowired
	MemberFinanceInfoService memberFinanceInfoService;

    @Autowired
    ApiCall apiCall;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 0 */6 * * *")
    public void execute() {
    	System.out.println("실행");

        List<String> members = memberservice.getMembersId();
        System.out.println(members);
        
        for (int i = 0; i < members.size(); i++) {
        	//회원의 카드번호 담을 맵
        	Map<String, String> map = new HashMap<>();
        	
        	//회원카드정보 받아옴
        	map = memberFinanceInfoService.getCardInfo(members.get(i));
        	
        	//만약 카드 등록이 되어있다면 api를 호출, 등록이 되어있지않다면 호출안함
        	if(!map.get("카드번호").equals("null")) {			
        		apiCall.getPosts(members.get(i));		
        		System.out.println(members.get(i) + "회원 가계부 업데이트");
        	}

        }
        
        logger.info("스케줄러가 실행되었습니다.");
    }
}
