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
      location.reload();
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
    let resultMsg = data.resultMsg;

    Swal.fire({
      title: "",
      text: data.resultMsg,
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


let searchHpHistory = getElementById('searchHpHistory');
searchHpHistory.addEventListener('change',loadData)

function loadData(){
  console.log(searchHpHistory);
  let term = searchHpHistory.value;

  $.ajax({
    type: 'POST',
    url: '/loadhistory',
    contentType: 'application/json;charset=UTF-8',
    data:{"term": term}
  }).done(function (response) {
    let data = JSON.parse(response);
    console.log(data);
  }).fail(function (request, status, error) {
    // Handle the error response from the server
    console.log("code: " + request.status)
    console.log("message: " + request.responseText)
    console.log("error: " + error);
  })
}
   
 


