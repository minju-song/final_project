package com.holoyolo.app.report.service;

import java.util.List;
import java.util.Map;

import com.holoyolo.app.board.service.BoardVO;
import com.holoyolo.app.member.service.MemberVO;

public interface ReportService {
	// 기본 CRUD
	// 신고 전체조회
	public List<ReportVO> selectReportTotalList(ReportVO reportVO);
	
	// 신고 단건조회
	public Map<String, Object> selectReportInfo(ReportVO reportVO);
	
	// 신고 등록
	public int insertReportInfo(ReportVO reportVO);
	
	// 신고사유 수정
	public Map<String, Object> updateReportReason(ReportVO reportVO);
	
	// 문의 삭제
	public boolean deleteReportInfo(int reportId);

	public int selectReportTotalCount(ReportVO reportVO);
	
	// 추가 인터페이스 작성 ↓↓
	
	//신고 등록
	public int insertReport(ReportVO reportVO);
	
	// 신고 횟수 ++
	public int updateMemberReportCnt(String reportedId);

	// 신고 횟수 초기화
	public int updateMemberReportCntReset(String reportedId);

	public int updateMemberInfo(MemberVO memberVO);
}
