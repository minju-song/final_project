// 작업 중

function showPage(replyCnt) {

	var endNum = Math.ceil(pageNum / pageSize) * pageSize;
	var startNum = endNum > pageSize ? endNum - pageSize : 1;

	var prev = startNum != 1;
	var next = false;

	if (endNum * 10 >= replyCnt) {
		endNum = Math.ceil(replyCnt / pageUnit);
	}

	if (endNum * 10 < replyCnt) {
		next = true;
	}

	var str = "";

	if (prev) {
		str += "<li class='page-item'><a class='page-link' href='" + (startNum - 1) + "'>Previous</a></li>";
	}

	for (var i = startNum; i <= endNum; i++) {
		var active = pageNum == i ? "active" : "";
		str += "<li class='page-item " + active + "'> <a class='page-link' href='" + i + "'>" + i + "</a></li>";
	}

	if (next) {
		str += "<li class='page-item'><a class='page-link' href='" + (endNum + 1) + "'>Next</a><li>";
	}

	str += "";


	$('.pagination').html(str);
}

export default showPage;