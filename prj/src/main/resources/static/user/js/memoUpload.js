/**
 * 메모 사진 업로드
 */
 
// 첨부파일
let fileBtn = document.querySelectorAll('.file_btn');
let realFile = document.querySelectorAll('.real_file');
for(let i=0; i<fileBtn.length; i++){
	fileBtn[i].addEventListener('click', () => realFile[i].click());
	realFile[i].addEventListener('change', (e) => imgFileSelectHandler(e));
}

// 파일이 첨부되면.
let uploadFiles = [];
function imgFileSelectHandler(e) {
  	
  	let files = e.currentTarget.files;
  	let memo = $(e.target).closest('.memo');
  	let memoImage = $(e.target).siblings('.inputMemoId').children('.memo_image');
  	console.log(files);
  	
	// 파일타입 검사 및 미리보기 생성
	[...files].forEach(file => {
        if (!file.type.match("image/.*")) {
			alert('이미지 파일만 업로드가 가능합니다.');
			return
        }

        // 태그 생성
        if ([...files].length < 7) {
			uploadFiles.push(file);
			const reader = new FileReader();
			reader.onload = (e) => {
	            $(memoImage).prepend('<div class="upload" style="background-image: url(\'' + e.target.result +'">');
			};
          	reader.readAsDataURL(file);
        }
	});
  	
  	console.log(uploadFiles);
  	
  	uploadImg();
}

//파일 업로드
function uploadImg() {
	let formData = new FormData();
  	let file = document.querySelector('.real_file').files[0];
  	formData.append("file", file);
	
	for (const x of formData) {
	 console.log(x);
	};
	
	
  	/*fetch('/member/myInfo/uploadImg', {
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
	.catch(err => console.log(err));*/
}