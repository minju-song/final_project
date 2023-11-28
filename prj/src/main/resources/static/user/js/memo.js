/**
 * memo.js
 */
 
 	//팔레트 클릭시 색상표 출력 함수 
	function openPalette(e, pal){
		if(pal.nextElementSibling.style.display == 'block'){
			pal.nextElementSibling.style.display = 'none';
			let selMenubars = e.currentTarget.parentElement.querySelectorAll('.menubars');
			for(let i=0; i<selMenubars.length; i++){
				selMenubars[i].classList.toggle('menubars_active');
			}
		}else{
			pal.nextElementSibling.style.display = 'block';
			let selMenubars = e.currentTarget.parentElement.querySelectorAll('.menubars');
			for(let i=0; i<selMenubars.length; i++){
				selMenubars[i].classList.toggle('menubars_active');
			}
		}
	}
	
	//핀 색상 변경 함수
	function pinChange(e){
		let memoId = e.currentTarget.parentElement.querySelector('.inputMemoId').dataset.memo;
		if(!e.target.classList.contains('bi-pin-fill')){
			e.target.src=`/user/images/pin-fill.svg`;
			bookmark = 'Y';
		}else{
			e.target.src=`/user/images/pin.svg`;
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
    	let memoId = e.currentTarget.parentElement.parentElement.parentElement.querySelector('.inputMemoId').dataset.memo;
    	let backcolor = e.currentTarget.parentElement.parentElement.parentElement;
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
 
 	//
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

	
	// 첨부파일
	// .addEventListener('click', () => realUpload.click());
	let fileBtn = document.querySelectorAll('.file_btn');
	let realFile = document.querySelectorAll('.real_file');
	for(let i=0; i<fileBtn.length; i++){
		fileBtn[i].addEventListener('click', () => realFile[i].click());
	}
	
	//hashtag 표시
    let input = document.querySelectorAll('input[name=tags]')
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
    
    //등록창에서 메뉴 변경
    $('#insertMemo').on('show.bs.modal', function(event) { 
    	let pins = $('#insertMemo').find('.modal-bi-pin')[0];
		pins.addEventListener('click', function(e){
			if(!e.target.classList.contains('bi-pin-fill')){
				e.target.src=`/user/images/pin-fill.svg`;
			}else{
				e.target.src=`/user/images/pin.svg`;
			}
			e.target.classList.toggle('bi-pin-fill');
		})
		let colors = document.querySelectorAll('[type=radio]');
    	for(let i=0; i<colors.length; i++){
	    	colors[i].addEventListener('click', function(e){
	    	let backcolor = e.currentTarget.parentElement.parentElement.parentElement;
	    	let color = e.currentTarget.value
	    	backcolor.style.backgroundColor = color;
	    	})
	    }
    });
    
    //메모 상세보기 변경 설정(핀, 색상)
	let writepins = $('#writedMemo').find('.modal-bi-pin')[0];
	writepins.addEventListener('click', function(e){
		if(!e.target.classList.contains('bi-pin-fill')){
			e.target.src=`/user/images/pin-fill.svg`;
		}else{
			e.target.src=`/user/images/pin.svg`;
		}
		e.target.classList.toggle('bi-pin-fill');
	})
	let writecolors = document.querySelectorAll('#writedMemo [type=radio]');
	for(let i=0; i<writecolors.length; i++){
    	writecolors[i].addEventListener('click', function(e){
    	let backcolor = e.currentTarget.parentElement.parentElement.parentElement;
    	let color = e.currentTarget.value
    	backcolor.style.backgroundColor = color;
    	})
    } 
	    
    //메모 상세보기
    let memoId = 0;
    let beforememopin = '';
    $('#writedMemo').on('show.bs.modal', function(event) { 
        beforememopin = $('#writedMemo').find('.modal-bi-pin')[0].src;
        memoId = $(event.relatedTarget).data('memo');
        //Ajax 요청
        $.ajax({
			url : '/member/memoInfo',  //이동할 jsp 파일 주소
			data : {memoId},
			success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
				$('#writedMemo').find('[name="content"]')[0].value = data.content;
				$('#writedMemo').find('[name=plustag]')[0].value = data.hashTag;
				$('#writedMemo').find('.modal-body')[0].style.backgroundColor = data.color;
				$('#writedMemo')[0].value = memoId;
				if(data.bookmark == 'Y'){
					$('#writedMemo').find('.modal-bi-pin')[0].src = '/user/images/pin-fill.svg';
				}else{
					$('#writedMemo').find('.modal-bi-pin')[0].src = '/user/images/pin.svg';
				}
			},
			error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
				console.log('실패');
			}
		})
	});
    
	//메모등록
    $('#insertMemo').on('hidden.bs.modal', function (event) {
  		let content = document.querySelector('#insertMemo').querySelector('[name=content]').value;
   		let plustag = document.querySelector('#insertMemo').querySelector('[name=plustag]').value;
   		let tag = document.querySelectorAll('#insertMemo .tagify__tag-text');
   		let color = document.querySelector('#insertMemo').querySelector('.modal-body').style.backgroundColor;
   		let bookmark = document.querySelector('#insertMemo').querySelector('.modal-bi-pin').src;
   		if(content != '' || plustag != ''){
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
				url : '/member/memoInsert',  //이동할 jsp 파일 주소
				data : {color, content, hashTag, bookmark},
				dataType:'text',
				success: function(memoId){   //데이터 주고받기 성공했을 경우 실행할 결과
			        //function(memoId)를 쓰게 되면 전달받은 데이터가 data안에 담아서 들어오게 된다.
					$('#insertMemo').find('[name="content"]')[0].value = '';
					$('#insertMemo').find('[name=plustag]')[0].value = '';
					$('#insertMemo').find('.modal-body')[0].style.backgroundColor = 'rgb(255, 242, 204)';
					$('#insertMemo').find('.modal-bi-pin')[0].src = '/user/images/pin.svg';
					//메모 이어붙이기
					addMemo(content, hashTag, color, bookmark, memoId)
				},
				error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
					console.log('등록실패');
				}
			})	
		}
		$('#insertMemo').find('.modal-body')[0].style.backgroundColor = 'rgb(255, 242, 204)';
		$('#insertMemo').find('.modal-bi-pin')[0].src = '/user/images/pin.svg';	
	});
	
	//메모 이어붙이기
	function addMemo(content, hashTag, color, bookmark, memoId){
		let clone = $('.btn-modal:eq(0)').clone();
		clone.css('display', 'block');
		clone.find('.inputMemoId')[0].dataset.memo = memoId;
		clone.find('[type=checkbox]')[0].value = memoId;
		clone.find('.memotext')[0].innerText = content;
		hashTag = hashTag.replace(" ", "").split(",");
		let tagify = clone.find('.tagify__tag-text');
		for(let i=0; i<hashTag.length; i++){
			if(tagify[i] != undefined){
				tagify[i].innerText = hashTag[i]
				clone.find('[name=tags]').prop('value', hashTag[i]);
				clone.find('[name=tags]').prop('defaultValue', hashTag[i]);
			}else if(tagify[i] === undefined){
				let clonetag = $('.tagify__tag:eq(0)').clone();
				clone.find('.tagify__input').remove();
				clonetag.find('.tagify__tag-text').text(hashTag[i]);
				clonetag.find('[name=tags]').prop('value', hashTag[i]);
				clonetag.find('[name=tags]').prop('defaultValue', hashTag[i]);
				clone.find('.tagify').append(clonetag);			
			}
		}
		if(color == ''){
			clone.find('.memo').prevObject[0].style.backgroundColor = 'rgb(255, 242, 204)';
		}else{
			clone.find('.memo').prevObject[0].style.backgroundColor = color;
		}
		if(bookmark == 'Y'){
			clone.find('.bi-pin')[0].src = '/user/images/pin-fill.svg';
			$('.importmemoStart').append(clone);
		}else{
			clone.find('.bi-pin')[0].src = '/user/images/pin.svg';
			$('.normalmemoStart').append(clone);
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
		});
		
		//핀 색상 변경
		let biPin =  clone[0].querySelector('.bi-pin');
		biPin.addEventListener('click', function(e){pinChange(e);	});
		
		//메모 바탕색 변경
		let memoColor = clone[0].querySelectorAll('.changeColor input[type=radio]');
		for(let i=0; i<memoColor.length; i++){
			memoColor[i].addEventListener('click', function(e){changeMemoColor(e);	});
		}
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
              hashTag = hashTag.replace(" ", "").split(",");
              
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
              console.log()
              let biPin = $(inputMemoId).siblings('.bi-pin');
              if(bookmark == 'Y' && beforememopin != $('#writedMemo').find('.modal-bi-pin')[0].src){
              	$(biPin).prop('src', '/user/images/pin-fill.svg')	
                $('.importmemoStart').append(memo);
              }else if(bookmark == 'N' && beforememopin != $('#writedMemo').find('.modal-bi-pin')[0].src){
              	$(biPin).prop('src', '/user/images/pin.svg')	
                $('.normalmemoStart').prepend(memo);
              }
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
