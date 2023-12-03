package com.holoyolo.app.member.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.holoyolo.app.attachment.service.AttachmentService;
import com.holoyolo.app.member.mapper.MemberMapper;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberMapper memberMapper;
	@Autowired
	AttachmentService attachmentService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Override
	public String joinUser(MemberVO memberVO) {
		MemberVO vo =  memberMapper.checkUserPhone(memberVO); // 회원 가입 전 인증된 번호가 있는지 확인.
		
		if(vo != null) {
			log.warn("이미 가입된 회원");
			return "JoinUser";
		} else {
			String rawPassword = memberVO.getPassword();
			String encPassword = passwordEncoder.encode(rawPassword);
			memberVO.setPassword(encPassword);
			memberVO.setRole("HA1"); //일반회원
			
			System.out.println(memberVO);
			
			int result =  memberMapper.joinUser(memberVO);
			
			if(result > 0) {
				return "Success";
			} else {
				return "Fail";
			}
		}
	}

	@Override
	public MemberVO selectUser(String username) {
		return memberMapper.selectUser(username);
	}

	@Override
	public Date selectJoinDate(String id) {
		MemberVO vo = new MemberVO();
		//회원아이디가져오기
		vo.setMemberId(id);
		vo = memberMapper.selectJoinDate(vo);
		return vo.getJoinDate();
	}
	
	// 회원 전체조회
	@Override
	public List<MemberVO> selectMemberAll() {
		return memberMapper.selectMemberAll();
	}

	// 회원 상세보기
	@Override
	public MemberVO selectMemberInfo(MemberVO memberVO) {
		return memberMapper.selectMemberInfo(memberVO);
	}
	
	// 회원 정보수정
	@Override
	public boolean updateMemberInfo(MemberVO memberVO) {
		boolean result = false;
		
		// 닉네임 변경인 경우 체크 후 업데이트
		if(memberVO.getNickname() != null) {
			String check = this.checkNickname(memberVO);
			if(check == "NOT_FOUND") {
				int cnt = memberMapper.updateMemberInfo(memberVO);
				if(cnt > 0) result = true;
			}
		}
		
		// 비밀번호 변경인 경우 암호화 진행 후 업데이트
		if(memberVO.getPassword() != null) {
			String encodePwd = passwordEncoder.encode(memberVO.getPassword());
			memberVO.setPassword(encodePwd);
			int cnt = memberMapper.updateMemberInfo(memberVO);
			if(cnt > 0) result = true;
		}
		
		// 닉네임, 비밀번호가 아닌 경우 바로 업데이트
		int cnt = memberMapper.updateMemberInfo(memberVO);
		if(cnt > 0) result = true;
		
		return result;
	}

	@Override
	public String checkMemberId(MemberVO memberVO) {
		String result = "NOT_FOUND";
		MemberVO vo = new MemberVO();
		log.warn("memberId ::: " + memberVO.getMemberId());
		
		vo = memberMapper.checkMemberId(memberVO);
		log.warn("사용가능한 아이디 ::: " + memberVO.getMemberId());
		
		if(vo != null) {
			result = "FOUND";
			log.warn("조회 결과 ::: " + result + ", " + vo.getMemberId());
		}
		
		return result;
	}

	@Override
	public String checkNickname(MemberVO memberVO) {
		String result = "NOT_FOUND";
		
		// 조회된 데이터가 있으면, 사용불가
		MemberVO vo = new MemberVO();
		vo = memberMapper.checkNickname(memberVO);
		if(vo != null) { 
			result = "FOUND";
		}
		
		return result;
	}

	@Override
	public String findMemberIdPwd(MemberVO memberVO) {
		MemberVO vo = new MemberVO();
		vo = memberMapper.findMemberIdPwd(memberVO);
		log.warn("조회 결과 ::: " + vo);
		
		if(vo != null && vo.getStopDate() == null) {
			return vo.getMemberId();
		} else {
			return "Fail";
		}
	}

	@Override
	public int updateMemberPwd(String memberId, String authNum) {
		return memberMapper.updateMemberPwd(memberId, authNum);
	}

	@Override
	public int updateMemberFailCnt(MemberVO memberVO) {
		return memberMapper.updateMemberFailCnt(memberVO);
	}

	@Override
	public int updateMemberFailCntReset(MemberVO memberVO) {
		return memberMapper.updateMemberFailCntReset(memberVO);
	}
	
	@Override
	public String uploadImage(MultipartFile file, String memberId) {
		String result = "Success";
		
		// 파일 업로드하고, DB에 저장할 파일명 리턴받기
		String imagePath = "none";
		try {
			imagePath = attachmentService.uploadImage(file, "myProfile");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// DB에 반영
		MemberVO memberVO = new MemberVO();
		memberVO.setMemberId(memberId);
		memberVO.setProfileImg(imagePath);
		
		memberMapper.updateMemberInfo(memberVO);
		
		if(imagePath == "none") result = "Fail";
		
		return "{\"result\":\"" + result + "\"}";
	}

	/**
	 * 휴대폰 변경-이미 사용중인 번호인지 체크
	 * @param memberVO
	 * @return
	 */
	@Override
	public boolean phoneCheck(MemberVO memberVO) {
		MemberVO vo = new MemberVO();
		vo = memberMapper.checkUserPhone(memberVO);
		if(vo != null) return false;
		
		return true;
	}
}
