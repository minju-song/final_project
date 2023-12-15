function toggleUpdateMode(id) {
    // index를 정수로 변환
    id = parseInt(id);

    let reviewContent = document.getElementById('reviewContent_' + id);
    let updateInput = document.getElementById('updateInput_' + id);

    console.log(reviewContent);

    if (updateInput.style.display == 'none') {
        // 수정 모드로 전환
        reviewContent.style.display = 'none';
        updateInput.style.display = 'inline';

        let divid = document.getElementById('stars_' + id);
        divid.innerHTML = `                    <div class="star_rating star_on">
            <span class="star on" value="1"> </span>
            <span class="star on" value="2"> </span>
            <span class="star on" value="3"> </span>
            <span class="star on" value="4"> </span>
            <span class="star on" value="5"> </span>
        </div>`;

        $('.star_on > .star').click(function () {
            $(this).parent().children('span').removeClass('on');
            $(this).addClass('on').prevAll('span').addClass('on');
            console.log($(this));
        })


    } else {
        // 읽기 모드로 전환
        reviewContent.style.display = 'inline';
        updateInput.style.display = 'none';
    }
}


$('.star_on > .star').click(function () {
    $(this).parent().children('span').removeClass('on');
    $(this).addClass('on').prevAll('span').addClass('on');
    console.log($(this));
})

function reviewWrite() {
    let writeContents = document.getElementById('writeContents').value;
    // console.log(clubId + '에 대한 리뷰 : ' + writeContents);
    let stars = document.querySelectorAll('.star_on > .on');
    let maxStar = 0;
    stars.forEach(function (star, idx) {
        // console.log(star.getAttribute("value"));
        if (maxStar < star.getAttribute("value")) {
            maxStar = star.getAttribute("value");
        }
    })

    // console.log('평점 : ' + maxStar);
    let data = { star: maxStar, clubId: clubId, contents: writeContents };
    $.ajax({
        type: 'POST',
        url: '/member/reviewinsert',
        data: data,
        success: function (result) {
            Swal.fire({
                title: "등록완료",
                text: "후기가 등록되었습니다.",
                icon: "success"
            }).then((resolve) => {
                console.log(result);
                $('#writeReview').hide();
                $('#completeReview').show();
                location.reload();
                // drawReview(result.review);

            });

        },
        error: function () {

        }
    })
}

function drawReview(data) {
    //리뷰입력 시 입력칸 바로 삭제
    let reviewlist = document.getElementById('reviewlist');

    let div = document.createElement('div');
    div.setAttribute("class", "rev");

    let h4 = document.createElement("h4");
    h4.innerText = '작성자 : ' + nickname;
    div.appendChild(h4);

    let div2 = document.createElement('div');
    div2.setAttribute("id", "stars_" + data.reviewId);
    div2.setAttribute("class", "star_rating");

    for (let i = 0; i < data.star; i++) {
        let span = document.createElement('span');
        span.classList.add('star', 'on');
        div2.appendChild(span);
    }
    div.appendChild(div2);

    let span = document.createElement('span');
    span.innerText = new Date();
    div.appendChild(span);

    let div3 = document.createElement('div');
    let h4_2 = document.createElement('h4');
    h4_2.setAttribute('id', 'reviewContent_' + data.reviewId);
    h4_2.style.display = 'inline';
    h4_2.innerText = data.contents;
    div3.appendChild(h4_2);

    let div4 = document.createElement('div');
    div4.setAttribute('id', 'updateInput_' + data.reviewId);
    div4.style.display = 'none';

    let input = document.createElement('input');
    input.setAttribute('type', 'text');
    input.setAttribute('value', data.contents);
    div4.appendChild(input);

    let button = document.createElement('button');
    button.classList.add('btn');
    button.innerText = '수정';
    button.setAttribute('onclick', "updateReview('" + data.reviewId + "', event)");
    div4.appendChild(button);

    div3.appendChild(div4);
    div.appendChild(div3);

    let button2 = document.createElement('button');
    button2.classList.add('btn');
    button2.innerText = '✔️';
    button2.setAttribute('onclick', "toggleUpdateMode('" + data.reviewId + "')");
    div.appendChild(button2);

    let button3 = document.createElement('button');
    button3.classList.add('btn');
    button3.innerText = '❌';
    button3.setAttribute('onclick', "deleteReview('" + data.reviewId + "')");
    div.appendChild(button3);

    reviewlist.appendChild(div);

    document.getElementById('writeContents').value = '';
}

function updateReview(id, event) {
    console.log('리뷰 아이디 : ' + id);

    let content = event.currentTarget.parentElement.children[0].value;
    console.log(document.getElementById('stars_' + id));

    let stars = document.querySelectorAll('.star_on > .on');
    let maxStar = 0;
    stars.forEach(function (star, idx) {
        // console.log(star.getAttribute("value"));
        if (maxStar < star.getAttribute("value")) {
            maxStar = star.getAttribute("value");
        }
    })

    //리뷰 내용업데이트
    console.log(content + '/ ' + maxStar);

    let data = { reviewId: id, star: maxStar, contents: content };
    $.ajax({
        type: 'POST',
        url: '/member/reviewUpdate',
        data: data,
        success: function (result) {
            Swal.fire({
                title: "수정완료",
                text: "후기가 수정되었습니다.",
                icon: "success"
            }).then((resolve) => {
                console.log(result);
                toggleUpdateMode(id);
                document.getElementById('reviewContent_' + id).innerText = content;

            });

        },
        error: function () {

        }
    })


}

function deleteReview(id) {
    console.log(id + '번 삭제')

    Swal.fire({
        title: "후기를 삭제하시겠습니까?",
        text: "즉시 삭제가 진행됩니다.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/member/reviewDelete?reviewId=' + id)
                .then(resolve => resolve.json())
                .then(result => {
                    console.log(result);
                    if (result.result == 'success') {
                        Swal.fire({
                            title: "삭제완료",
                            text: "삭제가 완료되었습니다.",
                            icon: "success"
                        }).then((resolve) => {
                            location.href = '/member/club/clubReview?clubId=' + clubId;

                        });
                    }
                    else {
                        Swal.fire({
                            title: "삭제실패",
                            text: "오류발생",
                            icon: "error"
                        }).then((resolve) => {
                            location.reload();

                        });
                    }
                });
        }
    })

}/**
 * 
 */