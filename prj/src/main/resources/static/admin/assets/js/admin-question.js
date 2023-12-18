/**
 * 
 */
// import colorMapping from './admin-common.js';
//import showPage from './admin-utils.js';

console.log("admin-question.js 작업중")


let pageNum = 0;
let pageSize = 0; //페이지 번호 수
let pageUnit = 0;  //한페이지에 출력할 행의 수

let questionYn = "";

// 진입시 총 문의 개수


	let search = '';

let today = new Date();

let year = today.getFullYear();
let month = ('0' + (today.getMonth() + 1)).slice(-2);
let day = ('0' + today.getDate()).slice(-2);

let dateString = year + '-' + month  + '-' + day;
$("#html5-date-input").val(dateString)

$("#defaultSelect").on("change", function(){
	pageUnit = $(this).val()
	renderQuestionList(questionYn, pageNum, search, pageUnit);
})

	// 문의 리스트 포멧
	function renderQuestionList(questionYn, page, search, pageUnit) {
		console.log(questionYn)
		$("tbody").empty();
		$.ajax({
			url: "/admin/question/list",
			data: { questionYn, page, search, pageUnit },
			method: "GET"
		})
			.done(function (data) {
				// 개수출력
				$("#initQuestionCount").text(`(${data.count})`)
				console.log(data)
				// 목록출력 
				let listData = data.list;
				$.each(listData, function (index, list) {
					let template = `<tr>
                                <td>${list.questionId}<i class="fab fa-angular fa-lg text-danger me-3"></i></td>
                                <td>${list.memberId}</td>
                                <td name="goQuestionDetail" class="cursor-pointer fw-bold">${list.title}</td>
                                <td><span id="badgeColor" class="badge ${colorMapping[list.questionType]} me-1">${list.questionType}</span></td>
                                <td>${list.writeDate}</td>
                                <td><span class="badge ${colorMapping[list.questionYn]} me-1">${list.questionYn}</span></td>
                                <!--<td>
                                    <div class="dropdown">
                                        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                            <i class="bx bx-dots-vertical-rounded"></i>
                                        </button>
                                        <div class="dropdown-menu">
                                            <a class="dropdown-item" href="javascript:void(0);"><i class="bx bx-edit-alt me-1"></i> 수정</a>
                                            <a class="dropdown-item" href="javascript:void(0);"><i class="bx bx-trash me-1"></i> 삭제</a>
                                        </div>
                                    </div>
                                </td>-->
                            </tr>`;
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
		


		// 현재 선택된 상태에 따라 다른 상태값을 전달
		let questionYn = ""; // 기본값은 전체
		if ($("button[name='pendingQuestionCount']").hasClass("active")) {
			questionYn = "답변대기";
		} else if ($("button[name='completedQuestionCount']").hasClass("active")) {
			questionYn = "답변완료";
		}

		renderQuestionList(questionYn, targetPageNum, search, pageUnit);
	})

	pageNum = 1;
	pageSize = 10.0; //페이지 번호 수
	pageUnit = 10;  //한페이지에 출력할 행의 수


	// 전체 클릭시 문의 리스트
	$(document).on("click", "button[name='totalQuestionCount']", function (e) {
		pageNum = 1;
		questionYn = "";
		search = "";
		$("#searchInput").val("");
		$("#pendingQuestionCount, #completedQuestionCount").removeClass('active');
		$("#totalQuestionCount").addClass('active');
		renderQuestionList(questionYn, pageNum, search, pageUnit);
	});

	// 답변대기 클릭시 문의 리스트
	$(document).on("click", "button[name='pendingQuestionCount']", function (e) {
		pageNum = 1;
		questionYn = "답변대기"
		search = "";
		$("#searchInput").val("");
		$("#totalQuestionCount, #completedQuestionCount").removeClass('active');
		$("#pendingQuestionCount").addClass('active');
		renderQuestionList(questionYn, pageNum, search, pageUnit);
	});

	// 답변완료 클릭시 문의 리스트
	$(document).on("click", "button[name='completedQuestionCount']", function (e) {
		pageNum = 1;
		questionYn = "답변완료"
		search = "";
		$("#searchInput").val("");
		$("#totalQuestionCount, #pendingQuestionCount").removeClass('active');
		$("#completedQuestionCount").addClass('active');
		renderQuestionList(questionYn, pageNum, search, pageUnit);
	});
	let searchTimer;
	// 검색
	$(document).on("keyup", '#searchInput', function () {
		clearTimeout(searchTimer);

		searchTimer = setTimeout(function () {
			pageNum = 1;
			search = $("#searchInput").val();

			if ($("button[name='pendingQuestionCount']").hasClass("active")) {
				questionYn = "답변대기";
			} else if ($("button[name='completedQuestionCount']").hasClass("active")) {
				questionYn = "답변완료";
			} else {
				questionYn = "";
			}
			renderQuestionList(questionYn, pageNum, search, pageUnit)
		}, 300); // 300 밀리초 (0.3초) 후에 검색 실행
	})

	// 상세페이지 이동
	$(document).on("click", "td[name='goQuestionDetail']", function (e) {
		let questionId = $(this).prev().prev().text();
		location.href = `/admin/question/detail?questionId=${questionId}`;
	});

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

	// 수정 - 입력창 열기
	const updateAnswerBtn = (answerId, questionId, event) => {
		// 타겟 찾기
		let target = event.target
		originContent = $(target).closest('.originContent') // 답변내용 부분
		updateInput = $(target).closest('.row').find('.updateInput') // 입력창 브븐

		// 모든 DOM의 클래스 초기화
		$(".updateInput").addClass('d-none');
		$(".originContent").removeClass('d-none');

		// 현재 선택한 DOM의 클래스를 조작
		$(updateInput).removeClass('d-none');
		$(originContent).addClass('d-none');
	}
	
	// 수정
	$(document).on("click", "button[name='updateConfirm']", function (e) {
		e.preventDefault();
		
		// 수정된 formData 가져오기
		let formData = getUpdateInputForm();
		let questionId = formData.questionId;
		let answerId = formData.answerId;
		console.log(formData)

		const requestUrl = `/admin/question/detail/${questionId}/${answerId}`
		$.ajax(requestUrl, {
			type: 'PUT',
			contentType: 'application/json',
			data: JSON.stringify(formData)
		})
			.done(result => {
			console.log(result)
				let target = event.target
				$(".updateInput").addClass('d-none');
				$(".originContent").removeClass('d-none');
				location.reload();
			})
			.fail(err => console.log(err))
	})

	// 수정 - 취소
	$(document).on("click", "button[name='updateCancel']", function (e) {
		// 모든 DOM의 클래스 초기화
		$(".updateInput").addClass('d-none');
		$(".originContent").removeClass('d-none');
	});

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

	// 삭제
	const deleteAnswerBtn = (answerId, questionId, event) => {
		const agreeCheck = confirm("답변을 삭제 하시겠습니까?");
		const requestUrl = `/admin/question/detail/${questionId}/${answerId}`
		let target = event.target;
		console.log(target)

		if (agreeCheck == true) {
			$.ajax({
				type: 'DELETE',
				url: requestUrl
			})
				.done(result => {
					if (result) {
						alert('댓글이 삭제되었습니다.');
						$(target).closest('.row').remove();
					} else {
						alert('삭제가 실패했습니다.');
					}
				})
				.fail(error => console.log(error))
		}
	}

function displayText() {

    // 입력된 텍스트 가져오기
    var inputText = document.getElementById("answer").value;

    // 개행 문자를 <br> 태그로 대체
    var displayText = inputText.replace(/\n/g, "<br>");

    // 화면에 표시document.getElementById("displayArea")
    document.getElementById("displayArea").innerHTML = displayText;
}
$(document).ready(function () {
	renderQuestionList("", 1, search,10);
})