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

var start = 0; //查询的开始位置

window.onload=function(){
	$("#top_bnt",window.parent.document).text(getParam("unit_name"));
	var str = null;
	var team_id = getParam("team_id");
	var keybox_id = getParam("keybox_id");
	var keyss_id = getParam("keyss_id");
	var unit_id = getParam("unit_id")
	
	if(keyss_id!=null&&keyss_id!=""){
		str = {"start":start,"keyss_id":keyss_id,"keybox_id":keybox_id,"team_id":team_id,"unit_id":unit_id,"style":0}
	}else if(keybox_id!=null&&keybox_id!=""){
		str = {"start":start,"keyss_id":-1,"keybox_id":keybox_id,"team_id":team_id,"unit_id":unit_id,"style":0}
	}else if(team_id!=null&&team_id!=""){
		str = {"start":start,"keyss_id":-1,"team_id":team_id,"unit_id":unit_id,"style":0}
	}else{
		str = {"start":start,"keyss_id":-1,"team_id":-1,"style":0}
	}
	
	$.ajax({
		dataType:"json",
		type:"post", 
		data:str,
		url:"/ES/ESServlet/findLog.action",
		success:function(data){
			document.getElementById("cont_addsite").style.display='block';
			if(jQuery.isEmptyObject(data)){
				
			}else{
				document.getElementById("log_foot").style.display='block';
				$.each(data,function(){
					$("#body").append("<div class=\"log_cent\"><p class=\"margin_1 width_9\">"+this.unit_name+"</p><p class=\"width_9\">"+this.team_name+"</p><p class=\"width_9\">"+this.keybox_name+"</p><p>"+this.users_name+"</p><p>"+this.manager_name+"</p><p class=\"width_16\">"+this.keyss_id+":"+this.keyss_name+"</p><p>"+this.style+"</p><p>"+this.application_time+"</p><p>"+this.approve_time+"</p><p>"+this.get_time+"</p><p class=\"no_rightBorder\">"+this.card_id+"</p></div>");
				})
			}
		}
	});
}
/**
 *  页面下拉时查询更多的unit
 */
$(function(){
	$(window).scroll(function() {
   		var scrollTop = $(this).scrollTop(),scrollHeight = $(document).height(),windowHeight = $(this).height();
    	var positionValue = (scrollTop + windowHeight) - scrollHeight;
    	
    	if (positionValue >= 0) {
    		var str = null;
    		var team_id = getParam("team_id");
    		var keybox_id = getParam("keybox_id");
    		var keyss_id = getParam("keyss_id");
    		var unit_id = getParam("unit_id")
    		
    		if(keyss_id!=null&&keyss_id!=""){
    			str = {"start":++start,"keyss_id":keyss_id,"keybox_id":keybox_id,"team_id":team_id,"unit_id":unit_id,"style":0}
    		}else if(keybox_id!=null&&keybox_id!=""){
    			str = {"start":++start,"keyss_id":-1,"keybox_id":keybox_id,"team_id":team_id,"unit_id":unit_id,"style":0}
    		}else if(team_id!=null&&team_id!=""){
    			str = {"start":++start,"keyss_id":-1,"team_id":team_id,"unit_id":unit_id,"style":0}
    		}else{
    			str = {"start":++start,"keyss_id":-1,"team_id":-1,"style":0}
    		}
    		$.ajax({
    			dataType:"json",
    			type:"post", 
    			data:str,
    			url:"/ES/ESServlet/findLog.action",
    			success:function(data){ 
    				if(jQuery.isEmptyObject(data)){
    					start--
    				}else{
    					$.each(data,function(){
    						$("#body").append("<div class=\"log_cent\"><p class=\"margin_1 width_9\">"+this.unit_name+"</p><p class=\"width_9\">"+this.team_name+"</p><p class=\"width_9\">"+this.keybox_name+"</p><p>"+this.users_name+"</p><p>"+this.manager_name+"</p><p class=\"width_16\">"+this.keyss_id+":"+this.keyss_name+"</p><p>"+this.style+"</p><p>"+this.application_time+"</p><p>"+this.approve_time+"</p><p>"+this.get_time+"</p><p class=\"no_rightBorder\">"+this.card_id+"</p></div>");
    					})
    				}
    			 } 
    		});
  		 }
	});
});


creatTabel = function(){
	var team_id = getParam("team_id");
	var keybox_id = getParam("keybox_id");
	var keyss_id = getParam("keyss_id");
	var unit_id = getParam("unit_id")
	
	if(team_id==null){
		team_id = -1
	}
	if(keyss_id==null){
		keyss_id = -1
	}
	if(keybox_id==null){
		keybox_id = 0
	}
	if(unit_id==null){
		unit_id = 0
	}
	window.location.href="/ES/ESServlet/export.action?team_id="+team_id+"&keybox_id="+keybox_id+"&keyss_id="+keyss_id+"&unit_id="+unit_id+"&style="+0;
}
</script>
</head>
<body id="body"  class="mar_bot">
	<div id="show">
		<div id="cont_addsite">
			<div class="log_top"><p class="width_9"><%=prop.get("unit")%>名称</p><p class="width_9"><%=prop.get("team")%>名称</p><p class="width_9"><%=prop.get("keybox")%>名称</p><p>使用人员</p><p>审批人员</p><p class="width_16">钥匙名称</p><p>操作方式</p><p>申请时间</p><p>取钥匙时间</p><p>还钥匙时间</p><p>卡号</p></div>
		</div>
	</div>
	<div id="log_foot" class="creatTable" onclick="creatTabel()">
		<p class="table_p">生成报表</p>
	</div>
</body>
</html>