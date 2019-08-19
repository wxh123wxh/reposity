<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport"
	content="width=device-width, initial-scale=1 user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主设备状态</title>
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
var start = 0;
window.onload=function(){
	var code = getParam("code");
	var str = null;
	
	if (code!=null&&code!="") {
		str = {"code":code,"start":start};
	}else{
		str = {"openId":getParam("openId"),"start":start};
	}
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post",
		data:str,
		url:"/weixin/servlet/findAllDevice.action", 
		success:function(data){ 
			if(data.openId!=null){
				$("#but").val(data.openId);
			}else{
				$.each(data,function(){
					if(this.online==0){
						$("#body").append("<div id=\"device_state\"><div id=\"device_info\"><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_operate\"><p class=\"red\"><img src=\"../image/offline.png\"><button class=\"height_blue\">掉线</button></p><p class=\"blue\" onclick=\"findSubDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/sub_blue.png\"><button class=\"sblue\">子设备</button></p></div></div>");
					}else{
						$("#body").append("<div id=\"device_state\"><div id=\"device_info\"><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_operate\"><p class=\"red\"><img src=\"../image/online.png\"><button class=\"height_blue\">在线</button></p><p class=\"blue\" onclick=\"findSubDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/sub_blue.png\"><button class=\"sblue\">子设备</button></p></div></div>");
					}
					$("#but").val(this.openId);
				})
				history.replaceState(null, "title", "/weixin/log/device_state.jsp?openId="+$("#but").val()+"&number="+Math.random());
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
        	var str = {"openId":getParam("openId"),"start":start};
        	$.ajax({  
        		dataType:"json",    //数据类型为json格式
        		type:"post",
        		data:str,
        		url:"/weixin/servlet/findAllDevice.action", 
        		success:function(data){ 
        			if(data.openId!=null){
        			}else{
        				$.each(data,function(){
        					if(this.online==0){
        						$("#body").append("<div id=\"device_state\"><div id=\"device_info\"><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_operate\"><p class=\"red\"><img src=\"../image/offline.png\"><button class=\"height_blue\">掉线</button></p><p class=\"blue\" onclick=\"findSubDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/sub_blue.png\"><button class=\"sblue\">子设备</button></p></div></div>");
        					}else{
        						$("#body").append("<div id=\"device_state\"><div id=\"device_info\"><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_operate\"><p class=\"red\"><img src=\"../image/online.png\"><button class=\"height_blue\">在线</button></p><p class=\"blue\" onclick=\"findSubDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/sub_blue.png\"><button class=\"sblue\">子设备</button></p></div></div>");
        					}
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
function findSubDevice(id,openId){
	var url = "/weixin/log/sub_state.jsp?id="+id+"&openId="+openId;
	window.location=url;
}
</script>
</head>
<body>
	<div id="body">
		<input type="hidden" id="but">
	</div>
</body>

</html>