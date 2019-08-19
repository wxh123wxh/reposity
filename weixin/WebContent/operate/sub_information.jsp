<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>子设备列表</title>
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
	var id = getParam("id");
	var openId = getParam("openId");
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post", 
		data:{"id":id,"openId":openId,"start":start},
		url:"/weixin/servlet/findSubDevice.action", 
		success:function(data){ 
			if(jQuery.isEmptyObject(data)){
			}else {
				$.each(data,function(){
					if(this.arming_status==1){
						$("#body").append("<div id=\"sub_state\"><div id=\"device_info\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"device_operate\"><p class=\"red\"><img src=\"../image/disarming.png\"><button class=\"height_blue\">撤防</button></p><p class=\"blue\" onclick=\"setMessage('"+this.id+"')\"><img src=\"../image/set.png\"><button class=\"sblue\">设置</button></p></div></div>");
					}else if(this.arming_status==0){
						$("#body").append("<div id=\"sub_state\"><div id=\"device_info\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"device_operate\"><p class=\"red\"><img src=\"../image/arming.png\"><button class=\"height_blue\">布防</button></p><p class=\"blue\" onclick=\"setMessage('"+this.id+"')\"><img src=\"../image/set.png\"><button class=\"sblue\">设置</button></p></div></div>");
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
   		if (positionValue == 0) {
        	//do something
        	start = start + 1;
        	var id = getParam("id");
			var openId = getParam("openId");
        	$.ajax({  
        		dataType:"json",    //数据类型为json格式
        		type:"post", 
        		data:{"id":id,"openId":openId,"start":start},
        		url:"/weixin/servlet/findSubDevice.action", 
        		success:function(data){ 
        			if(jQuery.isEmptyObject(data)){
        			}else {
        				$.each(data,function(){
        					if(this.arming_status==1){
        						$("#body").append("<div id=\"sub_state\"><div id=\"device_info\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"device_operate\"><p class=\"red\"><img src=\"../image/disarming.png\"><button class=\"height_blue\">撤防</button></p><p class=\"blue\" onclick=\"setMessage('"+this.id+"')\"><img src=\"../image/set.png\"><button class=\"sblue\">设置</button></p></div></div>");
        					}else if(this.arming_status==0){
        						$("#body").append("<div id=\"sub_state\"><div id=\"device_info\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"device_operate\"><p class=\"red\"><img src=\"../image/arming.png\"><button class=\"height_blue\">布防</button></p><p class=\"blue\" onclick=\"setMessage('"+this.id+"')\"><img src=\"../image/set.png\"><button class=\"sblue\">设置</button></p></div></div>");
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
	    //自定义操作//检查子页面有没有返回maxc_id
		 window.location.href = "/weixin/operate/device_information.jsp?openId="+getParam("openId")+"&number="+Math.random();
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
/**
 * 跳转到设置消息接收设置页面       id 子设备id 表示对那个子设备的消息接收设置
 */
function setMessage(id){
	var openId = getParam("openId");
	var zId = getParam("id");
	var url = "/weixin/operate/information.jsp?openId="+openId+"&sub_id="+id+"&zId="+zId;
	window.location=url;
}

</script>
</head>
<body id="body">
</body>
</html>