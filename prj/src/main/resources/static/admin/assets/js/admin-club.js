/**
 * 
 */
import colorMapping from './admin-common.js';
//import showPage from './admin-utils.js';

console.log("admin-club.js 작업중")

let pageNum = 0;
let pageSize = 0; //페이지 번호 수
let pageUnit = 0;  //한페이지에 출력할 행의 수

let questionYn = "";

// 진입시 총 문의 개수
$(document).ready(function () {
	let search = '';
	// 문의 리스트 포멧
	function renderClubList(openScope, page, search, clubId,memberId) {
		$("tbody").empty();
		$.ajax({
			url: "/admin/club/list",
			data: { openScope, page, search, clubId, memberId, pageUnit },
			method: "GET"
		})
			.done(function (data) {
				console.log(data.list.result)
				let listData = data.list.result;
				// 개수출력
				$("#initClubCount").text(`(${listData.length})`)
				// 목록출력 
				$.each(listData, function (index, list) {
					let template = `
								<tr>
									<input type="hidden" value="${list.clubId}"/>
									<td>${list.rn}</td>
									<td>${list.clubLeader}</td>
									<td id="goClubDetail" class="cursor-pointer fw-bold">${list.clubName}</td>
									<td class="text-center">${list.successCnt}</td>
									<td class="text-center"><span>${list.joinCnt} / </span><span >${list.clubPeople}</span></td>
									<td>${list.clubDate}</td>
									<td><span class="badge ${colorMapping[list.openScope]} me-1">${list.openScope}</span></td>
									<td>
										<div class="dropdown">
											<button type="button"
												class="btn p-0 dropdown-toggle hide-arrow"
												data-bs-toggle="dropdown">
												<i class="bx bx-dots-vertical-rounded"></i>
											</button>
											<div class="dropdown-menu">
												<a class="dropdown-item" href="javascript:void(0);"><i
													class="bx bx-edit-alt me-1"></i> 수정</a> <a
													class="dropdown-item" href="javascript:void(0);"><i
													class="bx bx-trash me-1"></i> 삭제</a>
											</div>
										</div>
									</td>
								</tr>
                            `;
					$('tbody').append(template);

					//페이징
					showPage(data.count);
					//게시물수


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
		search = "";


		// 현재 선택된 상태에 따라 다른 상태값을 전달
		let openScope = ""; // 기본값은 전체
		if ($("button[name='publicClubCount']").hasClass("active")) {
			openScope = "공개";
		} else if ($("button[name='privateClubCount']").hasClass("active")) {
			openScope = "비공개";
		}

		renderClubList(openScope, pageNum, search);
	})

	pageNum = 1;
	pageSize = 10.0; //페이지 번호 수
	pageUnit = 10;  //한페이지에 출력할 행의 수


	// 전체 클릭시 문의 리스트
	$(document).on("click", "button[name='totalClubCount']", function (e) {
		pageNum = 1;
		openScope = "";
		search = "";
		$("#searchInput").val("");
		$("#publicClubCount, #privateClubCount").removeClass('active');
		$("#totalClubCount").addClass('active');
		renderClubList(openScope, pageNum, search);
	});

	// 답변대기 클릭시 문의 리스트
	$(document).on("click", "button[name='publicClubCount']", function (e) {
		pageNum = 1;
		openScope = "공개"
		search = "";
		$("#searchInput").val("");
		$("#totalClubCount, #privateClubCount").removeClass('active');
		$("#publicClubCount").addClass('active');
		renderClubList(openScope, pageNum, search);
	});

	// 답변완료 클릭시 문의 리스트
	$(document).on("click", "button[name='privateClubCount']", function (e) {
		pageNum = 1;
		openScope = "비공개"
		search = "";
		$("#searchInput").val("");
		$("#totalClubCount, #publicClubCount").removeClass('active');
		$("#privateClubCount").addClass('active');
		renderClubList(openScope, pageNum, search);
	});
	let searchTimer;
	// 검색
	$(document).on("keyup", '#searchInput', function () {
		clearTimeout(searchTimer);

    searchTimer = setTimeout(function () {
		pageNum = 1;
		search = $("#searchInput").val();
		
		if ($("button[name='publicClubCount']").hasClass("active")) {
			openScope = "공개";
		} else if ($("button[name='privateClubCount']").hasClass("active")) {
			openScope = "비공개";
		} else {
			openScope = "";
		}
		renderClubList(openScope, pageNum, search)
		 }, 300); // 300 밀리초 (0.3초) 후에 검색 실행
	})

	// 상세페이지 이동
	$(document).on("click", "#goClubDetail", function (e) {
		let clubId = $(e.target).prev().prev().prev().val();
		location.href = `/admin/club/detail?clubId=${clubId}`;
	});

	// 페이지 버튼
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

	renderClubList("", 1, search,1,"");
})

	// 수정된 formData
	function getUpdateInputForm() {
		let target = event.target
		let formData = $(target).closest('#updateInputForm').serializeArray(); // 폼 태그 전용 메서드
		let objData = {};
		$.each(formData, (idx, obj) => {
			objData[obj.name] = obj.value;
		});
		return objData;
	}

$(document).ready(function () {
function renderClubDetail(clubId) {
		$("tbody").empty();
		$.ajax({
			url: "/admin/club/detail",
			data: { clubId },
			method: "GET"
		})
			.done(function (data) {
				console.log(data)
				let listData = data.list.result;
				// 개수출력
				$("#initClubCount").text(`(${listData.length})`)
				// 목록출력 
				$.each(listData, function (index, list) {
					let template = `
								
                            `;
					$('tbody').append(template);

					//페이징
					showPage(data.count);
					//게시물수


				});
			})
			.fail(function (error) {
				console.error("Error fetching question list: ", error);
			});

	}
	renderClubDetail(1);
	
	})