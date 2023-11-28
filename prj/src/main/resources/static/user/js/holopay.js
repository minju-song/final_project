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

  console.log(memberFinanceInfo);
  $.ajax({
    type: 'POST',
    url: '/addFinanceInfo',
    contentType: 'application/json;charset=UTF-8',
    data: JSON.stringify(memberFinanceInfo)
  }).done(function (response) {
    console.log(response);
    console.log(response.result);
    $("#myAccountInfo").html(response)
  }).fail(function (request, status, error) {
    // Handle the error response from the server
    console.log("code: " + request.status)
    console.log("message: " + request.responseText)
    console.log("error: " + error);

  })

}


//충전버튼
let rechargeBtn = document.getElementById('callHoloPayRechargeApiBtn');



rechargeBtn.addEventListener('click', callRechargeApi);
function callRechargeApi() {
  let rechargePrice = document.getElementById('rechargePrice').value;
  console.log(rechargePrice);
  let callRechargeApiInfo = {
    "Tram": rechargePrice,
    "accountNum": myAccountNum
  };

  console.log(callRechargeApiInfo);
  $.ajax({
    type: 'POST',
    url: '/apireq',
    contentType: 'application/json;charset=UTF-8',
    data: JSON.stringify(callRechargeApiInfo)
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

