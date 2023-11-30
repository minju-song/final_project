/**
 * 
 */

//url에서 클럽아이디 가져오기
let urlParams = new URLSearchParams(window.location.search);
let clubId = urlParams.get('clubId');

//클럽탈퇴버튼
let outClubBtn = document.getElementById('outClub');
outClubBtn.addEventListener("click", outClub);

function outClub(e) {
    console.log(userId + '가 '+clubId+'탈퇴원함');
    if(leader == true) {
        alert("먼저 모임장을 위임해주세요.");
        return;
    }

    fetch('/member/outClub?clubId='+clubId)
    .then(resolve => resolve.text())
    .then(result => {
        console.log(result);
        location.reload();
    });
}