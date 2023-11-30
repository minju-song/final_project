/**
 * trade.js
 */
 
 let select = document.getElementById('searchTitle');
console.log(select);

//셀렉트 검색 관련
let search = '';
let val = 'title';

//넘어오는 데이터 사이즈
/*let totalCount = [[${ tradeList.list.size() }]];
console.log(totalCount);
let pageSize = 6;
let pageNumber = 1;*/

//필요한 총 페이지
/*let _totalPages = Math.floor(totalCount / 6);

if (totalCount % 6 > 0) {
    _totalPages++;
}
console.log(_totalPages);

$(document).ready(function () {
    //페이징

    function count() {
        fetch('/clubCnt')
            .then(resolve => resolve.json())
            .then(result => {
                _totalPages = Math.floor(result.total / 6);

                if (result.total % 6 > 0) {
                    _totalPages++;
                }
                if (_totalPages == 0) {
                    _totalPages = 1
                }
                console.log('총 데이터갯수 : ' + result.total);
            })
    }


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
            console.log(_totalPages + '>> 전체 페이지');
            callList(page, search);
        }
    });
});


let flag = true;

//페이지 별 데이터리스트 가져옴
function callList(page, search) {
    if (flag) {
        flag = false;
        if (search != null && search != '') {
            fetch('/clubCnt?search=' + search + '&searchTitle=' + val)
                .then(resolve => resolve.json())
                .then(result => {
                    _totalPages = Math.floor(result.total / 6);

                    if (result.total % 6 > 0) {
                        _totalPages++;
                    }
                    if (_totalPages == 0) {
                        _totalPages = 1
                    }
                    console.log('총 데이터갯수 : ' + result.total);
                })
            fetch('/clubPaging?page=' + page + '&search=' + search + '&searchTitle=' + val)
                .then(resolve => resolve.json())
                .then(result => {
                    console.log('총페이지' + _totalPages);

                    $("#pagination").twbsPagination("changeTotalPages", _totalPages, page);
                    drawClub(result.result);
                    flag = true;
                })
        }
        else {

            fetch('/clubPaging?page=' + page)
                .then(resolve => resolve.json())
                .then(result => {
                    _totalPages = Math.floor(totalCount / 6);

                    if (totalCount % 6 > 0) {
                        _totalPages++;
                    }
                    if (_totalPages == 0) {
                        _totalPages = 1
                    }
                    console.log(_totalPages);
                    $("#pagination").twbsPagination("changeTotalPages", _totalPages, page);
                    drawClub(result.result);
                    flag = true;
                })
        }
    }
}


//들어온 데이터 화면에 그려줌
function drawClub(clubArr) {

    console.log(clubArr);
    let clubList = document.getElementById('clubList');
    while (clubList.firstChild) {
        clubList.removeChild(clubList.firstChild);
    }
    for (let i = 0; i < clubArr.length; i++) {
        // console.log(clubArr[i]);
        let divCard = document.createElement('div');
        // divCard.setAttribute("class", "col-6");
        divCard.classList.add("col-5", 'card_club');
        // <img alt="네이버로그인" th:src="@{/user/images/login/icon_naver.png}">
        let divImg = document.createElement('div');
        let img = document.createElement('img');
        img.setAttribute("src", "/user/images/club/" + clubArr[i].clubProfileImg);
        img.style.width = '100px';
        img.style.height = '100px';
        divImg.appendChild(img);
        divImg.setAttribute("class", "club_img");
        // divImg.innerText = '이미지';

        let divInfo = document.createElement('div');
        divInfo.innerHTML = '모임명 : ' + clubArr[i].clubName + '<br>'
            + '모임소개 : ' + clubArr[i].clubIntro + '<br>'
            + '모임장 : ' + clubArr[i].leaderName + '<br>'
            + clubArr[i].joinCnt + ' / ' + clubArr[i].clubPeople + ' [가입인원 / 모집인원]<br>'
            + '절약예산 / 기간단위 <br>';

        let btn = document.createElement('button');
        if (joinClub != 'null') {
            let ck = false;
            joinClub.forEach(club => {
                if (club.clubId == clubArr[i].clubId) {
                    btn.innerText = '내 모임';
                    ck = true;
                }
            });

            if (ck == false) {
                if (clubArr[i].joinCnt >= clubArr[i].clubPeople) {
                    btn.innerText = '가입불가';
                    btn.disabled = true;
                }
                else if (clubArr[i].openScope == 'OA1') {
                    btn.innerText = '바로가입';
                    btn.onclick = function () { join(clubArr[i].clubId) };
                }
                else {
                    btn.innerText = '가입신청';
                    btn.onclick = function () { submit(clubArr[i].clubId) }
                }
            }
        }

        else {
            if (clubArr[i].openScope == 'OA1') {
                btn.innerText = '바로가입';
                btn.onclick = function () { join(clubArr[i].clubId) };
            }
            else {
                btn.innerText = '가입신청';
                btn.onclick = function () { submit(clubArr[i].clubId, clubArr[i].clubLeader) };
            }
        }


        // data-target="#updateCard" data-whatever="수정"
        // th:data-cardno='${cardNo}' th:data-cardcompany='${cardCompany}'

        divInfo.appendChild(btn);

        divInfo.setAttribute("class", "club_info");
        divCard.appendChild(divImg);
        divCard.appendChild(divInfo);
        divCard.onclick = function () { test(clubArr[i].clubId); }
        // console.log(clubArr[i]);
        // p.innerText = clubArr[i].clubName + ' ' + clubArr[i].clubLeader;
        clubList.appendChild(divCard);
    }
}

//즉시 클럽가입
function join(clubId) {
    console.log(clubId + '번 클럽 즉시 가입');
    if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;
    if ([[${ result.userId }]] == "null") {
        location.href = "/loginForm";
    }
    else {
        Swal.fire({
            title: "클럽에 바로 가입하시겠습니까?",
            text: "즉시 가입이 가능합니다.",
            icon: "question",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes"
        }).then((result) => {
            if (result.isConfirmed) {
                fetch("/member/joinClub?clubId=" + clubId)
                    .then(response => response.text())
                    .then(result => {
                        if (result == "success") {
                            Swal.fire({
                                title: "가입완료",
                                text: "가입이 완료되었습니다.",
                                icon: "success"
                            }).then((result) => {
                                location.reload();

                            });
                        }
                        else {
                            Swal.fire({
                                title: "가입실패",
                                text: "오류났음",
                                icon: "error"
                            });
                        }

                        // location.reload();
                    })
            }
        });
    }
}

//클럽 가입 이벤트
function submit(clubId, clubLeader) {
    console.log(clubId + '번 클럽 가입 신청');
    if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;

    if ([[${ result.userId }]] == "null") location.href = "/loginForm";
    else {
        Swal.fire({
            title: "클럽에 가입 신청을 하시겠습니까?",
            text: "가입 허가를 받아합니다.",
            icon: "question",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes"
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    input: "textarea",
                    inputLabel: "가입 사유를 적어주세요.",
                    inputPlaceholder: "Type your message here...",
                    inputAttributes: {
                        "aria-label": "Type your message here"
                    },
                    showCancelButton: true
                }).then((text) => {
                    if (text) {
                        // fetch()
                        Swal.fire({
                            title: "신청 완료",
                            text: "신청이 완료되었습니다.",
                            icon: "success"
                        });
                    }
                });
            }
        });
    }
}

function test(clubId) {
    if ([[${ result.userId }]] == "null") location.href = "/loginForm";

    location.href = '/member/club/clubPage?clubId=' + clubId;
}


//셀렉트박스 값 바뀔 때마다 value바꿔줌
select.addEventListener('change', function () {
    val = select.value;
    search = search_input.value;
    console.log('셀렉트' + val);
    callList(1, search);
})


//검색어 입력할 때마다 데이터 호출 및 페이징
let search_input = document.getElementById('search_input');
search_input.addEventListener('keyup', function () {
    search = search_input.value;
    callList(1, search);
})*/