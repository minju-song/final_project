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
        previousLink.innerText = "이전";
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
        nextLink.innerText = "다음";
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


    let searchOption = ""
    if (searchBoardSet == "/board/info" ||
        searchBoardSet == "/board/chat" ||
        searchBoardSet == "/cs/help/notice" ||
        searchBoardSet == "/member/cs/help/question") {
        searchOption = document.getElementById('searchTitle').value
    }

    let boardType = "";
    if (searchBoardSet == "/board/chat") {
        boardType = "AA3"
    } else if (searchBoardSet == "/board/info") {
        boardType = "AA2"
    } else if (searchBoardSet == "/cs/help/notice") {
        boardType = "AA6"
    } else if (searchBoardSet == "/member/cs/help/question") {
        boardType = "AA8"
    }

    let setUrl = ""
    //검색 설정
    if (search != null && search != '' && boardType == "AA8") {
        setUrl = "/searchQNA"
    } else if (search != null && search != '') {
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
            if (searchBoardSet == "AA2" || searchBoardSet == "AA3") {
                // 클릭이벤트 제목에만 처리(전유진)
                //row.attr("onclick", `location.href='/member/board/view?boardId=${item.boardId}'`);
            } else if (searchBoardSet == "AA6") {
                row.attr("onclick", `location.href='/cs/help/notice/view?boardId=${item.boardId}'`);
            }

            if (data.totalRecords > page * recordsPerPage) {
                row.append($("<td class='board-no'>").text(data.totalRecords - index - (page - 1) * recordsPerPage));
                // row.append($("<td>").text(index + 1));
            } else {
                row.append($("<td class='board-no'>").text(data.totalRecords - (index + (page - 1) * recordsPerPage)));
            }
            // 클릭이벤트 제목에만 처리(전유진)
            row.append($("<td class='board-title' onclick=" + `location.href='/member/board/view?boardId=${item.boardId}'` + ">").text(item.title));
            row.append($("<td class='board-date'>").text(formatDate(item.writeDate)).css('text-align', 'center'));
            if (searchBoardSet == 'AA3' || searchBoardSet == "AA6") {

            } else if (searchBoardSet == 'AA2') {
                row.append($("<td class='board-nickname'>").text(item.nickname));
            }
            if(searchBoardSet !='AA8'){
                row.append($("<td class='board-likeAndView'>").append(`<span class="like-icon">${item.likeCount}</span><span class="view-icon">${item.views}</span>`));
            }
            

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
                heart.innerHTML = '❤️';
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
    heart.innerHTML = '❤️';
    heart.classList.add('animate__animated', 'animate__bounce');
}
//좋아요 취소
function deleteLike(heart) {
    heart.classList.remove('animate__bounceIn');
    heart.innerHTML = '🤍';
    heart.classList.add('animate__animated', 'animate__bounceIn');
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
        url: '/loadUpperReply',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({ "boardId": boardId, "start": start, "end": end }),
        success: function (data) {
            updateReplyTable(data, page);
            let totalPages = Math.ceil(data.totalRecords / recordsPerPage);
            setupReplyPagination(totalPages);
        },
        error: function (error) {

            console.error("error: " + error);
        }
    })
}
//댓글 출력
function updateReplyTable(data) {


    // 최상위 댓글 렌더링 시작
    async function renderUpperReply(upperReplyList) {
        let = replyList = upperReplyList.historyList;

        //상위댓글 생성

        try {
            let tbody = $("#ReplyTableBody");
            tbody.empty();

            $.ajax({
                type: 'POST',
                url: '/checkWriter',
                contentType: 'application/json;charset=UTF-8',
                data: JSON.stringify({ "boardId": upperReplyList.historyList[0].boardId }),
                success: function (data) {
                    let tbody = $("#ReplyTableBody");
                    tbody.empty()
                    let thisboard = data.board;

                    for (let i = 0; i < replyList.length; i++) {
                        let reply = replyList[i];

                        let row = $("<tr>");
                        if (reply.upperReplyId != 0) {
                            row.append($("<td>").text("↳"));
                            row.append($("<td>").text(reply.content).css({ "width": "50%", "max-width": "500px", "word-wrap": "break-word" }));
                        } else {
                            row.append($("<td colspan='2'>").text(reply.content).css({ "width": "50%", "max-width": "500px", "word-wrap": "break-word" }));
                        }
                        row.append($("<td>").text(formatDate(reply.writeDate)).css("text-align", "center"));

                        if (thisboard.menuType == "AA3") {
                            if (thisboard.memberId == reply.memberId) {
                                row.append($("<td>").text("작성자"));
                            } else {
                                row.append($("<td>").text("익명"));
                            }
                        } else if (thisboard.menuType == "AA2") {
                            row.append($("<td>").text(reply.nickname));
                        }

                        // 대댓글 버튼 추가
                        let rowReplyAddBtn = "";
                        if (reply.upperReplyId == 0) {
                            rowReplyAddBtn = $("<button>")
                                .text("답댓글")
                                .attr('id', 'rowReplyFormOpen_' + reply.replyId)
                                .attr('value', reply.replyId);
                            rowReplyAddBtn.on('click', function () {
                                rowReplyInsertForm(reply.replyId, row);
                            });
                        }
                        $.ajax({
                            type: 'POST',
                            url: '/searchReplyWriter',
                            contentType: 'application/json;charset=UTF-8',
                            data: JSON.stringify({ "memberId": reply.memberId }),
                            success: function (data) {

                                if (data.loginUser == reply.memberId) {
                                    // 수정버튼
                                    let ReplyUpdateBtn = $("<button>")
                                        .text("수정")
                                        .attr('id', 'replyUpdateBtn' + reply.replyId)
                                        .attr('value', reply.replyId);

                                    ReplyUpdateBtn.on('click', function () {
                                        replyUpdateForm(reply.replyId, reply.content, row);
                                        let updateBtnId = document.getElementById('replyUpdateBtn' + reply.replyId)
                                        updateBtnId.setAttribute('disabled', 'true');
                                    });
                                    // 삭제버튼
                                    let ReplyDeleteBtn = $("<button>")
                                        .text("삭제")
                                        .attr('id', 'replydeleteBtn' + reply.replyId)
                                        .attr('value', reply.replyId);

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
                                                    data: JSON.stringify({ "replyId": reply.replyId }),
                                                    success: function (data) {
                                                        Swal.fire({
                                                            title: "성공!",
                                                            text: data.resultMsg,
                                                            icon: "success",
                                                            closeOnClickOutside: false
                                                        }).then(function () {
                                                            replyLoad(1);
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    });
                                    row.append(ReplyUpdateBtn);
                                    row.append(ReplyDeleteBtn);
                                }
                            }

                        });

                        if (reply.upperReplyId == 0) {
                            row.append(rowReplyAddBtn);
                        }


                        //대댓글 출력
                        console.log(reply.replyId)
                        $.ajax({
                            type: 'POST',
                            url: '/loadRowReply',
                            contentType: 'application/json;charset=UTF-8',
                            data: JSON.stringify({ "upperReplyId": reply.replyId }),
                            success: function (rowReplyList) {
                                let rowList = rowReplyList.rowList;
                                if (rowList.length != 0) {

                                    for (let i = 0; i < rowList.length; i++) {

                                        let rowReply = rowList[i];
                                        console.log(rowReply)
                                        let row2 = $("<tr>");
                                        row2.append($("<td class='arrow'>").text("↳").css("color", "#232323"));
                                        row2.append($("<td>").text(rowReply.content).css({ "width": "50%", "max-width": "500px", "word-wrap": "break-word" }));
                                        row2.append($("<td>").text(formatDate(rowReply.writeDate)).css("text-align", "center"));

                                        if (thisboard.menuType == "AA3") {
                                            if (thisboard.memberId == rowReply.memberId) {
                                                row2.append($("<td>").text("작성자"));
                                            } else {
                                                row2.append($("<td>").text("익명"));
                                            }
                                        } else if (thisboard.menuType == "AA2") {
                                            row2.append($("<td>").text(rowReply.nickname));
                                        }
                                        $.ajax({
                                            type: 'POST',
                                            url: '/searchReplyWriter',
                                            contentType: 'application/json;charset=UTF-8',
                                            data: JSON.stringify({ "memberId": rowReply.memberId }),
                                            success: function (data) {
                                                if (data.loginUser == rowReply.memberId) {
                                                    // 수정버튼
                                                    let ReplyUpdateBtn = $("<button>")
                                                        .text("수정")
                                                        .attr('id', 'replyUpdateBtn' + rowReply.replyId)
                                                        .attr('value', rowReply.replyId);
                                                    ReplyUpdateBtn.on('click', function () {
                                                        replyUpdateForm(rowReply.replyId, rowReply.content, row);
                                                        let updateBtnId = document.getElementById('replyUpdateBtn' + rowReply.replyId)
                                                        updateBtnId.setAttribute('disabled', 'true');
                                                    });
                                                    // 삭제버튼
                                                    let ReplyDeleteBtn = $("<button>")
                                                        .text("삭제")
                                                        .attr('id', 'replydeleteBtn' + rowReply.replyId)
                                                        .attr('value', rowReply.replyId);

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
                                                                    data: JSON.stringify({ "replyId": rowReply.replyId }),
                                                                    success: function (data) {
                                                                        Swal.fire({
                                                                            title: "성공!",
                                                                            text: data.resultMsg,
                                                                            icon: "success",
                                                                            closeOnClickOutside: false
                                                                        }).then(function () {
                                                                            replyLoad(1);
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    });
                                                    row2.append(ReplyUpdateBtn);
                                                    row2.append(ReplyDeleteBtn);
                                                }
                                            }
                                        });
                                        row.after(row2)
                                    }
                                }
                            },
                            error: function (error) {
                                console.error("error: " + error);
                            }
                        })
                        tbody.append(row);
                    }
                },
                error: function () {
                    Swal.fire({
                        title: "댓글이 없습니다!",
                        text: data.resultMsg,
                        icon: "error",
                        closeOnClickOutside: false
                    }).then(function () {
                    })
                }
            })
        } catch (error) {
            console.error("error: " + error);
        }
    }

    renderUpperReply(data);
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

            let tbody = $("#ReplyTableBody");
            tbody.empty();
            replyLoad(currentPage);

            let replyForm = document.getElementById('replyForm');
            replyForm.value = ""


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

    const paginationContainer = document.getElementById("pagination-container");
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
        previousLink.innerText = "이전";
        previousLink.addEventListener("click", function () {
            if (currentPage > 1) {
                replyLoad(currentPage - 1);
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
                replyLoad(i);
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
        nextLink.innerText = "다음";
        nextLink.addEventListener("click", function () {
            replyLoad(currentPage + 1);
        });
        nextLi.appendChild(nextLink);
        ulElement.appendChild(nextLi);

        navElement.appendChild(ulElement);
        paginationContainer.appendChild(navElement);
    }

}

//대댓글 입력 폼
function rowReplyInsertForm(replyId, thisRow) {
    let rowReplyFormArea = document.createElement('div');
    rowReplyFormArea.id = "rowReplyFormArea_" + replyId;

    let replyForm = document.createElement('textarea');
    replyForm.id = "replyForm_" + replyId;
    replyForm.placeholder = '답글을 입력하세요.';
    replyForm.classList.add('form-control', 'text');

    let addReplyBtn = document.createElement('button');
    addReplyBtn.type = 'button';
    addReplyBtn.classList.add('btn', 'btn-primary');
    addReplyBtn.id = 'addReplyBtn_' + replyId;
    addReplyBtn.textContent = '등록';
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
        rowReplyFormArea.closest('.child-row').remove();
    });
    thisRow.find('#rowReplyFormOpen_' + replyId).prop('disabled', true);


    if (thisRow.next().length > 0 && thisRow.next().hasClass('child-row')) {
        // 이미 child-row가 있으면 해당 행에 추가
        thisRow.next().append(rowReplyFormArea);
    } else {
        // child-row가 없으면 새로운 행을 생성하여 추가
        let childRow = $("<tr class='child-row'>").append($("<td colspan='5'>").append(rowReplyFormArea));
        thisRow.after(childRow);
    }
    rowReplyFormArea.setAttribute('colspan', '4')
    rowReplyFormArea.append(replyForm, addReplyBtn, cancelBtn);

    if (rowReplyFormArea.hasClass('child-row')) {
        // 대댓글의 경우
        // 이미 child-row가 있으면 해당 행에 추가
        if (row.next().length > 0 && row.next().hasClass('child-row')) {
            row.next().append(rowReplyFormArea);
        } else {
            // child-row가 없으면 새로운 행을 생성하여 추가
            let childRow = $("<tr class='child-row'>").append($("<td colspan='5'>").append(rowReplyFormArea));
            row.after(childRow);
            renderedRows++; // 대댓글이 추가될 때 렌더링된 행의 수 증가
        }
    } else {
        // 일반 댓글인 경우
        tbody.append(row);
    }

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
        },
        error: function (error) {
            console.error("error: " + error);
        }
    })

}

//대댓글 로드
function rowReplyLoad(data) {
    // 시간순으로 정렬
    const sortedHistoryList = data.historyList.sort((a, b) => a.writeDate - b.writeDate);

    const commentMap = new Map(); // 댓글 ID를 키로 갖는 Map
    const rootComments = []; // 최상위 댓글을 저장하는 배열

    // 댓글을 Map에 추가하고 부모 댓글에 대댓글 추가
    sortedHistoryList.forEach((comment) => {
        comment.replies = []; // 대댓글을 저장할 배열 추가
        commentMap.set(comment.replyId, comment);


        let parentComment; // 변수를 선언하고 한 번만 할당하도록 변경

        if (comment.upperReplyId && commentMap.has(comment.upperReplyId)) {
            parentComment = commentMap.get(comment.upperReplyId);

            if (parentComment) {
                parentComment.replies.push(comment);
            }
        } else if (comment.upperReplyId) {
            // 대댓글인 경우
            parentComment = commentMap.get(comment.upperReplyId);

            if (parentComment) {
                parentComment.replies.push(comment);
            }
        } else {
            // 최상위 댓글인 경우 rootComments 배열에 추가
            rootComments.push(comment);
        }
    })

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

// 댓글 수정 폼 생성
function replyUpdateForm(replyId, content, thisRow) {
    // 수정 폼을 감싸는 셀 생성
    let rowReplyFormArea = document.createElement('div');
    rowReplyFormArea.id = "rowReplyFormArea_" + replyId;


    let updateBtnId = 'replyUpdateBtn' + replyId;
    $(updateBtnId).prop('disabled', true);

    // 수정 폼의 텍스트 영역 생성
    let replyForm = document.createElement('textarea');
    replyForm.id = "replyForm_" + replyId;
    replyForm.placeholder = '답글을 입력하세요.';
    replyForm.classList.add('form-control', 'text');
    replyForm.value = content;

    // 댓글 등록 버튼 생성
    let addReplyBtn = createButton('addReplyBtn_' + replyId, '댓글 등록', 'btn-primary');

    // 닫기 버튼 생성
    let cancelBtn = createButton('cancelReplyBtn_' + replyId, '닫기', 'btn-danger');

    // 행 생성
    let rowForm = document.createElement('tr');
    rowForm.id = 'rowReplyFormArea__' + replyId;

    // 셀 생성
    let td = document.createElement('td');
    td.setAttribute('colspan', '4');

    // 셀에 자식 요소 추가
    td.append(replyForm, addReplyBtn, cancelBtn);

    // 행에 셀 추가
    rowForm.append(td);

    // 행을 테이블에 추가
    thisRow.after(rowForm); // 이벤트가 발생한 행 다음에 추가

    // 취소 버튼 이벤트 리스너 등록
    cancelBtn.addEventListener('click', function () {
        let updateBtnId = document.getElementById('replyUpdateBtn' + replyId);
        $(updateBtnId).prop('disabled', false);
        rowForm.remove();
    });

    // 등록 버튼 이벤트 리스너 등록
    addReplyBtn.addEventListener('click', function () {
        let fixedContent = replyForm.value;
        updateReply(replyId, fixedContent);
    });
}

// 버튼 생성하는 함수
function createButton(id, text, className) {
    let button = document.createElement('button');
    button.type = 'button';
    button.classList.add('btn', className);
    button.id = id;
    button.textContent = text;
    return button;
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
                                location.href = '/board/info'
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



