/**
 * 
 */
// import colorMapping from './admin-common.js';

console.log("admin-holopay.js 작업중")

let pageNum = 0;
let pageSize = 0; //페이지 번호 수
let pageUnit = 0;  //한페이지에 출력할 행의 수
let search = '';

// 진입시 총 문의 개수
$(document).ready(function () {

function renderHolopayList(page, search) {
		console.log()
		$("tbody").empty();
		$.ajax({
			url: "/admin/holopay/list",
			data: { page, search },
			method: "GET"
		})
			.done(function (data) {
				// 개수출력
				$("#initMemberCount").text(`(${data.list.length})`)
				console.log(data)
				// 목록출력 
				let listData = data.list;
				$.each(listData, function (index, list) {
					let template = `<tr>
                                <td>${list.rn}<i class="fab fa-angular fa-lg text-danger me-3"></i></td>
                                <td>${list.memberId}</td>
                                <td class="cursor-pointer">${list.nickname}</td>
                                <td><span id="badgeColor" class="badge ${!isNaN(list.pointPrice) ? 'bg-label-info' : ''} me-1">${list.pointPrice}</span></td>
                                <td><span  class="badge ${!isNaN(list.holopayPrice) ? 'bg-label-info' : ''} me-1"">${list.holopayPrice}</span></td>
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
					showPage(data.list.length);
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

		renderHolopayList(pageNum, search);
	})
	
	pageNum = 1;
	pageSize = 10.0; //페이지 번호 수
	pageUnit = 10;  //한페이지에 출력할 행의 수
	
	// 검색
	let searchTimer;
	$(document).on("keyup", '#searchInput', function () {
		clearTimeout(searchTimer);

    	searchTimer = setTimeout(function () {
		pageNum = 1;
		search = $("#searchInput").val();
		
		renderHolopayList(pageNum, search)
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
	
	renderHolopayList(1, search)
	
	})