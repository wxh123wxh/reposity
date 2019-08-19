<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报警记录</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/weui.min.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
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
var flag = true;
var start = 0;
window.onload=function(){
	var code = getParam("code");
	var str = null;
	var openId = $("#but").val();
	
	if (code!=null&&code!="") {
		str = {"code":code,"start":start};
	}else if(openId!=null&&openId!=""){
		str = {"openId":openId,"start":start};
	}else{
		str = {"openId":getParam("openId"),"start":start};
	}
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post", 
		data:str,
		url:"/weixin/servlet/armingLog.action", 
		success:function(data){ 
			if(data.openId!=null){
				$("#but").val(data.openId);
			}else {
				$.each(data,function(){
					$("#body").append("<div id='devicelog'><div id='log_time'><p class='huise'>"+this.time+"</p></div><div id='log_content'><p class='white'>"+this.content+"</p></div></div>");
					$("#but").val(this.openId);
				})
			}
		 }  
	});
}
$(function(){
	$(window).scroll(function() {
   		var scrollTop = $(this).scrollTop(),scrollHeight = $(document).height(),windowHeight = $(this).height();
    	var positionValue = (scrollTop + windowHeight) - scrollHeight;
   		if (positionValue == 0) {
        	//do something
        	start = start + 1;
        	var str = null;
        	var openId = $("#but").val();
        	
        	if(openId!=null&&openId!=""){
        		str = {"openId":openId,"start":start};
        	}else{
        		str = {"openId":getParam("openId"),"start":start};
        	}
        	$.ajax({  
        		dataType:"json",    //数据类型为json格式
        		type:"post", 
        		data:str,
        		url:"/weixin/servlet/armingLog.action", 
        		success:function(data){ 
        			if(data.openId!=null){
        			}else {
        				$.each(data,function(){
        					$("#body").append("<div id='devicelog'><div id='log_time'><p class='huise'>"+this.time+"</p></div><div id='log_content'><p class='white'>"+this.content+"</p></div></div>");
        				})
        			}
        		 }  
        	});
  		 }
	});
});

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
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}

</script>
</head>
<body id="body">
	<input type="hidden" id="but">
</body>
</html>