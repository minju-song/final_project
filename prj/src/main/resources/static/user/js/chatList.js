console.log(list);

let ws;
let test = document.getElementById('test')
// if (Number(notread.innerText) == 0) notread.innerText = '';
$(document).ready(function () {
    // console.log(memberId);

    for (let i = 0; i < list.length; i++) {
        ws = new WebSocket("ws://" + location.host + "/tradeChat?tradeId=" + list[i].tradeId);

        ws.onmessage = function (msg) {
            let data = JSON.parse(msg.data);
            console.log(data);
            let notread = document.getElementById(data.tradeId).querySelector('.not');
            // document.getElementById(data.tradeId).querySelector('.not')
            let last = document.getElementById(data.tradeId).querySelector('.lastchat');
            console.log(notread);
            notread.innerText = Number(notread.innerText) + 1;
            last.innerHTML = '마지막 메시지 : ' + data.msg;

            notread.style.display = 'inline-block';
        }
    }
    //소켓생성


    //메시지수신
});/**
 * 
 */


//채팅방나가기
function deleteChat(chatId) {
    console.log(chatId + '채팅 삭제하고싶음');
    Swal.fire({
        title: "채팅방을 나가시겠습니까?",
        text: "즉시 채팅방이 삭제됩니다.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {

        }
    })
}