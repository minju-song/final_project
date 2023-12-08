/**
 * tradePay.js
 */

$(document).ready(function(){
    if(Number(pointCnt) + Number(holoPayCnt) < price){
        Swal.fire(
            '잔액부족',
            '홀로페이 잔액 충천 후 이용해주세요.',
            'success'
        )
		document.querySelector('.swal2-confirm').addEventListener('click', function(e){
		   location.href = "/tradeList";
		});
    }
})

//input number 최대값 지정
let number = document.querySelector('[type=text]');
let usePrice = document.querySelector('.usePrice');
let usePoint = document.querySelector('.usePoint');
let useHoloPay = document.querySelector('.useHoloPay');
let oriHoloPay = document.querySelector('.oriHoloPay');
let oriPoint = document.querySelector('.oriPoint');
let myPrice = document.querySelector('.myPrice');
usePoint.value = 0;
useHoloPay.value = price;
number.onkeyup = function() {
    if(pointCnt < number.value) {
        number.value = pointCnt;
        return false;
    }
    payCal(number.value)
}

//전액사용 클릭 이벤트
function payAllSel(){
    if(price < pointCnt){
        number.value = price;
    }else{
        number.value = pointCnt;
    }
    payCal(number.value);
    
}

//계산
function payCal(num){
    useHoloPay.innerText = (price - number.value).toLocaleString('ko-KR') + ' 원';
    useHoloPay.value = price - number.value;
    usePoint.innerText = num.toLocaleString('ko-KR') + ' P';
    usePoint.value = Number(num);
    if(usePoint.innerText == 'P'){
        usePoint.innerText = '0 P';
    }
    oriHoloPay.innerText = (holoPayCnt - useHoloPay.value).toLocaleString('ko-KR') + ' 원';
    oriHoloPay.value = holoPayCnt - useHoloPay.value;
    oriPoint.innerText = (pointCnt - usePoint.value).toLocaleString('ko-KR') + ' P';
    oriPoint.value = pointCnt - usePoint.value;
    myPrice.innerText = (oriPoint.value + oriHoloPay.value).toLocaleString('ko-KR') + ' 원';
}

//결제버튼 클릭 시
let payment = document.querySelector('.insertbutton');
payment.addEventListener('click', function(e){
    let point = usePoint.value;
    let holoPay = useHoloPay.value;
    Swal.fire({
        title: '결제를 진행하시겠습니까?',
        text: "결제를 진행하시려면 확인을 눌러주세요.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
    }).then((result) => {
	    $.ajax({
	        type:"POST",
	        url : '/member/insertPayPoint',  //이동할 jsp 파일 주소
	        data : {point, holoPay, tradeId},
	        success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
	            Swal.fire(
	                '결제 완료',
	                '결제를 완료했습니다!',
	                'success'
	            )
				document.querySelector('.swal2-confirm').addEventListener('click', function(e){
				   location.href = "/tradeList";
				});
	        },
	        error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
	            console.log('등록실패');
	        }
	    })	
    })
})