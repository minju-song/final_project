package com.holoyolo.app.report.service.impl;

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

	// 문의 전체조회
	@Override
	public List<ReportVO> selectReportAll() {
		return reportMapper.selectReportAll();
	}

	// 문의 단건조회
	@Override
	public ReportVO selectReportInfo(ReportVO reportVO) {
		return reportMapper.selectReportInfo(reportVO);
	}
	
	// 문의 등록
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

	@Override
	public Map<String, Object> updateReportInfo(ReportVO reportVO) {
		// TODO Auto-generated method stub
		return null;
	}

}
