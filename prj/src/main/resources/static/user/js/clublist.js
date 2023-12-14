

//셀렉트박스
let select = document.getElementById('searchTitle');



console.log(joinClub);


//셀렉트 검색 관련
//검색하는 데이터
let search = '';
//셀렉트값
let val = 'clubname';

//넘어오는 데이터 사이즈
let pageSize = 6;
let pageNumber = 1;

//필요한 총 페이지
let _totalPages = Math.floor(totalCount / 6);

if (totalCount % 6 > 0) {
    _totalPages++;
}

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
            callList(page, search);
        }
    });
});


let flag = true;

//페이지 별 데이터리스트 가져옴
function callList(page, search) {
    if (flag) {
        flag = false;

        //검색어가 있을 때
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
                    fetch('/clubPaging?page=' + page + '&search=' + search + '&searchTitle=' + val)
                        .then(resolve => resolve.json())
                        .then(result => {
                            console.log('총페이지' + _totalPages);

                            $("#pagination").twbsPagination("changeTotalPages", _totalPages, page);
                            drawClub(result.result);
                            flag = true;
                        })
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
                    $("#pagination").twbsPagination("changeTotalPages", _totalPages, page);
                    drawClub(result.result);
                    flag = true;
                })
        }
    }
}


//들어온 데이터 화면에 그려줌
function drawClub(clubArr) {
    console.log(clubArr)

    let clubList = document.getElementById('clubList');

    //자식데이터 다 삭제
    while (clubList.firstChild) {
        clubList.removeChild(clubList.firstChild);
    }

    //들어온 데이터들 차례대로 그려줌
    for (let i = 0; i < clubArr.length; i++) {

        //div만들어줌
        let divCard = document.createElement('div');
        divCard.classList.add("col-5", 'card_club');

        //이미지담을 div
        let divImg = document.createElement('div');
        let img = document.createElement('img');
        img.setAttribute("src", "images/" + clubArr[i].clubProfileImg);
        img.style.height = '100px';

        //이미지담을 div에 이미지담음
        divImg.appendChild(img);
        divImg.setAttribute("class", "club_img");

        //정보를 담을 div
        let divInfo = document.createElement('div');

        let maxChars = 18;

        let shortIntro = clubArr[i].clubIntro.length > maxChars
            ? clubArr[i].clubIntro.substring(0, maxChars) + "..."
            : clubArr[i].clubIntro;

        divInfo.innerHTML = '<div class="clubName">모임명 : ' + clubArr[i].clubName + '</div>'
            + '<div class="clubIntro">모임소개 : ' + shortIntro + '</div>'
            + '<div class="clubKing">모임장 : ' + clubArr[i].leaderName + '</div>'
            + '<div class="people">' + clubArr[i].joinCnt + ' / ' + clubArr[i].clubPeople + ' [가입인원 / 모집인원]</div>';

        //버튼생성
        let btn = document.createElement('button');

        //가입한 클럽이 있으면
        if (joinClub != 'null') {
            let ck = false;

            joinClub.forEach(club => {
                //가입한 클럽아이디랑 그려주는 클럽아이디가 같을 때
                if (club.clubId == clubArr[i].clubId) {
                    //가입날짜가 null이면 승인대기로 표시
                    if (club.joinDate == null) {
                        btn.innerText = '승인대기';
                        ck = true;
                        btn.disabled = true;
                    }
                    else if (club.stopDate != null) {
                        btn.innerText = '재가입';
                        ck = true;
                        btn.onclick = function () { rejoin(clubArr[i]) };
                    }
                    //null이 아니면 내 모임
                    else {
                        btn.innerText = '내 모임';
                        ck = true;
                        btn.disabled = true;
                    }
                }
            });

            //내가 가입한 클럽이 아닐 때
            if (ck == false) {
                //클럽가입원이 모집인원보다 많으면 가입불가
                if (clubArr[i].joinCnt >= clubArr[i].clubPeople) {
                    btn.innerText = '가입불가';
                    btn.disabled = true;
                }
                //바로 가입 가능한 클럽
                else if (clubArr[i].openScope == 'OA1') {
                    btn.innerText = '바로가입';
                    btn.onclick = function () { join(clubArr[i].clubId) };
                }
                //바로 가입 불가능한 클럽
                else {
                    btn.innerText = '가입신청';
                    btn.onclick = function () { submit(clubArr[i].clubName, clubArr[i].clubId, clubArr[i].clubLeader) }
                }
            }
        }

        //가입클럽이 없을 때
        else {
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
                btn.onclick = function () { submit(clubArr[i].clubName, clubArr[i].clubId, clubArr[i].clubLeader) };
            }
        }

        //버튼 넣어줌
        divInfo.appendChild(btn);

        divInfo.setAttribute("class", "club_info");
        divCard.appendChild(divImg);
        divCard.appendChild(divInfo);

        //divCard클릭 시 페이지이동
        divCard.onclick = function () { clubPage(clubArr[i].clubId); }
        clubList.appendChild(divCard);
    }
}

//재가입
function rejoin(club) {
    if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;

    console.log("재가입");
    console.log(club);

    Swal.fire({
        title: "모임에 재가입 신청을 하시겠습니까?",
        text: "재가입 무조건 허가를 받아합니다.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                input: "textarea",
                inputLabel: "재가입 사유를 적어주세요.",
                inputPlaceholder: "Type your message here...",
                inputAttributes: {
                    "aria-label": "Type your message here"
                },
                showCancelButton: true
            }).then((text) => {
                if (text) {
                    let clubVO = { clubName: club.clubName, clubId: club.clubId, clubLeader: club.clubLeader, text: text.value, type: 'rejoin' };
                    fetch('/sendmail/requestclub', {
                        method: 'POST',
                        headers: {
                            "Content-Type": `application/json`, // application/json 타입 선언
                        },
                        body: JSON.stringify(clubVO)
                    })
                        .then(result => console.log(result))
                        .then(result => {
                            Swal.fire({
                                title: "신청 완료",
                                text: "신청이 완료되었습니다.",
                                icon: "success"
                            }).then((result) => {
                                location.reload();
                            })
                        }).catch(err => console.log(err));
                }
            });
        }
    });
}

//즉시 클럽가입
function join(clubId) {

    //부모요소에게 이벤트가는거 방지
    if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;

    //로그인안했으면 로그인폼
    if (userId == "null") {
        location.href = "/loginForm";
    }
    else {
        Swal.fire({
            title: "모임에 바로 가입하시겠습니까?",
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
                                if (result.isConfirmed) {
                                    location.reload();
                                }

                            });
                        }
                        else {
                            Swal.fire({
                                title: "가입실패",
                                text: "오류났음",
                                icon: "error"
                            }); then((result) => {
                                if (result.isConfirmed) {
                                    location.reload();
                                }

                            });
                        }

                    })
            }
        });
    }
}

//클럽 가입 이벤트
function submit(clubName, clubId, clubLeader) {
    event.stopPropagation();
    if (event.stopPropagation) event.stopPropagation();
    else event.cancelBubble = true;

    //로그인안했으면 로그인으로
    if (userId == "null") location.href = "/loginForm";
    else {
        Swal.fire({
            title: "모임에 가입 신청을 하시겠습니까?",
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
                        let clubVO = { clubName: clubName, clubId: clubId, clubLeader: clubLeader, text: text.value, type: 'join' };
                        fetch('/sendmail/requestclub', {
                            method: 'POST',
                            headers: {
                                "Content-Type": `application/json`, // application/json 타입 선언
                            },
                            body: JSON.stringify(clubVO)
                        })
                            .then(result => console.log(result))
                            .then(result => {
                                Swal.fire({
                                    title: "신청 완료",
                                    text: "신청이 완료되었습니다.",
                                    icon: "success"
                                }).then((result) => {
                                    location.reload();
                                })
                            }).catch(err => console.log(err));
                    }
                });
            }
        });
    }
}

//클럽 상세페이지 이동
function clubPage(clubId) {
    if (userId == "null") location.href = "/loginForm";

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
})


