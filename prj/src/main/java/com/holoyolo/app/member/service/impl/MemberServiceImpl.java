package com.holoyolo.app.member.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.member.mapper.MemberMapper;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberMapper memberMapper;
	
	@Override
	public int joinUser(MemberVO memberVO) {
		return memberMapper.joinUser(memberVO);
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
	public MemberVO checkMemberId(MemberVO memberVO) {
		return memberMapper.checkMemberId(memberVO);
	}

	@Override
	public MemberVO checkNickname(MemberVO memberVO) {
		return memberMapper.checkNickname(memberVO);
	}

	@Override
	public MemberVO findMemberIdPwd(MemberVO memberVO) {
		return memberMapper.findMemberIdPwd(memberVO);
	}
	
}
