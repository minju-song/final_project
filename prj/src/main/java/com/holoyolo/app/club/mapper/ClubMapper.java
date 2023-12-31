package com.holoyolo.app.club.mapper;

import java.util.List;
import java.util.Map;

import com.holoyolo.app.club.service.ClubVO;

public interface ClubMapper {

	// 기본 CRUD
	// 모임 전체조회
	public List<ClubVO> selectClubAll(ClubVO clubVO);
	
	// 모임 단건조회
	public ClubVO getClubDetail(ClubVO clubVO);
	
	// 모임 등록
	public int insertClubInfo(ClubVO clubVO);
	
	// 모임 삭제
	public boolean deleteClubInfo(int clubId);
	
	// 추가 인터페이스 작성 ↓↓
	
	
	//알뜰모임 목록
	public List<ClubVO> getAllClubList();
	
	//알뜰모임 목록 페이징
	public List<ClubVO> getClubList(ClubVO vo);
	
	//알뜰모임 데이터 갯수
	public int cntData(ClubVO vo);
	
	//알뜰모임 상세보기
	public ClubVO getClub(ClubVO vo);
	
	//클럽생성
	public int insertClub(ClubVO vo);

	//클럽방장위임
	public int mandateKing(ClubVO vo);
	
	//클럽기본정보수정
	public int updateClubInfo(ClubVO vo);
	
	//클럽프로필 이미지 수정
	public int updateClubProfile(ClubVO vo);
	
	//클럽삭제
	public int delectClub(ClubVO vo);
	
	//베스트 클럽리스트(메인페이지용)
	public List<ClubVO> bestClubList(ClubVO vo);
	
	//마이페이지 알뜰모임 클럽리스트
	public List<ClubVO> getMyClub(String id);
}
