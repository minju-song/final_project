package com.holoyolo.app.report.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.report.service.ReportService;
import com.holoyolo.app.report.service.ReportVO;

@Controller
public class ReportController {
	
	@Autowired
	ReportService reportService;
	
	// 신고 페이지 이동
	@GetMapping("/admin/report")
	public String selectReportList(Model model) {
		return "admin/report/reportPage";
	}
	
	// 신고 조건조회
	@GetMapping("/admin/report/list")
	@ResponseBody
	public Map<String, Object> getReportMapAjax(ReportVO reportVO) {
		Map<String, Object> reportMap = new HashMap<>();
		reportMap.put("reportProcessType", reportVO.getReportProcessType());
		reportMap.put("list", reportService.selectReportTotalList(reportVO));
		reportMap.put("count", reportService.selectReportTotalCount(reportVO));
		return reportMap;
	}
}
