/**
 * 
 */

console.log("question.js")

$(answerDeleteBtn).on('click', function(e){
console.log(e)
	e.preventDefault();
	let answerId = $(this).attr("href");
	console.log(answerId)
});