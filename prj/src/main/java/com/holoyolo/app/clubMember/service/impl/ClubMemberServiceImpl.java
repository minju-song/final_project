package com.holoyolo.app.clubMember.service.impl;

import java.util.List;

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
	public int checkMyClub(ClubMemberVO vo) {
		return clubMemberMapper.checkMyClub(vo);
	}


}
