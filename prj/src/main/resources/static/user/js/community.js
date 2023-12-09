// 페이징
let currentPage = 1;
const recordsPerPage = 10; // 페이지당 표시할 레코드 수, 필요에 따라 조절

// 페이지 버튼 생성
function setupPagination(totalPages) {
    let paginationContainer = document.getElementById("pagination-container");

    if (paginationContainer) {
        paginationContainer.innerHTML = "";

        let navElement = document.createElement("nav");
        let ulElement = document.createElement("ul");
        ulElement.className = "pagination";
        navElement.setAttribute("aria-label", "...");
        // Previous Button
        let previousLi = document.createElement("li");
        previousLi.className = "page-item";
        let previousLink = document.createElement("a");
        previousLink.className = "page-link";
        previousLink.href = "#";
        previousLink.innerText = "«";
        previousLink.addEventListener("click", function () {
            if (currentPage > 1) {
                loadData(currentPage - 1);
            }
        });
        previousLi.appendChild(previousLink);
        ulElement.appendChild(previousLi);

        // Page Buttons
        for (let i = 1; i <= totalPages; i++) {

            let li = document.createElement("li");
            li.className = "page-item";
            let link = document.createElement("a");
            link.className = "page-link";
            link.href = "#";
            link.innerText = i;
            link.addEventListener("click", function () {
                loadData(i);
            });
            li.appendChild(link);
            ulElement.appendChild(li);
        }

        // Next Button
        let nextLi = document.createElement("li");
        let nextLink = document.createElement("a");
        nextLi.className = "page-item";
        nextLink.className = "page-link";
        nextLink.href = "#";
        nextLink.innerText = "»";
        nextLink.addEventListener("click", function () {
            if (currentPage < totalPages) {
                loadData(currentPage + 1);
            }
        });
        nextLi.appendChild(nextLink);
        ulElement.appendChild(nextLi);

        navElement.appendChild(ulElement);
        paginationContainer.appendChild(navElement);

    }
}
//게시판 목록 로드
function loadData(page, search) {
    currentPage = page;
    let start = (currentPage - 1) * recordsPerPage;
    let end = start + recordsPerPage;
    let searchBoardSet = location.pathname
    let boardType = "";
    let setUrl = ""
    let searchOption = document.getElementById('searchTitle').value
    if (searchBoardSet == "/board/chat") {
        boardType = "AA3"
    } else if (searchBoardSet == "/board/info") {
        boardType = "AA2"
    } else if (searchBoardSet == "/member/notice") {
        boardType = "AA6"
    }
    //검색 설정
    if (search != null && search != '') {
        setUrl = "/searchBoardLoad"
    } else {
        setUrl = "/boardLoad"
    }
    $.ajax({
        type: 'POST',
        url: setUrl,
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({ "start": start, "end": end, "type": boardType, "search": search, "searchType": searchOption }),
        success: function (data) {
            updateTable(data, page, "");
            let totalPages = Math.ceil(data.totalRecords / recordsPerPage);
            setupPagination(totalPages);
        },
        error: function (error) {
        }
    });
}
//테이블 갱신
function updateTable(data, page) {
    currentPage = page;
    let tbody = $("#boardTableBody");
    let searchBoardSet = data.boardType;
    tbody.empty(); // 기존 데이터를 지우고 새로운 데이터로 갱신

    if (data && data.historyList && data.historyList.length > 0) {
        // 데이터가 있을 경우 테이블에 행 추가

        data.historyList.forEach(function (item, index) {
            let row = $("<tr>");
            //row.attr("onclick", `location.href='/member/board/view?boardId=${item.boardId}'`);
            if (data.totalRecords > page * recordsPerPage) {
                row.append($("<td class='board-no'>").text(data.totalRecords - index - (page - 1) * recordsPerPage));
                // row.append($("<td>").text(index + 1));
            } else {
                row.append($("<td class='board-no'>").text(data.totalRecords - (index + (page - 1) * recordsPerPage)));
            }
            row.append($("<td class='board-title' onclick="+ `location.href='/member/board/view?boardId=${item.boardId}'` + ">").text(item.title));
            row.append($("<td class='board-date'>").text(formatDate(item.writeDate)));
            if (searchBoardSet == 'AA3') {

            } else if (searchBoardSet == 'AA2') {
                row.append($("<td class='board-nickname'>").text(item.nickname));
            }

            //row.append($("<td class='board-likeAndView'>").text(item.likeCount + '/' + item.views).css("text-align", "center"));
            row.append($("<td class='board-likeAndView'>").append(`<span class="like-icon">${item.likeCount}</span><span class="view-icon">${item.views}</span>`));

            tbody.append(row);
        });
    } else {
        // 데이터가 없을 경우 테이블에 메시지 추가
        let noDataMessage = $("<tr>").append($("<td colspan='5' class='text-center'>").text("게시물이 없습니다."));
        tbody.append(noDataMessage);
    }

}
//날짜 포맷
function formatDate(dateString) {
    let date = new Date(dateString);
    let year = date.getFullYear();
    let month = (date.getMonth() + 1).toString().padStart(2, '0');
    let day = date.getDate().toString().padStart(2, '0');
    return year + '-' + month + '-' + day;
}

//게시물조회
async function findPost() {

    // 1. URL 쿼리 스트링에서 게시글 번호 조회
    const searchParams = new URLSearchParams(location.search);
    const postId = Number(searchParams.get('boardId'));

    // 2. API 호출
    const url = `/api/board/${postId}`;
    const response = await fetch(url);
    const post = await response.json();


    // 3. 에디터 콘텐츠 렌더링
    const viewer = toastui.Editor.factory({
        el: document.querySelector('#viewer'),
        viewer: true,
        initialValue: post.content
    });

    document.getElementById('title').innerText = post.title;
    if (post.menuType == "AA2") {
        document.getElementById('writer').innerText = post.nickname;
    } else if (post.menuType == "AA3") {
        document.getElementById('writer').innerText = "작성자";
    }

    document.getElementById('boardLike').innerText = post.likeCount;
    document.getElementById('writeDate').innerText = formatDate(post.writeDate);
    document.getElementById('views').innerText = post.views;


}
//댓글 생성
function replyLoad(page) {
    currentPage = page;
    let start = (currentPage - 1) * recordsPerPage;
    let end = start + recordsPerPage;


    const searchParams = new URLSearchParams(location.search);
    const boardId = Number(searchParams.get('boardId'));

    $.ajax({
        type: 'POST',
        url: '/loadReply',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({ "boardId": boardId, "start": start, "end": end }),
        success: function (data) {
            updateReplyTable(data, page);
            let totalPages = Math.ceil(data.totalRecords / recordsPerPage);
            setupPagination(totalPages);
        },
        error: function (error) {

            console.error("error: " + error);
        }
    })
}

//댓글 테이블 생성
function updateReplyTable(data, page) {
    currentPage = page;
    let tbody = $("#ReplyTableBody");
    tbody.empty();

    // 트리 구조로 변환
    const rootComments = convertToTreeStructure(data);
    if (data.totalRecords != '0') {
        // 댓글 출력
        function renderComments(comments, depth = 0) {
            let checkBoard = "";
            let thisBoard;
            $.ajax({
                type: 'POST',
                url: '/checkWriter',
                contentType: 'application/json;charset=UTF-8',
                data: JSON.stringify({ "boardId": comments[0].boardId }),
                success: function (data) {
                    thisBoard = data.board;
                    checkBoard = thisBoard.menuType


                    comments.forEach((comment) => {
                        let row = $("<tr>");
                        if (comment.upperReplyId != 0) {
                            row.append($("<td>").text("↳").css("text-align", "right"));
                        } else {
                            row.append($("<td>").text("  "));
                        }
                        row.append($("<td>").text(comment.content).css("width", "40%"));
                        row.append($("<td>").text(formatDate(comment.writeDate)));
                        if (checkBoard == "AA3") {
                            if (thisBoard.memberId == comment.memberId) {
                                row.append($("<td>").text("작성자"));
                            } else {
                                row.append($("<td>").text("익명"));
                            }

                        } else if (checkBoard == "AA2") {
                            row.append($("<td>").text(comment.nickname));
                        }

                        let row2 = $("<tr>");

                        // 대댓글 버튼 추가

                        let rowReplyAddBtn = $("<button>")
                            .text("댓글")
                            .attr('id', 'rowReplyFormOpen_' + comment.replyId)
                            .attr('value', comment.replyId);
                        rowReplyAddBtn.on('click', function () {
                            rowReplyAddBtn.prop('disabled', true);
                            rowReplyInsertForm(comment.replyId, row2);
                        });

                        $.ajax({
                            type: 'POST',
                            url: '/searchReplyWriter',
                            contentType: 'application/json;charset=UTF-8',
                            success: function (userdata) {
                                if (userdata.loginUser == comment.memberId) {

                                    //수정버튼
                                    let ReplyUpdateBtn = $("<button>")
                                        .text("수정")
                                        .attr('id', 'replyUpdateBtn' + comment.replyId)
                                        .attr('value', comment.replyId);

                                    ReplyUpdateBtn.on('click', function () {
                                        replyUpdateForm(comment.replyId, comment.content, row2);
                                        ReplyUpdateBtn.prop('disabled', true);
                                    });

                                    row.append(ReplyUpdateBtn);
                                    //삭제버튼
                                    let ReplyDeleteBtn = $("<button>")
                                        .text("삭제")
                                        .attr('id', 'replydeleteBtn' + comment.replyId)
                                        .attr('value', comment.replyId);

                                    ReplyDeleteBtn.on('click', function () {
                                        Swal.fire({
                                            title: "댓글을 삭제하시겠습니까",
                                            text: "삭제된 글은 복구 할 수 없습니다",
                                            icon: "warning",
                                            showCancelButton: true,
                                            confirmButtonColor: "#3085d6",
                                            cancelButtonColor: "#d33",
                                            confirmButtonText: "삭제"
                                        }).then((result) => {
                                            if (result.isConfirmed) {
                                                $.ajax({
                                                    type: 'POST',
                                                    url: '/deleteReply',
                                                    contentType: 'application/json;charset=UTF-8',
                                                    data: JSON.stringify({ "replyId": comment.replyId }),
                                                    success: function (data) {
                                                        Swal.fire({
                                                            title: "성공!",
                                                            text: data.resultMsg,
                                                            icon: "success",
                                                            closeOnClickOutside: false
                                                        }).then(function () {
                                                            replyLoad(1);
                                                        })
                                                    }
                                                });
                                            }
                                        });
                                    });

                                    row.append(ReplyDeleteBtn);
                                }
                            },
                            error: function (error) {

                                console.error("error: " + error);
                            }
                        })

                        row.append(rowReplyAddBtn);
                        tbody.append(row, row2);
                        if (comment.replies.length > 0) {
                            renderComments(comment.replies, depth + 1);
                        }
                    });



                },
                error: function (error) {
                    console.error("error: " + error);
                }
            })



        }
        renderComments(rootComments);
    }



    // 댓글 출력 시작

}

//페이지 로딩 시 좋아요 표시
async function checkLike() {
    let searchParams = new URLSearchParams(location.search)
    let boardId = Number(searchParams.get('boardId'));
    $.ajax({
        type: 'POST',
        url: '/likeCheck',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({ "boardId": boardId }),
        success: function (data) {
            let heart = document.getElementById('boardLikeBtn');
            if (data.searchLike == "true") {
                heart.innerHTML = '&#128420;';
            }
        },
        error: function (error) {
            console.error("error: " + error);
        }
    })
}

//좋아요 추가
function addLike() {

    const searchParams = new URLSearchParams(location.search);
    let boardId = Number(searchParams.get('boardId'));
    let heart = document.getElementById('boardLikeBtn');
    let likeCount = document.getElementById('boardLike');
    $.ajax({
        type: 'POST',
        url: '/addLike',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({ "boardId": boardId }),
        success: function (data) {
            if (data.resultMsg == '추가') {
                changeText(heart);
                likeCount.innerText = parseInt(likeCount.innerText) + 1;

            } else if (data.resultMsg == '삭제') {
                deleteLike(heart);
                likeCount.innerText = parseInt(likeCount.innerText) - 1;

            }


        },
        error: function (error) {
            console.error("error: " + error);
        }
    });
}
//버튼 js
function changeText(heart) {
    heart.classList.remove('animate__bounce');
    heart.innerHTML = '&#128420;';
    heart.classList.add('animate__animated', 'animate__bounce');
}
//좋아요 취소
function deleteLike(heart) {
    heart.classList.remove('animate__bounceIn');
    heart.innerHTML = '&#129293;';
    heart.classList.add('animate__animated', 'animate__bounceIn');
}

//댓글 등록
function insertReply() {
    const searchParams = new URLSearchParams(location.search);
    let boardId = Number(searchParams.get('boardId'));
    let content = document.getElementById('replyForm').value
    $.ajax({
        type: 'POST',
        url: '/insertReply',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({ "boardId": boardId, "content": content }),
        success: function (data) {

            replyLoad(currentPage);

            let replyForm = document.getElementById('replyForm');
            replyForm.value = ""
            document.getElementById('replyFormOpen').disabled = false;

            let addReplyBtn = document.getElementById('addReplyBtn');
            addReplyBtn.remove();

            let cancelReplyBtn = document.getElementById('cancelReplyBtn')
            cancelReplyBtn.remove()
        },
        error: function (error) {
            console.error("error: " + error);
            Swal.fire({
                title: "",
                text: "댓글을 입력해 주세요",
                icon: "error",
                closeOnClickOutside: false
            }).then(function () {

            })
        }
    });
}

// 댓글페이지 버튼 생성
function setupReplyPagination(totalPages) {
    let paginationContainer = document.getElementById("pagination-container");

    if (paginationContainer) {
        paginationContainer.innerHTML = "";

        let navElement = document.createElement("nav");
        let ulElement = document.createElement("ul");
        ulElement.className = "pagination";
        navElement.setAttribute("aria-label", "...");
        // Previous Button
        let previousLi = document.createElement("li");
        previousLi.className = "page-item";
        let previousLink = document.createElement("a");
        previousLink.className = "page-link";
        previousLink.href = "#";
        previousLink.innerText = "«";
        previousLink.addEventListener("click", function () {
            if (currentPage > 1) {
                loadData(currentPage - 1);
            }
        });
        previousLi.appendChild(previousLink);
        ulElement.appendChild(previousLi);

        // Page Buttons
        for (let i = 1; i <= totalPages; i++) {

            let li = document.createElement("li");
            li.className = "page-item";
            let link = document.createElement("a");
            link.className = "page-link";
            link.href = "#";
            link.innerText = i;
            link.addEventListener("click", function () {
                loadData(i);
            });
            li.appendChild(link);
            ulElement.appendChild(li);
        }

        // Next Button
        let nextLi = document.createElement("li");
        let nextLink = document.createElement("a");
        nextLi.className = "page-item";
        nextLink.className = "page-link";
        nextLink.href = "#";
        nextLink.innerText = "»";
        nextLink.addEventListener("click", function () {
            if (currentPage < totalPages) {
                loadData(currentPage + 1);
            }
        });
        nextLi.appendChild(nextLink);
        ulElement.appendChild(nextLi);

        navElement.appendChild(ulElement);
        paginationContainer.appendChild(navElement);

    }
}

//대댓글 입력 폼
function rowReplyInsertForm(replyId, thisRow) {
    let rowReplyFormArea = document.createElement('td');
    rowReplyFormArea.id = "rowReplyFormArea_" + replyId;

    let replyForm = document.createElement('textarea');
    replyForm.id = "replyForm_" + replyId;
    replyForm.placeholder = '답글을 입력하세요.';
    replyForm.classList.add('form-control', 'text');

    let addReplyBtn = document.createElement('button');
    addReplyBtn.type = 'button';
    addReplyBtn.classList.add('btn', 'btn-primary');
    addReplyBtn.id = 'addReplyBtn_' + replyId;
    addReplyBtn.textContent = '댓글 등록';
    addReplyBtn.addEventListener('click', function () {
        insertRowReply(replyId);
    });

    let cancelBtn = document.createElement('button');
    cancelBtn.type = 'button';
    cancelBtn.id = 'cancelReplyBtn_' + replyId;
    cancelBtn.classList.add('btn', 'btn-danger');
    cancelBtn.textContent = '닫기';
    cancelBtn.addEventListener('click', function () {
        document.getElementById('rowReplyFormOpen_' + replyId).removeAttribute('disabled');
        rowReplyFormArea.remove();
    });
    thisRow.find('#rowReplyFormOpen_' + replyId).prop('disabled', true);
    rowReplyFormArea.setAttribute('colspan', '4')
    rowReplyFormArea.append(replyForm, addReplyBtn, cancelBtn);
    thisRow.append(rowReplyFormArea);
    document.getElementById('rowReplyFormOpen_' + replyId).disabled = true;

}

//대댓글 등록
function insertRowReply(upperReplyId) {

    const searchParams = new URLSearchParams(location.search);
    let boardId = Number(searchParams.get('boardId'));
    let content = document.getElementById('replyForm_' + upperReplyId).value

    $.ajax({
        type: 'POST',
        url: '/insertReply',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({ "boardId": boardId, "content": content, "upperReplyId": upperReplyId }),
        success: function (data) {
            replyLoad(currentPage);
            let rowReplyForm = document.getElementById('rowReplyForm');
            if (rowReplyForm) {
                rowReplyForm.remove();
            }
            document.getElementById('rowReplyFormOpen').disabled = false;

            let addReplyBtn = document.getElementById('addRowReplyBtn');
            addReplyBtn.remove();

            let cancelReplyBtn = document.getElementById('cancelRowReplyBtn');
            cancelReplyBtn.remove();
        },
        error: function (error) {
            console.error("error: " + error);
        }
    })

}

//대댓글 배치
function convertToTreeStructure(data) {

    const commentMap = new Map(); // 댓글 ID를 키로 갖는 Map
    const rootComments = []; // 최상위 댓글을 저장하는 배열

    // 댓글을 Map에 추가
    data.historyList.forEach((comment) => {
        comment.replies = []; // 대댓글을 저장할 배열 추가
        commentMap.set(comment.replyId, comment);
    });

    // 부모 댓글에 대댓글 추가
    data.historyList.forEach((comment) => {
        if (comment.upperReplyId && commentMap.has(comment.upperReplyId)) {
            const parentComment = commentMap.get(comment.upperReplyId);
            parentComment.replies.push(comment);
        } else {
            rootComments.push(comment); // 최상위 댓글인 경우 rootComments 배열에 추가
        }
    });

    return rootComments;
}

//댓글 삭제
function deletePost() {

    const searchParams = new URLSearchParams(location.search);
    const boardId = Number(searchParams.get('boardId'));

    $.ajax({
        type: 'POST',
        url: '/member/board/delete',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({ "boardId": boardId }),
        success: function (data) {
            Swal.fire({
                title: "",
                text: data.resultMsg,
                icon: "success",
                closeOnClickOutside: false
            }).then(function () {
                location.href = "/board/info"
            })
        },
        error: function (error) {

            console.error("error: " + error);
        }
    });

}

//댓글 수정 폼생성
function replyUpdateForm(replyId, content, thisRow) {
    let rowReplyFormArea = document.createElement('td');
    rowReplyFormArea.id = "rowReplyFormArea_" + replyId;
    console.log(replyId);
    let replyForm = document.createElement('textarea');
    replyForm.id = "replyForm_" + replyId;
    replyForm.placeholder = '답글을 입력하세요.';
    replyForm.classList.add('form-control', 'text');
    replyForm.value = content;



    let addReplyBtn = document.createElement('button');
    addReplyBtn.type = 'button';
    addReplyBtn.classList.add('btn', 'btn-primary');
    addReplyBtn.id = 'addReplyBtn_' + replyId;
    addReplyBtn.textContent = '댓글 등록';


    let cancelBtn = document.createElement('button');
    cancelBtn.type = 'button';
    cancelBtn.id = 'cancelReplyBtn_' + replyId;
    cancelBtn.classList.add('btn', 'btn-danger');
    cancelBtn.textContent = '닫기';

    rowReplyFormArea.setAttribute('colspan', '4');
    rowReplyFormArea.append(replyForm, addReplyBtn, cancelBtn);
    thisRow.append(rowReplyFormArea);
    cancelBtn.addEventListener('click', function () {
        let updateBtnId = document.getElementById('replyUpdateBtn' + replyId);
        $(updateBtnId).prop('disabled', false);
        rowReplyFormArea.remove();
    });
    addReplyBtn.addEventListener('click', function () {

        let fixedContent = replyForm.value;
        updateReply(replyId, fixedContent);
    });
}
//댓글수정 등록
function updateReply(replyId, content) {
    $.ajax({
        type: 'POST',
        url: '/updateReply',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({ "content": content, "replyId": replyId }),
        success: function (data) {
            Swal.fire({
                title: "",
                text: data.resultMsg,
                icon: "success",
                closeOnClickOutside: false
            }).then(function () {
                let updateBtnId = document.getElementById('replyUpdateBtn' + replyId);
                $(updateBtnId).prop('disabled', false);

                document.getElementById("rowReplyFormArea_" + replyId).remove();
                replyLoad(1);

            })
        },
        error: function (request, status, error) {

            console.error("error: " + error);
        }
    })
}
//신고
function reportFormSet() {
    console.log('신고')
    // 1. URL 쿼리 스트링에서 게시글 번호 조회
    const searchParams = new URLSearchParams(location.search);
    const postId = Number(searchParams.get('boardId'));
    let thisBoard = "";
    $.ajax({
        type: 'POST',
        url: '/checkWriter',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({ "boardId": postId }),
        success: function (data) {
            thisBoard = data.board;
            const { value: formValues } = Swal.fire({
                title: `신고하시겠습니까`,
                html: `
                <div class="container">
                    <div class="row">
                        <label class="col-3">신고 유형</label>
                        <select id="reportType" class="col-3 custom-select justify-content-end" >
                            <option value="SA1">욕설</option>
                            <option value="SA2">음란성</option>
                            <option value="SA3">모욕/비방</option>
                            <option value="SA4">사기</option>
                            <option value="SA5">기타</option>
                        </select>
                    </div>
                    <br>
                    <div class="row">
                        <label class="col-3">신고 내용 : </label>
                        <input type ="text" id="reportContent" class="form-control col-8">
                    </div>
                </div>           
                `,
                width: '35%',
                height: '40%',
                focusConfirm: false,
                preConfirm: () => {
                    let reportType = document.getElementById("reportType").value;
                    let reportContent = document.getElementById("reportContent").value;

                    $.ajax({
                        type: 'POST',
                        url: '/insertReport',
                        contentType: 'application/json;charset=UTF-8',
                        data: JSON.stringify({ "reportedId": thisBoard.memberId, "reportType": reportType, "reportContent": reportContent, "postId": thisBoard.boardId, "menuType": thisBoard.menuType }),
                        success: function (data) {
                            console.log(data)
                            Swal.fire({
                                title: "",
                                text: data.resultMsg,
                                icon: "success",
                                closeOnClickOutside: false
                            }).then(function () {
                                location.href='/board/info'
                            })
                        },
                        error: function (request, status, error) {
                            console.error("error: " + error);
                        }
                    })
                }
            });
        },
        error: function (error) {

            console.error("error: " + error);
        }
    })


}
