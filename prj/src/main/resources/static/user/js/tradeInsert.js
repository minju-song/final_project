/**
 * 중고거래 등록
 */
 
  //장소, 위도, 경도 저장
let tradePlace = '';
let latitude = '';
let longitude = '';
document.getElementById('placeSelectButton').addEventListener('click', function(){
	let width = 500; //팝업의 너비
	let height = 600; //팝업의 높이
	let geocoder = new daum.maps.services.Geocoder();
	new daum.Postcode({
        oncomplete: function(data) {
        	tradePlace = data.address; // 최종 주소 변수
        	geocoder.addressSearch(data.address, function(results, status) {
                // 정상적으로 검색이 완료됐으면
                if (status === daum.maps.services.Status.OK) {
                    let result = results[0];
                    latitude = result.y;
                    longitude = result.x;
                    console.log(latitude + ", " + longitude)
                    document.getElementById('latitude').value = latitude;
                    document.getElementById('longitude').value = longitude;
                }
        	});
        	if(tradePlace != ''){
               document.querySelector('.tradePlaceSelect').style.display = 'block';
               document.querySelector('.tradePlaceSelect').value = tradePlace;
            }	
        },
		theme: {
			searchBgColor: "#09203F",
			queryTextColor: "#FFFFFF"
	    }
    }).open({
		left: (window.screen.width / 2) - (width / 2),
	    top: (window.screen.height / 2) - (height / 2)
    });
})

//장소 선택 활성화&비활성화
$(document).on("change", "select[id=tradeType]", function(){
	let value = $(this).find("option:selected").val();
	if(value == 'TA1'){
		$('#placeSelectButton').attr("disabled", false);
		$('#placeSelectButton').css("background-color", "#09203f");
	}else{
		$('#placeSelectButton').attr("disabled", true);
		$('#placeSelectButton').css("background-color", "rgb(201 204 205)");
		document.querySelector('.tradePlaceSelect').style.display = 'none';
		document.getElementById('latitude').value = '';
        document.getElementById('longitude').value = '';
        document.querySelector('.tradePlaceSelect').value = '';
	}
});

//가격 양수만 입력
let number = document.getElementById('price');

number.onkeydown = function(e) {
    if(!((e.keyCode > 95 && e.keyCode < 106)
    	|| (e.keyCode > 47 && e.keyCode < 58) 
    	|| e.keyCode == 8)) {
        return false;
    }
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
    console.log(files);
    console.log(files.length);

    if ([...files].length >= 6) {
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

  let realUpload = document.querySelector('.real_file');
  let upload = document.querySelector('#tradeImg');

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