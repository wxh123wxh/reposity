<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width, initial-scale=1 user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主设备管理</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/weui.min.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript">

var start = 0; //查询的开始位置
/**
 * 页面加载前获取设备列表
 */
var flag = true;
window.onload=function(){
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post",
		data:{"start":start},
		url:"/weixin/servlet/findAllDevices.action", 
		success:function(data){ 
			if(data.data!=null){
				
			}else{
				$.each(data,function(){
					$("#body").append("<div><p class=\"display_inline_block\">名称："+this.id+"</p><p class=\"display_inline_block\"><input id=\""+this.id+"\" value=\""+this.overtime+"\"/></p><button onclick=\"change('"+this.id+"')\">修改</button></div>");
				})
			}
		}
	});
}
/**
 * 页面下拉时查询更多的设备记录
 */
$(function(){
	$(window).scroll(function() {
   		var scrollTop = $(this).scrollTop(),scrollHeight = $(document).height(),windowHeight = $(this).height();
    	var positionValue = (scrollTop + windowHeight) - scrollHeight;
   		if (positionValue == 0) {
        	//do something
        	start = start + 1;
        	$.ajax({  
        		dataType:"json",    //数据类型为json格式
        		type:"post",
        		data:{"start":start},
        		url:"/weixin/servlet/findAllDevices.action", 
        		success:function(data){ 
        			if(data.data!=null){
        				
        			}else{
        				$.each(data,function(){
        					$("#body").append("<div><p class=\"display_inline_block\">名称："+this.id+"</p><p  class=\"display_inline_block\"><input id=\""+this.id+"\" value=\""+this.overtime+"\"/></p><button onclick=\"change('"+this.id+"')\">修改</button></div>");
        				})
        			}
        		} 
        	});
  		 }
	});
});
function change(id){
	if(flag){
		var overtime = $("#"+id).val();
		var reg = new RegExp("^[0-9]+$");
		if(!reg.test(overtime)&&(100<overtime&&overtime<1)){
			alert("请输入0——100的数字");
		 	return false;
		}
		flag = false;
		$.ajax({  
			dataType:"json",    //数据类型为json格式
			type:"post", 
			data:{"id":id,"overtime":overtime},
			url:"/weixin/servlet/chageOverTime.action",
			success:function(data){ 
				if(data.date!=null&&data.date!=""){
					alert(data.date)
				}
				window.location="/weixin/updata.jsp?"
			},
			complete:function(){
				flag = true;
			}
		});
	}
}
</script>
</head>
<body id="body">
	
</body>
</html>