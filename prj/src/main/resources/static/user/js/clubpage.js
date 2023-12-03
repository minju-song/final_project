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




//클럽탈퇴
function outClub() {
    console.log(userId + '가 ' + clubId + '탈퇴원함');
    if (leader == true) {
        alert("먼저 모임장을 위임해주세요.");
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
    if (clubLeader != userId) {
        console.log("방장아님");
        return;
    }

    console.log("방장임");
    fetch('/member/mandate?clubId=' + clubId + "&clubLeader=" + memberId)
        .then(resolve => resolve.text())
        .then(result => {
            console.log(result);
            location.reload();
        });
}
