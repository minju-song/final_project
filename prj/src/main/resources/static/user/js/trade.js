/**
 * trade.js
 */
 

//넘어오는 데이터 사이즈
let pageSize = 6;

$(document).ready(function () {

    $('#pagination').twbsPagination({
        startPage: 1,
        totalPages: 1,
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
    callList(1); 
});

let flag = true;

//페이지 별 데이터리스트 가져옴
function callList(page) {

    // 검색 관련
    let searchTitle = document.getElementById('searchTitle').value;
    let sellCheck = document.querySelector('[type=checkbox]').checked;
    let tradeCategory =  document.getElementById('searchCategory').value;
    let search = document.getElementById('search_input').value;
	console.log(search, tradeCategory, sellCheck)

    if (flag) {
        flag = false; 
         
        fetch('/tradePaging?page=' + page + 
                        '&search=' + search      + 
                        '&searchTitle=' + searchTitle +
                        '&tradeCategory=' + tradeCategory + 
                        '&sellCheck=' + sellCheck)
        .then(resolve => resolve.json())
        .then(result => {

            _totalPages = Math.floor(result.total / pageSize);
            if (result.total % pageSize > 0) {
                _totalPages++;
            }
            if (_totalPages == 0) {
                _totalPages = 1
            }
            console.log('총페이지' + _totalPages);

            $("#pagination").twbsPagination("changeTotalPages", _totalPages, page);
            drawTrade(result.list);
            flag = true;

        })
    }    
}

//들어온 데이터 화면에 그려줌
function drawTrade(tradeArr){
	console.log(tradeArr);
	let tradeList = document.getElementById('tradeList');
	while (tradeList.firstChild) {
        tradeList.removeChild(tradeList.firstChild);
    }
	for (let i = 0; i < tradeArr.length; i++) {
		let divCol = document.createElement('div');
		divCol.classList.add('col', 'mb-5');
		let divCard = document.createElement('div');
		divCard.classList.add('card', 'tradeSize');
		divCol.appendChild(divCard);
		
		let divTradeType = document.createElement('div');
		divTradeType.classList.add('badge', 'position-absolute');
		divTradeType.innerText = tradeArr[i].tradeType;
		divCard.appendChild(divTradeType);
		
		let promiseStatus = document.createElement('div');
		promiseStatus.classList.add('badge', 'position-absolute');
		promiseStatus.style.right = '0.5rem';
		promiseStatus.innerText = tradeArr[i].promiseStatus;
		divCard.appendChild(promiseStatus);
		
		let img = document.createElement('img');
		img.classList.add('card-img-top');
		img.setAttribute("src", "user/images/" + "person.jfif");
		img.style.width = '294px';
        img.style.height = '197px';
		divCard.appendChild(img);
		
		let divBody = document.createElement('div');
		divBody.classList.add('card-body', 'p-4');
		divCard.appendChild(divBody);
		
		let divText = document.createElement('div');
		divText.classList.add('text-center');
		divBody.appendChild(divText);
		
		let title = document.createElement('h3');
		title.classList.add('fw-bolder');
		title.innerText = tradeArr[i].title;
		divText.appendChild(title);
		
		let divPrice = document.createElement('div');
		divText.appendChild(divPrice);
		
		let price = document.createElement('span');
		price.classList.add('text-muted', 'text-decoration-line-through');
		price.innerText = tradeArr[i].price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')+ '원';
		divPrice.appendChild(price);
		
		let divCategory = document.createElement('div');
		divBody.appendChild(divCategory);
		
		let category = document.createElement('span');
		category.classList.add('text-muted');
		category.innerText = tradeArr[i].tradeCategory;
		category.style.position = 'absolute';
		category.style.left = '-0.9rem';
        category.style.bottom = '-2.4rem';
		divCategory.appendChild(category);
		
		let divWriteDate = document.createElement('div');
		divBody.appendChild(divWriteDate);
		
		let writeDate = document.createElement('span');
		writeDate.classList.add('text-muted');
		writeDate.innerText = '2023-11-10';//tradeArr[i].writeDate;
		writeDate.style.position = 'absolute';
        writeDate.style.right = '-0.9rem';
        writeDate.style.bottom = '-2.4rem';
		divWriteDate.appendChild(writeDate);
		
		divCard.onclick = function(){ submit(tradeArr[i].tradeId, tradeArr[i].sellerId) };
		
		tradeList.appendChild(divCol);
	}
}

//중고거래 상세페이지 이동
function submit(tradeId, sellerId){
	console.log(tradeId, sellerId);
	location.href = '/member/tradeInfo?tradeId=' + tradeId + '&sellerId=' + sellerId;
}

//체크박스 선택
let check = document.querySelector('[type=checkbox]');
check.addEventListener('click', function(e){
    callList(1);
});

//카테고리 선택
let category = document.getElementById('searchCategory');
category.addEventListener('change', function () {
    callList(1);
})

//셀렉트박스 값 바뀔 때마다 value바꿔줌
document.getElementById('search_input').addEventListener('change', function () {
    callList(1);
})


//검색어 입력할 때마다 데이터 호출 및 페이징
let search_input = document.getElementById('search_input');
search_input.addEventListener('keyup', function () {
    callList(1);
})

//card 클릭 시 상세보기 창 이동
