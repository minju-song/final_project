package com.holoyolo.app.report.mapper;

import java.util.List;
import java.util.Map;

import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.report.service.ReportVO;

public interface ReportMapper {
	// 기본 CRUD
	// 신고 전체조회
	public List<ReportVO> selectReportTotalList(ReportVO reportVO);
	
	// 신고 단건조회
	public ReportVO selectReportInfo(ReportVO reportVO);
	public Map<String, Object> selectReportProcess(int reportId);
	
	// 신고 등록
	public int insertReportInfo(ReportVO reportVO);
	
	// 신고사유 수정
	public int updateReportReason(ReportVO reportVO);
	
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
}
