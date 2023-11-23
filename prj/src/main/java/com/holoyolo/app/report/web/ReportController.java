package com.holoyolo.app.report.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.holoyolo.app.report.service.ReportService;
import com.holoyolo.app.report.service.ReportVO;

public class ReportController {
	
	@Autowired
	ReportService reportService;
	
	@GetMapping("/admin/report")
	public String selectReportList(Model model) {
		List<ReportVO> list = reportService.selectReportAll();
		model.addAttribute("reportList", list);
		return "admin/reportMgt";
	}
}
