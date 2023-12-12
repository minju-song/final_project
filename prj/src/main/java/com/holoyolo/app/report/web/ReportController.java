package com.holoyolo.app.report.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holoyolo.app.auth.PrincipalDetails;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;
import com.holoyolo.app.report.service.ReportService;
import com.holoyolo.app.report.service.ReportVO;

@Controller
public class ReportController {

	@Autowired
	ReportService reportService;
	
	@Autowired
	MemberService memberService;

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
		Map<String, Object> reportInfo = reportService.selectReportInfo(reportVO);
		model.addAttribute("reportInfo", reportInfo.get("reportInfo"));
		return "admin/report/reportDetail";
	}

	// 신고 단건조회 - 처리

	@GetMapping("/admin/report/detail/{reportId}")
	@ResponseBody
	public Map<String, Object> getReportDetailMapAjax(ReportVO reportVO) {
		Map<String, Object> reportInfo = new HashMap<>();
		if(reportInfo != null) {
		 reportInfo = reportService.selectReportInfo(reportVO);
		}
		
		return reportInfo;
	}

	// 신고사유 반영
	@PutMapping("/admin/report/detail/{reportId}")
	@ResponseBody
	public Map<String, Object> reportReasonUpdate(@PathVariable("reportId") int reportId,
			@RequestBody ReportVO reportVO, MemberVO memberVO) {
		Map<String, Object> map = new HashMap<>();
		System.out.println("신고 정보 : " + reportVO);
		reportVO.setReportId(reportId);
		// 신고처리유형 : SB1(신고처리), SB2(반려)
		String reportType = reportVO.getReportProcessType();
		
		//신고당한 아이디
		String reportedId = reportVO.getReportedId();
		memberVO.setMemberId(reportedId);
		int reportedCnt = memberService.findById(reportedId).getReportCnt();
		System.out.println("111111111111111111===" + reportedCnt);
		MemberVO reportedRole = new MemberVO();
		
		if (reportType == "SB1") {
			if (reportedCnt == -1 || reportedCnt == 0) {
				// 신고카운트가 -1 또는 0이면 신고초기화
				reportService.updateMemberReportCntReset(reportedId);
				reportService.updateMemberReportCnt(reportedId);
			} else {
				reportedRole.setMemberId(reportedId);
		        // 신고횟수 10회 이상
		        if (reportedCnt >= 10) {
		            Date now = new Date();
		            reportedRole.setStopDate(now);
		        }
		        // 신고횟수 5회 이상
		        else if (reportedCnt >= 5) {
		        	reportedRole.setRole("ROLE_HA3");
		            memberService.addMonth(memberVO);
		        }
		        // 신고횟수 3회 이상
		        else if (reportedCnt >= 3) {
		            reportedRole.setRole("ROLE_HA2");
		        }
		        System.out.println(reportedRole);
		        // 날짜, 롤 업데이트
		        memberService.updateMemberInfo(reportedRole);
		        // 신고 횟수 증가
				reportService.updateMemberReportCnt(reportedId);
				reportService.updateReportReason(reportVO);
			}
		} else {
			reportService.updateReportReason(reportVO);
		}
		
		
		return map;
	}

	@PostMapping("/insertReport")
	@ResponseBody
	public Map<String, Object> insertReport(@RequestBody ReportVO reportVO,
			@AuthenticationPrincipal PrincipalDetails prd) {
		reportVO.setReporterId((String) prd.getUsername());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (reportService.insertReportInfo(reportVO) == 1) {
			resultMap.put("resultMsg", "신고가 접수되었습니다");
		}
		;

		return resultMap;
	}

	@PostMapping("member/insertReport")
	@ResponseBody
	public int insertReport(@AuthenticationPrincipal PrincipalDetails principalDetails,
							ReportVO reportVO) {
		reportVO.setReporterId(principalDetails.getUsername());
		return reportService.insertReport(reportVO);
	}
}
