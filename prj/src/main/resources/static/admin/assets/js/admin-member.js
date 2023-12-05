import colorMapping from './admin-common.js';

console.log("admin-member.js 작업중")

//진입시 멤버 조회
$(document).ready(function() {
	renderMemberList();
})

function renderMemberList(role, page) {
	$.ajax({
		url : "/admin/member/list",
		data : {role, page},
		method : "GET"
	})
	.done(function(data) {
		console.log(data)
		let memberData = data.list;
		$.each(memberData, function(index, list) {
				let template = `<tr>
                        <td>${list.memberName}<i class="fab fa-angular fa-lg text-danger me-3"></i></td>
                        <td name="goQuestionDetail" class="cursor-pointer">${list.memberId}</td>
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
		})
	})
}

