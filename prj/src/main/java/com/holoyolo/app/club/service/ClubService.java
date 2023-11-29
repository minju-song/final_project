package com.holoyolo.app.club.service;

import java.util.List;

import java.util.Map;

public interface ClubService {
	
	// 기본 CRUD
	// 모임 전체조회
	public List<ClubVO> selectClubAll();
	
	// 모임 단건조회
	public ClubVO selectClubInfo(ClubVO clubVO);
	
	// 모임 등록
	public int insertClubInfo(ClubVO clubVO);
	
	// 모임 수정
	public Map<String, Object> updateClubInfo(ClubVO clubVO);
	
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

}
