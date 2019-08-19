<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>一键操作密码设置</title>
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
var insert = null;
window.onload=function(){
	var code = getParam("code");
	if (code!=null&&code!="") {
		str = {"code":code};
	}else{
		str = {"openId":getParam("openId")};
	}
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post",
		data:str,
		url:"/weixin/servlet/getOnePassword.action", 
		success:function(data){ 
			if(data.password!=null){
				$("#but").val(data.openId);
				$("#password1").val(data.password);
				insert = data.password;
			}else{
				$("#but").val(data.openId);
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
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
function submit_from(){
	var password = $("#password1").val().trim();
	if (password!=""&&flag) {
		var openId = $("#but").val();
		if(insert==null){//表示没有一键操作密码 则添加
			 flag = false;
			 $.ajax({  
				dataType:"json",    //数据类型为json格式
				type:"post", 
				data:{"openId":openId,"password":password},
				url:"/weixin/servlet/insertPassword.action",
				success:function(data){ 
					alert(data.back)
					var url = "/weixin/operate/onePassword.jsp?openId="+$("#but").val();
					window.location=url;
				} ,
				complete:function(){
					flag = true;
				} 
			});
		}else {
			if(insert!=password){//输入的一键操作密码不同之前的则修改
				 flag = false;
				 $.ajax({  
					dataType:"json",    //数据类型为json格式
					type:"post", 
					data:{"openId":openId,"password":password},
					url:"/weixin/servlet/setPassword.action",
					success:function(data){ 
						alert(data.back)
						var url = "/weixin/operate/onePassword.jsp?openId="+$("#but").val();
						window.location=url;
					}  ,
					complete:function(){
						flag = true;
					}
				});
			}else{//输入的一键操作密码同之前的密码相同 则离开此页面
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
	}else {
		alert("密码为空或频繁操作")
	}
}

</script>
</head>
<body id="body">
	<div id="onePassword">
		<div id="password">
			<p>一键操作密码：</p>
		</div>
		<div id="inp">
			<input type="password" id="password1">
		</div>
		<div id="confrim" onclick="submit_from()">
			<p>
				<img src="../image/confrim.png" />
				<button type="button">确认设置(修改)</button>
			</p>
		</div>
	</div>
	<input type="hidden" id="but">
</body>
</html>