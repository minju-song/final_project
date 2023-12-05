


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
        previousLink.innerText = "Previous";
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
        nextLink.innerText = "Next";
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
function loadData(page) {
    currentPage = page;
    let start = (currentPage - 1) * recordsPerPage;
    let end = start + recordsPerPage;

    $.ajax({
        type: 'POST',
        url: '/boardLoad',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify({ "start": start, "end": end, "type": "AA2" }),
        success: function (data) {

            updateTable(data, page);
            // 페이징 처리
            let totalPages = Math.ceil(data.totalRecords / recordsPerPage);
            setupPagination(totalPages);
        },
        error: function (request, status, error) {
            console.error("code: " + request.status);
            console.error("message: " + request.responseText);
            console.error("error: " + error);
        }
    });
}
//테이블 갱신
function updateTable(data, page) {

    currentPage = page;
    let endPage = Math.ceil(data.totalRecords / recordsPerPage);
    console.log(endPage)

    let tbody = $("#boardTableBody");
    tbody.empty(); // 기존 데이터를 지우고 새로운 데이터로 갱신

    if (data && data.historyList && data.historyList.length > 0) {
        // 데이터가 있을 경우 테이블에 행 추가

        data.historyList.forEach(function (item, index) {
            let row = $("<tr>");
            row.attr("onclick", `location.href='/member/BoardInfo?boardId=${item.boardId}'`);
            if (data.totalRecords > page * recordsPerPage) {
                row.append($("<td>").text(data.totalRecords - index - (page - 1) * recordsPerPage));
                // row.append($("<td>").text(index + 1));
            } else {
                row.append($("<td>").text(data.totalRecords - (index + (page - 1) * recordsPerPage)));
            }
            row.append($("<td>").text(item.title));
            row.append($("<td>").text(formatDate(item.writeDate)));
            row.append($("<td>").text(item.nickname));
            row.append($("<td>").text(item.likeCount + '/' + item.views).css("text-align", "center"));

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
    document.getElementById('writer').innerText = post.nickname;
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
            console.log(data)
            updateReplyTable(data, page);

            let totalPages = Math.ceil(data.totalRecords / recordsPerPage);
            setupPagination(totalPages);
        },
        error: function (error) {

            console.error("error: " + error);
        }
    });

}

//댓글 테이블 생성
function updateReplyTable(data, page) {

    currentPage = page;
    let tbody = $("#ReplyTableBody");
    tbody.empty(); // 기존 데이터를 지우고 새로운 데이터로 갱신

    if (data && data.historyList && data.historyList.length > 0) {
        // 데이터가 있을 경우 테이블에 행 추가

        data.historyList.forEach(function (item, index) {
            console.log(item)
            let row = $("<tr>");
            if (data.totalRecords > page * recordsPerPage) {
                row.append($("<td>").text(data.totalRecords - index - (page - 1) * recordsPerPage));
            } else {
                row.append($("<td>").text(data.totalRecords - (index + (page - 1) * recordsPerPage)));
            }
            row.append($("<td>").text(item.content));
            row.append($("<td>").text(formatDate(item.writeDate)));
            row.append($("<td>").text(item.nickname));

            let rowReplyAddBtn = $("<button>").text("댓글").addClass('btn', 'btn-secondary').attr('id', 'rowReplyFormOpen').attr('value', item.replyId);
            rowReplyAddBtn.on('click', function () {
                rowReplyInsertForm();
            });
            row.append(rowReplyAddBtn);

            let rowReplyFormArea = $("<div>").attr('id', 'rowReplyFormArea');
            row.append(rowReplyFormArea);
            tbody.append(row);
        });
    } else {
        // 데이터가 없을 경우 테이블에 메시지 추가
        let noDataMessage = $("<tr>").append($("<td colspan='5' class='text-center'>").text("댓글이 없습니다."));
        tbody.append(noDataMessage);
    }

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

//댓글 입력 폼 생성
function openForm() {
    let replyForm = document.createElement('textarea');
    replyForm.id = 'replyForm';
    replyForm.placeholder = ' 답글을 입력하세요.';
    replyForm.classList.add('form-control', 'text');
    document.getElementById('replyFormArea').append(replyForm);

    let addReplyBtn = document.createElement('button');
    addReplyBtn.type = 'button';
    addReplyBtn.id = "addReplyBtn";
    addReplyBtn.classList.add('btn', 'btn-primary')
    addReplyBtn.textContent = '댓글 등록';

    let cancelBtn = document.createElement('button');
    cancelBtn.type = 'button';
    cancelBtn.id = "cancelReplyBtn";
    cancelBtn.classList.add('btn', 'btn-danger');
    cancelBtn.textContent = '닫기';

    addReplyBtn.addEventListener('click', insertReply)
    cancelBtn.addEventListener('click', function () {
        let replyForm = document.getElementById('replyForm');
        replyForm.remove();
        document.getElementById('replyFormOpen').disabled = false;

        let addReplyBtn = document.getElementById('addReplyBtn');
        addReplyBtn.remove();

        let cancelReplyBtn = document.getElementById('cancelReplyBtn')
        cancelReplyBtn.remove()
    });
    document.getElementById('addreplyBtn').append(addReplyBtn, cancelBtn);
    document.getElementById('replyFormOpen').disabled = true;

};

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
            console.log(data)
            replyLoad(currentPage);

            let replyForm = document.getElementById('replyForm');
            replyForm.remove();
            document.getElementById('replyFormOpen').disabled = false;

            let addReplyBtn = document.getElementById('addReplyBtn');
            addReplyBtn.remove();

            let cancelReplyBtn = document.getElementById('cancelReplyBtn')
            cancelReplyBtn.remove()
        },
        error: function (request, status, error) {
            console.error("code: " + request.status);
            console.error("message: " + request.responseText);
            console.error("error: " + error);
        }
    });
}

// 페이지 버튼 생성
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
        previousLink.innerText = "Previous";
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
        nextLink.innerText = "Next";
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
function rowReplyInsertForm() {
    let rowReplyFormArea = $("#rowReplyFormArea");
    let replyForm = $("<textarea>").attr('id', 'rowReplyForm').attr('placeholder', ' 답글을 입력하세요.').addClass('form-control');
    rowReplyFormArea.append(replyForm);


    let addReplyBtn = document.createElement('button');
    addReplyBtn.type = 'button';
    addReplyBtn.id = 'addRowReplyBtn';
    addReplyBtn.classList.add('btn', 'btn-primary');
    addReplyBtn.textContent = '댓글등록'
    addReplyBtn.addEventListener('click', insertReply);


    let cancelBtn = document.createElement('button');
    cancelBtn.type = 'button';
    cancelBtn.id = 'cancelRowReplyBtn';
    cancelBtn.classList.add('btn', 'btn-danger');
    cancelBtn.textContent = '취소'
    cancelBtn.addEventListener('click', function () {
       
        let rowReplyForm = document.getElementById('rowReplyForm');
        rowReplyForm.remove();
        document.getElementById('rowReplyFormOpen').disabled = false;

        let addReplyBtn = document.getElementById('addRowReplyBtn');
        addReplyBtn.remove();

        let cancelReplyBtn = document.getElementById('cancelRowReplyBtn');
        cancelReplyBtn.remove();
    });

    let testBtn = document.createElement('button');
    testBtn.type = 'button';
    testBtn.id = 'testBtn';
    testBtn.classList.add('btn', 'btn-secondary');
    testBtn.textContent = 'TEST'
    testBtn.addEventListener('click', function(){
       

        let searchReplyId1 = this.parentNode.parentNode;

        // 부모 노드의 부모 노드의 첫 번째 자식의 5번째자식
        let searchReplyId2 = searchReplyId1.childNodes[0];
        let searchReplyId = searchReplyId2.childNodes[5]
        console.log(searchReplyId1)
        console.log(searchReplyId2)
        console.log(searchReplyId)
    })
    

    rowReplyFormArea.append(addReplyBtn, cancelBtn, testBtn);
    rowReplyFormArea.insertAfter(replyForm.closest("tr"));
    $("#rowReplyFormOpen").prop('disabled', true);
    document.getElementById('rowReplyFormOpen').disabled = true;
}
//대댓글 등록

function rowReply() {
    let content = document.getElementById('rowReplyForm')
    console.log(content);
}

