/**
 * memo.js
 */
 
 	//팔레트 클릭시 색상표 출력 함수 
	function openPalette(e, pal){
		if(pal.nextElementSibling.style.display == 'block'){
			pal.nextElementSibling.style.display = 'none';
			let selMenubars = $(e.currentTarget).closest('.memo').find('.menubars');
			for(let i=0; i<selMenubars.length; i++){
				selMenubars[i].classList.toggle('menubars_active');
			}
		}else{
			pal.nextElementSibling.style.display = 'block';
			let selMenubars = $(e.currentTarget).closest('.memo').find('.menubars');
			for(let i=0; i<selMenubars.length; i++){
				selMenubars[i].classList.toggle('menubars_active');
			}
		}
	}
	
	//핀 색상 변경 함수
	function pinChange(e){
	console.log("전체리스트에서 핀클릭");
		let memoId = e.currentTarget.parentElement.querySelector('.inputMemoId').dataset.memo;
		console.log(memoId);
		if(!e.target.classList.contains('bi-pin-fill')){
			e.target.src=`/user/images/memo/pin-fill.svg`;
			bookmark = 'Y';
		}else{
			e.target.src=`/user/images/memo/pin.svg`;
			bookmark = 'N';
		}
		$.ajax({    
			type:"POST",
			url : '/member/memoUpdate',  //이동할 jsp 파일 주소
			data : {memoId, bookmark},
			dataType:'text',
			success : function(result) { // 결과 성공 콜백함수        
				if(bookmark == 'Y'){
					$('.importmemoStart').append($(e.target.parentElement));
				}else{
					$('.normalmemoStart').prepend($(e.target.parentElement));
				}
			},    
			error : function(request, status, error) { // 결과 에러 콜백함수        
				console.log(error)    
			}})
		e.target.classList.toggle('bi-pin-fill');
	};
	
	//색상 클릭 시 메모 바탕색 변경 함수
	function changeMemoColor(e){
    	console.log(e.currentTarget);
    	let memoId = $(e.currentTarget).closest('.memo').find('.inputMemoId').data('memo');
    	console.log(memoId);
    	let backcolor = e.currentTarget.closest('.memo');
    	let color = e.currentTarget.value
    	$.ajax({    
        type:"POST",
        url : '/member/memoUpdate',  //이동할 jsp 파일 주소
        data : {memoId, color},
        dataType:'text',
        success : function(result) { // 결과 성공 콜백함수        
           console.log(result);    
        },    
        error : function(request, status, error) { // 결과 에러 콜백함수        
           console.log(error)    
        }})
    	backcolor.style.backgroundColor = color;
    };
    
 	   
    
 
 	
  	const draggables = document.querySelectorAll(".draggable");
	const containers = document.querySelectorAll(".dragcontainer");
	//드래그 시작될 때
	draggables.forEach(draggable => {
	  draggable.addEventListener("dragstart", () => {
	    draggable.classList.add("dragging");
	  });
	  
	  //드래그가 끝날 때
	  draggable.addEventListener("dragend", () => {
	    draggable.classList.remove("dragging");
	    let memoId = draggable.querySelector('.inputMemoId').dataset.memo;
	    if($(draggable).parent('.dragcontainer')[0].classList.contains('importmemoStart')){
	    	bookmark = 'Y';
	    	console.log($(draggable).find('.bi-pin')[0].src)
	    	$(draggable).find('.bi-pin')[0].src=`/user/images/memo/pin-fill.svg`;
	    }else{
	    	bookmark = 'N';
	    	$(draggable).find('.bi-pin')[0].src=`/user/images/memo/pin.svg`;
	    }
	    $(draggable).find('.bi-pin')[0].classList.toggle('bi-pin-fill');
	    $.ajax({    
			type:"POST",
			url : '/member/memoUpdate',  //이동할 jsp 파일 주소
			data : {memoId, bookmark},
			dataType:'text',
			success : function(result) { // 결과 성공 콜백함수   
			console.log("성공")
			},    
			error : function(request, status, error) { // 결과 에러 콜백함수        
				console.log(error)    
			}})
	  });
	});
	
	//이벤트가 달린 영역 위에 드래그 요소가 있을 때
	containers.forEach(dragcontainer => {
	  dragcontainer.addEventListener("dragover", e => {
	    e.preventDefault();
	    const afterElement = getDragAfterElement(dragcontainer, e.clientX);
	    const draggable = document.querySelector(".dragging");
	    if (afterElement === undefined) {
	      dragcontainer.appendChild(draggable);
	    } else {
	      dragcontainer.insertBefore(draggable, afterElement);
	    }
	  });
	});
	
	function getDragAfterElement(dragcontainer, x) {
	  const draggableElements = [
	    ...dragcontainer.querySelectorAll(".draggable:not(.dragging)"),
	  ];
	
	  return draggableElements.reduce(
	    (closest, child) => {
	      const box = child.getBoundingClientRect();
	      const offset = x - box.left - box.width / 2;
	      // console.log(offset);
	      if (offset < 0 && offset > closest.offset) {
	        return { offset: offset, element: child };
	      } else {
	        return closest;
	      }
	    },
	    { offset: Number.NEGATIVE_INFINITY },
	  ).element;
	}
	
	//핀 클릭시 색상 변경
	let pins = document.querySelectorAll('.bi-pin');
	let bookmark = '';
	for(let i=0; i<pins.length; i++){
		pins[i].addEventListener('click', function(e){pinChange(e);	});
	}

	//팔레트 클릭시 색상표 출력
	let pal = document.querySelectorAll('.bi-palette');
	//console.log(pal[0].nextElementSibling);
	for(let i=0; i<pal.length; i++){
		pal[i].addEventListener('click', function(e){openPalette(e, pal[i]);	});
	}

	
	//hashtag 표시
    let input = $('input[name=tags]:gt(0)');
    for(let i=0; i<input.length; i++){
	    new Tagify(input[i], {
      	maxTags: 4
    	}) 
    }
    
    //색상 클릭 시 메모 바탕색 변경
    let colors = document.querySelectorAll('.changeColor input[type=radio]');
    for(let i=0; i<colors.length; i++){
	    colors[i].addEventListener('click', function(e){changeMemoColor(e);	});
    }
    
    //전체 삭제 버튼 클릭
    let deleteAllBtn = document.querySelector('.deleteAll');
    deleteAllBtn.addEventListener('click', function(e){
    	let checks = document.querySelectorAll('input[type=checkbox]');
    	if(checks[0].checked == true){
			for(let i=2; i<checks.length; i++){
				checks[i].checked = true;
			}	
		}else{
			for(let e=1; e<checks.length; e++){
    			checks[e].checked = false;
			}
    	}
    })
    
   //삭제버튼 클릭 시 해당 메모 삭제
   let deleteBtn = document.querySelector('.button');
    deleteBtn.addEventListener('click', function(e){
   		let deleteMemo = document.querySelectorAll('input[type=checkbox]');
    	for(let i=1; i<deleteMemo.length; i++){
	    	if(deleteMemo[i].checked == true) {
	    		let memoId = deleteMemo[i].value;
	    		$.ajax({
				url : '/member/memoDelete',  //이동할 jsp 파일 주소
				data : {memoId},
				success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
					deleteMemoSuc();
				},
				error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
					console.log("메모삭제 실패");
				}
			})
	    		deleteMemo[i].parentElement.parentElement.remove();
	    	}
    	}
    })
    
    //메모 삭제 알림창
    function deleteMemoSuc(){
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
		
		Toast.fire({
		    icon: 'success',
		    title: '선택한 메모가 삭제되었습니다.'
		})
    }
    
   //hashtag
   let inputhash = document.querySelectorAll('input[name=plustag]')
   for(let i=0; i<inputhash.length; i++){
       let tagify = new Tagify(inputhash[i]); // initialize Tagify
       // 태그가 추가되면 이벤트 발생
       tagify.on('add', function() {
         //console.log(tagify.value); // 입력된 태그 정보 객체
       })
    }
    
    //메모 상세보기 변경 설정(핀, 색상)
    let writepins = $('.modal').find('.modal-bi-pin');
    for(let i=0; i<writepins.length; i++){
		writepins[i].addEventListener('click', function(e){
			console.log("상세&등록 창에서의 핀 눌렀을 때")
			if(!e.target.classList.contains('bi-pin-fill')){
				e.target.src=`/user/images/memo/pin-fill.svg`;
			}else{
				e.target.src=`/user/images/memo/pin.svg`;
			}
			console.log("bi-pin-fill 토글한다")
			e.target.classList.toggle('bi-pin-fill');
		})
		let writecolors = document.querySelectorAll('.modal [type=radio]');
		for(let i=0; i<writecolors.length; i++){
	    	writecolors[i].addEventListener('click', function(e){
	    	let backcolor = e.currentTarget.parentElement.parentElement.parentElement.parentElement;
	    	let color = e.currentTarget.value
	    	backcolor.style.backgroundColor = color;
	    	})
	    } 
    }
	    
    //메모 상세보기
    let memoId = 0;
    let beforememopin = '';
    $('#writedMemo').on('show.bs.modal', function(event) { 
        memoId = $(event.relatedTarget).data('memo');
        beforememopin = event.relatedTarget.previousElementSibling.src;
        console.log(beforememopin)
        //Ajax 요청
        $.ajax({
			url : '/member/memoInfo',  //이동할 jsp 파일 주소
			data : {memoId},
			success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
				$('#writedMemo').find('[name="content"]')[0].value = data.content;
				$('#writedMemo').find('[name=plustag]')[0].value = data.hashTag;
				$('#writedMemo').find('.modal-body')[0].style.backgroundColor = data.color;
				$('#writedMemo')[0].value = memoId;
				
				// 이미지관련
				$('#writedMemo').find('.memo_image').empty();
				for(let i=0; i<data.images.length; i++) {
					let tag = $('<div>').addClass('upload').css('background-image', `url('/images/${data.images[i].saveFile}')`);
					let delBtn = $('<img>').attr('src', '/user/images/trade/delete-button.png').data('file', data.images[i].saveFile).addClass('delBtn position_absol test');
					console.log(delBtn[0])
					delBtn[0].addEventListener('click', delFileDie);
					$(tag).append(delBtn);
					$('#writedMemo').find('.memo_image').append(tag);
				}
				
				if(data.bookmark == 'Y'){
					$('#writedMemo').find('.modal-bi-pin')[0].src = '/user/images/memo/pin-fill.svg';
				}else{
					$('#writedMemo').find('.modal-bi-pin').removeClass('bi-pin-fill');
					$('#writedMemo').find('.modal-bi-pin')[0].src = '/user/images/memo/pin.svg';
				}
			},
			error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
				console.log('실패');
			}
		})
	});
    
	//메모등록
	let uploadFiles = [];
	$('#insertMemo').on('show.bs.modal', function(event) {
		//등록 모달 열릴때 추가된 이미지 태그 삭제, 파일 담는 배열 비우기
		$('#insertMemo').find('.memo_image').empty();
		uploadFiles = [];
	});
    $('#insertMemo').on('hidden.bs.modal', function (event) {
  		let content = document.querySelector('#insertMemo').querySelector('[name=content]').value;
   		let plustag = document.querySelector('#insertMemo').querySelector('[name=plustag]').value;
   		let tag = document.querySelectorAll('#insertMemo .tagify__tag-text');
   		let color = document.querySelector('#insertMemo').querySelector('.modal-body').style.backgroundColor;
   		let bookmark = document.querySelector('#insertMemo').querySelector('.modal-bi-pin').src;
   		
   		// 사진첨부
   		let formData = new FormData();
   		let target = $('.insert_file');
	  	let files = $('.insert_file')[0].files;
		
		// 첨부된 파일 목록 formData에 append
		if(uploadFiles.length > 0) {
			console.log('얍!');
			console.log(uploadFiles);
			for(let file of uploadFiles) {
				formData.append("uploadFiles", file); //통신을 위해 변수에 데이터를 담는다
			}
		}

   		if(content != '' || plustag != '' || uploadFiles.length > 0){
	   		if(bookmark.indexOf('pin.svg') == -1){
	   			bookmark = 'Y';
	   		}else{
	   			bookmark = 'N';
	   		}
	   		let hashTag = '';
	   		for(let i=0; i<tag.length; i++) {
	   			hashTag += tag[i].innerText;
	   			if(i<tag.length-1) {
	   				hashTag += ', '
	   			}
	   		}
	   		
	   		formData.append("color", color);
	   		formData.append("content", content);
	   		formData.append("hashTag", hashTag);
	   		formData.append("bookmark", bookmark);
	   		
	   		//ajax 요청
			$.ajax({
				type:"POST",
				url : '/member/memoInsert',  //이동할 jsp 파일 주소
				data : formData,
				dataType:'json',
				contentType: false,
  				processData: false,
				success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
			        //function(memoId)를 쓰게 되면 전달받은 데이터가 data안에 담아서 들어오게 된다.
					$('#insertMemo').find('[name="content"]')[0].value = '';
					$('#insertMemo').find('[name=plustag]')[0].value = '';
					$('#insertMemo').find('.modal-body')[0].style.backgroundColor = 'rgb(255, 242, 204)';
					$('#insertMemo').find('.modal-bi-pin')[0].src = '/user/images/memo/pin.svg';
					
					//사진파일 배열
					let imageArr = [];
					for(let i=0; i<data.images.length; i++) {
						imageArr[i] = data.images[i].saveFile;
					}
					console.log(imageArr);
					
					//메모 이어붙이기
					addMemo(content, hashTag, color, bookmark, data.memoId, imageArr);
				},
				error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
					console.log('등록실패');
				}
			})	
		}
		$('#insertMemo').find('.modal-body')[0].style.backgroundColor = 'rgb(255, 242, 204)';
		$('#insertMemo').find('.modal-bi-pin')[0].src = '/user/images/memo/pin.svg';	
	});
	
	//메모 이어붙이기
	function addMemo(content, hashTag, color, bookmark, memoId, images){
		console.log(images);
		let clone = $('.btn-modal:eq(0)').clone();
		clone.css('display', 'block');
		clone.find('.inputMemoId')[0].dataset.memo = memoId;
		clone.find('[type=checkbox]')[0].value = memoId;
		clone.find('.memotext')[0].innerText = content;
		clone.find('[name=tags]')[0].value = hashTag;
		new Tagify (clone.find('[name=tags]')[0]);
		if(color == ''){
			clone.find('.memo').prevObject[0].style.backgroundColor = 'rgb(255, 242, 204)';
		}else{
			clone.find('.memo').prevObject[0].style.backgroundColor = color;
		}
		if(bookmark == 'Y'){
			clone.find('.bi-pin')[0].src = '/user/images/memo/pin-fill.svg';
			$('.importmemoStart').append(clone);
		}else{
			clone.find('.bi-pin')[0].src = '/user/images/memo/pin.svg';
			clone.find('.bi-pin')[0].classList.remove('bi-pin-fill');
			$('.normalmemoStart').append(clone);
		}
		
		//이미지 그리기
		let memoImage = clone.find('.memo_image');
		for(let i=0; i<images.length; i++) {
			memoImage.append('<div class="upload" style="background-image: url(\'/images/' + images[i] +'">');
		}
		
		
		//이벤트
		//색상표 출력
		let biPalette = clone[0].querySelector('.bi-palette');
		biPalette.addEventListener('click', function(e){openPalette(e, biPalette);	});
		
		//드래그
		clone[0].addEventListener("dragstart", () => {
	    	clone[0].classList.add("dragging");
	    });
		
		clone[0].addEventListener("dragend", () => {
	    clone[0].classList.remove("dragging");
	    let memoId = clone[0].querySelector('.inputMemoId').dataset.memo;
	    if($(clone[0]).parent('.dragcontainer')[0].classList.contains('importmemoStart')){
	    	bookmark = 'Y';
	    	console.log($(clone[0]).find('.bi-pin')[0].src)
	    	$(clone[0]).find('.bi-pin')[0].src=`/user/images/memo/pin-fill.svg`;
	    }else{
	    	bookmark = 'N';
	    	$(clone[0]).find('.bi-pin')[0].src=`/user/images/memo/pin.svg`;
	    }
	    $(clone[0]).find('.bi-pin')[0].classList.toggle('bi-pin-fill');
	    $.ajax({    
			type:"POST",
			url : '/member/memoUpdate',  //이동할 jsp 파일 주소
			data : {memoId, bookmark},
			dataType:'text',
			success : function(result) { // 결과 성공 콜백함수   
			console.log("성공")
			},    
			error : function(request, status, error) { // 결과 에러 콜백함수        
				console.log(error)    
			}})
		});
		
		
		//핀 색상 변경
		let biPin =  clone[0].querySelector('.bi-pin');
		biPin.addEventListener('click', function(e){pinChange(e);	});
		
		//메모 바탕색 변경
		let memoColor = clone[0].querySelectorAll('.changeColor input[type=radio]');
		for(let i=0; i<memoColor.length; i++){
			memoColor[i].addEventListener('click', function(e){changeMemoColor(e);	});
		}
		
		//첨부파일
		let fileBtn =  clone[0].querySelector('.file_btn');
		let realFile = clone[0].querySelector('.real_file');
		fileBtn.addEventListener('click', () => realFile.click())
		realFile.addEventListener('change', (e) => imgFileSelectHandler(e));
	}
	
    //메모 수정
	$('#writedMemo').on('hidden.bs.modal', function (event) {
		let content = document.querySelector('#writedMemo').querySelector('[name=content]').value;
   		let plustag = document.querySelector('#writedMemo').querySelector('[name=plustag]').value;
   		let tag = document.querySelectorAll('#writedMemo .tagify__tag-text');
   		let color = document.querySelector('#writedMemo').querySelector('.modal-body').style.backgroundColor;
   		let bookmark = document.querySelector('#writedMemo').querySelector('.modal-bi-pin').src;
   		if(bookmark.indexOf('pin.svg') == -1){
	   			bookmark = 'Y';
   		}else{
   			bookmark = 'N';
   		}
   		let hashTag = '';
   		 for(let i=0; i<tag.length; i++) {
   			hashTag += tag[i].innerText;
   			if(i<tag.length-1) {
   				hashTag += ', '
   			}
   		}
   		//ajax 요청
      	$.ajax({
         type:"POST",
         url : '/member/memoUpdate',  //이동할 jsp 파일 주소
         data : {memoId, color, content, hashTag, bookmark},
         dataType:'text',
         success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
              //function(data)를 쓰게 되면 전달받은 데이터가 data안에 담아서 들어오게 된다.
              /*hashTag = hashTag.replace(" ", "").split(",");
              
              let inputMemoId = $('[data-memo="' + memoId + '"]');
              let tags = $(inputMemoId).find('tags');
              let hashtag = $(inputMemoId).find('input.hashtag');
              
              let tagval = '';
              $(tags).remove('tag');
              for(let i=0; i<hashTag.length; i++){
                 tagval += hashTag[i];
                 tagval += ', ';
              }
              
              let result = tagval.replace(/,\s*$/, ""); // 마지막 콤마제거한 최종 태그 결과
              $(hashtag).val(result); // input.hashtag의 value 수정
              
              let memoContent = $(inputMemoId).find('.memotext');
              $(memoContent).text(content);
              
              let memo = $(inputMemoId).parent('.memo');
              
              $(memo).css('background-color', color);
              
              $()
              
              let biPin = $(inputMemoId).siblings('.bi-pin');
              if(bookmark == 'Y' && beforememopin != $('#writedMemo').find('.modal-bi-pin')[0].src){
              	console.log("상세페이지 닫을 때 올라갑니다.")
              	$(biPin).prop('src', '/user/images/memo/pin-fill.svg')	
                $('.importmemoStart').append(memo);
              }else if(bookmark == 'N' && beforememopin != $('#writedMemo').find('.modal-bi-pin')[0].src){
                console.log("상세페이지 닫을 때 내려갑니다.")
              	$(biPin).prop('src', '/user/images/memo/pin.svg')	
                $('.normalmemoStart').prepend(memo);
              }*/
              location.reload();
         },
         error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
            console.log('수정실패');
         }
      })   
	});
	
	//새로고침 시 순서 저장
	window.onbeforeunload = function (event) {
		let memo = document.querySelectorAll('.memo');
		for(let i=2; i<memo.length; i++){
			let seqNo = i;
			let memoId = memo[i].querySelector('.inputMemoId').dataset.memo;
			$.ajax({    
		    type:"POST",
		    url : '/member/memoUpdate',  //이동할 jsp 파일 주소
		    data : {memoId, seqNo},
		    dataType:'text',
		    success : function(result) { // 결과 성공 콜백함수        
		       console.log(result);    
		    },    
		    error : function(request, status, error) { // 결과 에러 콜백함수        
		       console.log(error);    
		    }})
		}	
	}

	
	/* 파일업로드 추가 */
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
	  	let memoImage = $(e.target).closest('.memo').find('.memo_image');
	  	console.log(files);
	  	
	  	if($(e.target).hasClass("modal_file") === true) {
	  		console.log('test');
	  		memo = $(e.target).closest('.modal');
	  		memoImage = $(e.target).closest('.modal').find('.memo_image');
	  	}
	  	
		// 파일타입 검사 및 미리보기 생성
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
	        	console.log('태그생성');
	        	uploadFiles.push(file);
				const reader = new FileReader();
				reader.onload = (e) => {
					let tag = $('<div class="upload" style="background-image: url(\'' + e.target.result +'">');
					console.log(file);
					let delBtn = $('<img>').attr('src', '/user/images/trade/delete-button.png').addClass('delBtn position_absol test2');
					delBtn[0].setAttribute('data-file', file.name);
					delBtn[0].addEventListener('click', delFile);
					$(tag).append(delBtn);
		            $(memoImage).append(tag);
				};
	          	reader.readAsDataURL(file);
	        }
		});
	  	
	  	if($(e.target).hasClass("insert_file") !== true) {
		  	uploadImg(e);
	  	}
	}
	
	//파일 업로드
	function uploadImg(e) {
		if($(e.target).hasClass("modal_file") !== true) {
			memoId = $(e.target).closest('.memo').find('.inputMemoId').data('memo');
	  	}
		
		console.log(memoId);
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
	
	//파일 삭제
	function delFile(e) {
	    let fileName = e.target.getAttribute('data-file');
	
	    let newFilesArr = uploadFiles.filter((file, idx) => {
	      return file.name != fileName;
	    })
	
	    uploadFiles = newFilesArr;
	    console.log('야호!');
	    console.log(uploadFiles);
	
	    e.target.parentNode.remove();
	  }
	  
	//db 삭제
	function delFileDie(e) {
		let saveFile =  e.target.parentElement.style.backgroundImage.split('"')[1];
		saveFile = saveFile.substr(8);
		$.ajax({
			url : '/member/attachmentDelete',  //이동할 jsp 파일 주소
			data : {saveFile, postId: memoId, menuType: 'AA7'},
			success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
				console.log("성공");
				e.target.parentElement.remove();
			},
			error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
				console.log("실패");
			}
		})
	}