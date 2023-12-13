$(function () {
    $('#updateCard').on('show.bs.modal', function (event) {
        console.log("모달오픈");
        let cardno = $(event.relatedTarget).data('cardno');
        console.log($(event.relatedTarget).data('cardcompany'));
        document.getElementById('cardNum').value = cardno;


        let selectBox1 = document.getElementById('cardCompanySelect');
        let len1 = selectBox1.options.length;
        let cardCom = $(event.relatedTarget).data('cardcompany');

        for (let i = 0; i < len1; i++) {
            if (selectBox1.options[i].value == cardCom) {
                selectBox1.options[i].selected = true;
            }
        }
        // console.log(document.querySelector('option[value=' + $(event.relatedTarget).data('cardcompany') + ']'));
        // document.querySelector('option[value=' + $(event.relatedTarget).data('cardcompany') + ']').setAttribute('selected', 'selected');




    });
    $('#updateBudget').on('show.bs.modal', function (event) {

        let budgetprice = $(event.relatedTarget).data('budgetprice');
        console.log(budgetprice);
        document.getElementById('accbudgetprice').value = budgetprice;

        let selectBox2 = document.getElementById('accBudgetUnitSelect');
        let len2 = selectBox2.options.length;
        let budgetunit = $(event.relatedTarget).data('budgetunit');
        console.log(budgetunit);
        if (budgetunit == '일') budgetunitval = 'YA1';
        else if (budgetunit == '주') budgetunitval = 'YA2';
        else if (budgetunit == '월') budgetunitval = 'YA3';
        console.log(budgetunitval);
        for (let i = 0; i < len2; i++) {
            if (selectBox2.options[i].value == budgetunitval) {
                selectBox2.options[i].selected = true;
            }
        }
    });

});


let delCardBtn = document.getElementById("deleteCard");
if (delCardBtn != null) {
    delCardBtn.addEventListener("click", delModal);
}


function delModal(e) {
    let cardno = delCardBtn.value;
    let data = { cardno };
    console.log(cardno);
    Swal.fire({
        title: "등록된 카드를 삭제하시겠습니까?",
        text: "삭제된 정보는 복구될 수 없습니다.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch("/cardDelete", {
                method: "POST",
                body: JSON.stringify({
                    cardNo: cardno
                }),
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => response.json())
                .then(result => {
                    if (result.result == "success") {
                        Swal.fire({
                            title: "삭제완료",
                            text: "Your file has been deleted.",
                            icon: "success"
                        });
                    }
                    else {
                        Swal.fire({
                            title: "삭제실패",
                            text: "Your file has been deleted.",
                            icon: "error"
                        });
                    }

                    location.reload();
                })
        }
    });
}

let delBudgetBtn = document.getElementById("deleteBudget");
if (delBudgetBtn != null) {
    delBudgetBtn.addEventListener("click", delBudget);
}


function delBudget(e) {
    Swal.fire({
        title: "등록된 예산을 삭제하시겠습니까?",
        text: "삭제된 정보는 복구될 수 없습니다.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch("/budgetDelete")
                .then(response => response.json())
                .then(result => {
                    console.log(result);
                    if (result.result == "success") {
                        Swal.fire({
                            title: "삭제완료",
                            text: "Your file has been deleted.",
                            icon: "success"
                        });
                    }
                    else {
                        Swal.fire({
                            title: "삭제실패",
                            text: "Your file has been deleted.",
                            icon: "error"
                        });
                    }

                    location.reload();
                })
        }
    });
}

function getResult(date) {
    fetch('/getRecord?startDate=' + date)
        .then(resolve => resolve.json())
        .then(result => {
            console.log(result);
            let record = result.record;

            let recordContent = `<div>`;

            if (record.successState == 'SU3') {
                recordContent += `<h3> &#9200; 진행 중 &#9200;</h3>`
            }
            else if (record.successState == 'SU1') {
                recordContent += `<h3>&#128525; 성공 &#128525;</h3>`
            }
            else if (record.successState == 'SU2') {
                recordContent += `<h3>&#128552; 실패 &#128552;</h3>`
            }

            let recordDate = ``;
            if (dateFormat2(record.startDate) == dateFormat2(record.endDate)) {
                recordDate = dateFormat2(record.startDate);
            }
            else {
                recordDate = dateFormat2(record.startDate) + ` ~ ` + dateFormat2(record.endDate);
            }

            let recordUnit = ``;
            if (record.goalUnit == 'YA1') recordUnit = `  (하루)`;
            else if (record.goalUnit == 'YA2') recordUnit = `  (일주일)`;
            else recordUnit = `  (한달)`;

            recordContent += `<br><p><strong>도전기간</strong>  ` + recordDate + recordUnit + `</p>`
            recordContent += `<p><strong>도전금액</strong>  ` + record.goalPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + `원</p>`

            if (record.successState == 'SU1' || record.successState == 'SU2') {
                recordContent += '<p><strong>실제 소비금액</strong>  ' + record.sumPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + `원</p>`
            }

            Swal.fire({
                title: date,

                html: recordContent,
                showCloseButton: true,
                showCancelButton: false,
                focusConfirm: false,
                confirmButtonText: `확인`,
                confirmButtonAriaLabel: "확인"
            });
        })




}


function dateFormat2(str) {
    let date = new Date(str);
    let newDate = date.getFullYear() + '-' + ((date.getMonth() + 1) <= 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) +
        '-' + ((date.getDate()) <= 9 ? "0" + (date.getDate()) : (date.getDate()));
    return newDate;
}


//가계부 이용방법
function aboutNotice() {
    let about = `<h4>&#128149; 기본기능</h4>
                    <p>&#7; 당일 한정 본인의 소비, 소득을 수기로 기록할 수 있습니다.</p>
                    <p>&#7; 달력의 날짜를 클릭 시 해당 일의 거래내역과 총 소비, 소득금액을 조회할 수 있습니다.</p>
                <br>
                <h4>&#128149; 카드등록</h4>
                    <p>&#7; 카드 등록 시 수기입력을 하지 않아도 해당 카드의 거래내역을 조회합니다.</p>
                <br>
                <h4>&#128149; 소비 절약 도전</h4>
                    <p>&#7; 절약 예산 및 기간(일/주/월)을 등록할 수 있습니다.</p>
                    <p>&#7; 기간이 끝나는 다음 날 채점이 되어 성공, 실패릃 확인할 수 있습니다.</p>
                <br>
                <br>
                <h5 font-weight:bold;">[달력 표시]</h5>
                <strong style="color:green;">성공</strong>  <strong style="color:red;">실패</strong>  <strong style="color:skyblue;">진행 중</strong>`;
    Swal.fire({
        title: `<h3 style="color:#7a9cc6; font-weight:bold;">가계부 이용방법</h3>`,

        html: about,
        showCloseButton: true,
        showCancelButton: false,
        focusConfirm: false,
        confirmButtonText: `확인`,
        confirmButtonAriaLabel: "확인"
    });
}