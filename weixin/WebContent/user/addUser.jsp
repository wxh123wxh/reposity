<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>为用户添加主设备</title>
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
var flag = true;
var start = 0;
window.onload=function(){
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post", 
		data:{"glid":getParam("glid"),"gzid":getParam("gzid"),"start":start},
		url:"/weixin/servlet/findAllDeviceForUser.action", 
		success:function(data){ 
			if(data.openId!=null){
				$("#but").val(data.openId);
			}else {
				$.each(data,function(){
		    		$("#body").append("<div id=\"device\"><div id=\"device_info\" ><div><p>"+this.id+"</p><p>名称："+this.name+"</p></div><div id=\"follower\" onclick=\"addFolowerForDevice('"+this.id+"')\"><img id=\""+this.id+"\" src=\"../image/focus.png\"><p class=\"red\">添加</p></div></div></div>");
				})
			}
			$("#body").append("<div id=\"addDevice\" onclick=\"addDevice()\"><p><img src=\"../image/add.png\"><button>确认添加</button></p></div>");
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
        	$.ajax({  
        		dataType:"json",    //数据类型为json格式
        		type:"post", 
        		data:{"glid":getParam("glid"),"gzid":getParam("gzid"),"start":start},
        		url:"/weixin/servlet/findAllDeviceForUser.action", 
        		success:function(data){ 
        			if(data.openId!=null){
        				$("#but").val(data.openId);
        			}else {
        				$.each(data,function(){
        					$("#body").append("<div id=\"device\"><div id=\"device_info\" ><div><p>"+this.id+"</p><p>名称："+this.name+"</p></div><div id=\"follower\" onclick=\"addFolowerForDevice('"+this.id+"')\"><img id=\""+this.id+"\" src=\"../image/focus.png\"><p class=\"red\">添加</p></div></div></div>");
        				})
        			}
        		 }  
        	});
   			
  		 }
	});
});
function addFolowerForDevice(id){
	if($("#"+id).attr("src")=="../image/focus.png"){
		$("#"+id).attr("src", "../image/followed.png");
		$("#"+id).parent().children("p:last-child").html("已添加")
	}else if($("#"+id).attr("src")=="../image/followed.png"){
		$("#"+id).attr("src", "../image/focus.png");
		$("#"+id).parent().children("p:last-child").html("添加")
	}
}

var arr = new Array()
function addDevice(){
	if(flag){
		var len = $("img").length;
		for(var i = 0; i < len; i++){
			if ($("img")[i].getAttribute("src")=="../image/followed.png") {
				arr.push($("img")[i].getAttribute("id"))
			} 
		}
		 
		flag = false
		var str = {"glid":getParam("glid"),"gzid":getParam("gzid"),"arr":arr}
		$.ajax({  
			dataType:"json",    //数据类型为json格式
			type:"post", 
			data:str,
			traditional :true,  //注意这个参数是必须的
			url:"/weixin/servlet/addFolowerForDevice.action",
			success:function(data){ 
				if(data.back!=null){
					alert(data.back);
					var back = getParam("back");
				    if(back!=null&&back!=""){
						window.location.href = "/weixin/user/device.jsp?openId="+getParam("gzid")+"&glid="+getParam("glid")+"&number="+Math.random();
				    }else{
				    	setTimeout(function() {
							//这个可以关闭安卓系统的手机
							document.addEventListener("WeixinJSBridgeReady",function() {
								  WeixinJSBridge.call("closeWindow");
								  },false);
							//这个可以关闭ios系统的手机
							WeixinJSBridge.call("closeWindow");
						}, 100);
				    }
				}
			} ,
			complete:function(){
				flag = true;
			} 
		});
	}
}
$(function() {
	pushHistory();
	window.addEventListener('popstate', function(e) {
	    //自定义操作
	    var back = getParam("back");
	    if(back!=null&&back!=""){
			window.location.href = "/weixin/user/device.jsp?openId="+getParam("gzid")+"&glid="+getParam("glid")+"&number="+Math.random();
	    }else{
	    	setTimeout(function() {
				//这个可以关闭安卓系统的手机
				document.addEventListener("WeixinJSBridgeReady",function() {
					  WeixinJSBridge.call("closeWindow");
					  },false);
				//这个可以关闭ios系统的手机
				WeixinJSBridge.call("closeWindow");
			}, 100);
	    }
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
	<div></div>
</body>
</html>