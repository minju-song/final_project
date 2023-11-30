/**
 * trade.js
 */
 
 let select = document.getElementById('searchTitle');
console.log(select);

//셀렉트 검색 관련
let search = '';
let val = 'title';

//넘어오는 데이터 사이즈

console.log(totalCount);
let pageSize = 6;
let pageNumber = 1;

//필요한 총 페이지
let _totalPages = Math.floor(totalCount / 6);

if (totalCount % 6 > 0) {
    _totalPages++;
}
console.log(_totalPages);

$(document).ready(function () {
    //페이징

    function count() {
        fetch('/tradeCnt')
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
            fetch('/tradeCnt?search=' + search + '&searchTitle=' + val)
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
            fetch('/tradePaging?page=' + page + '&search=' + search + '&searchTitle=' + val)
                .then(resolve => resolve.json())
                .then(result => {
                    console.log('총페이지' + _totalPages);

                    $("#pagination").twbsPagination("changeTotalPages", _totalPages, page);
                    drawTrade(result.result);
                    flag = true;
                })
        }
        else {
            fetch('/tradePaging?page=' + page)
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
                    drawTrade(result.result);
                    flag = true;
                })
        }
    }
}

//들어온 데이터 화면에 그려줌
function drawTrade(tradeArr){
	console.log(tradeArr);
	let tradeList = document.getElementById('tradeList');
	/*while (tradeList.firstChild) {
        tradeList.removeChild(tradeList.firstChild);
    }*/
	for (let i = 0; i < tradeArr.length; i++) {
		let divCard = document.createElement('div');
		divCard.classList.add('card', 'h-100');
		let divTradeType = document.createElement('div');
		divTradeType.classList.add('badge', 'position-absolute');
		divTradeType.innerText = tradeArr[i].tradeType;
		divCard.appendChild(divTradeType);
		let promiseStatus = document.createElement('div');
		promiseStatus.classList.add('badge', 'position-absolute');
		promiseStatus.style.right = '0.5rem';
		promiseStatus.innerText = tradeArr[i].promiseStatus;
		divCard.appendChild(promiseStatus);
		
		tradeList.appendChild(divCard);
	}
	
	/*for (let i = 0; i < clubArr.length; i++) {
        let divCard = document.createElement('div');
        divCard.classList.add("col-5", 'card_club');
        let divImg = document.createElement('div');
        let img = document.createElement('img');
        img.setAttribute("src", "images/" + clubArr[i].clubProfileImg);
        img.style.width = '100px';
        img.style.height = '100px';
        divImg.appendChild(img);
        divImg.setAttribute("class", "club_img");

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
                    if (club.joinDate == null) {
                        btn.innerText = '승인대기';
                        ck = true;
                        btn.disabled = true;
                    }
                    else {
                        btn.innerText = '내 모임';
                        ck = true;
                        btn.disabled = true;
                    }
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
                    btn.onclick = function () { submit(clubArr[i].clubName, clubArr[i].clubId, clubArr[i].clubLeader) }
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
                btn.onclick = function () { submit(clubArr[i].clubName, clubArr[i].clubId, clubArr[i].clubLeader) };
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
    }*/
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
})