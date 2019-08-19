<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>子设备状态</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/weui.min.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
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
					if(this.arming_status==1&&this.online==0){
						$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p><p>密码："+this.password+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"getInfo('"+this.id+"','"+this.sub_id+"')\"><img src=\"../image/info.png\"><button id=\"but2\">详情</button></p><p onclick=\"findAllSector('"+this.id+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p></div></div>");
					}else if(this.arming_status==1&&this.online==1){
						$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p><p>密码："+this.password+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"getInfo('"+this.id+"','"+this.sub_id+"')\"><img src=\"../image/info.png\"><button id=\"but2\">详情</button></p><p onclick=\"findAllSector('"+this.id+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p></div></div>");
					}else if(this.arming_status==0&&this.online==0){
						$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p><p>密码："+this.password+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"getInfo('"+this.id+"','"+this.sub_id+"')\"><img src=\"../image/info.png\"><button id=\"but2\">详情</button></p><p onclick=\"findAllSector('"+this.id+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p></div></div>");
					}else if(this.arming_status==0&&this.online==1){
						$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p><p>密码："+this.password+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"getInfo('"+this.id+"','"+this.sub_id+"')\"><img src=\"../image/info.png\"><button id=\"but2\">详情</button></p><p onclick=\"findAllSector('"+this.id+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p></div></div>");
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
        					if(this.arming_status==1&&this.online==0){
        						$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p><p>密码："+this.password+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"getInfo('"+this.id+"','"+this.sub_id+"')\"><img src=\"../image/info.png\"><button id=\"but2\">详情</button></p><p onclick=\"findAllSector('"+this.id+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p></div></div>");
        					}else if(this.arming_status==1&&this.online==1){
        						$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p><p>密码："+this.password+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"getInfo('"+this.id+"','"+this.sub_id+"')\"><img src=\"../image/info.png\"><button id=\"but2\">详情</button></p><p onclick=\"findAllSector('"+this.id+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p></div></div>");
        					}else if(this.arming_status==0&&this.online==0){
        						$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p><p>密码："+this.password+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"getInfo('"+this.id+"','"+this.sub_id+"')\"><img src=\"../image/info.png\"><button id=\"but2\">详情</button></p><p onclick=\"findAllSector('"+this.id+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p></div></div>");
        					}else if(this.arming_status==0&&this.online==1){
        						$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p><p>密码："+this.password+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"getInfo('"+this.id+"','"+this.sub_id+"')\"><img src=\"../image/info.png\"><button id=\"but2\">详情</button></p><p onclick=\"findAllSector('"+this.id+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p></div></div>");
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
		 window.location.href = "/weixin/log/device_state.jsp?openId="+getParam("openId")+"&number="+Math.random();
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
function getInfo(id,sub_id){
	var zid = getParam("id");
	var openId = getParam("openId");
	var url="/weixin/log/sub_deviceInfo.jsp?openId="+openId+"&zid="+zid+"&sub_id="+sub_id+"&id="+id;
	window.location=url;
}
function findAllSector(id){
	var openId = getParam("openId");
	var zid = getParam("id");
	var url = "/weixin/log/state_sector.jsp?openId="+openId+"&id="+id+"&zid="+zid;
	window.location=url;
}
</script>
</head>
<body id="body">
</body>
</html>