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
	const pins = document.querySelectorAll('.bi-pin');
	
	for(let i=0; i<pins.length; i++){
		pins[i].addEventListener('click', function(e){
			if(pins[i].src == 'http://localhost:8090/user/images/pin.svg' ){
				pins[i].src=`/user/images/pin-fill.svg`;		
			}else{
				pins[i].src=`/user/images/pin.svg`;
			}
		});
	}

	//팔레트 클릭시 색상표 출력
	const pal = document.querySelectorAll('.bi-palette');
	//console.log(pal[0].nextElementSibling);
	for(let i=0; i<pal.length; i++){
		pal[i].addEventListener('click', function(e){
			if(pal[i].nextElementSibling.style.display == 'none'){
				pal[i].nextElementSibling.style.display = 'block';				
			}else{
				pal[i].nextElementSibling.style.display = 'none';	
			}
		});
	}
	
	// 첨부파일
	// .addEventListener('click', () => realUpload.click());
	const fileBtn = document.querySelectorAll('.file_btn');
	const realFile = document.querySelectorAll('.real_file');
	for(let i=0; i<fileBtn.length; i++){
		fileBtn[i].addEventListener('click', () => realFile[i].click());
	}
	