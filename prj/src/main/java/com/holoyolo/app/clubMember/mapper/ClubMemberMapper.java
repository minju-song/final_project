package com.holoyolo.app.clubMember.mapper;

import java.util.List;
import java.util.Map;

import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubMember.service.ClubMemberVO;

public interface ClubMemberMapper {
	
	//해당 회원이 가입한 클럽 리스트
	public List<ClubMemberVO> getClubJoin(ClubMemberVO vo);
	
	//즉시 클럽가입
	public int joinClub(ClubMemberVO vo);
	
	//클럽가입신청
	public int reqClub(ClubMemberVO vo);
	
	//클럽가입승인
	public int acceptClub(ClubMemberVO vo);
	
	//현재 가입자수 조회
	public int countMember(int clubId);
	
	//해당 클럽의 회원리스트
	public List<ClubMemberVO> getMembers(ClubVO vo);
	
	//내가 가입한 클럽인지 확인
	public ClubMemberVO checkMyClub(ClubMemberVO vo);
	
	//클럽탈퇴
	public int outClubMember(ClubMemberVO vo);
	
	//가입승인받을 수 있는지 체크
	public Map<String, Object> checkAccept(ClubMemberVO vo);
	
	//클럽재가입승낙
	public int reJoin(ClubMemberVO vo);
	
	//클럽재가입신청
	public int reqRejoin(ClubMemberVO vo);
}
