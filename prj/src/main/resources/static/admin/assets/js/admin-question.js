/**
 * 
 */
//
console.log("admin-question.js 작업중")

const colorMapping = {
    '가계부': 'bg-label-danger',
    '중고거래': 'bg-label-warning',
    '알뜰모임': 'bg-label-yellow',
    '커뮤니티': 'bg-label-success',
    '메모': 'bg-label-info',
    '홀로페이': 'bg-label-navy',
    '포인트': 'bg-label-primary',
    '기타': 'bg-label-secondary',
    '답변대기': 'bg-label-yellow',
    '답변완료': 'bg-label-success'
};


// 문의 리스트 포멧

function renderQuestionList(url, questionYn, paging) {
    $("tbody").empty();

    $.ajax({
        url: url,
        data: { questionYn },
        method: "GET"
    })
        .done(function (data) {
            // 개수출력
            console.log(data)
            $("#initQuestionCount").text(`${data.count}개`)

            // 목록출력 
            let listData = data['list'];
            console.log(listData);
            $.each(listData, function (index, list) {
                let template = `<tr name="goQuestionDetail">
                                <td>${list.questionId}<i class="fab fa-angular fa-lg text-danger me-3"></i></td>
                                <td>${list.title}</td>
                                <td>${list.memberId}</td>
                                <td><span id="badgeColor" class="badge ${colorMapping[list.questionType]} me-1">${list.questionType}</span></td>
                                <td>${list.writeDate}</td>
                                <td><span class="badge ${colorMapping[list.questionYn]} me-1">${list.questionYn}</span></td>
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
                showPage(data.paging);
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
    renderQuestionList("/admin/question/list","",targetPageNum)
})

pageNum = 1;
pageSize = 10.0; //페이지 번호 수
pageUnit = 5;  //한페이지에 출력할 행의 수


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

    console.log(str);

    $('.pagination').html(str);
    
}

// 전체 클릭시 문의 리스트
$(document).on("click", "button[name='totalQuestionCount']", function (e) {
    renderQuestionList("/admin/question/list", "","");
});

// 답변대기 클릭시 문의 리스트
$(document).on("click", "button[name='pendingQuestionCount']", function (e) {
    renderQuestionList("/admin/question/list", "답변대기","");
});

// 답변완료 클릭시 문의 리스트
$(document).on("click", "button[name='completedQuestionCount']", function (e) {
    renderQuestionList("/admin/question/list", "답변완료","");
});

// 상세페이지 이동
$(document).on("click", "tr[name='goQuestionDetail']", function (e) {
    let questionId = $(this).find('td:first-child').text();
    location.href = `/admin/question/detail?questionId=${questionId}`;
});



// 진입시 총 문의 개수
$(document).ready(function () {
    renderQuestionList("/admin/question/list", "");
})


// 수정 - 입력창 열기
const updateAnswerBtn = (answerId, questionId, e) => {
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

    const requestUrl = `/admin/question/detail/${questionId}/${answerId}`
    $.ajax(requestUrl, {
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(formData)
    })
        .done(result => {
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
const deleteAnswerBtn = (answerId, questionId, e) => {
    const agreeCheck = confirm("답변을 삭제 하시겠습니까?");
    const requestUrl = `/admin/question/detail/${questionId}/${answerId}`
    let target = event.target;

    if (agreeCheck == true) {
        $.ajax({
            type: 'DELETE',
            url: requestUrl
        })
            .done(result => {
                if (result) {
                    alert('댓글이 삭제되었습니다.');
                    target.closest('.row').remove();
                } else {
                    alert('삭제가 실패했습니다.');
                }
            })
            .fail(error => console.log(error))
    }
}