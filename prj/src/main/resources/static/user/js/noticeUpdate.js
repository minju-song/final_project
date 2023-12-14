/**
 * tradeUpdate
 */

//이미지 삭제
function fileDeleteBtn(e) {
  let saveFile = e.target.dataset.file;
  $.ajax({
    url: '/member/attachmentDelete',  //이동할 jsp 파일 주소
    data: { saveFile, postId },
    success: function (data) {   //데이터 주고받기 성공했을 경우 실행할 결과
      console.log("성공");
      e.target.parentElement.remove();
    },
    error: function () {   //데이터 주고받기가 실패했을 경우 실행할 결과
      console.log("실패");
    }
  })
}









$(function () {
  let uploadFiles = [];
  /*
    // 저장 버튼
    document.querySelector('.saveBtn').addEventListener('click', function (e) {
      // form이 가지고 있는 내부 데이터를 하나의 클래스로 담는 역할.. 
      // jquery.serialize() or jquery.serializeArray()와 유사
      let formData = new FormData();
      // 1) Form 태그 내부의 데이터를 한번에 담음(통신을 위한 정보를 담는다)
      // 2) Ajax를 이용해서 'content-type:multipart/form-data'를 보내는 경우
  
      if (uploadFiles.length > 0) {
        for (let file of uploadFiles) {
          formData.append("uploadFiles", file); //통신을 위해 변수에 데이터를 담는다
        }
  
        //jQuery.ajax
        $.ajax({
          url: 'uploadAjax',
          type: 'POST',
          //기본값은 true, ajax 통신을 통해 데이터를 전송할 때, 기본적으로 key와 value값을 Query String으로 변환해서 보냅니다.
          processData: false,
          // multipart/form-data타입을 사용하기위해 false 로 지정합니다.
          contentType: false,
          data: formData,
          success: function (result) {
            console.log(result);
            for (let image of result) {
              console.log(image);
              let imgTag = $('<img/>').prop('src', 'images/' + image);
              $('#imageWrap').append(imgTag);
            }
          },
          error: function (reject) {
            console.log(reject);
          }
        });
      }
    })*/


  // 첨부된 이미지 파일 리스트
  function getImageFiles(e) {

    const files = e.currentTarget.files;
    const imagePreview = document.querySelector('.file-list');
    //const docFrag = new DocumentFragment();
    //console.log(typeof files, files);



    if ((Number(imgListSize) + [...files].length) >= 6) {
      swal.fire(
        '이미지 업로드 제한!',
        '이미지는 최대 5개까지 업로드가 가능합니다.',
        'warning'
      )
      return;
    }

    // 파일타입 검사
    [...files].forEach(file => {
      if (!file.type.match("image/.*")) {
        swal.fire(
          '부적절한 형식!',
          '이미지 파일만 업로드가 가능합니다.',
          'warning'
        )
        return
      }

      // 태그 생성
      if ([...files].length < 7) {
        uploadFiles.push(file);
        const reader = new FileReader();
        reader.onload = (e) => {
          const preview = createElement(e, file);
          imagePreview.appendChild(preview);
        };
        reader.readAsDataURL(file);
      }
    });

    console.log(uploadFiles);
  }

  function createElement(e, file) {
    const li = document.createElement('li');
    const img = document.createElement('img');
    const btn = document.createElement('img');
    li.setAttribute('calss', 'position_rela');
    img.setAttribute('src', e.target.result);
    img.setAttribute('class', 'imgSize');
    img.setAttribute('data-file', file.name);
    btn.setAttribute('data-file', file.name);
    btn.setAttribute('src', '/user/images/trade/delete-button.png');
    btn.setAttribute('class', 'delBtn position_absol');
    btn.addEventListener('click', delFile);
    li.appendChild(btn);
    li.appendChild(img);

    return li;
  }


  let canselBtn = document.getElementById('cancelBtn');
  let urlParams = new URL(location.href).searchParams;
  let checkMenu = location.pathname;
  let thisBoardId = urlParams.get('boardId');

  let realUpload = document.querySelector('.real_file');
  let upload = "";

  if (checkMenu == "/cs/help / question / update") {
    cancelBtn.addEventListener('click', (e) => { location.href = "/cs/help/question/view?boardId=" + thisBoardId }); 
    upload = document.querySelector('#questionAttach');

  } else {
    upload = document.querySelector('#noticeImg');
    cancelBtn.addEventListener('click', (e) => { location.href = "/cs/help/notice/view?boardId=" + thisBoardId });
  }


  console.log(thisBoardId);

  

  upload.addEventListener('click', () => realUpload.click());
  realUpload.addEventListener('change', getImageFiles);

  function delFile(e) {
    let fileName = e.target.getAttribute('data-file');

    let newFilesArr = uploadFiles.filter((file, idx) => {
      return file.name != fileName;
    })

    uploadFiles = newFilesArr;

    e.target.parentNode.remove();
  }
})

//http:형식 확인
/*$('#openKakaoAddr').change(() => {
  let expUrl = /^http[s]?:\/\/([\S]{3,})/i;
  let strUrl = $('#openKakaoAddr').val();
  console.log(expUrl.test(strUrl))
  if (!expUrl.test(strUrl)) {
    swal.fire(
          '부적절한 형식!',
            'url 형식에 맞지 않습니다',
          'warning'
        )
  }
})*/



