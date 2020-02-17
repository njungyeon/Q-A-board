String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};

$(".answer-write input[type=submit]").click(addAnswer);

function addAnswer(e){
	e.preventDefault(); //이 설정을 통해서 기존에는 서버로 요청하는 것을 막고 있다. 
	console.log("click me");
	
	var queryString = $(".answer-write").serialize(); //이것을 하면 전송될 데이터를 출력해볼 수 있다.
	//console.log("query : ", queryString);
	
	var url = $(".answer-write").attr("action");
	console.log("url :", url);
	$.ajax({
		type: 'post',
		url: url,
		data : queryString,
		error : onError,
		success : onSuccess});
}

function onError(){
	
}

function onSuccess(data, status){
	console.log(data);
	var answerTemplate = $("#answerTemplate").html();
	var template = answerTemplate.format(data.writer.userId, data.createDate, data.contents, data.question.id, data.id);
	$(".qna-comment-slipp-articles").prepend(template);
	
	$(".answer-write textarea").val("");
	//$("textarea[name=contents]").val(""); 와 같다
}

$(".link-delete-article").click(deleteAnswer);

//$(document).on('click', '.link-delete-article', deleteAnswer);   
//이걸 안하면 화면이 렌더링 되고 보여지기 전에 서버에서 전달된 데이터만 보이게 된다
//이벤트가 제대로 먹기 전에 페이지가 먼저 보여줘서 그런건가 싶다
	
function deleteAnswer(e){
	e.preventDefault();
	
	var deleteBtn = $(this);
	var url = deleteBtn.attr("href");
	console.log(url);
	
	$.ajax({
		type : 'get',
		url : url,
		dataType : 'json',
		error : function(xhr, status){
			console.log("error")
		},
		success : function(data, status){
			console.log(data);
			if(data.valid){
				deleteBtn.closest("article").remove();
			}else{
				alert(data.errorMessage);
			}
		}
	})
}