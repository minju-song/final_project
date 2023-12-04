


// 페이징
let currentPage = 1;
const recordsPerPage = 5; // 페이지당 표시할 레코드 수, 필요에 따라 조절

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

function formatDate(dateString) {
    let date = new Date(dateString);
    let year = date.getFullYear();
    let month = (date.getMonth() + 1).toString().padStart(2, '0');
    let day = date.getDate().toString().padStart(2, '0');
    return year + '-' + month + '-' + day;
}


