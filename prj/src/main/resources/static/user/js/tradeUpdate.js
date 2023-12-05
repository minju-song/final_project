/**
 * tradeUpdate
 */

//장소, 위도, 경도 저장
let tradePlace = `[[${tradeInfo.tradePlace}]]`;
let latitude = `[[${tradeInfo.latitude}]]`;
let longitude = `[[${tradeInfo.longitude}]]`;
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
if(`[[${tradeInfo.tradeType}]]` == '직거래'){
  $('#placeSelectButton').attr("disabled", false);
  $('#placeSelectButton').css("background-color", "#09203f");
}

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