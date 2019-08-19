<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<title>修改设备</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/weui.min.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript">
var getParam = function(name){  
	//通过此方法截取的属性中文必须decodeURI()解码
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
function toVaild(){
	var reg = new RegExp("^[A-Za-z0-9\u4e00-\u9fa5]+$");
	var name = document.getElementById("name").value;
	if(!reg.test(name)){
		alert("名称请输入中文、数字或英文！");
	 	return false;
	}
	var oldname = decodeURI(getParam("name"))
	var boo;
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		async: false,
		type:"post", 
		data:{"name":name,"openId":getParam("openId"),"oldname":oldname},
		url:"/weixin/servlet/checkNameUpdate.action",
		success:function(data){ 
			boo = data.check;
		}  
	});
	if(boo==="yes"){
		return true;
	}else{
		alert(boo)
		return false;
	}
}
function backDevice(){
	var openId = getParam("openId");
	var url = "/weixin/device/device.jsp?openId="+openId;
	window.location=url;
}
var timestamp = 0;
function submit_from(){
	var flag = toVaild()
	var time=new Date().getTime();
	if(flag&&(time-timestamp>100)){
		timestamp = time;
    	document.getElementById("from_submit").submit();//表单提交  
	}
}
</script>
</head>
<body>
	<form action="/weixin/servlet/updateDevice.action" method="post"
		id="from_submit">
		<div id="deviceAdd">
			<div id="deviceinfo">
				<div>
					<p>主设备名称</p>
				</div>
				<div>
					<input type="text" name="name" id="name" value="${param.name }">
				</div>
			</div>
			<div id="confrim" onclick="submit_from()">
				<p>
					<img src="../image/confrim.png" />
					<button type="button">确认</button>
				</p>
			</div>
			<input type="hidden" name="id" value="${param.id}" id="id"> <input
				type="hidden" name="openId" value="${param.openId}">
		</div>
	</form>
</body>
</html>