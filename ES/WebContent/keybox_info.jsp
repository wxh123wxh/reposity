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
	$("#top_bnt",window.parent.document).text(getParam("keybox_name"));
	$.ajax({
		dataType:"json",
		type:"post", 
		data:{"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id")},
		url:"/ES/ESServlet/findKeyBoxById.action",
		success:function(data){
			if(jQuery.isEmptyObject(data)){
				
			}else{
				$(".keybox_inp").eq(0).val(data.name)
				$(".keybox_inp").eq(1).val(data.maxc)
				$(".keybox_inp").eq(2).val(data.ip)
				$(".keybox_inp").eq(3).val(data.ip_mask)
				$(".keybox_inp").eq(4).val(data.ip_gatewey)
				$(".keybox_inp").eq(5).val(data.password)
				$(".keybox_inp").eq(6).val(data.cent1_ip)
				$(".keybox_inp").eq(7).val(data.cent2_ip)
				$(".keybox_inp").eq(8).val(data.cent3_ip)
				$(".keybox_inp").eq(9).val(data.cent4_ip)
				$(".keybox_inp").eq(10).val(data.cent1_sourcePort)
				$(".keybox_inp").eq(11).val(data.cent1_destPort)
				$(".keybox_inp").eq(12).val(data.cent2_sourcePort)
				$(".keybox_inp").eq(13).val(data.cent2_destPort)
				$(".keybox_inp").eq(14).val(data.cent3_sourcePort)
				$(".keybox_inp").eq(15).val(data.cent3_destPort)
				$(".keybox_inp").eq(16).val(data.cent4_sourcePort)
				$(".keybox_inp").eq(17).val(data.cent4_destPort)
				$(".keybox_inp").eq(18).val(data.onlines)
			}
		}
	});
}

$(function() {
	pushHistory();
	window.addEventListener('popstate', function(e) {
		//自定义操作
		window.location.href = "/ES/keybox.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&number="+Math.random();
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
var time = null;
var arr = new Array()
var lasttimestamp = 0;

info_confrim = function(){
	for(var a=2;a<=17;a++){
		var val = $(".keybox_inp").eq(a).val().trim()
		if(val!=""){
			var validVal = valid(val,a)
			if(validVal!="no"){
				arr.push(val)
			}else{
				break;
			}
		}else{
			break;
		}
	}
	if(arr.length==16){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"arr":arr,"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id")},
			url:"/ES/ESServlet/updateKeyBox.action",
			traditional: true,
			success:function(data){
				if(data.data=="命令已发送"){
					lasttimestamp = (new Date()).getTime();
					time = setInterval("getBack(1)", 1000);
					$("#loading").addClass("loadingWrap");
				}else{
					alert(data.data)
				}
			}
		});
	}
	arr.length=0;
}
function getBack(size){
	$.ajax({  
		type:"post", 
		data:{"keybox_id":getParam("keybox_id")},
		url:"/ES/ESServlet/getBack.action",
		dataType:"json",
		success:function(data){
			var timestamp = (new Date()).getTime();
			
			if(data.data=="配置成功"){
				clearTimeout(time);
				time = null;
				$("#loading").removeClass("loadingWrap");
				alert("操作成功")
				window.location="/ES/keybox_info.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+getParam("keybox_name");
			}else if(timestamp-lasttimestamp>60*1000*size){
				clearTimeout(time);
				time = null;
				$("#loading").removeClass("loadingWrap");
				alert("操作失败")
			}
		 }  
	});
} 
window.onbeforeunload=function(e){  
	stop();
} 
function stop(){
	if(time!=null){
		clearTimeout(time);
		console.log("刷新、关闭页面时关闭定时器");
		time=null;
	}
}
back = function(){
	window.location.href = "/ES/keybox.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&number="+Math.random();
}

valid = function(val,index){
	var reg1 = new RegExp("^[0-9]{6}$");
	var reg2 = new RegExp("^[0-9]{1,5}$");
	var reg3 = new RegExp("^[0-9]{1,3}$");
	
	if(index==5){
		if(!reg1.test(val)){
			alert("密码请输入6位数字！");
			return "no"
		}
	}else if(index>=10){
		if(!reg2.test(val)){
			alert("端口请输入1——5位数字");
			return "no"
		}
	}else{
		var arr2 = val.split(".");
		for(var a=0;a<arr2.length;a++){
			if(reg3.test(arr2[a])&&arr2[a]>=0&&arr2[a]<=255&&arr2.length==4){
			
			}else{
				alert("ip格式错误");
				return "no"
			}
		}
	}
}
deleteRemotes = function(e){
	var bln = confirm("确定删除吗?");
	if(bln){
		$.ajax({  
			type:"post", 
			data:{"keybox_id":getParam("keybox_id"),"element":e},
			url:"/ES/ESServlet/deleteRemotes.action",
			dataType:"json",
			success:function(data){
				if(data.data=="命令已发送"){
					lasttimestamp = (new Date()).getTime();
					time = setInterval("getBack(1)", 1000);
					$("#loading").addClass("loadingWrap");
				}else{
					alert(data.data)
				}
			}
		});
	}
}
sendAllCard = function(){
	$.ajax({  
		type:"post", 
		data:{"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id")},
		url:"/ES/ESServlet/sendAllCard.action",
		dataType:"json",
		success:function(data){
			if(data.data=="命令已发送"){
				lasttimestamp = (new Date()).getTime();
				time = setInterval("getBack(1)", 1000);
				$("#loading").addClass("loadingWrap");
			}else{
				alert(data.data)
			}
		}
	});
}
sendAllUser = function(){
	$.ajax({  
		type:"post", 
		data:{"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id")},
		url:"/ES/ESServlet/sendAllUser.action",
		dataType:"json",
		success:function(data){
			if(data.data=="命令已发送"){
				lasttimestamp = (new Date()).getTime();
				time = setInterval("getBack(1)", 1000);
				$("#loading").addClass("loadingWrap");
			}else{
				alert(data.data)
			}
		}
	});
}

</script>
</head>
<body id="body">
	<div class="keybox_info_cont">
		<div class="keybox_info">
			<div>
				<p>钥匙箱名称</p>
			</div>
			<div>
				<input class="keybox_inp" readonly="readonly"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>钥匙箱max地址</p>
			</div>
			<div>
				<input class="keybox_inp" readonly="readonly"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>钥匙箱IP地址</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>钥匙箱IP掩码</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>钥匙箱IP网关</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>钥匙箱密码</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心1 IP地址</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心2 IP地址</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心3 IP地址</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心4 IP地址</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心1源端口</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心1目的端口</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心2源端口</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心2目的端口</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心3源端口</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心3目的端口</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心4源端口</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>中心4目的端口</p>
			</div>
			<div>
				<input class="keybox_inp"/>
			</div>
		</div>
		<div class="keybox_info">
			<div>
				<p>钥匙箱状态</p>
			</div>
			<div>
				<input class="keybox_inp" readonly="readonly"/>
			</div>
		</div>
		<div class="confrim">
			<button onclick="info_confrim()">配置IP</button>
			<button onclick="sendAllCard()">配置卡</button>
			<button onclick="sendAllUser()">配置用户</button>
			<button class="confrim_bnt3" onclick="deleteRemotes('User')">删除远程配置的用户</button>
			<button class="confrim_bnt3" onclick="deleteRemotes('Card')">删除远程配置的卡</button>
			<button class="confrim_bnt4" onclick="deleteRemotes('Key')">删除远程配置的钥匙</button>
		</div>
		<div class="confrim" onclick="back()">
			<button class="confrim_bnt1">返回上层</button>
		</div>
	</div>
	
	<div class="" id="loading"></div>
</body>
</html>