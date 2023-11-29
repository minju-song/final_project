/**
 * 
 */


let totalCount = [[${ list.size() }]];
console.log(totalCount);
let pageSize = 6;
let pageNumber = 1;

let _totalPages = totalCount / 6;

if (totalCount % 6 > 0) {
    _totalPages++;
}
console.log(_totalPages);
$('#pagination').twbsPagination({
    startPage: 1,
    totalPages: _totalPages,
    visiblePages: 5,

    first: '<span sris-hidden="true">«</span>',

    //" » "라는 문자열로 마지막글 방향을 표시
    last: '<span sris-hidden="true">»</span>',

    prev: "이전",
    next: "다음",
    onPageClick: function (event, page) {

        callList(page);
    }
});

function callList(page) {
    fetch('/clubPaging?page=' + page)
        .then(resolve => resolve.json())
        .then(result => {
            // console.log(result);
            drawClub(result);
        })
}


function drawClub(clubArr) {

    // console.log(clubArr);
    let clubList = document.getElementById('clubList');
    while (clubList.firstChild) {
        clubList.removeChild(clubList.firstChild);
    }
    for (let i = 0; i < clubArr.length; i++) {
        let p = document.createElement('p');
        // console.log(clubArr[i]);
        p.innerText = clubArr[i].clubName;
        clubList.appendChild(p);
    }
}

