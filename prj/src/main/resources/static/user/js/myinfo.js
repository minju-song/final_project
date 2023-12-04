/**
 * 마이페이지-나의정보
 */

$(function(){
debugger
let url = $('#infoView').data("url");

/* 닉네임 변경 start */
$('#nickUpdate').click((e) => {
	let btnText = $('#nickUpdate').text();

	if (btnText == '닉네임 변경') {
		// input 포커싱
		$('#nickName').removeAttr('readonly');
		$('#nickName').focus();
		$('#nickName').addClass('form-control');
	}

	// 닉네임 변화시 버튼 변경
	$('#nickName').keyup((e) => {
		$('#nickUpdate').text('닉네임 저장');
		$('#nickUpdate').css('background-color', '#007bff');
	})

	if (btnText == '닉네임 저장') {
		const nickname = $('#nickName').val();
		$.ajax('/member/myinfo/updateInfo', {
			type: 'post',
			data: { nickname: nickname }
		})
			.done(result => {
				if (result) {
					Toast.fire({
						icon: 'success',
						title: '닉네임이 변경되었습니다!'
					})
					$('#infoView').load(url);
				} else {
					Toast.fire({
						icon: 'error',
						title: '이미 사용중인 닉네임입니다!'
					})
				}
			})
			.fail(err => console.log(err))
	}
})
/* 닉네임 변경 end */


/* 자기소개 변경 start */
let bool = false;
$('#memberIntro').on({
	change: function (e) {
		bool = true;
	},
	focusout: function (e) {
		if (bool) {
			let memberIntro = $(e.target).val();

			$.ajax('/member/myinfo/updateInfo', {
				type: 'post',
				data: { memberIntro: memberIntro }
			})
				.done((result) => {
					if (result) {
						Toast.fire({
							icon: 'success',
							title: '소개글이 변경되었습니다!'
						})
						$('#infoView').load(url);
					} else {
						Toast.fire({
							icon: 'error',
							title: '죄송합니다, 저장 중 오류가 발생했습니다'
						})
						
						$('#infoView').load(url);
					}
				})
				.fail(err => console.log(err))
		}
	}
})
/* 자기소개 변경 end */



/* 이름/주소 변경 start */
var _memberName = $('#memberName').val();
var _addr = $('#addr').val();
var _detailAddr = $('#detailAddr').val();
$('#infoUpdate').click(() => {
	// 수정 가능한 화면 보여주기.
	$('#info_view').css('display', 'none');
	$('#info_input').css('display', 'block');
})

$('#infoSave').click(() => {
	let memberName = $('#memberName').val();
	let addr = $('#addr1').val();
	let detailAddr = $('#detailAddr').val();

	if ((memberName != _memberName) || (addr != _addr) || (detailAddr != _detailAddr)) {
		addr += ' '; // 상세주소랑 띄워주기
		$.ajax('/member/myinfo/updateInfo', {
			type: 'post',
			data: {
				memberName: memberName,
				addr: addr,
				detailAddr: detailAddr
			}
		})
			.done((result) => {
				if (result) {
					Toast.fire({
						icon: 'success',
						title: '변경되었습니다!'
					})
					
					$('#infoView').load(url);
				} else {
					Toast.fire({
						icon: 'error',
						title: '죄송합니다, 저장 중 오류가 발생했습니다'
					})
					
					$('#infoView').load(url);
				}
			})
			.fail(err => console.log(err))
	} else {
		Toast.fire({
			icon: 'question',
			title: '변경된 내용이 없습니다'
		})
		
		$('#infoView').load(url);
	}
})
$('#infoCancle').click(() => {
	Swal.fire({
		text: "저장하지 않고 돌아가시려면 확인을 눌러주세요",
		showCancelButton: true,
		confirmButtonText: "확인",
	}).then((result) => {
		if (result.isConfirmed) {
			
			$('#infoView').load(url);
		}
	});
})
/* 이름/주소 변경 start */


/* 휴대폰번호 변경하기 start */
$('#phoneUpdate').click((e) => {
	$(e.target).css('display', 'none'); // 버튼 변경(변경하기 => 본인인증)
	$('#authNumCheck').css('display', 'inline-block');
	$('#phone').prop('readonly', false);
	$('#phone').focus();
})

/* 본인인증 문자 발송 start */
var time = 180; // 인증시간
var remainingMin = document.querySelector('#time_min');
var remainingSec = document.querySelector('#time_sec');
var authPassYn = false;

$('#authNumCheck').click(function () {
	const reception = $('#phone').val();

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
			// 이미 사용중인 번호인지 확인
			$.ajax('/member/myinfo/phoneCheck', {
				type: 'post',
				data: {phone: reception}
			})
			.done((result) => {
				if(result == true) { // 인증번호 발송 가능
					sendSms(reception); 
				} else {
					Swal.fire({
						icon: "error",
						title: "이미 인증된 정보가 있습니다",
						text: "입력하신 번호를 확인해주세요",
						confirmButtonText: '확인'
					})
					return;
				}
			})
			.fail(err => console.log(err));
		}
	}
});


function sendSms(reception) {
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
			if (result == 'Success') {
				Swal.fire({
					icon: "success",
					title: "인증번호가 발송되었습니다",
					confirmButtonText: '확인'
				});

				// 인증 만료시간 보여주기
				let interval = setInterval(function () {

					if (time > 0) { // >= 0 으로하면 -1까지 출력된다.
						time = time - 1; // 여기서 빼줘야 3분에서 3분 또 출력되지 않고, 바로 2분 59초로 넘어간다.
						let min = Math.floor(time / 60);
						let sec = String(time % 60).padStart(2, "0");
						$('#auth_timer').css('display', 'block');
						$('#auth_timer').text(min + ':' + sec);

						// 인증완료 상태 이면 interval 종료
						if (authPassYn == true) {
							$('#auth_timer').css('display', 'none');
							$('#userCheck').text('인증완료');
							$('#userCheck').prop('disabled', true);
							updatePhone();
							clearInterval(interval);
						}

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
/* 본인인증 문자 발송 end */

/* 인증번호 확인 및 업데이트 start */
$('#userCheck').click(function () {
	let authNum = $('#checkPhone').val();
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

		// 인증번호 확인 후 업데이트 시작
		const phone = $('#phone').val();
		$.ajax('/checkAuthNum/phoneUpdate', {
			type: 'get',
			data: { authNum, phone }
		})
			.done(result => {
				if (result == 'Success') {
					authPassYn = true; // 인증완료 상태로 변환
					Toast.fire({
						icon: 'success',
						title: '인증완료! 정상적으로 변경되었습니다'
					})
					
					$('#infoView').load(url);
				} else {
					Swal.fire({
						icon: "error",
						title: "인증실패!, 인증번호를 확인해 주세요",
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
/* 인증번호 확인 및 업데이트 end */

/* 비밀번호 변경 start */
$('#password').keydown((e) => {
	$('#passwordUpdate').prop('disabled', false);
	$('#passwordUpdate').removeClass('btn_disabled');
})

$('#passwordUpdate').click(() => {
	const password = $('#password').val();
	const passwordCheck = $('#passwordCheck').val();

	if(password == passwordCheck) {
		$.ajax('/member/myinfo/updateInfo', {
			type: 'post',
			data: {password}
		})
			.done((result) => {
				if (result) {
					Toast.fire({
						icon: 'success',
						title: '비밀변호가 변경되었습니다!'
					})
					
					$('#infoView').load(url);
				} else {
					Toast.fire({
						icon: 'error',
						title: '죄송합니다, 저장 중 오류가 발생했습니다'
					})
					
					$('#infoView').load(url);
				}
			})
			.fail(err => console.log(err))
	} else {
		Toast.fire({
			icon: 'error',
			title: '비밀번호가 일치하지 않습니다!'
		})
	}
})
/* 비밀번호 변경 end */
})
