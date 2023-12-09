/**
 * tradeInfo
 */


if(latitude != ''){
	let mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	    mapOption = { 
	        center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
	        level: 3 // 지도의 확대 레벨
	    };
	
	let map = new kakao.maps.Map(mapContainer, mapOption);
	
	// 마커가 표시될 위치입니다 
	let markerPosition  = new kakao.maps.LatLng(latitude, longitude); 
	
	// 마커를 생성합니다
	let marker = new kakao.maps.Marker({
	    position: markerPosition
	});
	
	// 마커가 지도 위에 표시되도록 설정합니다
	marker.setMap(map);
	
	let iwContent = '<div style="padding:5px;">'+tradePlace+' <br><a href="https://map.kakao.com/link/map/'+tradePlace+', '+longitude+', '+longitude+'" style="color:blue" target="_blank">큰지도보기</a> <a href="https://map.kakao.com/link/to/'+tradePlace+', '+latitude+', '+longitude+'" style="color:blue" target="_blank">길찾기</a></div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
	    iwPosition = new kakao.maps.LatLng(latitude, longitude); //인포윈도우 표시 위치입니다
	
	// 인포윈도우를 생성합니다
	let infowindow = new kakao.maps.InfoWindow({
	    position : iwPosition, 
	    content : iwContent 
	});
	  
	// 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
	infowindow.open(map, marker); 
}else{
	document.querySelector('.noneMap').style.display = 'block';
	document.querySelector('.bottomdown30').style.display = 'none !important';
}

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

document.getElementById('heart').addEventListener('click', function(e){
console.log("eee")
	let heartCount = '';
	if(!e.target.classList.contains('bi-heart')){
		e.target.src = '/user/images/trade/fill-heart.png';
		heartCount = Number(heart) + 1;
		document.querySelector('.heartCount').innerText = '관심 ' + heartCount;
		$.ajax({
			type: 'POST',
			url : '/member/heartInsert',  //이동할 jsp 파일 주소
			data : {tradeId},
			success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
				console.log("성공");
			},
			error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
				console.log('실패');
			}
		})	
	}else{
		e.target.src = '/user/images/trade/bin-heart.png';
		heartCount = heart;
		document.querySelector('.heartCount').innerText = '관심 ' + heartCount;
		$.ajax({
			url : '/member/heartDelete',  //이동할 jsp 파일 주소
			data : {tradeId},
			success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
				console.log("성공");
			},
			error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
				console.log("실패");
			}
		})
	}
	e.target.classList.toggle('bi-heart');
})


//약속상태, 구매자 수정
function promiseEvent(tradeId, e){
let cd = {'약속잡기': 'TD1', '약속확정' : 'TD2', '거래확정' : 'TD3', '약속취소' : 'TD4'}

	let pay = e.target;
	let promiseStatus =  cd[e.target.innerText]
	
	//약속 수락
	if(promiseStatus != 'TD4'){
		$.ajax({
			type: 'POST',
			url : '/member/BuyerIdUpdate',  //이동할 jsp 파일 주소
			data : {tradeId, promiseStatus},
			success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
				console.log("성공");
				if(pay.innerText == '약속잡기'){
					pay.innerText = '약속취소';
					mailSend();
				}else{
					pay.parentElement.parentElement.remove();
				}
			},
			error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
				console.log("실패");
			}
		})
	}else{
	//약속 취소
		let buyerId = '';
		$.ajax({
			type: 'POST',
			url : '/member/BuyerIdUpdate',  //이동할 jsp 파일 주소
			data : {tradeId, buyerId, promiseStatus},
			success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
				console.log("성공");
				if(pay.parentElement.nextElementSibling){
					pay.parentElement.nextElementSibling.remove();
				}else{
					pay.innerText = '약속잡기';							
				}
			},
			error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
				console.log("실패");
			}
		})
	}
}

//메일 보내기
function mailSend(){
	$.ajax({
		type: 'POST',
		url : '/sendmail/trade/promise',  //이동할 jsp 파일 주소
		data : {title, sellerId, tradeId},
		success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
			console.log("성공");
			if(pay.parentElement.nextElementSibling){
				pay.parentElement.nextElementSibling.remove();
			}else{
				pay.innerText = '약속잡기';							
			}
		},
		error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
			console.log("실패");
		}
	})
}

//신고버튼
function reportBtn(postId, reportedId, e){
	Swal.fire({
        title: '신고 유형 및 사유를 작성해주세요.',
        html: `<label for="reportType" class="form-label">신고 유형</label> 
        	    <select id="reportType" class="form-select" required>
					<option selected value="">선택하세요</option>
					<option value="SA1">욕설</option>
					<option value="SA2">음란성</option>
					<option value="SA3">모욕/비방</option>
					<option value="SA4">사기</option>
					<option value="SA5">기타</option>
				</select>
				<input type="text" placeholder="사유를 입력하세요." id="reportContent">`
    }).then((result) => {
		if (result.isConfirmed) {
		let reportType = document.querySelector('#reportType').value;
		let reportContent = document.querySelector('#reportContent').value;
		let menuType = 'AA1';
			$.ajax({
				type: 'POST',
				url : '/member/insertReport',  //이동할 jsp 파일 주소
				data : {postId, reportedId, menuType, reportType, reportContent},
				success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
					Swal.fire({
				      title: "신고 접수",
				      text: "신고가 접수되었습니다.",
				      icon: "success"
				    });
				},
				error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
					console.log("실패");
				}
			})
		}
	});
}