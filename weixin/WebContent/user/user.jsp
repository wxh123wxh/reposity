<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/weui.min.css"/>
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
	var code = getParam("code");
	var str = null;
	
	if (code!=null) {
		str = {"code":code,"start":start};
	}else{
		str = {"openId":getParam("openId"),"start":start};
	}
	
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post",
		data:str,
		url:"/weixin/servlet/findAllFollower.action", 
		success:function(data){ 
			if(data.openId!=null){
				$("#but").val(data.openId);
			}else{
				$.each(data,function(){
					if (this.headimgurl==""&&this.city=="") {
		    			$("#body").append("<div id=\"userinfo\"><div id=\"heard\"><img src=\"../image/head.png\"><P>"+this.nickname+"</P><P>未设置</P></div><div id=\"u_operate\"><div onclick=\"findFollowerDevice('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/device.png\"><button id=\"user_but1\">设备</button></div><div onclick=\"deleteOpen('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/delete.png\"><button id=\"user_but2\">删除</button></div></div></div>");
					}else if (!this.headimgurl==""&&this.city=="") {
						$("#body").append("<div id=\"userinfo\"><div id=\"heard\"><img src=\""+this.headimgurl+"\"><P>"+this.nickname+"</P><P>未设置</P></div><div id=\"u_operate\"><div onclick=\"findFollowerDevice('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/device.png\"><button id=\"user_but1\">设备</button></div><div onclick=\"deleteOpen('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/delete.png\"><button id=\"user_but2\">删除</button></div></div></div>");
					}else if (this.headimgurl==""&&!this.city=="") {
						$("#body").append("<div id=\"userinfo\"><div id=\"heard\"><img src=\"../image/head.png\"><P>"+this.nickname+"</P><P>"+this.city+"</P></div><div id=\"u_operate\"><div onclick=\"findFollowerDevice('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/device.png\"><button id=\"user_but1\">设备</button></div><div onclick=\"deleteOpen('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/delete.png\"><button id=\"user_but2\">删除</button></div></div></div>");
					}else if (!this.headimgurl==""&&!this.city=="") {
						$("#body").append("<div id=\"userinfo\"><div id=\"heard\"><img src=\""+this.headimgurl+"\"><P>"+this.nickname+"</P><P>"+this.city+"</P></div><div id=\"u_operate\"><div onclick=\"findFollowerDevice('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/device.png\"><button id=\"user_but1\">设备</button></div><div onclick=\"deleteOpen('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/delete.png\"><button id=\"user_but2\">删除</button></div></div></div>");
					}
		    		$("#but").val(this.glid);
				})
				history.replaceState(null, "title", "/weixin/user/user.jsp?openId="+$("#but").val()+"&number="+Math.random());
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
        	str = {"openId":getParam("openId"),"start":start};
        	$.ajax({  
        		dataType:"json",    //数据类型为json格式
        		type:"post",
        		data:str,
        		url:"/weixin/servlet/findAllFollower.action", 
        		success:function(data){ 
        			if(data.openId!=null){
        			}else{
        				$.each(data,function(){
        					if (this.headimgurl==""&&this.city=="") {
        		    			$("#body").append("<div id=\"userinfo\"><div id=\"heard\"><img src=\"../image/head.png\"><P>"+this.nickname+"</P><P>未设置</P></div><div id=\"u_operate\"><div onclick=\"findFollowerDevice('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/device.png\"><button id=\"user_but1\">设备</button></div><div onclick=\"deleteOpen('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/delete.png\"><button id=\"user_but2\">删除</button></div></div></div>");
        					}else if (!this.headimgurl==""&&this.city=="") {
        						$("#body").append("<div id=\"userinfo\"><div id=\"heard\"><img src=\""+this.headimgurl+"\"><P>"+this.nickname+"</P><P>未设置</P></div><div id=\"u_operate\"><div onclick=\"findFollowerDevice('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/device.png\"><button id=\"user_but1\">设备</button></div><div onclick=\"deleteOpen('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/delete.png\"><button id=\"user_but2\">删除</button></div></div></div>");
        					}else if (this.headimgurl==""&&!this.city=="") {
        						$("#body").append("<div id=\"userinfo\"><div id=\"heard\"><img src=\"../image/head.png\"><P>"+this.nickname+"</P><P>"+this.city+"</P></div><div id=\"u_operate\"><div onclick=\"findFollowerDevice('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/device.png\"><button id=\"user_but1\">设备</button></div><div onclick=\"deleteOpen('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/delete.png\"><button id=\"user_but2\">删除</button></div></div></div>");
        					}else if (!this.headimgurl==""&&!this.city=="") {
        						$("#body").append("<div id=\"userinfo\"><div id=\"heard\"><img src=\""+this.headimgurl+"\"><P>"+this.nickname+"</P><P>"+this.city+"</P></div><div id=\"u_operate\"><div onclick=\"findFollowerDevice('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/device.png\"><button id=\"user_but1\">设备</button></div><div onclick=\"deleteOpen('"+this.openid+"','"+this.glid+"')\" id=\"operate\"><img src=\"../image/delete.png\"><button id=\"user_but2\">删除</button></div></div></div>");
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
function deleteOpen(openId,glid){
	var bln = confirm("确定删除此用户吗?");   
	if(bln&&flag){
		flag = false;
		$.ajax({  
			dataType:"json",    //数据类型为json格式
			type:"post", 
			data:{"openId":openId,"glid":glid},
			url:"/weixin/servlet/deleteOpen.action",
			success:function(data){ 
				if(data.date!=null&&data.date!=""){
					alert(data.date)
				}else{
					$("#but").val(this.glid);
					window.location.href = data.url;
				}
			 },
			complete:function(){
				flag = true;
			}
		});
	}
}

function findFollowerDevice(openId,glid){
	var url = "/weixin/user/device.jsp?openId="+openId+"&glid="+glid;
	window.location=url;
}
</script>
</head>
<body id="body">
<input type="hidden" id="but">
</body>
</html>