/**
 * 아이디/비밀번호 찾기 관련
 */
 
/* 탭클릭 이벤트 start */
$('#find_id').click(function(e){
	if($('#find_pwd a').hasClass('active') == true) {
		$('#find_pwd a').removeClass('active');
	}
	$('#find_id a').toggleClass('active');
	$('#findIdForm').css('display', 'flex');
	$('#findPwdForm').css('display', 'none');
})

$('#find_pwd').click(function(e){
	if($('#find_id a').hasClass('active') == true) {
		$('#find_id a').removeClass('active');
	}
	$('#find_pwd a').toggleClass('active');
	$('#findIdForm').css('display', 'none');
	$('#findPwdForm').css('display', 'flex');
})
/* 탭클릭 이벤트 end */




// 아이디 찾기 관련 이벤트 start

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
	$('#findIdForm #userCheck').click(function () {
		let authNum = $('#findIdForm #checkPhone').val();
		console.log(authNum);
		if (authNum == '') {
			Swal.fire({
				icon: "error",
				title: "인증번호를 입력하세요",
				confirmButtonText: '확인'
			});
			$('#findIdForm #checkPhone').focus();
		} else {
			// 인증번호 수정 막기
			$('#findIdForm #checkPhone').prop('readonly', true);
	
			// 인증번호 확인 시작
			$.ajax('/checkAuthNum', {
				type: 'get',
				data: { authNum }
			})
				.done(result => {
					console.log('넘어온 값은', result);
					if (result == 'Success') {
						$('#findIdForm #submitBtn').attr('data-disabled', 'false');
						$('#findIdForm #submitBtn').css('background-color', '#2c456f');
						$('#findIdForm #submitBtn').css('color', '#fff');
					} else {
						Swal.fire({
							icon: "error",
							title: "일치하지 않습니다, 인증번호를 확인해 주세요",
							confirmButtonText: '확인'
						});
						$('#findIdForm #checkPhone').prop('readonly', false);
						$('#findIdForm #checkPhone').val('');
						$('#findIdForm #checkPhone').focus();
					}
				})
				.fail(err => console.log(err))
		}
	})
	/* 인증번호 확인 end */
	
	/* 아이디 찾기 버튼 클릭시 start */
	$('#findIdForm #submitBtn').click(function () {
		let bool = inputCheck('findIdForm'); // 입력여부 검증
		
		console.log(bool);
		
		if(bool == true) { // 아이디 찾기 진행		
			// 인증버튼이 활성화되어있을 때(인증을 하지 않은 경우)
			if($('#findIdForm #userCheck').prop('disabled') == false) {
				Swal.fire({
					icon: "error",
					title: "인증을 진행해 주세요",
					text: "본인인증 버튼을 클릭하면 인증번호가 발송됩니다",
					confirmButtonText: '확인'
				});
				return false;
			}
			
			// 입력한 아이디와 휴대폰번호로 일치하는 건 확인
			let memberVO = $("#findIdForm").serialize();
			$.ajax('/find', {
					type: 'get',
					data: memberVO
				})
					.done(result => {
						console.log('넘어온 값은', result);
						if (result == 'Fail') {
							Swal.fire({
								title: '일치하는 정보가 없습니다',
								text: '확인을 누르시면 회원가입 페이지로 이동합니다',
								icon: 'error',
								confirmButtonText: '확인'
							}).then((result) => {
								if (result.isConfirmed) {
									window.location.href = '/joinForm';
								} else if (result.isDismissed) {
									window.location.href = '/findForm';
								}
							})
						} else {
							Swal.fire({
								icon: "success",
								title: "회원님의 아이디는 [" + result + "]입니다",
								text: "확인을 누르시면 로그인 페이지로 이동합니다.",
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
									window.location.href = '/findForm';
								}
							});
						}
					})
					.fail(err => console.log(err))
			
		} else {
			return;
		}
	})
	/* 아이디 찾기 버튼 클릭시 end */
	
// 아이디 찾기 관련 이벤트 end




// 비밀번호 찾기 관련 이벤트 start

	/* 비밀번호 찾기 버튼 클릭시 start */
	$('#findPwdForm #submitBtn').click(function () {
		let bool = inputCheck('findPwdForm'); // 입력여부 검증
		
		console.log(bool);
		
		if(bool == true) { // 비밀번호 찾기 진행		
						
			// 입력한 이름과 아이디로 일치하는 회원 찾기(성공이면 임시비밀번호 발급)
			let memberVO = $("#findPwdForm").serialize();
			let email = $('#findPwdForm #memberId').val();
			$.ajax('/find', {
					type: 'get',
					data: memberVO
				})
					.done(result => {
						console.log('넘어온 값은', result);
						if (result == 'Fail') {
							Swal.fire({
								title: '일치하는 정보가 없습니다',
								text: '입력하신 정보를 확인해 주세요',
								icon: 'error',
								confirmButtonText: '확인'
		
							})
						} else {
							// 임시비밀번호 메일 발송
							console.log(memberVO);
							console.log(email);
							$.ajax('/sendmail/password', {
								type: 'post',
								data: memberVO
							})
								.done(result => {
									console.log('넘어온 값은', result);
								})
								.fail(err => console.log(err))
						
							Swal.fire({
								icon: "success",
								title: "임시비밀번호를 발송했습니다",
								html: "안전 발송을 위해 약간의 시간이 소요될 수 있습니다<br/>확인을 누르시면 로그인페이지로 이동합니다",
								confirmButtonColor: '#3085d6',
								confirmButtonText: '확인',
							}).then((result) => {
								if (result.isConfirmed) {
									window.location.href = '/loginForm';
								}
							})
						}
					})
					.fail(err => console.log(err))
			
		} else {
			return;
		}
	})
	/* 비밀번호 찾기 버튼 클릭시 end */

// 비밀번호 찾기 관련 이벤트 end




function inputCheck(formId) {
	let inputs;
	if(formId == 'findIdForm') {
		inputs = $('#findIdForm input');
	} else if (formId == 'findPwdForm') {
		inputs = $('#findPwdForm input');
	}
	let cnt = 0;
	
	for(let i=0; i<inputs.length; i++) {
		if($(inputs[i]).val() == '') {
			cnt++;
			let targetId = $(inputs[i]).prop('id');
			
			// 이름이 공백일때
			if(targetId == 'memberName') {
				Swal.fire({
					icon: "error",
					title: "이름을 입력해 주세요",
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
			
			// 아이디가 공백일때
			if(targetId == 'memberId') {
				Swal.fire({
					icon: "error",
					title: "아이디를 입력해 주세요",
					text: "이메일 형식으로 입력해 주세요",
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









