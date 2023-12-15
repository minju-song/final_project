

// newMsg.style.display = 'none';
let data = {};

let ws;
let sendBtn = document.getElementById('sendChat');
let msg = document.getElementById('sendArea');
let chatBox = document.getElementById('chattingDiv');

chatBox.scrollTop = chatBox.scrollHeight;
$(document).ready(function () {
	// console.log(memberId);
	//소켓생성
	fetch('/checkMessageAll?tradeChatRoomId=' + room.tradeId)
		.then(result => result.json())
		.then(resolve => console.log(resolve));

	ws = new WebSocket("ws://" + location.host + "/tradeChat?tradeId=" + trade.tradeId);
	//ws = new WebSocket("ws://www.holoyolo.site/tradeChat?tradeId=" + trade.tradeId);
	for (let i = 0; i < chats.length; i++) {
		chats[i].type = 'msg';
		chats[i].date = dateFormat(chats[i].realDate);
		drawChat(chats[i]);
	}

	//메시지수신
	ws.onmessage = function (msg) {
		let data = JSON.parse(msg.data);
		// console.log(data);
		// fetch('/checkMessageRead?tradeChatId=' + data.tradeId)
		// 	.then(result => result.text())
		// 	.then(resolve => console.log(resolve));
		fetch('/checkMessageAll?tradeChatRoomId=' + data.tradeId)
			.then(result => result.json())
			.then(resolve => console.log(resolve));

		data.date = data.date.substr(0, 21);
		drawChat(data);
	}
});

//엔터클릭
msg.onkeyup = function (e) {
	if (e.keyCode == 13 && e.shiftKey) {
		return;
	}
	else if (e.keyCode == 13) {
		send();
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
		data.tradeId = trade.tradeId;
		data.memberName = memberName;
		data.type = 'msg';
		let temp = JSON.stringify(data);

		// console.log(temp);

		fetch("/member/insertTradeChat", {
			method: "POST",
			body: temp,
			headers: {
				"Content-Type": "application/json"
			}
		}).then(response => response.json())
			.then(result => {
				// console.log(result);
				ws.send(temp);
			})

	}

	msg.value = '';
}

//채팅그려주는 함수
function drawChat(data) {
	if (data.type == 'msg') {
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
			p.innerHTML = '<span style="padding:0 15px;">' + data.date + '</span>' + ' 나';
		}
		else {
			div.classList.add('msg', 'other_div');
			div2.setAttribute('class', 'other');
			if (you.memberId == null) {
				p.innerHTML = '알 수 없음' + '<span style="padding:0 15px;">' + data.date + '</span>';
			}
			else {
				p.innerHTML = data.memberName + '<span style="padding:0 15px;">' + data.date + '</span>';
			}
		}


		divbox.appendChild(div2);
		divbox.appendChild(p);
		div.appendChild(divbox);
		chatBox.appendChild(div);
	}


	chatBox.scrollTop = chatBox.scrollHeight;

}

function dateFormat(str) {
	let date = new Date(str);
	let newDate = date.getFullYear() + '. ' + ((date.getMonth() + 1) <= 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) +
		'. ' + ((date.getDate()) <= 9 ? "0" + (date.getDate()) : (date.getDate())) + '.  ';
	let ampm = ((date.getHours()) < 12 ? "오전" : "오후");
	let hour;

	if (ampm == '오후') {
		if (date.getHours() != 12) {
			hour = date.getHours() - 12;
		}
		else {
			hour = date.getHours();
		}
	}
	else {
		hour = date.getHours();
	}

	newDate += ' ' + ampm + ' ' + hour + ':' + ((date.getMinutes()) <= 9 ? "0" + (date.getMinutes()) : (date.getMinutes()))
	return newDate;
}/**
 * 
 */