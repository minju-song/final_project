//import colorMapping from './admin-common.js';

console.log("admin-member.js 작업중")

let pageNum = 0;
let pageSize = 0; //페이지 번호 수
let pageUnit = 0;  //한페이지에 출력할 행의 수

let role = '';


//진입시 멤버 조회
$(document).ready(function () {
let search = '';

	function renderMemberList(role, page, search) {
		$("tbody").empty();
		$.ajax({
			url: "/admin/member/list",
			data: { role, page, search },
			method: "GET"
		})
			.done(function (data) {
				console.log(data)
				// 개수출력
				$("#initMemberCount").text(`(${data.count})`)

				// 목록출력
				let memberData = data.list;
				$.each(memberData, function (index, list) {
					let template = `<tr>
                        <td>${list.rn}</td>
                        <td>${list.memberId}</td>
                        <td>${list.memberName}<i class="fab fa-angular fa-lg text-danger me-3"></i></td>
                        <td>${list.phone}</td>
                        <td>${list.addr}</td>
                        <td>${list.joinDate}</td>
                        <td><span class="badge ${colorMapping[list.role]} me-1">${list.role}</span></td>
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

					// 페이징
					showPage(data.count);

				})
			})
	}
	// 페이징
	$(document).on("click", ".pagination li a", function (e) {
		e.preventDefault();
		console.log('page 작업중..')

		let targetPageNum = $(this).attr("href");
		console.log("targetPageNum :: " + targetPageNum);
		pageNum = targetPageNum;
		search = "";


		// 현재 선택된 상태에 따라 다른 상태값을 전달
		let role = ""; // 기본값은 전체
		if ($("button[name='generalMemberCount']").hasClass("active")) {
			role = "일반회원";
		} else if ($("button[name='transactionSuspensionCount']").hasClass("active")) {
			role = "거래정지";
		} else if ($("button[name='accountSuspensionCount']").hasClass("active")) {
			role = "계정정지";
		}

		renderMemberList(role, targetPageNum, search);
	})
	pageNum = 1;
	pageSize = 10.0; //페이지 번호 수
	pageUnit = 10;  //한페이지에 출력할 행의 수

	// 클릭시 회원리스트
	// 1. 전체
	$(document).on("click", "button[name='totalMemberCount']", function (e) {
		// 초기값
		pageNum = 1;
		role = "";
		search = "";
		$("#searchInput").val("");

		// 버튼 활성화
		$("#generalMemberCount, #transactionSuspensionCount, #accountSuspensionCount").removeClass('active');
		$("#totalMemberCount").addClass('active');

		// 렌더링
		renderMemberList(role, pageNum, search);
	});

	// 2. 일반회원
	$(document).on("click", "button[name='generalMemberCount']", function (e) {
		// 초기값
		pageNum = 1;
		role = "일반회원";
		search = "";
		$("#searchInput").val("");

		// 버튼 활성화
		$("#totalMemberCount, #transactionSuspensionCount, #accountSuspensionCount").removeClass('active');
		$("#generalMemberCount").addClass('active');

		// 렌더링
		renderMemberList(role, pageNum, search);
	});

	// 3. 거래정지
	$(document).on("click", "button[name='transactionSuspensionCount']", function (e) {
		// 초기값
		pageNum = 1;
		role = "거래정지";
		search = "";
		$("#searchInput").val("");

		// 버튼 활성화
		$("#accountSuspensionCount, #generalMemberCount, #totalMemberCount").removeClass('active');
		$("#transactionSuspensionCount").addClass('active');

		// 렌더링
		renderMemberList(role, pageNum, search);
	});

	// 4. 계정정지
	$(document).on("click", "button[name='accountSuspensionCount']", function (e) {
		// 초기값
		pageNum = 1;
		role = "계정정지";
		search = "";
		$("#searchInput").val("");

		// 버튼 활성화
		$("#totalMemberCount, #generalMemberCount, #transactionSuspensionCount").removeClass('active');
		$("#accountSuspensionCount").addClass('active');

		// 렌더링
		renderMemberList(role, pageNum, search);
	});
	let searchTimer;
	// 검색
	$(document).on("keyup", '#searchInput', function () {
		clearTimeout(searchTimer);

    searchTimer = setTimeout(function () {
		pageNum = 1;
		search = $("#searchInput").val();
		
				// 현재 선택된 상태에 따라 다른 상태값을 전달
		let role = ""; // 기본값은 전체
		if ($("button[name='generalMemberCount']").hasClass("active")) {
			role = "일반회원";
		} else if ($("button[name='transactionSuspensionCount']").hasClass("active")) {
			role = "거래정지";
		} else if ($("button[name='accountSuspensionCount']").hasClass("active")) {
			role = "계정정지";
		}
		renderMemberList(role, pageNum, search)
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
	renderMemberList('', 1,search);
})