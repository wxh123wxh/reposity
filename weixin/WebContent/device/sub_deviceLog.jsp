<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport"
	content="width=device-width, initial-scale=1 user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>操作 日志</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/weui.min.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<style type="text/css">
</style>
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
var start = 0;
window.onload=function(){
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post",
		data:{"id":getParam("id"),"sub_id":getParam("sub_id"),"start":start},
		url:"/weixin/servlet/getsub_RZ.action", 
		success:function(data){ 
			$.each(data,function(){
				$("#body").append("<div id='devicelog'><div id='log_time'><p class='huise'>操作者 : "
						+this.nickname+"</p></div><div id='log_content'><p class='huise'>"
						+this.time+"</p></div><div id='log_content'><p class='white'>"+this.content+"</p></div></div>");
			})
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
        	$.ajax({  
        		dataType:"json",    //数据类型为json格式
        		type:"post",
        		data:{"id":getParam("id"),"sub_id":getParam("sub_id"),"start":start},
        		url:"/weixin/servlet/getsub_RZ.action", 
        		success:function(data){ 
        			if(jQuery.isEmptyObject(data)){
        			}else{
        				$.each(data,function(){
        					$("#body").append("<div id='devicelog'><div id='log_time'><p class='huise'>操作者 : "
        							+this.nickname+"</p></div><div id='log_content'><p class='huise'>"
        							+this.time+"</p></div><div id='log_content'><p class='white'>"+this.content+"</p></div></div>");
        				})
        			}
        		 }  
        	});
   			
  		 }
	});
});
</script>
</head>
<body id="body">
</body>
</html>