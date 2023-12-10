/**
 * tradeInfo
 */



//중고거래 삭제
function deleteTrade(){
	$.ajax({
		url : '/member/tradeDelete',  //이동할 jsp 파일 주소
		data : {tradeId},
		success: function(data){
			Swal.fire({
                title: '정말로 그렇게 하시겠습니까?',
                text: "다시 되돌릴 수 없습니다. 신중하세요.",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: '승인',
                cancelButtonText: '취소'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire(
                        '중고거래 삭제',
                        '선택하신 중고거래를 삭제했습니다!',
                        'success'
                    )
					document.querySelector('.swal2-confirm').addEventListener('click', function(e){
					   location.href = "/tradeList";
					});
                }
            })
		},
		error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
			console.log("중고거래 삭제 실패");
		}
	})
}

