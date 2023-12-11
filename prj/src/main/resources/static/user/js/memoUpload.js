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
			const reader = new FileReader();
			reader.onload = (e) => {
	            $(memoImage).prepend('<div class="upload" style="background-image: url(\'' + e.target.result +'">');
			};
          	reader.readAsDataURL(file);
        }
	});
  	
  	uploadImg(e);
}

//파일 업로드
function uploadImg(e) {
	let memoId = $(e.target).siblings('.inputMemoId').data('memo');
	let target = e.target;
	let formData = new FormData();
  	let files = target.files;
	
	
	// 메모 아이디 formData에 append
	formData.append("memoId", memoId);
	// 첩부된 파일 목록 formData에 append
	for(let file of files) {
		formData.append("uploadFiles", file); //통신을 위해 변수에 데이터를 담는다
	}
	for (const x of formData) {
	 console.log(x);
	};
	
	//jQuery.ajax
	$.ajax({
       url: '/member/memoUploadImg',	
       type: 'POST',
       processData: false,
       contentType: false,	
       data: formData,               
       success: function(result){
           console.log(result);
           
       },
       error: function(reject){	
           console.log(reject);
       }
   }); 
}