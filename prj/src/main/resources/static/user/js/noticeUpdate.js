/**
 * tradeUpdate
 */

//이미지 삭제
function fileDeleteBtn(e) {
  let saveFile = e.target.dataset.file;
  let urlParams = new URL(location.href).searchParams;
  let postId = urlParams.get('boardId');
  let menuType = "";


  if (location.pathname == "/cs/help/question/update") {
    menuType = "AA8"
  } else if (location.pathname == "/cs/help/notice/update") {
    menuType = "AA6"
  }

  $.ajax({
    url: '/member/attachment/delete',  
    data: { saveFile, postId, menuType },
    success: function (data) {   //데이터 주고받기 성공했을 경우 실행할 결과
      console.log("성공");

      console.log(saveFile +"==" + postId +"==" + menuType)
      e.target.parentElement.remove();
    },
    error: function () {   //데이터 주고받기가 실패했을 경우 실행할 결과
      console.log("실패");
    }
  })
}


$(function () {
  let uploadFiles = [];
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

  let cancelBtn = document.getElementById('cancelBtn');
  let urlParams = new URL(location.href).searchParams;
  let checkMenu = location.pathname;
  let thisBoardId = urlParams.get('boardId');

  let realUpload = document.querySelector('.real_file');
  let upload = "";

  if (checkMenu == "/cs/help/question/update") {
    cancelBtn.addEventListener('click', (e) => { location.href = "/cs/help/question/view?boardId=" + thisBoardId });
    upload = document.querySelector('#questionImg');

  } else if (checkMenu == "/cs/help/notice/update") {
    upload = document.querySelector('#noticeImg');
    cancelBtn.addEventListener('click', (e) => { location.href = "/cs/help/notice/view?boardId=" + thisBoardId });
  }



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



