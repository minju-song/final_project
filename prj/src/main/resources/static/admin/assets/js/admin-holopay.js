/**
 * 
 */
import colorMapping from './admin-common.js';

console.log("admin-holopay.js 작업중")

function renderQuestionList(page, search) {
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
                                <td>${list.questionId}<i class="fab fa-angular fa-lg text-danger me-3"></i></td>
                                <td name="goQuestionDetail" class="cursor-pointer">${list.nickname}</td>
                                <td>${list.memberId}</td>
                                <td><span id="badgeColor" class="badge ${!isNaN(list.pointPrice) ? 'bg-label-info' : ''} me-1">${list.pointPrice}</span></td>
                                <td><span  class="badge ${!isNaN(list.holopayPrice) ? 'bg-label-yellow' : ''} me-1"">${list.holopayPrice}</span></td>
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
					//showPage(data.count);
					//게시물수


				});
			})
			.fail(function (error) {
				console.error("Error fetching question list: ", error);
			});

	}
	
	renderQuestionList()