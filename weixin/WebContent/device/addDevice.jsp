<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1 user-scalable=no" />
<title>添加主设备</title>

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


function toVaild(){
	var reg = new RegExp("^[A-Za-z0-9\u4e00-\u9fa5]+$");
	var reg2 = new RegExp("^[A-Za-z0-9]+$");
	var reg3 = new RegExp("^[0-9]+$");
	var name = document.getElementById("name").value.trim();
	var id = document.getElementById("id").value.trim();
	var sub_num = document.getElementById("sub_num").value.trim();
	var sector_num = document.getElementById("sector_num").value.trim();
	if(!reg.test(name)){
		alert("名称请输入中文、数字或字母！");
	 	return false;
	}
	if(!reg2.test(id)){
		alert("设备编号请输入数字或字母！");
	 	return false;
	}
	if(!reg3.test(sub_num)){
		alert("子设备数量请输入数字！");
	 	return false;
	}else {
		if(!(parseInt(sub_num)>=1&&parseInt(sub_num)<=256)){
			alert("子设备数量在(1-256)之间！")
			return false;
		}
	}
	if(!reg3.test(sector_num)){
		alert("防区数量请输入数字！");
	 	return false;
	}else {
		if(!(parseInt(sector_num)>=1&&parseInt(sector_num)<=8)){
			alert("防区数量在(1-8)之间！")
			return false;
		}
	}
	var boo;
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		async: false,
		type:"post", 
		data:{"name":name,"openId":getParam("openId"),"id":id},
		url:"/weixin/servlet/checkName.action",
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
var timestamp = 0;//上一次调用的时间
function submit_from(){
	var flag = toVaild();
	var time = new Date().getTime();
	if(flag&&(time-timestamp>100)){
		timestamp = time;
    	document.getElementById("from_submit").submit();//表单提交  
	}
}
</script>
</head>
<body>
	<!-- 以后还要验证设备E6U和编号是否书写正确 -->
	<form action="/weixin/servlet/addDevice.action" method="post"
		id="from_submit">
		<div id="deviceAdd">
			<div id="deviceinfo">
				<div>
					<p>主设备名称</p>
				</div>
				<div>
					<input type="text" name="name" id="name">
				</div>
			</div>
			<div id="deviceinfo">
				<div>
					<p>主设备编号</p>
				</div>
				<div>
					<input type="text" name="id" id="id">
				</div>
			</div>
			<div id="deviceinfo">
				<div>
					<p>子设备个数</p>
				</div>
				<div>
					<input type="text" name="sub_num" id="sub_num">
				</div>
			</div>
			<div id="deviceinfo">
				<div>
					<p>子设备防区个数</p>
				</div>
				<div>
					<input type="text" name="sector_num" id="sector_num">
				</div>
			</div>
			<input type="hidden" name="openId" value="${param.openId}"
				id="openid">
			<div id="confrim" onclick="submit_from()">
				<p>
					<img src="../image/confrim.png" />
					<button type="button">确认</button>
				</p>
			</div>
		</div>
	</form>
</body>
</html>