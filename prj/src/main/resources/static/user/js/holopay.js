//계좌 등록 버튼 
let addAmountBtn = document.getElementById("addAmountBtn");
addAmountBtn.addEventListener('click', checkAmount);
function checkAmount() {
  let amountName = document.getElementById('amountName').value;
  let amountNum = document.getElementById('amountNum').value;
  console.log(amountName);
  console.log(amountNum);
  let memberFinanceInfo = {
    "bankname": amountName,
    "account": amountNum
  };

  $.ajax({
    type: 'POST',
    url: '/addFinanceInfo',
    contentType: 'application/json;charset=UTF-8',
    data: JSON.stringify(memberFinanceInfo)
  }).done(function (response) {
    Swal.fire({
      title: "",
      text: "계좌가 등록되었습니다.",
      icon: "success",
      closeOnClickOutside: false
    }).then(function () {
      location.reload()
    })
  }).fail(function (request, status, error) {
    // Handle the error response from the server
    console.log("code: " + request.status)
    console.log("message: " + request.responseText)
    console.log("error: " + error);

  })

}


//충전버튼
let rechargeBtn = document.getElementById('callHoloPayRechargeApiBtn');
rechargeBtn.addEventListener('click', rechargeCheck);
function rechargeCheck() {
  let rechargePrice = document.getElementById('rechargePrice').value;
  if (rechargePrice < 10000) {
    Swal.fire({
      title: "",
      text: "최소 충전금액은 10,000원 입니다.",
      icon: "info",
      closeOnClickOutside: false
    }).then(function () {

    })
  }
  callRechargeApi(rechargePrice);
}

//api 호출부
function callRechargeApi(rechargePrice) {
  console.log(rechargePrice);
  let callRechargeApiInfo = {
    "Tram": rechargePrice,
  };

  console.log(callRechargeApiInfo);
  $.ajax({
    type: 'POST',
    url: '/apireq',
    contentType: 'application/json;charset=UTF-8',
    data: JSON.stringify(callRechargeApiInfo)
  }).done(function (response) {
    let data = JSON.parse(response)
    if (data.resultCode == 1) {
      viewIcon = "success"
    } else if (data.resultCode == 4) {
      viewIcon = "error"
    }

    Swal.fire({
      title: "",
      text: data.resultMsg,
      icon: viewIcon,
      closeOnClickOutside: false
    }).then(function () {
      location.reload()

    });
  }).fail(function (request, status, error) {
    // Handle the error response from the server
    console.log("code: " + request.status)
    console.log("message: " + request.responseText)
    console.log("error: " + error);
  })

}
//반환버튼
let withdrawBtn = document.getElementById('callHoloPaywithdrawApi');



withdrawBtn.addEventListener('click', callwithdrawApi);
function callwithdrawApi() {
  let withdrawPrice = document.getElementById('withdrawPrice').value;
  console.log(withdrawPrice);
  let callwithdrawApiInfo = {
    "Tram": withdrawPrice,
    "accountNum": myAccountNum
  };

  console.log(callwithdrawApiInfo);
  $.ajax({
    type: 'POST',
    url: '/withdrawapireq',
    contentType: 'application/json;charset=UTF-8',
    data: JSON.stringify(callwithdrawApiInfo)
  }).done(function (response) {
    let data = JSON.parse(response)
    if (data.resultCode == 2) {
      viewIcon = "success"
    } else if (data.resultCode == 3) {
      viewIcon = "error"
    } else {
      viewIcon = "error"
    }
    Swal.fire({
      title: "",
      text: data.resultMsg,
      icon: viewIcon,
      closeOnClickOutside: false
    }).then(function () {
      if (data.resultCode == 2) {
        location.reload()
      }
    })
  }).fail(function (request, status, error) {
    // Handle the error response from the server
    console.log("code: " + request.status)
    console.log("message: " + request.responseText)
    console.log("error: " + error);



  })

}
let search = document.getElementById('searchHpHistory');
search.addEventListener('change', loadData);

function loadData() {

let term = $("#searchHpHistory").val();
  $.ajax({
    type: 'POST',
    url: '/loadhistory',
    contentType: 'application/json;charset=UTF-8',
    data: JSON.stringify({ "term": term }),
    success: function (data) {
      // 성공 시 데이터를 이용해 목록을 동적으로 생성하는 로직 추가
      let tbody = $("#hpHistoryTable tbody");
      tbody.empty(); // 기존 데이터를 지우고 새로운 데이터로 갱신

      if (data && data.length > 0) {
        // 데이터가 있을 경우 테이블에 행 추가
        data.forEach(function (item) {
          let row = $("<tr>");
          row.append($("<td>").text(item.hpHistoryId));
          row.append($("<td>").text(item.price));
          row.append($("<td>").text(item.holopayComment));
          row.append($("<td>").text(item.hpDate)); // 날짜 형식에 따라 수정

          tbody.append(row);
        });
      } else {
        // 데이터가 없을 경우 테이블에 메시지 추가
        let noDataMessage = $("<tr>").append($("<td colspan='4' class='text-center'>").text("거래내역 없음"));
        tbody.append(noDataMessage);
      }
    },
    error: function (request, status, error) {
      // Handle the error response from the server
      console.error("code: " + request.status)
      console.error("message: " + request.responseText)
      console.error("error: " + error);
    }
  });
}







