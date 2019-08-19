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
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<title>authCard</title>
<link rel="stylesheet" href="css/layui.css">
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
var style = "<%=request.getSession().getAttribute("style")%>"
var arr = new Array();

window.onload=function(){
	document.getElementById("cont_addsite").style.display='block';
	$("#top_bnt",window.parent.document).text(getParam("unitManager_name"));
	$.ajax({
		dataType:"json",
		type:"post", 
		data:{"unit_id":getParam("unit_id"),"start":start,"unit_manager_id":getParam("unit_manager_id")},
		url:"/ES/ESServlet/findTeamByAuth.action",
		success:function(data){
			if(jQuery.isEmptyObject(data)){
				
			}else{
				$.each(data,function(){
					if(this.flage==true){
						$("#body").append("<div class=\"div_p_3\"><p class=\"p_3_1\">"+this.iid+"</p><p class=\"p_3_2\">"+this.name+"</p><p class=\"p_3_3\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/dui.png\" iid=\""+this.iid+"\"></p></div>");
						arr.push(this.iid+"")
					}else{
						$("#body").append("<div class=\"div_p_3\"><p class=\"p_3_1\">"+this.iid+"</p><p class=\"p_3_2\">"+this.name+"</p><p class=\"p_3_3\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/discheck.png\" iid=\""+this.iid+"\"></p></div>");
					}
				})
			}
		}
	});
}
$(function(){
	$(window).scroll(function() {
   		var scrollTop = $(this).scrollTop(),scrollHeight = $(document).height(),windowHeight = $(this).height();
    	var positionValue = (scrollTop + windowHeight) - scrollHeight;
    	
    	if (positionValue >= 0) {
    		$.ajax({
    			dataType:"json",
    			type:"post", 
    			data:{"unit_id":getParam("unit_id"),"start":++start,"unit_manager_id":getParam("unit_manager_id")},
    			url:"/ES/ESServlet/findTeamByAuth.action",
    			success:function(data){
    				if(jQuery.isEmptyObject(data)){
    					
    				}else{
    					$.each(data,function(){
    						if(this.flage==true){
    							$("#body").append("<div class=\"div_p_3\"><p class=\"p_3_1\">"+this.iid+"</p><p class=\"p_3_2\">"+this.name+"</p><p class=\"p_3_3 p_5_img\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/dui.png\" iid=\""+this.iid+"\"></p></div>");
    							arr.push(this.iid)
    						}else{
    							$("#body").append("<div class=\"div_p_3\"><p class=\"p_3_1\">"+this.iid+"</p><p class=\"p_3_2\">"+this.name+"</p><p class=\"p_3_3 p_5_img\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/discheck.png\" iid=\""+this.iid+"\"></p></div>");
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
		//自定义操作
		window.location.href = "/ES/unitManager.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
check = function(iid,event){
	if($(event).children().eq(0).attr("src")=="./image/discheck.png"){
		$(event).children().eq(0).attr("src","./image/dui.png")
		arr.push(iid)
	}else {
		$(event).children().eq(0).attr("src","./image/discheck.png")
		var index =arr.indexOf(iid)
		arr.splice(index,1);
	}
}
disCheckAll = function(){
	arr.length = 0
	var len = $("img").length;
	
	for(var i = 0; i < len; i++){
		$($("img")[i]).attr("src","./image/discheck.png")
	}
}
confirmCheck = function(){
	if(arr.length==0){
		arr.push(-1)
	}
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post",
		traditional: true,
		data:{"unit_manager_id":getParam("unit_manager_id"),"iids":arr,"unit_id":getParam("unit_id")},
		url:"/ES/ESServlet/addUnitManagerAuth.action",
		success:function(data){ 
			alert(data.data)
			if(data.data=="设置成功"){
				window.location="/ES/unitManagerAuth.jsp?unit_manager_id="+getParam("unit_manager_id")+"&unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name")+"&unitManager_name="+getParam("unitManager_name");
			}
		} 
	});
}
checkAll = function(){
	arr.length = 0
	var len = $("img").length;
	
	for(var i = 0; i < len; i++){
		$($("img")[i]).attr("src","./image/dui.png")
		arr.push($($("img")[i]).attr("iid"))
	}
}
</script>
</head>
<body id="body" class="mar_bot">
	<div id="show">
		<div id="cont_addsite">
			<div class="site_search_add_user"><button class="search_user" onclick="disCheckAll()">全不选</button><button class="add_user" onclick="confirmCheck()">确定</button><button class="delete_user" onclick="checkAll()">全选</button></div>
			<div class="div_p_3 margin_4"><p class="p_3_1"><%=prop.get("team")%>编号</p><p class="p_3_2"><%=prop.get("team")%>名称</p><p class="p_3_3">开锁权限</p></div>
		</div>
	</div>
	
</body>
</html>