package com.holoyolo.app.club.service.impl;

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
	public List<ClubVO> selectClubAll() {
		return clubMapper.selectClubAll();
	}

	// 모임 단건조회
	@Override
	public ClubVO selectClubInfo(ClubVO clubVO) {
		return clubMapper.selectClubInfo(clubVO);
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
	@Override
	public Map<String, Object> updateClubInfo(ClubVO clubVO) {
		return null;
	}
	
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
//		for(int i = 0; i < memberList.size(); i++) {
//			ClubMemberVO temp = memberList.get(i);
//			if(clubSuccessHistoryMemberMapper.getRankingLast(temp) != null) {
//				temp.setRanking(clubSuccessHistoryMemberMapper.getRankingLast(temp).getRanking());
//				memberList.add(i, temp);
//				
//			}
//		}
		map.put("members", memberList);
		
		//클럽의 평균 성공률
		Double avgSuccess = clubSuccessHistoryMapper.getSuccessPct(vo);
		map.put("avg", avgSuccess);
		
		ClubBudgetVO budget = clubBudgetMapper.getClubBudget(vo.getClubId());
		if(budget != null) {			
			if(budget.getClubBudgetUnit().equals("YA1")) budget.setClubBudgetUnit("일");
			else if(budget.getClubBudgetUnit().equals("YA2")) budget.setClubBudgetUnit("주");
			else budget.setClubBudgetUnit("월");
			map.put("budget", budget);
		}
		else {
			map.put("budget", null);
		}
		
		return map;
	}

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
		
		map.put("length", list.size());
		map.put("result", list);
		
		return map;
	}

	
	
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

	@Override
	public String insertClub(ClubVO vo) {
		if(clubMapper.insertClub(vo)>0) {
			int clubId = vo.getClubId();
			ClubBudgetVO budVO = new ClubBudgetVO();
			budVO.setClubBudgetPrice(vo.getPrice());
			budVO.setClubBudgetUnit(vo.getUnit());
			budVO.setClubId(clubId);
			if(clubBudgetMapper.insertClubBudget(budVO) > 0) {
				return "success";
			}
			return "budgetFail";
		}
		return "fail";
	}






}
