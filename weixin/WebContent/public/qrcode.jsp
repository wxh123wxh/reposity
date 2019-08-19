<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>二维码</title>
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
window.onload=function(){
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post", 
		data:{"code":getParam("code")},
		url:"/weixin/servlet/getOpenId.action", 
		success:function(data){ 
			if(data.openId==""){
				alert("生成二维码失败，请重新生成")
			}else{
				$("#img").attr("src", "../image/"+data.openId+".jpg");
			}
		 }  
	});
}
$(function() {
	pushHistory();
	window.addEventListener('popstate', function(e) {
	    //自定义操作
		setTimeout(function() {
			//这个可以关闭安卓系统的手机
			document.addEventListener("WeixinJSBridgeReady",function() {
				  WeixinJSBridge.call("closeWindow");
				  },false);
			//这个可以关闭ios系统的手机
			WeixinJSBridge.call("closeWindow");
		}, 100);
	}, false);
})
</script>
</head>
<body>
	<center>
		<img id="img" style="height: 50%; width: 80%">
	</center>
</body>
</html>