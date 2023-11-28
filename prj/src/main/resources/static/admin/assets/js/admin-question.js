/**
 * 
 */
//th:switch="${answerId == info.answerId}"
console.log("admin-question.js 작업중")
//타겟 수정버튼 클릭 시
//1. 입력창 display => updateInput
//2. 컨텐츠 display none => originContent
//3. 다른 타겟 입력창 display none => .updateInput
// 수정
const updateAnswerBtn = (answerId, questionId, e) => {
	const reqUrl = `/admin/question/detail/update/${questionId}/${answerId}`
	let target = event.target
	// 답변내용 부분
	originContent = $(target).closest('.originContent')
	// 입력창 브븐
	updateInput = $(target).closest('.row').find('.updateInput')

	// 모든 DOM의 클래스 초기화
	$(".updateInput").addClass('d-none');
	$(".originContent").removeClass('d-none');

	// 현재 선택한 DOM의 클래스를 조작
	$(updateInput).removeClass('d-none');
	$(originContent).addClass('d-none');


	$.ajax({
		type: 'PUT',
		url: reqUrl,
		success: function(result, target) {

			console.log(result)
			console.log(target)
		}, error() {
			alert('통신이 원활하지 않습니다.')
		}
	})

}

// 삭제
const deleteAnswerBtn = (answerId, questionId, e) => {
	const agreeCheck = confirm("답변을 삭제 하시겠습니까?");
	const reqUrl = `/admin/question/detail/delete/${questionId}/${answerId}`
	let target = event.target;

	if (agreeCheck == true) {
		$.ajax({
			type: 'DELETE',
			url: reqUrl,

			success: function(result) {
				if (result) {
					alert('댓글이 삭제되었습니다.');
					target.closest('.row').remove();
				} else {
					alert('삭제가 실패했습니다.');
				}
			}, error() {
				alert('통신이 원활하지 않습니다.')
			}
		})
	}

}
