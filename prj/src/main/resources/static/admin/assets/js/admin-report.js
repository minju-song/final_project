import colorMapping from './admin-common.js';
//console.log(window.location.pathname) 현재 경로
console.log("admin-report.js 작업중");



let pageNum = 0;
let pageSize = 0; //페이지 번호 수
let pageUnit = 0;  //한페이지에 출력할 행의 수

function renderReportList(reportProcessType, page) {
	$("tbody").empty();
	$.ajax({
		url: "/admin/report/list",
		data: { reportProcessType, page,pageUnit },
		method: "GET"
	})
		.done(function(data) {

			// 목록출력 
			let reportData = data.list;
			// 개수출력
			$("#initReportCount").text(`${reportData.length}개`)
			console.log(reportData);
			$.each(reportData, function(index, item) {
				let template = `<tr>
                                <td>${item.reportId}<i class="fab fa-angular fa-lg text-danger me-3"></i></td>
                                <td>${item.reporterId}</td>
                                <td class="cursor-pointer" name="goReportDetail">${item.reportContent}</td>
                                <td><span id="badgeColor" class="badge ${colorMapping[item.reportType]} me-1">${item.reportType}</span></td>
                                <td>${item.reportDate}</td>
                                <td><span class="badge ${colorMapping[item.reportProcessType]} me-1">${item.reportProcessType}</span></td>
                                <td>
                                    <div class="dropdown">
                                        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                            <i class="bx bx-dots-vertical-rounded"></i>
                                        </button>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="javascript:void(0);"><i class="bx bx-edit-alt me-1"></i> 수정</a>
                                            <a class="dropdown-item" href="javascript:void(0);"><i class="bx bx-trash me-1"></i> 삭제</a>
                                        </div>
                                    </div>
                                </td>
                            </tr>`;
				$('tbody').append(template);

				//페이징
				showPage(data.count);
				//게시물수
				//검색

			});
		})
		.fail(function(error) {
			console.error("Error fetching question list: ", error);
		});
}

// 페이징
$('.pagination').on("click", "li a", function(e) {
	e.preventDefault();
	console.log('page 작업중..')

	let targetPageNum = $(this).attr("href");
	console.log("targetPageNum :: " + targetPageNum);
	pageNum = targetPageNum;

	// 현재 선택된 상태에 따라 다른 상태값을 전달
	let reportProcessType = ""; // 기본값은 전체
	if ($("button[name='rejectedReport']").hasClass("active")) {
		reportProcessType = "반려";
	} else if ($("button[name='completedReport']").hasClass("active")) {
		reportProcessType = "신고처리";
	}
	renderReportList(reportProcessType, targetPageNum);
})

pageNum = 1;
pageSize = 10.0; //페이지 번호 수
pageUnit = 8;  //한페이지에 출력할 행의 수

// 상세페이지 이동
$(document).on("click", "td[name='goReportDetail']", function(e) {
	let questionId = $(this).prev().text();
	location.href = `/admin/question/detail?questionId=${questionId}`;
});

// 전체 클릭시 신고 리스트
$(document).on("click", "button[name='totalReport']", function(e) {
	e.preventDefault();
	$("#completedReport").removeClass('active');
	$("#rejectedReport").removeClass('active');
	$("#totalReport").addClass('active');
	renderReportList("", pageNum)
})
// 반려 클릭시 신고 리스트
$(document).on("click", "button[name='rejectedReport']", function(e) {
	e.preventDefault();
	$("#totalReport").removeClass('active');
	$("#completedReport").removeClass('active');
	$("#rejectedReport").addClass('active');
	renderReportList("반려", pageNum)
})
// 신고처리 클릭시 신고 리스트
$(document).on("click", "button[name='completedReport']", function(e) {
	e.preventDefault();
	$("#totalReport").removeClass('active');
	$("#rejectedReport").removeClass('active');
	$("#completedReport").addClass('active');
	renderReportList("신고처리", pageNum)
})

function showPage(replyCnt) {

	var endNum = Math.ceil(pageNum / pageSize) * pageSize;
	var startNum = endNum > pageSize ? endNum - pageSize : 1;

	var prev = startNum != 1;
	var next = false;

	if (endNum * 10 >= replyCnt) {
		endNum = Math.ceil(replyCnt / pageUnit);
	}

	if (endNum * 10 < replyCnt) {
		next = true;
	}

	var str = "";

	if (prev) {
		str += "<li class='page-item'><a class='page-link' href='" + (startNum - 1) + "'>Previous</a></li>";
	}

	for (var i = startNum; i <= endNum; i++) {
		var active = pageNum == i ? "active" : "";
		str += "<li class='page-item " + active + "'> <a class='page-link' href='" + i + "'>" + i + "</a></li>";
	}

	if (next) {
		str += "<li class='page-item'><a class='page-link' href='" + (endNum + 1) + "'>Next</a><li>";
	}

	str += "";


	$('.pagination').html(str);
}
// init
$(document).ready(function() {
	renderReportList();
})