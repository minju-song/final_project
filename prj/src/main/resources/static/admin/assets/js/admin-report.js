// import colorMapping from './admin-common.js';
//console.log(window.location.pathname) 현재 경로
console.log("admin-report.js 작업중");

$(document).ready(function () {
	function renderReportProcessForm(status, type) {
		$("#reportArea").empty();
		console.log(window.location.search.split('=')[1])
		let reportId = window.location.search.split('=')[1];

		$.ajax({
			url: `/admin/report/detail/${reportId}`,
			method: "GET",
			data: { reportId },
			success: function (data) {
			console.log(data);
				let originContent = data.reportInfo.processComment
				let reporterId = data.reportInfo.reporterId
				let reportedId = data.reportInfo.reportedId
				let menuType = data.reportInfo.menuType
				console.log(originContent)
				let template = `
			<form id="reportForm">
				<input type="hidden" name="reportId" value="${reportId}"/> 
				<input type="hidden" name="reportProcessType" value="${type}"/> 
				<input type="hidden" name="reporterId" value="${reporterId}"/> 
				<input type="hidden" name="reportedId" value="${reportedId}"/> 
				<input type="hidden" name="menuType" value="${menuType}"/> 
				<span class="fw-bold">${status}처리사유 작성</span>
				<div class="mt-2">
					<textarea class="form-control" id="processComment"
						name="processComment" placeholder="내용을 입력해주세요." rows="15" value="${originContent != null ? originContent : ''}">${originContent != null ? originContent : ''}</textarea>
				</div>
				<div id="reportFormBtnGroup" class="mt-2 position-absolute end-0">
					<button id="reportFormResetBtn" type="reset" class="btn btn-primary me-4">취소</button>
				</div>
			</form>
	`
				$("#reportArea").append(template);

				// textarea 입력시 처리
				$("#processComment").keydown(function (e) {
					$("#reportFormBtnGroup").empty();
					$("#reportFormResetBtn").remove();
					let template = `<button id="reportFormSubmitBtn" type="submit" class="btn btn-primary me-4">보내기</button>`;
					$("#reportFormBtnGroup").append(template);
				});
			},
			error: function (error) {
				console.error('Error in AJAX request:', error);
			}
		});

		// 취소 클릭
		$(document).on("click", "#reportFormResetBtn", function () {
			$("#reportForm").remove();
			$.ajax({
				url: `/admin/report/detail/${reportId}`,
				contentType: "application/json",
				data: { reportId },
				method: "GET"
			})
				.done(function (data) {
					console.log(data.reportInfo)
					let comment = data.reportInfo.processComment
					let type = data.reportInfo.reportProcessType
					$("#reportArea").empty();
					let template = reportCommentTemplate(type, comment)
					$("#reportArea").append(template);
				})
				.fail(function (error) {
					console.error("Error fetching question list: ", error);
				})
		})

		// 신고처리사유 보내기 클릭
		$(document).on("click", "#reportFormSubmitBtn", function () {
			let id = window.location.search.split('=')[1];
			let formData = getUpdateInputForm();
			$.ajax({
				url: `/admin/report/detail/${id}`,
				contentType: "application/json",
				data: JSON.stringify(formData),
				method: "PUT"
			})
				.done((data) => {
					let type = formData.reportProcessType
					let comment = formData.processComment
					let reporterId = formData.reporterId;
					let reportedId = formData.reportedId;
					let menuType = formData.menuType;
					console.log(menuType)

					// 템플릿 활용
					let template = reportCommentTemplate(type, comment)
					
					
					if(type == "SB1"){
					// 1. 관련 회원이 해당페이지 접근시
					// reporterId 신고자 아이디
					
					//유저가
					// 2. 신고당한 회원 메세지
					// 3. 신고한 회원에게 메세지
					// 4. 신고당한 회원 신고횟수 +1
					
					}

					$("#reportArea").empty().append(template)
					
					
				})
				.fail(function (error) {
					console.error("Error fetching question list: ", error);
				})
			$("#reportArea").empty();
			console.log(type)
		})
		// 신고처리내용 템플릿
				function reportCommentTemplate(type, comment) {
			return `
					<div>
						<h5 class="fw-bold badge bg-label-${type == '신고처리' ? 'info' : 'danger'}">해당 신고는 아래 사유로
							${type == '신고처리' ? '신고' : '반려'}처리 됨</h5>
						<p>${comment}</p>
					</div>
				  `
		}
	}

	let status = ""
	let type = ""

	$(document).on("click", "#completedBtn", function (e) {
		status = "신고"
		type = "SB1"
		renderReportProcessForm(status, type);
	})

	$(document).on("click", "#rejectedBtn", function (e) {
		status = "반려"
		type = "SB2"
		renderReportProcessForm(status, type);
	})


	// 수정된 formData
	function getUpdateInputForm() {
		let target = event.target
		let formData = $(target).closest('#reportForm').serializeArray(); // 폼 태그 전용 메서드
		let objData = {};
		$.each(formData, (idx, obj) => {
			objData[obj.name] = obj.value;
		});
		return objData;
	}



	let pageNum = 0;
	let pageSize = 0; //페이지 번호 수
	let pageUnit = 0;  //한페이지에 출력할 행의 수

	function renderReportList(reportProcessType, page, search) {
		$("tbody").empty();
		$.ajax({
			url: "/admin/report/list",
			data: { reportProcessType, page, search, pageUnit },
			method: "GET"
		})
			.done(function (data) {
				console.log(data);
				// 목록출력 
				let reportData = data.list;
				// 개수출력
				$("#initReportCount").text(data.count);
				console.log(reportData);
				$.each(reportData, function (index, item) {
					let template = `<tr>
                                <td>${item.reportId}<i class="fab fa-angular fa-lg text-danger me-3"></i></td>
                                <td>${item.reporterId}</td>
                                <td class="cursor-pointer fw-bold" name="goReportDetail">${item.reportContent}</td>
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
			.fail(function (error) {
				console.error("Error fetching question list: ", error);
			});
	}

	// 페이징
	$('.pagination').on("click", "li a", function (e) {
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
	$(document).on("click", "td[name='goReportDetail']", function (e) {
		let reportId = $(this).prev().prev().text();
		location.href = `/admin/report/detail?reportId=${reportId}`;
	});

	// 전체 클릭시 신고 리스트
	$(document).on("click", "button[name='totalReport']", function (e) {
		pageNum = 1;
		reportType = "";
		search = "";
		$("#searchInput").val("");
	
		$("#completedReport, #rejectedReport").removeClass('active');
		$("#totalReport").addClass('active');
		renderReportList(reportType, pageNum, search)
	})
	// 반려 클릭시 신고 리스트
	$(document).on("click", "button[name='rejectedReport']", function (e) {
		pageNum = 1;
		reportType = "반려";
		search = "";
		$("#searchInput").val("");
		
		$("#totalReport, #completedReport").removeClass('active');
		$("#rejectedReport").addClass('active');
		renderReportList(reportType, pageNum, search)
	})
	// 신고처리 클릭시 신고 리스트
	$(document).on("click", "button[name='completedReport']", function (e) {
		pageNum = 1;
		reportType = "신고처리";
		search = "";
		$("#searchInput").val("");
	
		$("#totalReport, #rejectedReport").removeClass('active');
		$("#completedReport").addClass('active');
		renderReportList(reportType, pageNum, search)
	})
	
	// 검색
	let searchTimer;
	$(document).on("keyup", '#searchInput', function () {
		clearTimeout(searchTimer);

		searchTimer = setTimeout(function () {
			pageNum = 1;
			search = $("#searchInput").val();

			if ($("button[name='rejectedReport']").hasClass("active")) {
				reportType = "반려";
			} else if ($("button[name='completedReport']").hasClass("active")) {
				reportType = "신고처리";
			} else {
				reportType = "";
			}
			renderReportList(reportType, pageNum, search)
		}, 300); // 300 밀리초 (0.3초) 후에 검색 실행
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
	renderReportList();
});