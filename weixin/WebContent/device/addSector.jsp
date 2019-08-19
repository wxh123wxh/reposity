<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport"
	content="width=device-width, initial-scale=1 user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>添加防区</title>
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
	var reg2 = new RegExp("^[1-8]$");
	var name = document.getElementById("name").value.trim();
	var sec_id = document.getElementById("sec_id").value.trim();
	var sub_id = document.getElementById("sub_id").value.trim();
	if(!reg2.test(sec_id)){
		alert("防区编号在(1-8)之间！")
	 	return false;
	}
	if(!reg.test(name)){
		alert("名称请输入中文、数字或字母！");
	 	return false;
	}
	var boo;
	var length;
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		async: false,
		type:"post", 
		data:{"name":name,"openId":getParam("openId"),"zId":getParam("zId"),"sec_id":sec_id},
		url:"/weixin/servlet/checkNameSector.action",
		success:function(data){ 
			boo = data.check;
			length = data.length;
		}  
	});
	if(boo==="yes"){
		if(length<8){
			return true;
		}else{
			alert("已有8个防区，不能再添加！")
			return false;
		}
	}else{
		alert(boo)
		return false;
	}
}
var timestamp=0;
function submit_from(){
	var flag = toVaild();
	var time = new Date().getTime();
	if(flag&&(time-timestamp>100)){
		timestamp = time
    	document.getElementById("from_submit").submit();//表单提交  
	}
}
</script>
</head>
<body>
	<!-- 添加之前要先判断子设备不能超过E6U规定的数目 -->
	<!-- 以后还要验证设备编号是否书写正确，重复 -->
	<form action="/weixin/servlet/addSector.action" method="post"
		id="from_submit">
		<div id="deviceAdd">
			<div id="deviceinfo">
				<div>
					<p>防区编号</p>
				</div>
				<div>
					<input type="text" name="sec_id" id="sec_id">
				</div>
			</div>
			<div id="deviceinfo">
				<div>
					<p>防区名称</p>
				</div>
				<div>
					<input type="text" name="name" id="name">
				</div>
			</div>
			<div id="confrim" onclick="submit_from()">
				<p>
					<img src="../image/confrim.png" />
					<button type="button">确认</button>
				</p>
			</div>
			<input type="hidden" name="openId" value="${param.openId}"> <input
				type="hidden" name="sub_id" value="${param.zId}" id="sub_id">
			<input type="hidden" name="maxc_id" value="${param.maxc_id}">
		</div>
	</form>
</body>
</html>