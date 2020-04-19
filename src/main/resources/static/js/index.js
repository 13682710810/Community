$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	// val() 方法返回或设置被选元素的值。
	// 获取标题和内容,val()如果未设置参数，则返回被选元素的当前值。
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	//发送异步请求(POST)
	$.post(
		CONTEXT_PATH+"/discuss/add",
		{"title":title,"content":content},
		//回调函数
		function(data) {
			data=$.parseJSON(data);
			//在提示框中显示返回信息
			$("#hintBody").text(data.msg);
			//显示提示框
			$("#hintModal").modal("show");
			//2秒后隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
				//刷新页面
				if(data.code==0){
					window.location.reload();
				}
			}, 2000);
		}
	);

}