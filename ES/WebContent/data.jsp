<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/weui.min.css" />
<script type="text/javascript" src="jquery-3.2.1/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="jquery-3.2.1/jquery.form.js"></script>
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
window.onload=function(){
	$("#top_bnt",window.parent.document).text(getParam("unit_name"));
	$.ajax({
		dataType:"json",
		type:"post", 
		url:"/ES/ESServlet/findConfig.action",
		success:function(data){
			if(data.receivePort!=0){
				$("#port").val(data.receivePort)
			}else{
				$("#port").val("请设置监听端口")
			}
			$("#day").val(data.intervalDay)
		}
	});
	ShowDiv('MyDiv4','fade4_2')
}
function ShowDiv(show_div,bg_div){
	document.getElementById(show_div).style.display='block';
	document.getElementById(bg_div).style.display='block' ;
	var bgdiv = document.getElementById(bg_div);
	bgdiv.style.width = document.body.scrollWidth; 
	$("#"+bg_div).height($(document).height());
};
setConfigTime = function(){
	var day = $("#day").val().trim()
	if(day!=""){
		var reg = new RegExp("^[0-9]+$");
		if(reg.test(day)&&(day>0&&day<=10)){
			$.ajax({
				dataType:"json",
				type:"post", 
				data:str = {"day":day},
				url:"/ES/ESServlet/setConfigTime.action",
				success:function(data){
					alert(data.data)
				}
			});
		}else{
			alert("天数在1——10之间")
		}
	}else{
		alert("天数不能为空")
	}
}
setConfigPort = function(){
	var port = $("#port").val().trim()
	if(port!=""){
		var reg = new RegExp("^[0-9]+$");
		if(reg.test(port)&&(port>=0&&port<=65535)){
			$.ajax({
				dataType:"json",
				type:"post", 
				data:str = {"port":port},
				url:"/ES/ESServlet/setConfigPort.action",
				success:function(data){
					alert(data.data)
				}
			});
		}else{
			alert("port在0——65535之间")
		}
	}else{
		alert("port不能为空")
	}
}
var lasttimestamp = 0;
function upload() {
	var timestamp = (new Date()).getTime();
	if(timestamp-lasttimestamp>65*1000){
		var options = { 
			type: 'POST',
			url: '/ES/ESServlet/upload.action',
			dataType:"json",
		       success:function(data) {
		       alert(data.data)
			}
		};
		// jquery 表单提交 
		$("#form").ajaxSubmit(options);
		lasttimestamp = timestamp
	}else{
		alert("不要频繁操作")
	}
	return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转 
} 
copyData = function(){
	var timestamp = (new Date()).getTime();
	if(timestamp-lasttimestamp>65*1000){
		$.ajax({
			dataType:"json",
			type:"post", 
			url:"/ES/ESServlet/copyData.action",
			success:function(data){
				alert(data.data)
			}
		});
		lasttimestamp = timestamp
	}else{
		alert("不要频繁操作")
	}
}
show = function(){
	$("#file_name_show").val($("#file").val())
}
</script>
</head>
<body id="body" class="mar_bot">
<div id="fade4_2" class="black_overlay"></div>
	<div id="MyDiv4" class="white_content mar_top">
		<div>
			<P class="tip_p margin_30">数据处理</P>
			<div id="cont">
				<div class="cente"><span class="font_size_18 data_span">监听端口号</span><input id="port" type="text" class="width_200 cente paddingTopAndBut10"/><button onclick="setConfigPort()" class="data_bnt2">配置</button></div>
				<div class="cente"><span class="font_size_18 data_span">备份间隔天</span><input id="day" type="text" class="width_200"/><button onclick="setConfigTime()" class="data_bnt2">确认</button></div>
				<form action="/ES/ESServlet/upload.action" id="form" method="post" enctype="multipart/form-data" onsubmit="return false;">
					<div class="file-box font_size_18" >
						<input type="file" id="file" name="file" class="file-btn" accept=".sql" onchange="show()"/>
						请选择文件
					</div>
					<input type="text" id="file_name_show"  class="width_200 file_inp" readonly="readonly"/>
					<button type="submit" class="data_bnt" onclick="upload()">导入</button>
				</form>
				<div class="cont_lab" onclick="copyData()"><p class="width_100 blanch" >导出组态数据（C:\lock\copyData文件夹）</p></div>
			</div>
		</div>
	</div>
	<div>
	<input type="hidden" id="remday">
</div>
</body>
</html>