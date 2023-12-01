package com.holoyolo.app.clubMember.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubMember.mapper.ClubMemberMapper;
import com.holoyolo.app.clubMember.service.ClubMemberService;
import com.holoyolo.app.clubMember.service.ClubMemberVO;

@Service
public class ClubMemberServiceImpl implements ClubMemberService {
	
	@Autowired
	ClubMemberMapper clubMemberMapper;

	@Override
	public List<ClubMemberVO> getClubJoin(String id) {
		ClubMemberVO vo = new ClubMemberVO();
		
		vo.setMemberId(id);
		
		if(clubMemberMapper.getClubJoin(vo).isEmpty()) {
			return null;
		}
		else {			
			List<ClubMemberVO> list = clubMemberMapper.getClubJoin(vo);
			return list;
		}
	}

	@Override
	public int joinClub(ClubMemberVO vo) {
		return clubMemberMapper.joinClub(vo);
	}


	@Override
	public ClubMemberVO checkMyClub(ClubMemberVO vo) {
		return clubMemberMapper.checkMyClub(vo);
	}

	@Override
	public int reqClub(ClubMemberVO vo) {
		return clubMemberMapper.reqClub(vo);
	}

	@Override
	public String acceptClub(ClubMemberVO vo) {
		Map<String, Object> ck = new HashMap<>();
		ck = clubMemberMapper.checkAccept(vo);
		System.out.println("확인"+ck);
		System.out.println("확인"+ck.get("COUNT"));
		if(Integer.parseInt(String.valueOf(ck.get("COUNT"))) < Integer.parseInt(String.valueOf(ck.get("PEOPLE")))) {
			clubMemberMapper.acceptClub(vo);
			return "success";
		}
		else {
			return "fail";
		}
	}

	@Override
	public int outClubMember(ClubMemberVO vo) {
		return clubMemberMapper.outClubMember(vo);
	}

	@Override
	public int reJoin(ClubMemberVO vo) {
		return clubMemberMapper.reJoin(vo);
	}
	
	//클럽재가입신청
	public int reqRejoin(ClubMemberVO vo) {
		return clubMemberMapper.reqRejoin(vo);
	}


}
