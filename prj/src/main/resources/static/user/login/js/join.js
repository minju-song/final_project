/**
 * 회원가입 관련
 */

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
							title: '사용할 수 없는 아이디입니다',
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
		let email = $('#memberId').val();
	
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
						
						// 회원가입 메일 발송
						console.log(memberVO);
						$.ajax('/sendmail/joinmail', {
							type: 'post',
							data: memberVO
						})
							.done(result => {
								console.log('넘어온 값은', result);
							})
							.fail(err => console.log(err))
						
					} else if(result == 'JoinUser') {
						Swal.fire({
							icon: "error",
							title: "이미 가입된 정보가 있습니다",
							text: "아이디 또는 비밀번호를 찾으려면 확인을 누르세요",
							showCancelButton: true,
							confirmButtonColor: '#3085d6',
							cancelButtonColor: '#d33',
							confirmButtonText: '확인',
							cancelButtonText: '취소',
							reverseButtons: true, // 버튼 순서 거꾸로
						}).then((result) => {
							if (result.isConfirmed) {
								window.location.href = '/findForm';
							} else if (result.isDismissed) {
								window.location.href = '/joinForm';
							}
						});
					} else if(result == 'Fail') {
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




