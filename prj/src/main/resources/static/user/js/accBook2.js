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

delCardBtn.addEventListener("click", delModal);

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
delBudgetBtn.addEventListener("click", delBudget);


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
}/**
 * 
 */