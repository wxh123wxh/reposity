<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width, initial-scale=1 user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>错误页面</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/weui.min.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript">
var getParam = function(name){  
	var search = decodeURI(document.location.search);
    var matcher = search.substr(1).split("&");
    var items = null;
    if(null != matcher){  
            for(var i in matcher){
            	var a = matcher[i].substr(0,matcher[i].indexOf("="));
            	if(name===a){
            		items = matcher[i].substr(matcher[i].indexOf("=")+1);
            		return items;
            	}
            }
    }
    return items;  
};  
$(function() {
	pushHistory();
	window.addEventListener('popstate', function(e) {
	    //自定义操作
		setTimeout(function() {
			 //这个可以关闭安卓系统的手机
			 document.addEventListener(
			      "WeixinJSBridgeReady",
			      function() {
			         WeixinJSBridge.call("closeWindow");
			      },
			     false
			 );
			    //这个可以关闭ios系统的手机
			   WeixinJSBridge.call("closeWindow");
		}, 100);
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
window.onload=function(){
	var text = getParam("message")
	$("#text").html(text);
}
</script>
</head>
<body>
	<h1>
		<font color="red">${message}</font>
	</h1>
	<br>
	<h1>
		<font color="red" id="text"></font>
	</h1>
	<br>
</body>
</html>