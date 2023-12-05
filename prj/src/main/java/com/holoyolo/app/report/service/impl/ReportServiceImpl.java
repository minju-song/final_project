package com.holoyolo.app.report.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.report.mapper.ReportMapper;
import com.holoyolo.app.report.service.ReportService;
import com.holoyolo.app.report.service.ReportVO;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired // 매퍼 주입
	ReportMapper reportMapper;

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
		
		return result;
	}
	
	// 신고 등록
	@Override
	public int insertReportInfo(ReportVO reportVO) {
		int result = reportMapper.insertReportInfo(reportVO);
		
		if (result == 1) {
			return reportVO.getReportId();
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

}
