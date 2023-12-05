/**
 * 이미지 첨부
 */

var realUpload = document.querySelector('.real_upload');
var upload = document.querySelector('.upload');

// 썸네일을 클릭하면 input[type="file"]에 클릭이벤트를 발생
upload.addEventListener('click', () => realUpload.click());

realUpload.addEventListener('change', (e) => imgFileSelectHandler(e))

function imgFileSelectHandler(e) {
	
  	let files = e.target.files;
  	let reader = new FileReader();

  	// 이미지 화면에 로드
  	reader.onload = function (e) {
    	$(".upload").css("background-image", "url(" + e.target.result + ")");
  	}
  	reader.readAsDataURL(files[0]);
  	console.log(reader.readyState); //0(로드되지않음), 1(데이터로딩중), 2(모든 요청이 읽기 완료)
  	
  	uploadImg();
}

//파일 업로드
function uploadImg() {
	let formData = new FormData();
  	let file = document.querySelector('.real_upload').files[0];
  	formData.append("file", file);

  	fetch('/member/myInfo/uploadImg', {
		method : 'post',
		body : formData
	})
	.then((response) => response.json())
  	.then((data) => {
  		console.log(data);
  		const Toast = Swal.mixin({
		    toast: true,
		    position: 'center',
		    showConfirmButton: false,
		    timer: 2000,
		    timerProgressBar: true,
		    didOpen: (toast) => {
		        toast.addEventListener('mouseenter', Swal.stopTimer)
		        toast.addEventListener('mouseleave', Swal.resumeTimer)
		    }
		})
  		if(data.result == 'Success') {
			Toast.fire({
			    icon: 'success',
			    title: '프로필 사진이 변경되었습니다!'
			})
  		} else {
  			Toast.fire({
			    icon: 'error',
			    title: '죄송합니다, 다시 시도해 주세요'
			})
  		}
  	})
	.catch(err => console.log(err));
}

