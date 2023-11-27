
//const joinDate = /*[[${joinDate}]]*/
window.onload = function () { buildCalendar(); }    // 웹 페이지가 로드되면 buildCalendar 실행

let nowMonth = new Date();  // 현재 달을 페이지를 로드한 날의 달로 초기화
let today = new Date();     // 페이지를 로드한 날짜를 저장
today.setHours(0, 0, 0, 0);    // 비교 편의를 위해 today의 시간을 초기화

// 달력 생성 : 해당 달에 맞춰 테이블을 만들고, 날짜를 채워 넣는다.
function buildCalendar() {


    fetch('/getMonthPrice?payDate=' + dateFormat(nowMonth))
        .then(resolve => resolve.json())
        .then(result => {
            let price = result.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
            document.getElementById("sumByMonth").innerText = '💸 ' + (nowMonth.getMonth() + 1) + '월 총 소비 금액 : ' + price + '원 💸';
        })


    let firstDate = new Date(nowMonth.getFullYear(), nowMonth.getMonth(), 1);     // 이번달 1일
    let lastDate = new Date(nowMonth.getFullYear(), nowMonth.getMonth() + 1, 0);  // 이번달 마지막날

    let tbody_Calendar = document.querySelector(".Calendar > tbody");
    document.getElementById("calYear").innerText = nowMonth.getFullYear();             // 연도 숫자 갱신
    document.getElementById("calMonth").innerText = leftPad(nowMonth.getMonth() + 1);  // 월 숫자 갱신

    while (tbody_Calendar.rows.length > 0) {                        // 이전 출력결과가 남아있는 경우 초기화
        tbody_Calendar.deleteRow(tbody_Calendar.rows.length - 1);
    }

    let nowRow = tbody_Calendar.insertRow();        // 첫번째 행 추가           

    for (let j = 0; j < firstDate.getDay(); j++) {  // 이번달 1일의 요일만큼
        let nowColumn = nowRow.insertCell();        // 열 추가
    }

    for (let nowDay = firstDate; nowDay <= lastDate; nowDay.setDate(nowDay.getDate() + 1)) {   // day는 날짜를 저장하는 변수, 이번달 마지막날까지 증가시키며 반복  
        let nowColumn = nowRow.insertCell();        // 새 열을 추가하고



        let newDIV = document.createElement("p");
        newDIV.innerHTML = leftPad(nowDay.getDate());        // 추가한 열에 날짜 입력

        fetch('/getSuccess?startDate=' + dateFormat(nowDay))
            .then(resolve => resolve.text())
            .then(result => {
                if (result == 'SU1') {
                    newDIV.classList.add("success");
                }
                else if (result == 'SU2') {
                    // newDIV.className = "fail";
                    newDIV.classList.add("fail");
                }
                else if (result == 'SU3') {
                    newDIV.classList.add("ing");
                }
            });

        nowColumn.appendChild(newDIV);

        if (nowDay.getDay() == 6) {                 // 토요일인 경우
            nowRow = tbody_Calendar.insertRow();    // 새로운 행 추가
        }

        let newDate = dateFormat(nowDay);
        let ck = 0;
        // if (newDate.getMonth() == joinDate.getMonth() && newDate.getDate() >= joinDate.getDate()) {
        //     ck = 1
        // }
        newDIV.id = newDate;
        newDIV.value = newDate;

        if (nowDay < today) {                       // 지난날인 경우
            newDIV.classList.add("pastDay");
            newDIV.onclick = function () { choiceDate(this); }
        }
        else if (nowDay.getFullYear() == today.getFullYear() && nowDay.getMonth() == today.getMonth() && nowDay.getDate() == today.getDate()) { // 오늘인 경우           
            newDIV.className = "today";
            newDIV.onclick = function () { choiceDate(this); }
            // choiceDate(this);
        }
        else {                                      // 미래인 경우
            newDIV.className = "futureDay";

        }

        if (nowDay.getFullYear() == today.getFullYear() && nowDay.getMonth() == today.getMonth() && nowDay.getDate() == today.getDate()) {
            choiceDate(newDIV);
        }
    }
}


function dateFormat(date) {
    let newDate = date.getFullYear() + '-' + ((date.getMonth() + 1) <= 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) +
        '-' + ((date.getDate()) <= 9 ? "0" + (date.getDate()) : (date.getDate()));
    return newDate;
}

// 날짜 선택
function choiceDate(newDIV) {

    if (document.getElementsByClassName("choiceDay")[0]) {                              // 기존에 선택한 날짜가 있으면
        document.getElementsByClassName("choiceDay")[0].classList.remove("choiceDay");  // 해당 날짜의 "choiceDay" class 제거
    }
    newDIV.classList.add("choiceDay");           // 선택된 날짜에 "choiceDay" class 추가

    fetch('/getAb?payDate=' + dateFormat(new Date(newDIV.id)))
        .then(resolve => resolve.json())
        .then(result => {
            drawHistory(result, dateFormat(new Date(newDIV.id)));
            if (dateFormat(today) == dateFormat(new Date(newDIV.id))) {
                drawInput();
            }
        })
        .catch(err => console.log(err));
    //sumPrice


    console.log('오늘: ' + dateFormat(today));
    console.log('들어온값: ' + dateFormat(new Date(newDIV.id)));

    let paydate = dateFormat(new Date(newDIV.id));
    getSumPrice(paydate);
}

function getSumPrice(payDate) {
    fetch('/getSumPrice?payDate=' + payDate)
        .then(resolve => resolve.json())
        .then(result => {
            if (result == 0) {
                document.getElementById('sumPrice').innerText = '거래내역이 없습니다.';
            }
            else {
                let price = result.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                document.getElementById('sumPrice').innerText = '총 소비금액 : ' + price + '원';
            }
        })
}

function drawInput() {
    console.log("수기입력");

    let keys = ['inputOutput', 'paymentType', 'bankname', 'price', 'payStore', 'btn'];
    let form = document.createElement('form');
    let tr = document.createElement('tr');

    for (const key of keys) {
        console.log(key);
        let td = document.createElement('td');
        if (key == 'inputOutput' || key == 'paymentType') {
            let select = document.createElement('select');
            select.setAttribute("id", key);
            if (key == 'inputOutput') {
                let option1 = document.createElement('option')
                option1.innerText = '지출';
                option1.value = 'GA2';
                select.appendChild(option1);
                let option2 = document.createElement('option')
                option2.innerText = '소득';
                option2.value = 'GA1';
                select.appendChild(option2);
            }
            else {
                let option1 = document.createElement('option')
                option1.innerText = '현금';
                option1.value = 'GA1';
                select.appendChild(option1);

                let option2 = document.createElement('option')
                option2.innerText = '이체';
                option2.value = 'GA2';
                select.appendChild(option2);

            }
            td.appendChild(select);
        }
        else if (key == 'btn') {
            let btn = document.createElement('button');
            // btn.onclick = function () { choiceDate(this); }

            btn.innerText = '➕';
            btn.setAttribute("class", "mybtn");
            btn.onclick = function () { insertHistory(); }
            td.appendChild(btn);
        }
        else {
            let input = document.createElement('input');
            if (key == 'price') {
                input.setAttribute("placeholder", "(원)");
            }
            else if (key == 'bankname') {
                input.setAttribute("placeholder", "은행명");
            }
            else if (key == 'payStore') {
                input.setAttribute("placeholder", "사용처");
            }
            input.setAttribute("id", key);
            input.setAttribute("class", "input");
            td.appendChild(input);

        }
        td.setAttribute("required", "true");
        tr.appendChild(td);
    }
    tr.setAttribute("id", "inputTr");
    document.querySelector('#hisTable').appendChild(tr);

    // form.appendChild(tr);
    // let btn = document.createElement('button');
    // btn.onclick = function () { choiceDate(this); }

    // btn.innerText = '입력';
    // btn.setAttribute("class", "btn btn-primary");
    // btn.onclick = function () { insertHistory(); }
    // document.querySelector('#hisTable').appendChild(tr);
    // document.querySelector('#hisTable').appendChild(btn);
}

//오늘 거래내역 현금은 삭제 가능

//거래내역 수기등록
function insertHistory() {

    let inputOutput = document.getElementById("inputOutput").value;
    let paymentType = document.getElementById("paymentType").value;
    let bankname = document.getElementById("bankname").value;
    let price = document.getElementById("price").value;
    let payStore = document.getElementById("payStore").value;
    let data = { inputOutput, paymentType, bankname, price, payStore };
    $.ajax({
        type: 'POST',
        url: '/insertHistory',
        data: data,
        success: function (result) {
            console.log(result);
            data.abHistoryId = result;
            drawTr(data, 1, dateFormat(today));
            getSumPrice(dateFormat(today));
        },
        error: function () {
            // console.log(error);
        }
    })

    let inputs = document.querySelectorAll('input');

    inputs.forEach(function (input) {
        input.value = '';
    })


}

//거래내역 그려줌
function drawHistory(hisArr, thisDate) {
    let newThis = new Date(thisDate);
    // console.log(dateFormat(newThis) + ' >>> drawHistory 날짜')
    const parent = document.querySelector('#hisTable');

    //기존 거래내역 삭제
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }

    //거래내역 반복문돌리면서 그려줌
    hisArr.forEach(function (history) {
        drawTr(history, 0, dateFormat(newThis));
    })
}

function drawTr(history, ck, drawDate) {
    // console.log(drawDate + '>>>> drawTr날짜')
    console.log(history);
    let tr = document.createElement('tr');
    for (const key in history) {
        let td = document.createElement('td');
        if (key == 'inputOutput') {
            if (history[key] == 'GA2') {
                td.innerText = '지출';
            }
            else {
                td.innerText = '소득';
            }
            tr.appendChild(td);
        }
        else if (key == 'paymentType') {
            if (history[key] == 'GA1') {
                td.innerText = '현금';
            }
            else if (history[key] == 'GA2') {
                td.innerText = '이체';
            }
            else {
                td.innerText = '카드';
            }
            tr.appendChild(td);
        }
        else if (key == 'price') {
            let price = history[key].toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
            td.innerText = price + '원';
            tr.appendChild(td);
        }
        else if (key == 'bankname' || key == 'payStore') {
            td.innerText = history[key];
            tr.appendChild(td);
        }


        // else if (key == 'btn') {

        //     let btn = document.createElement('button');
        //     btn.innerText = '삭제';
        //     btn.onclick = function () { delHistory };
        //     td.appendChild(btn);
        //     tr.appendChild(td);
        // }

    }

    if (drawDate == dateFormat(today) && history.paymentType != 'GA3') {
        let td = document.createElement('td');
        console.log("오늘날짜임 >>> drawTr");
        let btn = document.createElement('button');
        btn.innerText = '❌';
        btn.onclick = function () { delHistory(history.abHistoryId) };
        btn.setAttribute("class", 'mybtn');
        td.appendChild(btn);
        tr.appendChild(td);
    }
    // $('#insertHist').before(tr);
    tr.setAttribute("id", 'history-' + history.abHistoryId);
    if (ck == 0) {
        document.querySelector('#hisTable').appendChild(tr);
    }
    else {
        document.querySelector('#inputTr').before(tr);
    }

}

function delHistory(id) {
    console.log(id + ' 삭제');
    Swal.fire({
        title: "등록된 거래내역을 삭제하시겠습니까?",
        text: "삭제된 내역은 복구될 수 없습니다.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/delHistory?abHistoryId=' + id)
                .then(response => response.json())
                .then(result => {
                    if (result.result == "success") {
                        Swal.fire({
                            title: "삭제완료",
                            text: "Your file has been deleted.",
                            icon: "success"
                        });
                        document.getElementById('history-' + id).remove();
                        getSumPrice(dateFormat(today));
                    }
                    else {
                        Swal.fire({
                            title: "삭제실패",
                            text: "Your file has been deleted.",
                            icon: "error"
                        });
                    }

                    // location.reload();
                })
        }
    });


}






//날짜같은지
const isSameDate = (date1, date2) => {
    return date1.getFullYear() === date2.getFullYear()
        && date1.getMonth() === date2.getMonth()
        && date1.getDate() === date2.getDate();
}

// 이전달 버튼 클릭
function prevCalendar() {
    nowMonth = new Date(nowMonth.getFullYear(), nowMonth.getMonth() - 1, nowMonth.getDate());   // 현재 달을 1 감소
    buildCalendar();    // 달력 다시 생성
}
// 다음달 버튼 클릭
function nextCalendar() {
    nowMonth = new Date(nowMonth.getFullYear(), nowMonth.getMonth() + 1, nowMonth.getDate());   // 현재 달을 1 증가
    buildCalendar();    // 달력 다시 생성
}

// input값이 한자리 숫자인 경우 앞에 '0' 붙혀주는 함수
function leftPad(value) {
    if (value < 10) {
        value = "0" + value;
        return value;
    }
    return value;
}
