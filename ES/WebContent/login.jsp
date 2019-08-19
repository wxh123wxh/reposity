<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/weui.min.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-3.2.1/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-3.2.1/jquery.form.js"></script>
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
	ShowDiv('MyDiv','fade')
}
function ShowDiv(show_div,bg_div){
	document.getElementById(show_div).style.display='block';
	document.getElementById(bg_div).style.display='block' ;
	var bgdiv = document.getElementById(bg_div);
	bgdiv.style.width = document.body.scrollWidth; 
	$("#"+bg_div).height($(document).height());
};
login = function(){
	var name = $("#username").val().trim()
	var password = $("#password").val().trim()
	var remember = 0;

	if(name!=""&&password!=""){
		if($("#img").attr("src")=="${pageContext.request.contextPath}/image/check.png"){
			remember = 1
		}
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"name":name,"password":password,"remember":remember},
			url:"/ES/ESServlet/login.action",
			success:function(data){
				if(data.url!=null){
					window.location.href = data.url
				}else{
					$("#errorMessage").text(data.data)
				}
			}
		});
	}else{
		$("#errorMessage").text("用户名或密码错误，请重新输入.")
	}
}
remPssword = function(e){
	if($(e).attr("src")=="${pageContext.request.contextPath}/image/check.png"){
		$(e).attr("src","${pageContext.request.contextPath}/image/discheck.png")
	}else{
		$(e).attr("src","${pageContext.request.contextPath}/image/check.png")
	}
}
getPassword = function(){
	var name = $("#username").val().trim()
	if(name!=""){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"name":name},
			url:"/ES/ESServlet/getPassword.action",
			success:function(data){
				if(data.data==null){
					if(data.password!=null){
						$("#password").val(data.password)
					}else{
						$("#password").val(data.password)
					}
					$("#img").attr("src","${pageContext.request.contextPath}/image/check.png")
				}
			}
		});
	}
}
</script>
</head>
	<div id="fade" class="black_overlay"></div>
	<div id="MyDiv" class="white_content mar_top width40">
		<div>
			<P class="tip_p margin_30">钥匙箱管理系统</P>
			<div class="tips"><span id="errorMessage" class="red"></span></div>
			<div class="login_cont">
				<input type="text" id="username" class="login_inp" placeholder="请输入用户名" onchange="getPassword()"><br>
				<input type="password" id="password" class="login_inp margin_tip20" placeholder="请输入密码"><br>
				<div class="login_div">
					<img src="${pageContext.request.contextPath}/image/discheck.png" onclick="remPssword(this)" id="img"><span class="login_span">记住密码</span>
				</div>
				<input type="button" value="登录" class="login_bnt" onclick="login()">
			</div>
		</div>
	</div>
</body>
</html>