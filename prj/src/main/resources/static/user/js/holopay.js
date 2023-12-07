

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
let viewIcon="";
  console.log(callRechargeApiInfo);
  $.ajax({
    type: 'POST',
    url: '/apireq',
    contentType: 'application/json;charset=UTF-8',
    data: JSON.stringify(callRechargeApiInfo)
  }).done(function (data) {
    //let data = JSON.parse(response)
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
      location.reload();
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
  $.ajax({
    type: 'POST',
    url: '/withdrawapireq',
    contentType: 'application/json;charset=UTF-8',
    data: JSON.stringify(callwithdrawApiInfo)
  }).done(function (data) {
  //  let data = JSON.parse(response)
  console.log(data)
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



// 페이징
let currentPage = 1;
const recordsPerPage = 10; // 페이지당 표시할 레코드 수, 필요에 따라 조절

// 페이지 버튼 생성
function setupPagination(totalPages) {
  let paginationContainer = document.getElementById("pagination-container");

  if (paginationContainer) {
    paginationContainer.innerHTML = "";

    let navElement = document.createElement("nav");
    navElement.setAttribute("aria-label", "...");
    let ulElement = document.createElement("ul");
    ulElement.className = "pagination";

    // Previous Button
    let previousLi = document.createElement("li");
    previousLi.className = "page-item disabled";
    let previousLink = document.createElement("a");
    previousLink.className = "page-link";
    previousLink.innerText = "Previous";
    previousLink.addEventListener("click", function () {
      if (currentPage > 1) {
        loadData(currentPage - 1);
      }
    });
    previousLi.appendChild(previousLink);
    ulElement.appendChild(previousLi);

    // Page Buttons
    for (let i = 1; i <= totalPages; i++) {
      let li = document.createElement("li");
      li.className = "page-item";
      let link = document.createElement("a");
      link.className = "page-link";
      link.href = "#";
      link.innerText = i;
      link.addEventListener("click", function () {
        loadData(i);
      });
      li.appendChild(link);
      ulElement.appendChild(li);
    }

    // Next Button
    let nextLi = document.createElement("li");
    let nextLink = document.createElement("a");
    nextLi.className = "page-item";
    nextLink.className = "page-link";
    nextLink.href = "#";
    nextLink.innerText = "Next";
    nextLink.addEventListener("click", function () {
      if (currentPage < totalPages) {
        loadData(currentPage + 1);
      }
    });
    nextLi.appendChild(nextLink);
    ulElement.appendChild(nextLi);

    navElement.appendChild(ulElement);
    paginationContainer.appendChild(navElement);
  }
}


//기간별 조회 이벤트
let search = document.getElementById('searchHpHistory');
search.addEventListener('change', function () {
  // 이벤트가 발생할 때 현재 페이지를 전달하여 loadData 함수 호출
  loadData(currentPage);
});

// 페이지 로드 시에도 데이터를 로드하도록 호출


function loadData(page) {
  currentPage = page;
  let term = $("#searchHpHistory").val();
  let start = (currentPage - 1) * recordsPerPage;
  let end = start + recordsPerPage;
  $.ajax({
    type: 'POST',
    url: '/loadhistory',
    contentType: 'application/json;charset=UTF-8',
    data: JSON.stringify({ "term": term, "start": start, "end": end }),
    success: function (data) {
      updateTable(data);
      // 페이징 처리
      let totalPages = Math.ceil(data.totalRecords / recordsPerPage);
      setupPagination(totalPages);
    },
    error: function (request, status, error) {
      // Handle the error response from the server
      console.error("code: " + request.status);
      console.error("message: " + request.responseText);
      console.error("error: " + error);
    }
  });
}
//테이블 갱신
function updateTable(data) {
  let tbody = $("#hpHistoryTableBody");
  tbody.empty(); // 기존 데이터를 지우고 새로운 데이터로 갱신
  if (data && data.historyList && data.historyList.length > 0) {
    // 데이터가 있을 경우 테이블에 행 추가
    data.historyList.forEach(function (item, index) {
      let row = $("<tr>");
      row.append($("<td>").text(index + 1));
      row.append($("<td>").text(item.hpType));
      row.append($("<td>").text(item.price));
      row.append($("<td>").text(item.holopayComment));
      row.append($("<td>").text(formatDate(item.hpDate)));

      tbody.append(row);
    });
  } else {
    // 데이터가 없을 경우 테이블에 메시지 추가
    let noDataMessage = $("<tr>").append($("<td colspan='5' class='text-center'>").text("거래내역 없음"));
    tbody.append(noDataMessage);
  }

}

$(document).ready(function () {
  loadData(currentPage);
});

function formatDate(dateString) {
  let date = new Date(dateString);
  let year = date.getFullYear();
  let month = (date.getMonth() + 1).toString().padStart(2, '0');
  let day = date.getDate().toString().padStart(2, '0');
  return year + '-' + month + '-' + day;
}

function getTransactionType(type) {
  switch (type) {
    case 'HP1':
      return '충전';
    case 'HP2':
      return '인출';
    // 다른 유형에 대한 처리 추가 가능
    default:
      return type;
  }
  
}


loadData(currentPage);


