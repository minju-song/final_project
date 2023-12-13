let labelArr = [];
let pctData = [];
let backgroundColorArr = [];
let borderColorArr = [];
for (let i = 0; i < histories.length; i++) {
    console.log(histories[i]);
    if (dateFormat(histories[i].startDate) == dateFormat(histories[i].endDate)) {
        labelArr[i] = dateFormat(histories[i].startDate);
    }
    else {
        labelArr[i] = dateFormat(histories[i].startDate) + ' ~ ' + dateFormat(histories[i].endDate);
    }
    pctData[i] = histories[i].successPct;
    if (histories[i].successState == 'SU1') {
        backgroundColorArr[i] = 'rgba(54, 162, 235, 0.2)';
        borderColorArr[i] = 'rgba(54, 162, 235, 1)';
    }
    else {
        backgroundColorArr[i] = 'rgba(255, 99, 132, 0.2)';
        borderColorArr[i] = 'rgba(255, 99, 132, 1)';
    }
}

console.log(labelArr);

$(document).ready(function () {
    var ctx = document.getElementById('myChart');
    var myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labelArr,
            datasets: [{
                label: '성공률',
                data: pctData,
                backgroundColor: backgroundColorArr,
                borderColor: borderColorArr,
                borderWidth: 1
            }]
        },
        options: {
            responsive: false,
            scales: {
				yAxes: [{
					ticks: {
						min: 0,
						max: 100,
						fontSize : 14,
					}
				}]
            },
        }
    });

})


function dateFormat(str) {
    let date = new Date(str);
    let newDate = date.getFullYear() + '-' + ((date.getMonth() + 1) <= 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) +
        '-' + ((date.getDate()) <= 9 ? "0" + (date.getDate()) : (date.getDate()));
    return newDate;
}/**
 * 
 */