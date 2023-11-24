/**
 * memo.js
 */
 
  //각 요소와 컨테이너에 이벤트 리스너 달기
  const draggables = document.querySelectorAll(".draggable");
  const containers = document.querySelectorAll(".memo-container");

  draggables.forEach(draggable => {
    draggable.addEventListener("dragstart", () => {
      draggable.classList.add("dragging");
    });

    draggable.addEventListener("dragend", () => {
      draggable.classList.remove("dragging");
    });
  });

  containers.forEach(container => {
    container.addEventListener("dragover", e => {
      e.preventDefault();
      const draggable = document.querySelector(".dragging");
	      container.appendChild(draggable);      
    });
  });
  
  //다른 요소 사이에 넣기
  function getDragAfterElement(container, x) {
  const draggableElements = [
    ...container.querySelectorAll(".draggable:not(.dragging)"),
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
  
  containers.forEach(container => {
	  container.addEventListener("dragover", e => {
	    e.preventDefault();
	    const afterElement = getDragAfterElement(container, e.clientX);
	    const draggable = document.querySelector(".dragging");
	    if (afterElement === undefined) {
	      container.appendChild(draggable);
	    } else {
	      container.insertBefore(draggable, afterElement);
	    }
	  });
	});
	
	//핀 클릭시 색상 변경
	let pins = document.querySelectorAll('.bi-pin');
	
	for(let i=0; i<pins.length; i++){
		pins[i].addEventListener('click', function(e){
			if(!e.target.classList.contains('bi-pin-fill')){
				e.target.src=`/user/images/pin-fill.svg`;
			}else{
				e.target.src=`/user/images/pin.svg`;
			}
			e.target.classList.toggle('bi-pin-fill');
		});
	}

	//팔레트 클릭시 색상표 출력
	let pal = document.querySelectorAll('.bi-palette');
	//console.log(pal[0].nextElementSibling);
	for(let i=0; i<pal.length; i++){
		pal[i].addEventListener('click', function(e){
			if(pal[i].nextElementSibling.style.display == 'block'){
				pal[i].nextElementSibling.style.display = 'none';
				let selMenubars = e.currentTarget.parentElement.querySelectorAll('.menubars');
				for(let e=0; e<selMenubars.length; e++){
					//selMenubars[e].style.display = 'none';
					selMenubars[e].classList.toggle('menubars_active');
				}
			}else{
				pal[i].nextElementSibling.style.display = 'block';
				let selMenubars = e.currentTarget.parentElement.querySelectorAll('.menubars');
				for(let e=0; e<selMenubars.length; e++){
					//selMenubars[e].style.display = 'block';
					selMenubars[e].classList.toggle('menubars_active');
				}
			}
		});
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
    let colors = document.querySelectorAll('input[type=radio]');
    for(let i=0; i<colors.length; i++){
	    colors[i].addEventListener('click', function(e){
	    	let memoColor = e.currentTarget.parentElement.parentElement.parentElement;
	    	memoColor.style.backgroundColor = e.currentTarget.value;
	    })
    }
    
     //전체 삭제 버튼 클릭
    let deleteAllBtn = document.querySelector('.deleteAll');
    deleteAllBtn.addEventListener('click', function(e){
    	let checks = document.querySelectorAll('input[type=checkbox]');
    	for(let i=0; i<checks.length; i++){
    		checks[i].checked = true;
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
		            //function(data)를 쓰게 되면 전달받은 데이터가 data안에 담아서 들어오게 된다. 
					//console.log(JSON.stringify(data));   
					console.log("성공");
				},
				error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
					alert('실패');
				}
			})
	    		deleteMemo[i].parentElement.parentElement.remove();
	    	}
    	}
    })
    
   //hashtag
   let inputhash = document.querySelectorAll('input[name=plustag]')
   for(let i=0; i<inputhash.length; i++){
       let tagify = new Tagify(inputhash[i]); // initialize Tagify
       // 태그가 추가되면 이벤트 발생
       tagify.on('add', function() {
         //console.log(tagify.value); // 입력된 태그 정보 객체
       })
    }
    
    //메모 아이디 가져와서 모달창에 뿌리기
    $(function(){
        $('#writedMemo').on('show.bs.modal', function(event) {          
            memoId = $(event.relatedTarget).data('memo');
            //Ajax 요청
            $.ajax({
				url : '/member/memoInfo',  //이동할 jsp 파일 주소
				data : {memoId},
				success: function(data){   //데이터 주고받기 성공했을 경우 실행할 결과
		            //function(data)를 쓰게 되면 전달받은 데이터가 data안에 담아서 들어오게 된다. 
					//console.log(JSON.stringify(data));   
					$('#writedMemo').find('[name="content"]')[0].value = data.content;
					$('#writedMemo').find('[name=plustag]')[0].value = data.hashTag;
					$('#writedMemo').find('.modal-body')[0].style.backgroundColor = data.color;
					
					if(data.bookmark == 'Y'){
						$('#writedMemo').find('.bi-pin')[0].src = '/user/images/pin-fill.svg';
					}else{
						$('#writedMemo').find('.bi-pin')[0].src = '/user/images/pin.svg';
					}
				},
				error:function(){   //데이터 주고받기가 실패했을 경우 실행할 결과
					alert('실패');
				}
			})
		})
    });
    
