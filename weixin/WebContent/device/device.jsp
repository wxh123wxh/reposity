<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width, initial-scale=1 user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主设备管理</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/weui.min.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript">
/**
 * 获取请求路径中的参数
 * name 要获取的参数名
 * document.location.search  返回嫂索的部分即？和后面的部分
 */
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

var start = 0; //查询的开始位置
/**
 * 页面加载前获取设备列表
 */
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
		url:"/weixin/servlet/findAllDevice.action", 
		success:function(data){ 
			if(data.openId!=null){
				$("#but").val(data.openId);
			}else{
				$.each(data,function(){
					if(this.online==0){
						$("#body").append("<div id=\"device\"><div id=\"device_info\" ><div><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_change\"><img src=\"../image/edit.png\" onclick=\"updateDevice('"+this.id+"','"+this.openId+"','"+this.name+"')\"></div></div><div id=\"device_operate\"><p><img src=\"../image/offline.png\"><button id=\"but1\">掉线</button></p><p onclick=\"getRZ('"+this.id+"','"+this.openId+"')\"><img src=\"../image/log.png\"><button id=\"but2\">日志</button></p><p onclick=\"findSubDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">设备</button></p><p onclick=\"deleteDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/delete.png\"><button id=\"but4\">删除</button></p></div></div>");
					}else{
						$("#body").append("<div id=\"device\"><div id=\"device_info\" ><div><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_change\"><img src=\"../image/edit.png\" onclick=\"updateDevice('"+this.id+"','"+this.openId+"','"+this.name+"')\"></div></div><div id=\"device_operate\"><p><img src=\"../image/online.png\"><button id=\"but1\">在线</button></p><p onclick=\"getRZ('"+this.id+"','"+this.openId+"')\"><img src=\"../image/log.png\"><button id=\"but2\">日志</button></p><p onclick=\"findSubDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">设备</button></p><p onclick=\"deleteDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/delete.png\"><button id=\"but4\">删除</button></p></div></div>");
					}
					$("#but").val(this.openId);
				})
			}
			//把当前页面历史记录的某一条用指定路径代替
			history.replaceState(null, "title", "/weixin/device/device.jsp?openId="+$("#but").val()+"&number="+Math.random());
			var open = $("#but").val();
			$("#body").append("<div id=\"addDevice\" onclick=\"addDevice('"+open+"')\"><p><img src=\"../image/add.png\"><button>添加主设备</button></p></div>");
		} 
	});
}
$(function() {
	pushHistory();//在当前页面历史记录前加一条历史记录
	window.addEventListener('popstate', function(e) {
	    //自定义操作 在离开页面的时候删除open
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


/**
 * 页面下拉时查询更多的设备记录
 */
$(function(){
	$(window).scroll(function() {
   		var scrollTop = $(this).scrollTop(),scrollHeight = $(document).height(),windowHeight = $(this).height();
    	var positionValue = (scrollTop + windowHeight) - scrollHeight;
   		if (positionValue == 0) {
        	//do something
        	start = start + 1;
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
   				url:"/weixin/servlet/findAllDevice.action", 
   				success:function(data){ 
   					if(data.openId!=null){
   					}else{
   						$.each(data,function(){
   							if(this.online==0){
   								$("#body").append("<div id=\"device\"><div id=\"device_info\" ><div><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_change\"><img src=\"../image/edit.png\" onclick=\"updateDevice('"+this.id+"','"+this.openId+"','"+this.name+"')\"></div></div><div id=\"device_operate\"><p><img src=\"../image/offline.png\"><button id=\"but1\">掉线</button></p><p onclick=\"getRZ('"+this.id+"','"+this.openId+"')\"><img src=\"../image/log.png\"><button id=\"but2\">日志</button></p><p onclick=\"findSubDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">设备</button></p><p onclick=\"deleteDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/delete.png\"><button id=\"but4\">删除</button></p></div></div>");
   							}else{
   								$("#body").append("<div id=\"device\"><div id=\"device_info\" ><div><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_change\"><img src=\"../image/edit.png\" onclick=\"updateDevice('"+this.id+"','"+this.openId+"','"+this.name+"')\"></div></div><div id=\"device_operate\"><p><img src=\"../image/online.png\"><button id=\"but1\">在线</button></p><p onclick=\"getRZ('"+this.id+"','"+this.openId+"')\"><img src=\"../image/log.png\"><button id=\"but2\">日志</button></p><p onclick=\"findSubDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">设备</button></p><p onclick=\"deleteDevice('"+this.id+"','"+this.openId+"')\"><img src=\"../image/delete.png\"><button id=\"but4\">删除</button></p></div></div>");
   							}
   						})
   					}
   				 } 
   			});
  		 }
	});
});
function getRZ(id,openId){
	var url = "/weixin/device/deviceLog.jsp?id="+id+"&openId="+openId+"&sub_id="+-1;
	window.location=url;
}
function updateDevice(id,openId,name){
	var url = "/weixin/device/updateDevice.jsp?id="+id+"&openId="+openId+"&name="+name;
	window.location=url;
}
function addDevice(openId){
	var url = "/weixin/device/addDevice.jsp?openId="+openId;
	window.location=url;
}
function findSubDevice(id,openId){
	var url = "/weixin/device/SubDevice.jsp?id="+id+"&openId="+openId;
	window.location=url;
}
var flag = true;
function deleteDevice(id,openId){
	var bln = confirm("确定删除此主设备吗?");   
	if(bln&&flag){
		flag = false;
		$.ajax({  
			dataType:"json",    //数据类型为json格式
			type:"post", 
			data:{"id":id,"openId":openId},
			url:"/weixin/servlet/deleteDevice.action",
			success:function(data){ 
				if(data.date!=null&&data.date!=""){
					alert(data.date)
				}else{
					window.location.href = data.url;
				}
			},
			complete:function(){
				flag = true;
			}
		});
	}
}
</script>
</head>
<body id="body">
	<input type="hidden" id="but">
</body>
</html>