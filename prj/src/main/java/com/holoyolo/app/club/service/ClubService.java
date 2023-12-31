package com.holoyolo.app.club.service;

import java.util.List;

import java.util.Map;

import com.holoyolo.app.clubMember.service.ClubMemberVO;

public interface ClubService {
	
	// 기본 CRUD
	// 모임 전체조회
	public Map<String, Object> selectClubAll(ClubVO clubVO);
	
	// 모임 단건조회
	public Map<String, Object> getClubDetail(ClubVO clubVO);
	
	// 모임 등록
	public int insertClubInfo(ClubVO clubVO);
	
	// 모임 수정
//	public Map<String, Object> updateClubInfo(ClubVO clubVO);
	
	// 모임 삭제
	public boolean deleteClubInfo(int clubId);
	
	// 추가 인터페이스 작성 ↓↓
	
	
	//알뜰모임 데이터 갯수
	public int cntData(ClubVO vo);
	
	
	//알뜰모임 클럽리스트페이지 이동
	public Map<String, Object> clubListPage(String memberId);
	
	//클럽상세페이지이동
	public Map<String, Object> getClubPage(ClubVO vo);
	
	//클럽 페이징
	public Map<String, Object> clubPaging(ClubVO vo);
	
	//클럽생성
	public String insertClub(ClubVO vo);

	//클럽방장위임
	public String mandateKing(ClubVO vo);
	
	//클럽단건조회
	public ClubVO getClub(int id);
	
	//클럽기본정보수정
	public int updateClubInfo(ClubVO vo);
	
	//클럽프로필 이미지 수정
	public int updateClubProfile(ClubVO vo);
	
	//클럽삭제

	public int delectClub(ClubVO vo);
	
	//베스트 클럽리스트(메인페이지용)
	public List<ClubVO> bestClubList(ClubVO vo);
	
	//마이페이지 나의 모임 페이지이동
	public Map<String, Object> getMyClub(String id);

}
