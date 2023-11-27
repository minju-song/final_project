/**
 * 회원가입 관련
 */

/* 주소 검색 start */
var element_layer = document.getElementById('layer'); // 우편번호 찾기 화면을 넣을 element

function closeDaumPostcode() {
	// iframe을 넣은 element를 안보이게 한다.
	element_layer.style.display = 'none';
}

function sample2_execDaumPostcode() {
	new daum.Postcode({
		oncomplete: function (data) {
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
			if (data.userSelectedType === 'R') {
				// 법정동명이 있을 경우 추가한다. (법정리는 제외)
				// 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
				if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
					extraAddr += data.bname;
				}
				// 건물명이 있고, 공동주택일 경우 추가한다.
				if (data.buildingName !== '' && data.apartment === 'Y') {
					extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
				}
				// 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
				if (extraAddr !== '') {
					extraAddr = ' (' + extraAddr + ')';
				}
				// 조합된 참고항목을 해당 필드에 넣는다.
				document.getElementById("detailAddr").value = extraAddr;

			} else {
				document.getElementById("detailAddr").value = '';
			}

			// 우편번호와 주소 정보를 해당 필드에 넣는다.
			//document.getElementById('sample2_postcode').value = data.zonecode;
			document.getElementById("addr1").value = addr;
			// 커서를 상세주소 필드로 이동한다.
			document.getElementById("detailAddr").focus();

			// iframe을 넣은 element를 안보이게 한다.
			// (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
			element_layer.style.display = 'none';
		},
		width: '100%',
		height: '100%',
		maxSuggestItems: 5
	}).embed(element_layer);

	// iframe을 넣은 element를 보이게 한다.
	element_layer.style.display = 'block';

	// iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.
	initLayerPosition();
}

// 브라우저의 크기 변경에 따라 레이어를 가운데로 이동시키고자 하실때에는
// resize이벤트나, orientationchange이벤트를 이용하여 값이 변경될때마다 아래 함수를 실행 시켜 주시거나,
// 직접 element_layer의 top,left값을 수정해 주시면 됩니다.
function initLayerPosition() {
	var width = 630; //우편번호서비스가 들어갈 element의 width
	var height = 490; //우편번호서비스가 들어갈 element의 height
	var borderWidth = 2; //샘플에서 사용하는 border의 두께

	// 위에서 선언한 값들을 실제 element에 넣는다.
	element_layer.style.width = width + 'px';
	element_layer.style.height = height + 'px';
	element_layer.style.border = borderWidth + 'px solid';
	// 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
	element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width) / 2 - borderWidth) + 'px';
	element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height) / 2 - borderWidth) + 'px';
}
/* 주소 검색 end */


/* 본인인증 문자 발송 start */
let time = 180; // 인증시간
let remainingMin = document.querySelector('#time_min');
let remainingSec = document.querySelector('#time_sec');

$('#authNumCheck').click(function () {
	let reception = $('#phone').val();
	if (reception == '') {
		Swal.fire({
			icon: "error",
			title: "휴대폰 번호를 입력하세요",
			confirmButtonText: '확인'
		}).then(function () {
			$('#phone').focus();
		})

	} else {
		// 휴대폰 번호 검증
		const patternPhone = /01[016789][^0][0-9]{2,3}[0-9]{3,4}/;
		if (!patternPhone.test(reception)) {
			Swal.fire({
				icon: "error",
				title: "휴대폰 번호를 확인해 주세요",
				text: "하이픈없이 숫자만 입력하셔야합니다",
				confirmButtonText: '확인'
			})
			return;
		} else {
			// 인증번호 입력란 show
			$('#check_phone_wrap').css('display', 'flex');
			$('#checkPhone').focus();
			// 휴대폰번호 수정 막기
			$('#phone').prop('readonly', true);
			$('#authNumCheck').prop('disabled', true);

			// 문자발송 시작
			$.ajax('/sendSms', {
				type: 'get',
				data: { reception }
			})
				.done(result => {
					console.log('넘어온 값은', result);
					if (result == 'Success') {
						Swal.fire({
							icon: "success",
							title: "인증번호가 발송되었습니다",
							confirmButtonText: '확인'
						});

						// 인증번호 만료시간..
						let interval = setInterval(function () {
							if (time > 0) { // >= 0 으로하면 -1까지 출력된다.
								time = time - 1; // 여기서 빼줘야 3분에서 3분 또 출력되지 않고, 바로 2분 59초로 넘어간다.
								let min = Math.floor(time / 60);
								let sec = String(time % 60).padStart(2, "0");
								$('#auth_timer').css('display', 'block');
								$('#auth_timer').text(min + ':' + sec);

								if (($('#submitBtn').attr('data-disabled') == 'false')) {
									$('#auth_timer').css('display', 'none');
									$('#userCheck').text('인증완료');
									$('#userCheck').prop('disabled', true);
									clearInterval(interval);
								}
								// time = time - 1
							} else if (time <= 0) {
								Swal.fire({
									icon: "warning",
									title: "인증시간이 초과되었습니다",
									confirmButtonText: '확인'
								});
								// 남은시간 hide
								$('#auth_timer').css('display', 'none');
								// 인증번호 발송 버튼 재활성화.
								$('#phone').prop('readonly', false);
								$('#authNumCheck').prop('disabled', false);
								// 인증번호 입력란 hide
								$('#check_phone_wrap').css('display', 'none');
								// 인증시간 초기화
								time = 180;
								clearInterval(interval);
							}
						}, 1000);

					} else {
						Swal.fire({
							icon: "error",
							title: "인증번호 발송 실패! 다시 시도해 주세요",
							text: "계속해서 문제가 발생한다면 고객센터로 연락바랍니다",
							confirmButtonText: '확인'
						});
					}

				})
				.fail(err => console.log(err))
		}
	}
});
/* 본인인증 문자 발송 start */

/* 인증번호 확인 start */
$('#userCheck').click(function () {
	let authNum = $('#checkPhone').val();
	console.log(authNum);
	if (authNum == '') {
		Swal.fire({
			icon: "error",
			title: "인증번호를 입력하세요",
			confirmButtonText: '확인'
		});
		$('#checkPhone').focus();
	} else {
		// 인증번호 수정 막기
		$('#checkPhone').prop('readonly', true);

		// 인증번호 확인 시작
		$.ajax('/checkAuthNum', {
			type: 'get',
			data: { authNum }
		})
			.done(result => {
				console.log('넘어온 값은', result);
				if (result == 'Success') {
					$('#submitBtn').attr('data-disabled', 'false');
					$('#submitBtn').css('background-color', '#2c456f');
					$('#submitBtn').css('color', '#fff');
				} else {
					Swal.fire({
						icon: "error",
						title: "일치하지 않습니다, 인증번호를 확인해 주세요",
						confirmButtonText: '확인'
					});
					$('#checkPhone').prop('readonly', false);
					$('#checkPhone').val('');
					$('#checkPhone').focus();
				}
			})
			.fail(err => console.log(err))
	}
})
/* 인증번호 확인 end */

/* 아이디 중복 확인 start */
$('#idCheck').click(function () {
	let patternEmail = new RegExp("([!#-'*+/-9=?A-Z^-~-]+(\.[!#-'*+/-9=?A-Z^-~-]+)*|\"\(\[\]!#-[^-~ \t]|(\\[\t -~]))+\")@([!#-'*+/-9=?A-Z^-~-]+(\.[!#-'*+/-9=?A-Z^-~-]+)*|\[[\t -Z^-~]*])");
	let memberId = $('#memberId').val();

	if (memberId == '') {
		Swal.fire({
			icon: "error",
			title: "아이디를 입력해 주세요",
			confirmButtonText: '확인'
		});
		$('#memberId').focus();
	} else {
		if (!patternEmail.test(memberId)) {
			Swal.fire({
				icon: "error",
				title: "이메일 형식이 아닙니다",
				confirmButtonText: '확인'
			});
			$('#memberId').focus();
		} else {
			$.ajax('/join/idCheck', {
				type: 'get',
				data: { memberId }
			})
				.done(result => {
					console.log('넘어온 값은', result);
					if (result == 'NOT_FOUND') {
						Swal.fire({
							icon: "success",
							title: "사용가능한 아이디입니다",
							confirmButtonText: '확인'
						});
						$('#idCheck').prop('disabled', true);
					} else {
						Swal.fire({
							title: '이미 가입된 아이디입니다',
							text: "확인을 누르시면 로그인페이지로 이동합니다",
							icon: 'error',
							showCancelButton: true,
							confirmButtonColor: '#3085d6',
							cancelButtonColor: '#d33',
							confirmButtonText: '확인',
							cancelButtonText: '취소',
							reverseButtons: true, // 버튼 순서 거꾸로

						}).then((result) => {
							if (result.isConfirmed) {
								window.location.href = '/loginForm';
							} else if (result.isDismissed) {
								document.querySelector('#memberId').value = '';
								document.querySelector('#memberId').focus();
							}
						})
					}
				})
				.fail(err => console.log(err))
		}
	}
})

//중복확인 후 아이디를 변경했다면 버튼 활성화
$('#memberId').keyup(function () {
	$('#idCheck').prop('disabled', false);
});

/* 아이디 중복 확인 end */

/* 닉네임 중복 확인 start */
$('#nickCheck').click(function () {
	let nickname = $('#nickname').val();

	if (nickname == '') {
		Swal.fire({
			icon: "error",
			title: "닉네임을 입력해 주세요",
			confirmButtonText: '확인'
		});
		$('#nickname').focus();
	} else {
		$.ajax('/join/nickCheck', {
			type: 'get',
			data: { nickname }
		})
			.done(result => {
				console.log('넘어온 값은', result);
				if (result == 'NOT_FOUND') {
					Swal.fire({
						icon: "success",
						title: "사용가능한 닉네임입니다",
						confirmButtonText: '확인'
					});
					$('#nickCheck').prop('disabled', true);
				} else {
					Swal.fire({
						icon: "error",
						title: "이미 사용중인 닉네임입니다",
						confirmButtonText: '확인'
					});
					$('#nickname').val('');
					$('#nickname').focus();
				}
			})
			.fail(err => console.log(err))
	}
})

//중복확인 후 닉네임을 변경했다면 버튼 활성화
$('#nickname').keyup(function () {
	$('#nickCheck').prop('disabled', false);
});

/* 닉네임 중복 확인 end */

/* 가입하기 클릭시 start */
$('#submitBtn').click(function () {
	let bool = inputCheck(); // 입력여부 검증
	console.log('입력여부 검증결과입니다. ', bool);
	
	if(bool == true) { // 가입하기 진행		
		// 인증버튼이 활성화되어있을 때(인증을 하지 않은 경우)
		if($('#userCheck').prop('disabled') == false) {
			Swal.fire({
				icon: "error",
				title: "인증을 진행해 주세요",
				text: "본인인증 버튼을 클릭하면 인증번호가 발송됩니다",
				confirmButtonText: '확인'
			});
			return false;
		}
		
		// 아이디 중복확인 체크
		if ($('#idCheck').prop('disabled') == false) {
			Swal.fire({
				icon: "error",
				title: "아이디 중복확인을 해주세요",
				confirmButtonText: '확인'
			});
			return;
		}
		// 닉네임 중복확인 체크
		if ($('#nickCheck').prop('disabled') == false) {
			Swal.fire({
				icon: "error",
				title: "닉네임 중복확인을 해주세요",
				confirmButtonText: '확인'
			});
			return;
		}
	
		// 비밀번호 체크
		let pwd = $('#password').val();
		let pwdCheck = $('#passwordCheck').val();
	
		if (pwd == pwdCheck) { // 비밀번호 일치하면 회원가입 진행
			//$('#joinForm').submit();
			let memberVO = $("#joinForm").serialize();
			$.ajax('/join', {
				type: 'post',
				data: memberVO
			})
				.done(result => {
					console.log('넘어온 값은', result);
					if (result == 'Success') {
						Swal.fire({
							title: '회원가입이 완료되었습니다!',
							text: '취소를 누르시면 메인페이지,\n확인을 누르시면 로그인페이지로 이동합니다',
							icon: 'success',
							showCancelButton: true,
							confirmButtonColor: '#3085d6',
							cancelButtonColor: '#d33',
							confirmButtonText: '확인',
							cancelButtonText: '취소',
							reverseButtons: true, // 버튼 순서 거꾸로
	
						}).then((result) => {
							if (result.isConfirmed) {
								window.location.href = '/loginForm';
							} else if (result.isDismissed) {
								window.location.href = '/';
							}
						})
					} else {
						Swal.fire({
							icon: "error",
							title: "죄송합니다, 오류가 발생했습니다",
							text: "문제가 계속된다면 고객센터로 연락바랍니다",
							confirmButtonText: '확인'
						});
					}
				})
				.fail(err => console.log(err))
			
		} else {
			Swal.fire({
				icon: "error",
				title: "비밀번호가 일치하지 않습니다",
				confirmButtonText: '확인'
			});
			$('#passwordCheck').val('');
		}
		
	} else {
		return;
	}
	
})
/* 가입하기 클릭시 end */

// 입력여부 검증 함수
function inputCheck() {
	let inputs = $('input');
	let cnt = 0;
	
	for(let i=0; i<inputs.length; i++) {
		if($(inputs[i]).val() == '') {
			let targetId = $(inputs[i]).prop('id');
			
			if(targetId != 'detailAddr') {
				cnt++;
			}
			
			// 이름이 공백일때
			if(targetId == 'memberName') {
				Swal.fire({
					icon: "error",
					title: "이름을 입력해 주세요",
					confirmButtonText: '확인'
				});
				return false;
			}
			
			// 아이디가 공백일때
			if(targetId == 'memberId') {
				Swal.fire({
					icon: "error",
					title: "아이디를 입력해 주세요",
					confirmButtonText: '확인'
				});
				return false;
			}
			
			// 닉네임이 공백일때
			if(targetId == 'nickname') {
				Swal.fire({
					icon: "error",
					title: "닉네임을 입력해 주세요",
					confirmButtonText: '확인'
				});
				return false;
			}
			
			// 비밀번호가 공백일때
			if(targetId == 'password') {
				Swal.fire({
					icon: "error",
					title: "비밀번호를 입력해 주세요",
					confirmButtonText: '확인'
				});
				return false;
			}
			
			// 비밀번호확인이 공백일때
			if(targetId == 'passwordCheck') {
				Swal.fire({
					icon: "error",
					title: "비밀번호 확인을 진행해 주세요",
					confirmButtonText: '확인'
				});
				return false;
			}
			
			// 주소가 공백일때
			if(targetId == 'addr') {
				Swal.fire({
					icon: "error",
					title: "주소를 입력해 주세요",
					confirmButtonText: '확인'
				});
				return false;
			}
			
			// 휴대폰이 공백일때
			if(targetId == 'phone') {
				Swal.fire({
					icon: "error",
					title: "휴대폰 번호를 입력해 주세요",
					text: "하이픈없이 숫자만 입력하셔야합니다",
					confirmButtonText: '확인'
				});
				return false;
			}
			
			// 인증번호가 공백일때
			if(targetId == 'checkPhone') {
				Swal.fire({
					icon: "error",
					title: "인증을 진행해 주세요",
					text: "본인인증 버튼을 클릭하면 인증번호가 발송됩니다",
					confirmButtonText: '확인'
				});
				return false;
			}
		}
	}
	
	// 모든 값이 다 입력되었을 때 true 반환
	if(cnt == 0) {
		return true;
	}
}




