package com.holoyolo.app.member.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.holoyolo.app.member.mapper.MemberMapper;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberMapper memberMapper;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public String joinUser(MemberVO memberVO) {
		MemberVO vo =  memberMapper.checkUserPhone(memberVO); // 회원 가입 전 인증된 번호가 있는지 확인.
		
		if(vo != null) {
			System.out.println(vo.getPhone() + " :: 이미 가입된 회원입니다.");
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
	public Map<String, Object> updateMemberInfo(MemberVO memberVO) {
		return null;
	}

	@Override
	public String checkMemberId(MemberVO memberVO) {
		String result = "NOT_FOUND";
		MemberVO vo = new MemberVO();
		System.out.println("넘어온 아이디 ::: " + memberVO.getMemberId());
		
		vo = memberMapper.checkMemberId(memberVO);
		System.out.println(memberVO.getMemberId() + "는 가입이 가능한 아이디 입니다.");
		
		if(vo != null) {
			result = "FOUND";
			System.out.println("결과는 ::: " + vo.getMemberId() + ", " + result);
		}
		
		return result;
	}

	@Override
	public String checkNickname(MemberVO memberVO) {
		String result = "NOT_FOUND";
		MemberVO vo = new MemberVO();
		System.out.println("넘어온 닉네임 ::: " + memberVO.getNickname());
		
		vo = memberMapper.checkNickname(memberVO);
		System.out.println(memberVO.getNickname() + "는 사용이 가능한 닉네임 입니다.");
		
		if(vo != null) {
			result = "FOUND";
			System.out.println("결과는 ::: " + vo.getNickname() + ", " + result);
		}
		
		return result;
	}

	@Override
	public String findMemberIdPwd(MemberVO memberVO) {
		MemberVO vo = new MemberVO();
		vo = memberMapper.findMemberIdPwd(memberVO);
		System.out.println("조회된 결과:: " + vo);
		
		if(vo != null) {
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

	
}
