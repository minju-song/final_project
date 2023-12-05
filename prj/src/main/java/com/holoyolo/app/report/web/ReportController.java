package com.holoyolo.app.report.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	// 신고 단건조회 - 이동
	@GetMapping("/admin/report/detail")
	public String selectReportInfo(ReportVO reportVO, Model model) {
		Map<String,Object> reportInfo = reportService.selectReportInfo(reportVO);
		model.addAttribute("reportInfo", reportInfo.get("reportInfo"));
		return "admin/report/reportDetail";
	}
	
	// 신고 단건조회 - 처리
	@GetMapping("/admin/report/detail/{reportId}")
	@ResponseBody
	public Map<String, Object> getReportDetailMapAjax(ReportVO reportVO) {
		Map<String,Object> reportInfo = reportService.selectReportInfo(reportVO);
		return reportInfo;
	}
	
	// 신고사유 수정
	@PutMapping("/admin/report/detail/{reportId}")
	@ResponseBody
	public Map<String, Object> reportReasonUpdate(@PathVariable("reportId") int reportId,
												  @RequestBody ReportVO reportVO) {
		reportVO.setReportId(reportId);
		return reportService.updateReportReason(reportVO);
	}
	
}
