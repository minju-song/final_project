/**
 * 
 */
//
console.log("admin-question.js 작업중")

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

	const requestUrl = `/admin/question/detail/update/${questionId}/${answerId}`
	$.ajax(`/admin/question/detail/update/${questionId}/${answerId}`, {
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(formData)
	})
		.done(result => {
			console.log(result);
			let target = event.target
			$(".updateInput").addClass('d-none');
			$(".originContent").removeClass('d-none');
			location.reload();
		})
		.fail(err => console.log(err))
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




// 삭제
const deleteAnswerBtn = (answerId, questionId, e) => {
	const agreeCheck = confirm("답변을 삭제 하시겠습니까?");
	const requestUrl = `/admin/question/detail/delete/${questionId}/${answerId}`
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

// 색상
//bg-label-primary
//console.log($("#badgeColor").text());
//const questionList = '[[${questionList}]]';



// 답변 다시 보여주기 => 추후 구현
// $.ajax('/admin/question/detail', {
// 	type: 'get',
// 	data: answerInfo
// })
// 	.done(result => {
// 		console.log('Query String', result);
// 	})
// 	.fail(err => console.log(err))