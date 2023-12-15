package com.holoyolo.app.mail.service;

import java.security.SecureRandom;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.trade.service.TradeVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender javaMailSender;
	private final SpringTemplateEngine templateEngine;
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	/**
	 * 메일발송
	 * @param emailVO
	 * @param type
	 * @return
	 */
	public void sendMail(EmailVO emailVO, TradeVO tradeVO, String type) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(emailVO.getTo()); // 수신자
			mimeMessageHelper.setSubject(emailVO.getSubject()); // 제목
			
			if(type.equals("password")) {
				String authNum = createCode(10); //임시비밀번호
				memberService.updateMemberPwd(emailVO.getTo(), passwordEncoder.encode(authNum));
				mimeMessageHelper.setText(setContext(authNum, type), true); // 메일 본문 내용, HTML 여부
			} else if(type.equals("promise")) {
				mimeMessageHelper.setText(setContext(tradeVO, type), true);
			} else if(type.equals("promiseOk")) {
				mimeMessageHelper.setText(setContext(tradeVO, type), true);
			}
			javaMailSender.send(mimeMessage);
			
			log.info("Mail Send Success");
			
			//return authNum;
			
		} catch(MessagingException e) {
			log.info("Mail Send Fail");
			throw new RuntimeException(e);
		}
	}

	/**
	 * 임시 비밀번호 생성
	 * @return
	 */
	private String createCode(int size) {
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
				'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&' };
		StringBuffer sb = new StringBuffer();
		SecureRandom sr = new SecureRandom();
		sr.setSeed(new Date().getTime());
		int idx = 0;
		int len = charSet.length;
		for (int i = 0; i < size; i++) {
			// idx = (int) (len * Math.random());
			idx = sr.nextInt(len); // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
			sb.append(charSet[idx]);
		}
		return sb.toString();
	}
	
	/**
	 * thymeleaf를 통한 html 적용
	 * @param code
	 * @param type
	 * @return
	 */
    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        String path = "user/mailbody/";
        return templateEngine.process(path + type, context);
    }
    
    /**
     * 중고거래 약속잡기
     * @param tradeVO
     * @param type
     * @return
     */
    public String setContext(TradeVO tradeVO, String type) {
    	Context context = new Context();
        context.setVariable("nickname", tradeVO.getNickname());
        context.setVariable("title", tradeVO.getTitle());
        context.setVariable("sellerId", tradeVO.getSellerId());
        context.setVariable("buyerId", tradeVO.getBuyerId());
        context.setVariable("tradeId", tradeVO.getTradeId());
        String path = "user/mailbody/";
    	return templateEngine.process(path + type, context);
    }
    
    
	//클럽가입요청
    public void sendRequest(EmailVO emailVO) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(emailVO.getTo()); // 수신자
			mimeMessageHelper.setSubject(emailVO.getSubject()); // 제목
			mimeMessageHelper.setText(setContextReq(emailVO.getClubId(),emailVO.getClubName(),emailVO.getReqId(), emailVO.getText(),emailVO.getType()), true); // 메일 본문 내용, HTML 여부
			javaMailSender.send(mimeMessage);
			
			log.info("Mail Send Success");
			
			//return authNum;
			
		} catch(MessagingException e) {
			log.info("Mail Send Fail");
			throw new RuntimeException(e);
		}
    }
    
    public String mandate(EmailVO emailVO) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(emailVO.getTo()); // 수신자
			mimeMessageHelper.setSubject(emailVO.getSubject()); // 제목
			mimeMessageHelper.setText(setContextMandate(emailVO.getClubId(),emailVO.getClubName(),emailVO.getReqId(), emailVO.getText(),emailVO.getType(),emailVO.getLeader()), true); // 메일 본문 내용, HTML 여부
			javaMailSender.send(mimeMessage);
			
			log.info("Mail Send Success");
			return "success";
			//return authNum;
			
		} catch(MessagingException e) {
			log.info("Mail Send Fail");
			throw new RuntimeException(e);

		}
    }
    
    public String setContextMandate(int clubId,String clubName, String reqId, String text, String type, String leader) {
        Context context = new Context();
        context.setVariable("club", clubId); //클럽아이디
        context.setVariable("member", reqId); //요청하는 전클럽리더
        context.setVariable("clubName", clubName); //클럽이름
        context.setVariable("text", text); // 위임사유
        context.setVariable("type", type);
        context.setVariable("leader", leader);
        String path = "user/mailbody/mandate";
        return templateEngine.process(path, context);
    }
    
    public String setContextReq(int clubId,String clubName, String reqId, String text, String type) {
        Context context = new Context();
        context.setVariable("club", clubId);
        context.setVariable("member", reqId);
        context.setVariable("clubName", clubName);
        context.setVariable("text", text);
        context.setVariable("type", type);
        String path = "user/mailbody/request";
        return templateEngine.process(path, context);
    }
    
    public String deleteReason(EmailVO emailVO) {
    	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(emailVO.getTo()); // 수신자
			mimeMessageHelper.setSubject(emailVO.getSubject()); // 제목
			mimeMessageHelper.setText(setContextDeleteReason(emailVO.getClubId(),emailVO.getClubName(), emailVO.getText()), true); // 메일 본문 내용, HTML 여부
			javaMailSender.send(mimeMessage);
			
			log.info("Mail Send Success");
			return "success";
			//return authNum;
			
		} catch(MessagingException e) {
			log.info("Mail Send Fail");
			throw new RuntimeException(e);

		}
    }
    
    public String setContextDeleteReason(int clubId,String clubName, String text) {
        Context context = new Context();
        context.setVariable("club", clubId); //클럽아이디
        context.setVariable("clubName", clubName); //클럽이름
        context.setVariable("text", text); // 삭제사유
        String path = "user/mailbody/deleteReason";
        return templateEngine.process(path, context);
    }
}
