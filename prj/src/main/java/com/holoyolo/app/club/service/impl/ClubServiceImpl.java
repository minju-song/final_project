package com.holoyolo.app.club.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.holoyolo.app.club.mapper.ClubMapper;
import com.holoyolo.app.club.service.ClubService;
import com.holoyolo.app.club.service.ClubVO;
import com.holoyolo.app.clubBudget.mapper.ClubBudgetMapper;
import com.holoyolo.app.clubBudget.service.ClubBudgetVO;
import com.holoyolo.app.clubMember.mapper.ClubMemberMapper;
import com.holoyolo.app.clubMember.service.ClubMemberService;
import com.holoyolo.app.clubMember.service.ClubMemberVO;
import com.holoyolo.app.clubSuccessHistory.mapper.ClubSuccessHistoryMapper;
import com.holoyolo.app.clubSuccessHistory.service.ClubSuccessHistoryVO;
import com.holoyolo.app.clubSuccessHistoryMember.mapper.ClubSuccessHistoryMemberMapper;

@Service
public class ClubServiceImpl implements ClubService {
	
	@Value("${file.loading.path}")
    private String loadingPath;
	
	@Autowired
	ClubMapper clubMapper;
	
	@Autowired
	ClubMemberMapper clubMemberMapper;
	
	@Autowired
	ClubSuccessHistoryMapper clubSuccessHistoryMapper;
	
	@Autowired
	ClubSuccessHistoryMemberMapper clubSuccessHistoryMemberMapper;
	
	@Autowired
	ClubBudgetMapper clubBudgetMapper;
	
	
	@Autowired
	ClubMemberService clubMemberService;


	// 모임 전체조회
	@Override
	public Map<String, Object> selectClubAll(ClubVO clubVO) {
		Map<String, Object> map = new HashMap<>();
		List<ClubVO> list = clubMapper.selectClubAll(clubVO);
		
		for(int i=0; i<list.size(); i++) {
			ClubVO temp = new ClubVO();
			temp = list.get(i);
			temp.setJoinCnt(clubMemberMapper.countMember(list.get(i).getClubId()));
			temp.setSuccessCnt(clubSuccessHistoryMapper.getSuccessCount(list.get(i).getClubId()));
			list.set(i, temp);
		}
		
		map.put("result", list);
		
		return map;
	}

	// 모임 단건조회
	@Override
	public Map<String, Object> getClubDetail(ClubVO clubVO) {
		Map<String, Object> result = new HashMap<>();
		
		ClubVO findClubVO = clubMapper.getClubDetail(clubVO);
		
		result.put("clubInfo", findClubVO);
		
		return result;
	}
	
	// 모임 등록
	@Override
	public int insertClubInfo(ClubVO clubVO) {
		int result = clubMapper.insertClubInfo(clubVO);
		
		if (result == 1) {
			return clubVO.getClubId();
		} else {
			return -1;
		}
	}
	
	// 모임 수정
//	@Override
//	public Map<String, Object> updateClubInfo(ClubVO clubVO) {
//		return null;
//	}
	
	// 모임 삭제
	@Override
	public boolean deleteClubInfo(int clubId) {
		return false;
	}


	@Override
	public int cntData(ClubVO vo) {
		return clubMapper.cntData(vo);
	}

	@Override
	public Map<String, Object> getClubPage(ClubVO vo) {
		Map<String, Object> map = new HashMap<>();
		//클럽 전체 정보 얻어옴
		vo = clubMapper.getClub(vo);
		//클럽가입자수 계산
		vo.setJoinCnt(clubMemberMapper.countMember(vo.getClubId()));
		map.put("club", vo);
		
		//가입한 회원목록
		List<ClubMemberVO> memberList = clubMemberMapper.getMembers(vo);
		map.put("members", memberList);
		
		//클럽의 평균 성공률
		Double avgSuccess = clubSuccessHistoryMapper.getSuccessPct(vo);
		map.put("avg", avgSuccess);
		
		//클럽의 현재 예산정보
		ClubBudgetVO budget = clubBudgetMapper.getClubBudget(vo.getClubId());
		map.put("budget", budget);
		
		
		//클럽의 현재 진행중인 기간
		ClubSuccessHistoryVO csvo = new ClubSuccessHistoryVO();
		csvo = clubSuccessHistoryMapper.getIng(vo.getClubId());
		System.out.println("언제까지?"+csvo);
		if(csvo != null) {			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			map.put("start",simpleDateFormat.format(csvo.getStartDate()));
			map.put("end",simpleDateFormat.format(csvo.getEndDate()));
		}
		else {
			map.put("start","없음");
			map.put("end","없음");
		}
		return map;
	}

	//클럽페이징
	@Override
	public Map<String, Object> clubPaging(ClubVO vo) {
		Map<String, Object> map = new HashMap<>();
		List<ClubVO> list = clubMapper.getClubList(vo);
		
		for(int i=0; i<list.size(); i++) {
			ClubVO temp = new ClubVO();
			temp = list.get(i);
			temp.setJoinCnt(clubMemberMapper.countMember(list.get(i).getClubId()));
//			temp.setClubProfileImg(loadingPath+"club/profile/"+list.get(i).getClubProfileImg());
			list.set(i, temp);
		}

		map.put("result", list);
		
		return map;
	}

	
	//클럽목록리스트
	@Override
	public Map<String, Object> clubListPage(String memberId) {
		Map<String, Object> map = new HashMap<>();
		
		List<ClubVO> list = clubMapper.getAllClubList();
		map.put("list", list);
		
		if(memberId != "null") {
			List<ClubMemberVO> clubJoinlist = clubMemberService.getClubJoin(memberId);
			map.put("joinlist", clubJoinlist);
			map.put("userId", memberId);
		}
		else {
			map.put("joinlist", "null");
			map.put("userId", memberId);
		}
		return map;
	}

	//클럽생성
	@Override
	public String insertClub(ClubVO vo) {
		//클럽생성함
		if(clubMapper.insertClub(vo)>0) {
			int clubId = vo.getClubId();
			
			//멤버-클럽 테이블에 리더 가입시킴
			ClubMemberVO cmVO = new ClubMemberVO();
			cmVO.setClubId(clubId);
			cmVO.setMemberId(vo.getClubLeader());
			clubMemberMapper.joinClub(cmVO);
			
			//생성 시 받은 예산 등록
			ClubBudgetVO budVO = new ClubBudgetVO();
			
			//예산 금액, 기간, 클럽아이디 설정
			budVO.setClubBudgetPrice(vo.getPrice());
			budVO.setClubBudgetUnit(vo.getUnit());
			budVO.setClubId(clubId);
			if(clubBudgetMapper.insertClubBudget(budVO) > 0) {
				if(clubSuccessHistoryMapper.insertSuccessIng(budVO) > 0) return "success";
			}
			return "budgetFail";
		}
		return "fail";
	}

	//모임장위임
	@Override
	public String mandateKing(ClubVO vo) {
		if(clubMapper.mandateKing(vo) > 0) {
			return "success";
		}
		else {
			return "fail";
		}

	}

	//클럽단건조회
	@Override
	public ClubVO getClub(int id) {
		ClubVO vo = new ClubVO();
		vo.setClubId(id);
		return clubMapper.getClub(vo);
	}

	//클럽기본정보업데이트
	@Override
	public int updateClubInfo(ClubVO vo) {
		return clubMapper.updateClubInfo(vo);
	}

	//클럽프로필업데이트
	@Override
	public int updateClubProfile(ClubVO vo) {
		return clubMapper.updateClubProfile(vo);
	}

	//클럽삭제
	@Override
	public int delectClub(ClubVO vo) {
		return clubMapper.delectClub(vo);
	}

	@Override
	public List<ClubVO> bestClubList(ClubVO vo) {
		return clubMapper.bestClubList(vo);
	}

	//마이페이지 나의 클럽
	@Override
	public Map<String, Object> getMyClub(String id) {
		Map<String, Object> map = new HashMap<>();
		
		List<ClubVO> list =  clubMapper.getMyClub(id);
		map.put("clubs", list);
		return map;
	}






}
