<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>可操作的主设备列表</title>
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
	var openId = getParam("openId");
	var glid = getParam("glid");
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post",
		data:{"openId":openId,"glid":glid,"start":start},
		url:"/weixin/servlet/findFollowerDevice.action", 
		success:function(data){ 
			$.each(data,function(){
				$("#body").append("<div id=\"device\"><div id=\"device_info\" ><div><p>"+this.id+"</p><p>名称："+this.name+"</p></div><div id=\"follower\" onclick=\"deleteDeviceForFollower('"+this.id+"')\"><img src=\"../image/delete.png\"><p class=\"red\">删除</p></div></div></div>");
			})
			$("#body").append("<div id=\"addDevice\" onclick=\"addDeviceForFollower()\"><p><img src=\"../image/add.png\"><button>添加主设备</button></p></div>");
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
        	var openId = getParam("openId");
        	var glid = getParam("glid");
        	$.ajax({  
        		dataType:"json",    //数据类型为json格式
        		type:"post",
        		data:{"openId":openId,"glid":glid,"start":start},
        		url:"/weixin/servlet/findFollowerDevice.action", 
        		success:function(data){ 
        			if(jQuery.isEmptyObject(data)){
        			}else{
        				$.each(data,function(){
        					$("#body").append("<div id=\"device\"><div id=\"device_info\" ><div><p>"+this.id+"</p><p>名称："+this.name+"</p></div><div id=\"follower\" onclick=\"deleteDeviceForFollower('"+this.id+"')\"><img src=\"../image/delete.png\"><p class=\"red\">删除</p></div></div></div>");
        				})
        			}
        		 }  
        	});
  		 }
	});
});
/**
 * 删除操作者可操作的设备     id 设备id
 */
function deleteDeviceForFollower(id){
	var openId = getParam("openId");
	var glid = getParam("glid");
	var bln = confirm("确定删除用户对此主设备的管理吗?");   
	if(bln&&flag){
		flag = false
		$.ajax({  
			dataType:"json",    //数据类型为json格式
			type:"post", 
			data:{"id":id,"glid":glid,"openId":openId},
			url:"/weixin/servlet/deleteDeviceForFollower.action",
			success:function(data){ 
				if(data.date!=null&&data.date!=""){
					alert(data.date)
				}else{
					window.location.href = data.url;
				}
			 } ,
			complete:function(){
				flag = true;
			} 
		});
	}
}
$(function() {
	pushHistory();
	window.addEventListener('popstate', function(e) {
	    //自定义操作
	    var maxc_id = getParam("zid");//检查子页面有没有返回maxc_id
		window.location.href = "/weixin/user/user.jsp?openId="+getParam("glid")+"&number="+Math.random();
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
function addDeviceForFollower(){
	var openId = getParam("openId");
	var glid = getParam("glid");
	var url = "/weixin/user/addUser.jsp?glid="+glid+"&gzid="+openId+"&back=back";
	window.location=url;
}
</script>
</head>
<body id="body">

</body>
</html>