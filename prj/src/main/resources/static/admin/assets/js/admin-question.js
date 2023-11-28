/**
 * 
 */

console.log("admin-question.js 작업중")

// 수정
const updateAnswerBtn = (answerId, questionId, e) => {
	const reqUrl = `/admin/question/detail/update/${questionId}/${answerId}`
	let target = event.target;
	
	console.log(target)
	console.log(this)
	
	let updateInput = target.parentElement.parentElement.nextElementSibling.classList;
	let originContent = $(target).closest('.originContent');
	console.log(originContent);
	console.log(reqUrl)



	$('.updateInput').toggleClass('d-none');

	

	$.ajax({
		type: 'PUT',
		url: reqUrl,
		success: function (result, target) {
			
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

			success: function (result) {
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

