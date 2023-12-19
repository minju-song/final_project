/**
 * 고객센터 글 등록 (공지/1:1)
 */


$(function () {
  let uploadFiles = [];

  // 첨부된 이미지 파일 리스트
  function getImageFiles(e) {

    const files = e.currentTarget.files;
    const imagePreview = document.querySelector('.file-list');

    if ([...files].length >= 6) {
      alert('이미지는 최대 5개까지 업로드가 가능합니다.');
      return;
    }

    // 파일타입 검사
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

  let realUpload = document.querySelector('.real_file');
  let upload = document.querySelector('#noticeImg');

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

//첨부파일 용량체크
document.addEventListener('DOMContentLoaded', function () {
  var form = document.querySelector('form');
  form.addEventListener('submit', function (event) {
    var imageInput = document.querySelector('input[name="imageFiles"]');
    var attachmentInput = document.querySelector('input[name="attachmentFiles"]');

    var totalSize = getTotalSize([imageInput, attachmentInput]);

    if (totalSize > 10 * 1024 * 1024) { // 10MB
      event.preventDefault();
      Swal.fire({
        icon: 'error',
        title: '파일 크기 초과',
        text: '이미지 및 첨부파일의 크기는 10MB를 초과할 수 없습니다.'
      });
    }
  });

  function getTotalSize(fileInputs) {
    var totalSize = 0;
    for (var i = 0; i < fileInputs.length; i++) {
      var files = fileInputs[i].files;
      if (files) {
        for (var j = 0; j < files.length; j++) {
          totalSize += files[j].size;
        }
      }
    }
    return totalSize;
  }
});


let cancelBtn = document.getElementById('cancelBtn');
cancelBtn.addEventListener('click', (e) => { location.href = "/cs/help/notice"});