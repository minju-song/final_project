package com.holoyolo.app.accBookHistory.service;

import java.util.List;

import javax.activation.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.holoyolo.app.accBookHistory.web.ApiCall;
import com.holoyolo.app.member.mapper.MemberMapper;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.member.service.impl.MemberServiceImpl;

@Component
public class ApiJob implements Job {
//    private final ApiCall apiCall;
	
	@Autowired
	MemberService service;

    @Autowired
    ApiCall apiCall;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	System.out.println("실행");

        List<MemberVO> members = service.getMembersId();
        System.out.println(members);
        
        for (int i = 0; i < members.size(); i++) {
            apiCall.getPosts(members.get(i).getMemberId());
            System.out.println(members.get(i) + "회원 가계부 업데이트");
        }
    }
}
