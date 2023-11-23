package com.holoyolo.app.report.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
	// 기본 CRUD
	// 문의 전체조회
	public List<ReportVO> selectReportAll();
	
	// 문의 단건조회
	public ReportVO selectReportInfo(ReportVO reportVO);
	
	// 문의 등록
	public int insertReportInfo(ReportVO reportVO);
	
	// 문의 수정
	public Map<String, Object> updateReportInfo(ReportVO reportVO);
	
	// 문의 삭제
	public boolean deleteReportInfo(int reportId);
	
	// 추가 인터페이스 작성 ↓↓
}
