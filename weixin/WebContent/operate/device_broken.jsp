<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主设备列表</title>
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
	var code = getParam("code");
	var str = null;
	
	if (code!=null&&code!="") {
		str = {"code":code,"start":start};
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
					if(this.one_broken==1){
						$("#body").append("<div id=\"device_state\"><div id=\"device_info\"><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_operate\"><p id=\"p1\" onclick=\"addOneBroken('"+this.id+"')\"><img src=\"../image/followed.png\" id=\""+this.id+"\"><button class=\"red\">取消</button></p><p class=\"blue\" onclick=\"findSubDevice('"+this.id+"','"+this.openId+"','"+this.name+"')\"><img src=\"../image/sub_blue.png\"><button class=\"sblue\">子设备</button></p></div></div>");
					}else{
						$("#body").append("<div id=\"device_state\"><div id=\"device_info\"><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_operate\"><p id=\"p1\" onclick=\"addOneBroken('"+this.id+"')\"><img src=\"../image/discheck.png\"  id=\""+this.id+"\"><button class=\"red\">添加</button></p><p class=\"blue\" onclick=\"findSubDevice('"+this.id+"','"+this.openId+"','"+this.name+"')\"><img src=\"../image/sub_blue.png\"><button class=\"sblue\">子设备</button></p></div></div>");
					}
					$("#but").val(this.openId);
				})
				history.replaceState(null, "title", "/weixin/operate/device_broken.jsp?openId="+$("#but").val()+"&number="+Math.random());
			}
			var open = $("#but").val();
			$("#body").append("<div id=\"addDevice\" onclick=\"ShowDiv('MyDiv','fade')\"><p><img src=\"../image/add.png\"><button>一键撤防</button></p></div>");
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
        	var str = {"openId":getParam("openId"),"start":start};
        	$.ajax({  
        		dataType:"json",    //数据类型为json格式
        		type:"post",
        		data:str,
        		url:"/weixin/servlet/findAllDevice.action", 
        		success:function(data){ 
        			if(data.openId!=null){
        			}else{
        				$.each(data,function(){
        					if(this.one_broken==1){
        						$("#body").append("<div id=\"device_state\"><div id=\"device_info\"><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_operate\"><p id=\"p1\" onclick=\"addOneBroken('"+this.id+"')\"><img src=\"../image/followed.png\"  id=\""+this.id+"\"><button class=\"red\">取消</button></p><p class=\"blue\" onclick=\"findSubDevice('"+this.id+"','"+this.openId+"','"+this.name+"')\"><img src=\"../image/sub_blue.png\"><button class=\"sblue\">子设备</button></p></div></div>");
        					}else{
        						$("#body").append("<div id=\"device_state\"><div id=\"device_info\"><p>"+this.id+"</p><p>名称："+this.name+"</p><p>管理员："+this.nickname+"</p></div><div id=\"device_operate\"><p id=\"p1\" onclick=\"addOneBroken('"+this.id+"')\"><img src=\"../image/discheck.png\"  id=\""+this.id+"\"><button class=\"red\">添加</button></p><p class=\"blue\" onclick=\"findSubDevice('"+this.id+"','"+this.openId+"','"+this.name+"')\"><img src=\"../image/sub_blue.png\"><button class=\"sblue\">子设备</button></p></div></div>");
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
function findSubDevice(id,openId,name){
	var url = "/weixin/operate/broken.jsp?id="+id+"&openId="+openId+"&name="+name;
	window.location=url;
}
/**
 * 添加或更新一键撤防操作的状态
 */
function addOneBroken(id){
	if(flag){
		flag = false;
		var open = $("#but").val();
		var one_broken;
		
		if($("#"+id).attr("src")=="../image/discheck.png"){
			$("#"+id).attr("src", "../image/followed.png");
			$("#"+id).parent().children("button:last-child").html("取消")
			one_broken = 1;
		}else if($("#"+id).attr("src")=="../image/followed.png"){
			$("#"+id).attr("src", "../image/discheck.png");
			$("#"+id).parent().children("button:last-child").html("添加")
			one_broken = 0;
		}
		$.ajax({  
			dataType:"json", 
			type:"post", 
			data:{"id":id,"one_broken":one_broken,"openId":open},
			url:"/weixin/servlet/addOneBroken.action",
			complete:function(){
				flag = true;
			}
		});
	}
}
/**
 * 一键撤防操作
 */
function oneBroken(){
	var open = $("#but").val();
	var password = $("#password").val();
	var onePassword = $("#onePass").val();
	
	if(password==""){
		alert("密码不能为空！")
	}else{
		if(onePassword!=""&&onePassword!=password){
			alert("密码错误!");
		}else{
			if(flag){
				flag = false;
				var remem = "not"; //用来判断是否记住一键操作密码
				if($("#cont_img").attr("src")!=img){
					if($("#cont_img").attr("src")=="../image/check.png"){
						remem = "true";
					}else if($("#cont_img").attr("src")=="../image/discheck.png"){
						remem = "false";
					}
				}
				$.ajax({  
					dataType:"json", 
					type:"post", 
					data:{"openId":open,"remember":remem,"onePassword":password,"method":onePassword},
					url:"/weixin/servlet/oneBroken.action",
					complete:function(){
						flag = true;
					}
				});
				alert("已发送撤防命令！")
			}else{
				alert("不要频繁操作");
			}
		}
	}
}
//弹出输入密码框前的设置
function getPasswordAll(){
	if(flag){
		flag = false;
		var open = $("#but").val();
		$.ajax({  
			dataType:"json",    //数据类型为json格式
			async: false,
			type:"post", 
			data:{"openId":open},
			url:"/weixin/servlet/getPasswordAll.action",
			success:function(data){ 
				if(data.remember!=null){
					if(data.remember=="true"){
						$("#password").val(data.password);
						$("#cont_img").attr("src","../image/check.png")
					}else{
						$("#password").val("");
						$("#cont_img").attr("src","../image/discheck.png")
					}
					$("#onePass").val(data.password);
				}else{
					$("#password").val("");
					$("#onePass").val("");
					$("#cont_img").attr("src","../image/discheck.png")
				}
			},
			complete:function(){
				flag = true;
			}
		});
	}else{
		alert("不要频繁操作")
	}
}
var img = null;
function ShowDiv(show_div,bg_div){
	getPasswordAll()
	img = $("#cont_img").attr("src");
	document.getElementById(show_div).style.display='block';
	document.getElementById(bg_div).style.display='block' ;
	var bgdiv = document.getElementById(bg_div);
	bgdiv.style.width = document.body.scrollWidth; 
	// bgdiv.style.height = $(document).height();
	$("#"+bg_div).height($(document).height());
};
//取消键关闭弹出层
function CloseDiv1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
}
//确定键关闭弹出成
function CloseDiv2(show_div,bg_div){
	document.getElementById(show_div).style.display='none';
	document.getElementById(bg_div).style.display='none';
	oneBroken();
}
function check(){
	var a = $("#cont_img").attr("src");
	
	if(a=="../image/check.png"){
		$("#cont_img").attr("src","../image/discheck.png")
	}else{
		$("#cont_img").attr("src","../image/check.png")
	}
}
</script>
</head>
<body id="body">
	<input type="hidden" id="but">
	<input type="hidden" id="onePass">
	<!--弹出层时背景层DIV-->
	<div id="fade" class="black_overlay"></div>
	<div id="MyDiv" class="white_content">
		<div>
			<P id="p1">请输入密码！</P>
			<div id="cont">
				<input type="password" id="password" class="left_float" />
				<div class="left_float" onclick="check()" id="div1">
					<img src="../image/discheck.png" id="cont_img" />
					<button class="border_none fs">记住</button>
				</div>
			</div>
			<div class="top_border">
				<div class="left_float width center"
					onclick="CloseDiv1('MyDiv','fade')">
					<input type="button" value="取消" class="border_none">
				</div>
				<div class="center border_none" onclick="CloseDiv2('MyDiv','fade')">
					<input type="button" value="确认" class="border_none sblue">
				</div>
			</div>
		</div>
	</div>
</body>
</html>