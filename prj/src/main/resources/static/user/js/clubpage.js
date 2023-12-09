/**
 * 
 */

//url에서 클럽아이디 가져오기
let urlParams = new URLSearchParams(window.location.search);
let clubId = urlParams.get('clubId');

$(function () {

    $('#updateBudget').on('show.bs.modal', function (event) {

        let budgetprice = $(event.relatedTarget).data('budgetprice');
        console.log(budgetprice);
        document.getElementById('clubBudgetPrice').value = budgetprice;

        let clubIdInput = document.getElementById('clubId');
        clubIdInput.value = clubId

        let selectBox2 = document.getElementById('clubBudgetUnitSelect');
        let len2 = selectBox2.options.length;
        let budgetunit = $(event.relatedTarget).data('budgetunit');
        console.log(budgetunit);
        if (budgetunit == '일') budgetunitval = 'YA1';
        else if (budgetunit == '주') budgetunitval = 'YA2';
        else if (budgetunit == '월') budgetunitval = 'YA3';
        console.log(budgetunitval);
        for (let i = 0; i < len2; i++) {
            if (selectBox2.options[i].value == budgetunitval) {
                selectBox2.options[i].selected = true;
            }
        }
    });
})

function clubRemove() {
    Swal.fire({
        title: "모임을 삭제하시겠습니까?",
        text: "즉시 삭제가 진행됩니다.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/member/clubDelete?clubId=' + clubId)
                .then(resolve => resolve.text())
                .then(result => {
                    console.log(result);
                    if (result == 'success') {
                        Swal.fire({
                            title: "삭제완료",
                            text: "삭제가 완료되었습니다.",
                            icon: "success"
                        }).then((resolve) => {
                            location.href = '/clublist';

                        });
                    }
                    else {
                        Swal.fire({
                            title: "삭제실패",
                            text: "오류발생",
                            icon: "error"
                        }).then((resolve) => {
                            location.reload();

                        });
                    }
                })
        }
    })
}



//클럽탈퇴
function outClub() {
    console.log(userId + '가 ' + clubId + '탈퇴원함');
    if (leader == true) {
        Swal.fire("먼저 모임장을 위임해주세요.");
        return;
    }

    Swal.fire({
        title: "모임을 탈퇴하시겠습니까?",
        text: "즉시 탈퇴가 진행됩니다.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/member/outClub?clubId=' + clubId)
                .then(resolve => resolve.text())
                .then(result => {
                    if (result == "success") {
                        Swal.fire({
                            title: "탈퇴완료",
                            text: "탈퇴가 완료되었습니다.",
                            icon: "success"
                        }).then((resolve) => {
                            location.reload();

                        });
                    }
                    else {
                        Swal.fire({
                            title: "탈퇴실패",
                            text: "오류났음",
                            icon: "error"
                        }); then((resolve) => {
                            location.reload();

                        });
                    }
                });
        }
    });
}

//클럽가입신청
function sendMail() {
    console.log("실행");
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
                    let clubVO = { clubName: clubName, clubId: clubId, clubLeader: clubLeader, text: text.value, type: 'rejoin' };
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

//클럽재가입
function rejoin() {
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
                    let clubVO = { clubName: clubName, clubId: clubId, clubLeader: clubLeader, text: text.value, type: 'rejoin' };
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

//클럽바로가입
function join() {
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

//방장위임
function mandate(memberId) {
    if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;
    if (clubLeader != userId) {
        console.log("방장아님");
        return;
    }

    Swal.fire({
        title: "모임장을 위임하시겠습니까?",
        text: "회원의 동의를 받아합니다.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                input: "textarea",
                inputLabel: "위임 사유를 적어주세요.",
                inputPlaceholder: "Type your message here...",
                inputAttributes: {
                    "aria-label": "Type your message here"
                },
                showCancelButton: true
            }).then((text) => {
                if (text) {
                    let clubVO = { clubName: clubName, clubId: clubId, clubLeader: memberId, text: text.value, type: 'mandate' };
                    fetch('/sendmail/mandate', {
                        method: 'POST',
                        headers: {
                            "Content-Type": `application/json`, // application/json 타입 선언
                        },
                        body: JSON.stringify(clubVO)
                    })
                        .then(resolve => resolve.text())
                        .then(result => {
                            if (result == 'success') {
                                Swal.fire({
                                    title: "위임 신청 완료",
                                    text: "위임 신청이 완료되었습니다.",
                                    icon: "success"
                                }).then((result) => {
                                    // location.reload();
                                })
                            }
                            else {
                                Swal.fire({
                                    title: "위임 신청 실패",
                                    text: "위임 신청을 실패습니다.",
                                    icon: "error"
                                }).then((result) => {
                                    // location.reload(); 
                                })
                            }
                        }).catch(err => console.log(err));
                }
            });
        }
    });

}

/**
 * 이미지 첨부
 */
console.log("test");

let realUpload = document.querySelector('.real_upload');
let upload = document.querySelector('.upload');

// 썸네일을 클릭하면 input[type="file"]에 클릭이벤트를 발생
upload.addEventListener('click', () => realUpload.click());

realUpload.addEventListener('change', (e) => imgFileSelectHandler(e))

function imgFileSelectHandler(e) {

    let files = e.target.files;
    let reader = new FileReader();

    // 이미지 화면에 로드
    reader.onload = function (e) {
        $(".upload").css("background-image", "url(" + e.target.result + ")");
    }
    reader.readAsDataURL(files[0]);
    console.log(reader.readyState); //0(로드되지않음), 1(데이터로딩중), 2(모든 요청이 읽기 완료)

    uploadImg();
}

//파일 업로드
function uploadImg() {
    let formData = new FormData();
    let file = document.querySelector('.real_upload').files[0];
    formData.append("img", file);
    formData.append("clubId", clubId);

    fetch('/member/clubUpdateImg', {
        method: 'post',
        body: formData
    })
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            const Toast = Swal.mixin({
                toast: true,
                position: 'center',
                showConfirmButton: false,
                timer: 2000,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.addEventListener('mouseenter', Swal.stopTimer)
                    toast.addEventListener('mouseleave', Swal.resumeTimer)
                }
            })
            if (data.result == 'success') {
                Toast.fire({
                    icon: 'success',
                    title: '프로필 사진이 변경되었습니다!'
                })
            } else {
                Toast.fire({
                    icon: 'error',
                    title: '죄송합니다, 다시 시도해 주세요'
                })
            }
        })
        .catch(err => console.log(err));
}

//회원정보모달
function memberInfo(mid, mname) {
    console.log(clubId + '클럽의 ' + mname + '회원을 조회');

    fetch('/clubSuccessMember5?memberId=' + mid + '&clubId=' + clubId)
        .then(resolve => resolve.json())
        .then(result => {
            console.log(result.list);
            let history = `<div style="height:300px; overflow-y: auto;">`;

            history += `<img src="/images/` + result.list[0].profileImg + `" style="width:100px; height:100px;object-fit: cover; border-radius:50px;">`
                + `<br><h3><strong>"` + result.list[0].memberIntro + `"</strong></h3>`
            for (let i = 0; i < result.list.length; i++) {
                let rankingText = ``;
                if (result.list[i].ranking == 1) rankingText = `&#129351; ` + result.list[i].ranking + `위 &#129351;`;
                else if (result.list[i].ranking == 2) rankingText = `&#129352; ` + result.list[i].ranking + `위 &#129352;`;
                else if (result.list[i].ranking == 3) rankingText = `&#129353; ` + result.list[i].ranking + `위 &#129353;`;
                else rankingText = `&#127881; ` + result.list[i].ranking + `위 &#127881;`;
                history += `<hr><div>
                <strong style="color:#7373e9;">` + rankingText + `</strong> `
                    + `<p>예산기간 : ` + dateFormat(result.list[i].startDate) + ` ~ ` + dateFormat(result.list[i].endDate) + '</p>'
                    + `</div>`
            }
            if (result.list.length == 0) {
                history = `<h4>내역이 없습니다.</h4>`;
            }
            history += `</div>`
            Swal.fire({
                title: "<strong>" + mname + "</strong><span style='font-size:smaller;'> 님의 지난 성적 (최근 5회)</span>",
                html: history,
                showCloseButton: true,
                showCancelButton: false,
                focusConfirm: false,
                confirmButtonText: `
            확인
            `,
                confirmButtonAriaLabel: "확인"
            });
        })
}

function dateFormat(str) {
    let date = new Date(str);
    let newDate = date.getFullYear() + '-' + ((date.getMonth() + 1) <= 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) +
        '-' + ((date.getDate()) <= 9 ? "0" + (date.getDate()) : (date.getDate()));
    return newDate;
}


