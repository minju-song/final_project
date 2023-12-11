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

