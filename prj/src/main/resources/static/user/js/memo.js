/**
 * memo.js
 */
 
  //각 요소와 컨테이너에 이벤트 리스너 달기
  const draggables = document.querySelectorAll(".draggable");
  const containers = document.querySelectorAll(".container");

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
