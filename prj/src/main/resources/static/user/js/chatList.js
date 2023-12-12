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