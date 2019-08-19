<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width, initial-scale=1 user-scalable=no" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改子设备</title>
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
/**
 * 检查更新的子设备名称是否已存在
 */
function toVaild(){
	var reg = new RegExp("^[A-Za-z0-9\u4e00-\u9fa5]+$");
	var name = document.getElementById("name").value.trim();
	var zId = document.getElementById("zId").value;
	var password = document.getElementById("password").value.trim();
	if(!reg.test(name)){
		alert("名称请输入中文、数字或英文！");
	 	return false;
	}
	if(!reg.test(password)){
		alert("密码请输入中文、数字或英文！");
	 	return false;
	}
	var oldname = getParam("oldname")
	var boo;
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		async: false,
		type:"post", 
		data:{"name":name,"openId":getParam("openId"),"zId":zId,"oldname":oldname,"sub_id":-1},
		url:"/weixin/servlet/checkNameSub.action",
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
var timestamp = 0;
function submit_from(){
	var flag = toVaild()
	var time = new Date().getTime();
	if(flag&&(time - timestamp>100)){
		timestamp = time;
    	document.getElementById("from_submit").submit();//表单提交  
	}
}
</script>
</head>
<body>
	<form action="/weixin/servlet/updateSubDevice.action" method="post"
		id="from_submit">
		<div id="deviceAdd">
			<div id="deviceinfo">
				<div>
					<p>子设备名称</p>
				</div>
				<div>
					<input type="text" name="name" id="name" value="${param.oldname }">
				</div>
			</div>
			<div id="deviceinfo">
				<div>
					<p>子设备密码</p>
				</div>
				<div>
					<input type="password" name="password" id="password"
						value="${param.password }">
				</div>
			</div>
			<div id="confrim" onclick="submit_from()">
				<p>
					<img src="../image/confrim.png" />
					<button type="button">确认</button>
				</p>
			</div>

			<input type="hidden" name="id" value="${param.id}" id="id">
			<input type="hidden" name="openId" value="${param.openId}"> 
			<input type="hidden" name="zId" value="${param.zId}" id="zId">
		</div>
	</form>
</body>
</html>