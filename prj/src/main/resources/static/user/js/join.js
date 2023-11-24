/**
 * 회원가입 관련
 */

// 우편번호 찾기 화면을 넣을 element
var element_layer = document.getElementById('layer');

function closeDaumPostcode() {
    // iframe을 넣은 element를 안보이게 한다.
    element_layer.style.display = 'none';
}

function sample2_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("addr2").value = extraAddr;
            
            } else {
                document.getElementById("addr2").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            //document.getElementById('sample2_postcode').value = data.zonecode;
            document.getElementById("addr1").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("addr2").focus();

            // iframe을 넣은 element를 안보이게 한다.
            // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
            element_layer.style.display = 'none';
        },
        width : '100%',
        height : '100%',
        maxSuggestItems : 5
    }).embed(element_layer);

    // iframe을 넣은 element를 보이게 한다.
    element_layer.style.display = 'block';

    // iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.
    initLayerPosition();
}

// 브라우저의 크기 변경에 따라 레이어를 가운데로 이동시키고자 하실때에는
// resize이벤트나, orientationchange이벤트를 이용하여 값이 변경될때마다 아래 함수를 실행 시켜 주시거나,
// 직접 element_layer의 top,left값을 수정해 주시면 됩니다.
function initLayerPosition(){
    var width = 630; //우편번호서비스가 들어갈 element의 width
    var height = 490; //우편번호서비스가 들어갈 element의 height
    var borderWidth = 2; //샘플에서 사용하는 border의 두께

    // 위에서 선언한 값들을 실제 element에 넣는다.
    element_layer.style.width = width + 'px';
    element_layer.style.height = height + 'px';
    element_layer.style.border = borderWidth + 'px solid';
    // 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
    element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width)/2 - borderWidth) + 'px';
    element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height)/2 - borderWidth) + 'px';
}


// 본인인증 문자 발송.
let time = 180; // 인증시간
let remainingMin = $('#time_min');
let remainingSec = $('#time_sec');
$('#authNumCheck').click(function() {
	let reception = $('#phone').val();
	if(reception == '') {
		alert('휴대폰 번호를 입력하세요.');
		$('#phone').focus();
	} else {
		// 휴대폰 번호 검증
		const patternPhone = /01[016789][^0][0-9]{2,3}[0-9]{3,4}/;
		if(!patternPhone.test(reception)) {
			alert('휴대폰번호를 확인 해주세요');
			return;
		} else {
			// 인증번호 입력란 show
			$('#check_phone_wrap').css('display', 'flex');
			// 휴대폰번호 수정 막기
			$('#phone').prop('readonly', true);
			$('#authNumCheck').prop('disabled', true);
			
			// 문자발송 시작
			$.ajax('/join/sendJoinSms', {
				type : 'get',
				data : {reception}
			})
			.done(result => {
				console.log('넘어온 값은', result);
				if(result == 'Success') {
					alert('인증번호가 발송되었습니다!');
					
					// 인증번호 만료시간..
					let interval = setInterval(function () {
						if (time > 0) { // >= 0 으로하면 -1까지 출력된다.
							time = time - 1; // 여기서 빼줘야 3분에서 3분 또 출력되지 않고, 바로 2분 59초로 넘어간다.
							let min = Math.floor(time / 60);
							let sec = String(time % 60).padStart(2, "0");
							remainingMin.innerText = min;
							remainingSec.innerText = sec;
							
							if(($('#joinBtn').prop('disabled') == false)) {
								remainingMin.innerText = '인증완료!';
								remainingSec.innerText = '';
								clearInterval(interval);
							}
							// time = time - 1
						} else {
							alert('인증시간이 초과되었습니다.');
							// 인증번호 발송 버튼 재활성화.
							$('#phone').prop('readonly', false);
							$('#authNumCheck').prop('disabled', false);
							// 인증번호 입력란 hide
							$('#check_phone_wrap').css('display', 'none');
						}
					}, 1000);
					
				} else {
					alert('인증번호 발송 실패! 다시 시도해주세요.\n계속해서 문제가 발생한다면 고객센터로 연락바랍니다.');
				}
				
			})
			.fail(err => console.log(err))
		}
	}
});
// 인증번호 확인
$('#userCheck').click(function() {
	let authNum = $('#checkPhone').val();
	console.log(authNum);
	if(authNum == '') {
		alert('인증번호를 입력하세요.');
		$('#checkPhone').focus();
	} else {
		// 인증번호 수정 막기
		$('#checkPhone').prop('readonly', true);
		
		// 인증번호 확인 시작
		$.ajax('/join/checkAuthNum', {
			type : 'get',
			data : {authNum}
		})
		.done(result => {
			console.log('넘어온 값은', result);
			if(result == 'Success') {
				$('#joinBtn').prop('disabled', false);
				$('#joinBtn').css('background-color', '#09203f');
				$('#joinBtn').css('color', '#fff');
			} else {
				alert('일치하지 않습니다!');
				$('#checkPhone').prop('readonly', false);
				$('#checkPhone').val('');
				$('#checkPhone').focus();
			}
		})
		.fail(err => console.log(err))
	}
})


