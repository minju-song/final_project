/**
 * 
 */

console.log(memberName);
let data = {};

let ws;
let sendBtn = document.getElementById('sendChat');
let msg = document.getElementById('sendArea');
let chatBox = document.getElementById('chattingDiv');

chatBox.scrollTop = chatBox.scrollHeight;




$(document).ready(function () {
    console.log(memberId);
    //소켓생성
    ws = new WebSocket("ws://" + location.host + "/chat?clubId=" + clubId);

    getLatestNotice();

    //메시지수신
    ws.onmessage = function (msg) {
        let data = JSON.parse(msg.data);
        console.log(data);


        drawChat(data);
    }
});

//엔터클릭
msg.onkeyup = function (e) {
    if (e.keyCode == 13 && !e.shiftKey) {
        send();
    }
    else if (e.keyCode == 13 && e.shiftKey) {
        return;
    }

}

//보내기버튼 클릭
sendBtn.onclick = function () {
    send();
}

//메시지 전송
function send() {
    if (msg.value.trim() != '') {
        data.memberId = memberId;
        data.msg = msg.value;
        data.date = new Date().toLocaleString();
        data.clubId = clubId;
        data.memberName = memberName;
        data.type = 'msg';
        let temp = JSON.stringify(data);
        ws.send(temp);
        console.log(temp);

    }

    msg.value = '';
}

//메시지그려줌
function drawChat(data) {
    if (data.type == 'msg' || data.type == 'notice') {
        let div = document.createElement('div');


        let divbox = document.createElement('div');
        divbox.setAttribute('class', 'box');

        let div2 = document.createElement('div');


        div2.innerText = data.msg;

        let p = document.createElement('p');
        p.setAttribute('class', 'sender');

        if (data.memberId == memberId) {
            div.classList.add('msg', 'me_div');
            div2.setAttribute('class', 'me');
            p.innerHTML = '<span style="padding:0 15px;">' + data.date.substr(0, 21) + '</span>' + ' 나';
        }
        else {
            div.classList.add('msg', 'other_div');
            div2.setAttribute('class', 'other');
            p.innerHTML = data.memberName + '<span style="padding:0 15px;">' + data.date.substr(0, 21) + '</span>';
        }

        if (data.type == 'notice') {
            div2.setAttribute('id', 'noticeChat');
            getLatestNotice();
        }

        div2.onclick = function () { insertNotice(data) };
        divbox.appendChild(div2);
        divbox.appendChild(p);
        div.appendChild(divbox);
        chatBox.appendChild(div);
    }

    else if (data.type == 'enter') {
        let p = document.createElement('p');
        p.setAttribute('class', 'enter');
        p.innerText = data.msg
        chatBox.appendChild(p);
    }

    else if (data.type == 'size') {
        let sizeSpan = document.getElementById('size');
        sizeSpan.innerText = data.size + '명 접속 중';
    }



    chatBox.scrollTop = chatBox.scrollHeight;

}

//공지설정
function insertNotice(data) {
    console.log(data);


    let temp = JSON.stringify(data);

    Swal.fire({
        title: "공지사항으로 설정하시겠습니까?",
        text: "공지는 언제든 확인 가능합니다.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch("/member/insertChat", {
                method: "POST",
                body: temp,
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => response.json())
                .then(result => {
                    if (result.result == 'success') {
                        Swal.fire({
                            title: "등록완료",
                            text: "공지사항으로 등록되었습니다.",
                            icon: "success"
                        }).then((resolve) => {

                            if (data.msg.length > 10) {
                                data.msg = data.msg.substr(0, 10) + '...';
                            }

                            let notice = {};
                            notice.memberId = memberId;
                            notice.msg = memberName + '님이 [' + data.msg + '] 을(를) 새로운 공지사항으로 등록하였습니다.';
                            notice.date = new Date().toLocaleString();
                            notice.clubId = clubId;
                            notice.memberName = memberName;
                            notice.type = 'notice';
                            let not = JSON.stringify(notice);
                            ws.send(not);

                        });

                    }
                    else {
                        Swal.fire({
                            title: "등록실패",
                            text: "공지사항으로 등록 실패했습니다.",
                            icon: "error"
                        }).then((resolve) => {
                            // location.href = '/clublist';

                        });
                    }
                })
        }
    });
}

//제일 마지막 공지 가져오기
function getLatestNotice() {
    fetch('/getLatestNotice?clubId=' + clubId)
        .then(resolve => resolve.text())
        .then(result => {
            let noticeText = document.getElementById('noticeText');

            if (result.length > 68) {
                result = result.substr(0, 68) + '...';
            }
            else if (result.length == 0) {
                result = '등록된 공지사항이 없습니다.';
            }
            noticeText.innerText = result;
            console.log(result);
        })
}

//공지사항 리스트
function noticeList() {
    fetch('/getNoticeList?clubId=' + clubId)
        .then(resolve => resolve.json())
        .then(result => {
            console.log(result.list);
            let notice = `<div style="height:300px; overflow-y: auto;">`;
            for (let i = 0; i < result.list.length; i++) {
                notice += `<hr><div>` +
                    `<h4>` + result.list[i].msg.replace(/\n/g, '<br>') + `</h4><br>`
                    + `<p>` + result.list[i].memberName + '</p>'
                    + `<p>` + dateFormat(result.list[i].realDate) + '</p>'
                    + `</div>`
            }

            if (result.list.length == 0) {
                notice = `<h4>등록된 공지사항이 없습니다.</h4>`;
            }

            notice += `</div>`
            Swal.fire({
                title: "<strong>&#128276;공지사항&#128276;</strong>",

                html: notice,
                showCloseButton: false,
                showCancelButton: false,
                focusConfirm: false
            });
        })
}

function dateFormat(str) {
    let date = new Date(str);
    console.log(str, ' ', date);
    let newDate = date.getFullYear() + '년 ' + ((date.getMonth() + 1) <= 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) +
        '월' + ((date.getDate()) <= 9 ? "0" + (date.getDate()) : (date.getDate()))
        + '일  ' + ((date.getHours() + 1) <= 9 ? "0" + (date.getHours() + 1) : (date.getHours() + 1)) + '시 ' + ((date.getMinutes() + 1) <= 9 ? "0" + (date.getMinutes() + 1) : (date.getMinutes() + 1)) + '분';
    return newDate;
}
