package com.holoyolo.app.report.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.board.service.BoardVO;
import com.holoyolo.app.member.mapper.MemberMapper;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.report.mapper.ReportMapper;
import com.holoyolo.app.report.service.ReportService;
import com.holoyolo.app.report.service.ReportVO;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired // 매퍼 주입
	ReportMapper reportMapper;
	
	@Autowired
	MemberMapper memberMapper;

	// 신고 전체조회
	@Override
	public List<ReportVO> selectReportTotalList(ReportVO reportVO) {
		return reportMapper.selectReportTotalList(reportVO);
	}

	// 신고 단건조회
	@Override
	public Map<String, Object> selectReportInfo(ReportVO reportVO) {
		Map<String, Object> result = new HashMap<>();

		ReportVO findReportVO = reportMapper.selectReportInfo(reportVO);
		result.put("reportInfo", findReportVO);
		
		// 신고된 게시글의 정보 가져오기
		BoardVO boardInfo = reportMapper.selectReportBoardInfo(findReportVO);
		if (boardInfo == null) {
			boardInfo = new BoardVO();
			boardInfo.setTitle("삭제된 게시글 입니다.");
		}
		result.put("boardInfo", boardInfo);

		return result;
	}

	// 신고 등록
	@Override
	public int insertReportInfo(ReportVO reportVO) {
		System.out.println(reportVO);
		int result = reportMapper.insertReportInfo(reportVO);

		if (result == 1) {
			return result;
		} else {
			return -1;
		}
	}

	@Override
	public boolean deleteReportInfo(int reportId) {
		return false;
	}

	// 신고사유 수정
	@Override
	public Map<String, Object> updateReportReason(ReportVO reportVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;

		int result = reportMapper.updateReportReason(reportVO);
		if (result == 1) {
			isSuccessed = true;
		}
		map.put("result", isSuccessed);
		map.put("target", reportVO);

		return map;
	}

	// 신고 개수
	@Override
	public int selectReportTotalCount(ReportVO reportVO) {
		return reportMapper.selectReportTotalCount(reportVO);
	}

	//신고 등록
	@Override
	public int insertReport(ReportVO reportVO) {
		int result = reportMapper.insertReport(reportVO);

		if (result == 1) {
			return result;
		} else {
			return -1;
		}
	}
	
	@Override
	public int updateMemberReportCnt(String reportedId) {
		return reportMapper.updateMemberReportCnt(reportedId);
	}

	@Override
	public int updateMemberReportCntReset(String reportedId) {
		return reportMapper.updateMemberReportCntReset(reportedId);
	}

	@Override
	public int updateMemberInfo(MemberVO memberVO) {
		return memberMapper.updateMemberInfo(memberVO);
	}

}
