<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page language="java" import="com.wx.service.ServletServiceImpl"%>
<%@ page language="java" import="java.io.InputStreamReader"%>
<%@ page import="java.util.Properties" %>
<%
   Properties prop=new Properties();
   prop.load(new InputStreamReader(ServletServiceImpl.class.getClassLoader().getResourceAsStream("config.properties"), "UTF-8"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="css/layui.css">
<link rel="stylesheet" href="css/index.css">
<script type="text/javascript" src="jquery-3.2.1/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="jquery-3.2.1/jquery.form.js"></script>
<script type="text/javascript">
var old_show = null;
window.onload=function(){
	$.ajax({
		dataType:"json",
		type:"post", 
		url:"/ES/ESServlet/findUnit.action",
		success:function(data){
			if(data.data!=null){
				$("#top_bnt").text("请添加"+'<%=prop.get("unit")%>'+"信息")
				$("#u_name").val("请添加"+'<%=prop.get("unit")%>'+"信息")
				$("#unit_show").removeClass("unit_hidden");
			  	$("#unit_show").addClass("unit_show");
				$("#unit_show").attr("src", 'unit.jsp?number='+Math.random());
				old_show = "unit_show"
			}else{
				$("#top_bnt").text(data.name)
				$("#u_name").val(data.name)
				$("#team_show").removeClass("unit_hidden");
			  	$("#team_show").addClass("unit_show");
				$("#team_show").attr("src", 'team.jsp?unit_id='+data.unit_number+'&unit_name='+data.name);
				$("#u_unmber").val(data.unit_number)
				old_show = "team_show"
			}
		}
	});
}
reClass = function(e){
	if($(e).parent().hasClass("layui-nav-itemed")){
		$(e).parent().removeClass("layui-nav-itemed");
	}else{
		$(e).parent().addClass("layui-nav-itemed");
	}
}
showUit = function(e){
	$("#"+e).addClass("unit_show");
	if(old_show!=null){
		$("#"+old_show).removeClass("unit_show");
	  	$("#"+old_show).addClass("unit_hidden");
	}
	$("#"+e).removeClass("unit_hidden");
  	$("#"+e).addClass("unit_show");
  	
  	if(e=="unit_show"){
  		if($("#u_unmber").val()!=null&&$("#u_unmber").val()!=""){
  			$("#unit_show").removeClass("unit_show");
		  	$("#unit_show").addClass("unit_hidden");
  			$("#team_show").removeClass("unit_hidden");
		  	$("#team_show").addClass("unit_show");
		  	
			$("#team_show").attr("src", 'team.jsp?unit_id='+$("#u_unmber").val()+'&unit_name='+$("#u_name").val());
			old_show = "team_show";
  		}else{
  			old_show = e;
	  		$("#unit_show").attr("src", 'unit.jsp?number='+Math.random()+'&unit_name='+$("#u_name").val());
  		}
  	}else if(e=="log_show"){
  		old_show = e;
  		$("#log_show").attr("src", 'logs.jsp?number='+Math.random()+'&unit_name='+$("#u_name").val());
  	} else if(e=="handleLog_show"){
  		old_show = e;
  		$("#handleLog_show").attr("src", 'handleLog.jsp?number='+Math.random()+'&unit_name='+$("#u_name").val());
  	} else if(e=="data_show"){
  		$("#data_show").attr("src", 'data.jsp?number='+Math.random()+'&unit_name='+$("#u_name").val());
  		old_show = e;
  	}
}
exit = function(){
	$.ajax({
		dataType:"json",
		type:"post", 
		url:"/ES/ESServlet/exit.action",
		success:function(data){
			window.location.href = data.url
		}
	});
}
$(function() {
	pushHistory();
	window.addEventListener('popstate', function(e) {
		 //自定义操作
		window.location.href = "/ES/index.jsp";
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
changeLevel = function(){
	var id = "<%=request.getSession().getAttribute("id")%>"
	var style = "<%=request.getSession().getAttribute("style")%>"
	var name = "<%=request.getSession().getAttribute("user")%>"
	var password = prompt("请输入要修改的密码")
	
	if(password!=null&&password.trim()!=""){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"password":password,"id":id,"style":style,"name":name},
			url:"/ES/ESServlet/changeLevel.action",
			success:function(data){
				alert(data.data)
			}
		});
	}
	
}
</script>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
  <div class="layui-header">
  <div class="layui-logo">解锁钥匙管理箱</div>
    <!-- 头部区域（可配合layui已有的水平导航） -->
    <ul class="layui-nav layui-layout-left" id="top_ul">
      <li class="layui-nav-item"><button class="top_bnt" id="top_bnt"></button></li>
    </ul>
    <ul class="layui-nav layui-layout-right">
      <li class="layui-nav-item">
        <a onclick="changeLevel()"><%=request.getSession().getAttribute("user") %></a>
      <li class="layui-nav-item"><a onclick="exit()">退出</a></li>
    </ul>
  </div>
  
  <div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
      <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
      <ul class="layui-nav layui-nav-tree"  lay-filter="test">
        <li class="layui-nav-item over"><a onclick="showUit('unit_show')">基本信息</a></li>
      	<li class="layui-nav-item over"><a onclick="showUit('handleLog_show')">远程记录</a></li>
      	<li class="layui-nav-item over"><a onclick="showUit('log_show')">事件记录</a></li>
      	<li class="layui-nav-item over"><a onclick="showUit('data_show')">数据处理</a></li>
      </ul>
    </div>
  </div>
  
  <div class="layui-body">
     <!-- 内容主体区域 -->
  	<iframe frameborder="0" id="unit_show" class="unit_hidden"></iframe> 
  	<iframe frameborder="0" id="log_show" class="unit_hidden"></iframe> 
    <iframe frameborder="0" id="handleLog_show" class="unit_hidden"></iframe>
  	<iframe frameborder="0" id="data_show" class="unit_hidden"></iframe> 
  	<iframe frameborder="0" id="team_show" class="unit_hidden"></iframe> 
  </div>
</div>
<input type="hidden" id="u_unmber">
<input type="hidden" id="u_name">
</body>
</html>