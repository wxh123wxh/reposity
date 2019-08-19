<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>防区状态</title>
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
window.onload=function(){
	var id = getParam("id");
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post", 
		data:{"id":id},
		url:"/weixin/servlet/findAllSector.action", 
		success:function(data){ 
			if(jQuery.isEmptyObject(data)){
			}else {
				$.each(data,function(){
					if(this.arming_status==1&&this.online==0){
						$("#body").append("<div id=\"device_sectorstate\"><div id=\"changeColor\"><div id=\"device_info\" ><div><p>编号："+this.sec_id+"</p><p>名称："+this.name+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but1\">掉线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but2\">撤防</button></p><p onclick=\"getInfo('"+this.id+"')\"><img src=\"../image/info.png\"><button id=\"but3\">详情</button></p></div></div></div>");
					}else if(this.arming_status==1&&this.online==1){
						$("#body").append("<div id=\"device_sectorstate\"><div id=\"changeColor\"><div id=\"device_info\" ><div><p>编号："+this.sec_id+"</p><p>名称："+this.name+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but1\">在线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but2\">撤防</button></p><p onclick=\"getInfo('"+this.id+"')\"><img src=\"../image/info.png\"><button id=\"but3\">详情</button></p></div></div></div>");
					}else if(this.arming_status==0&&this.online==0){
						$("#body").append("<div id=\"device_sectorstate\"><div id=\"changeColor\"><div id=\"device_info\" ><div><p>编号："+this.sec_id+"</p><p>名称："+this.name+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but1\">掉线</button></p><p><img src=\"../image/arming.png\"><button id=\"but2\">布防</button></p><p onclick=\"getInfo('"+this.id+"')\"><img src=\"../image/info.png\"><button id=\"but3\">详情</button></p></div></div></div>");
					}else if(this.arming_status==0&&this.online==1){
						$("#body").append("<div id=\"device_sectorstate\"><div id=\"changeColor\"><div id=\"device_info\" ><div><p>编号："+this.sec_id+"</p><p>名称："+this.name+"</p></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but1\">在线</button></p><p><img src=\"../image/arming.png\"><button id=\"but2\">布防</button></p><p onclick=\"getInfo('"+this.id+"')\"><img src=\"../image/info.png\"><button id=\"but3\">详情</button></p></div></div></div>");
					}
			    })
			}
		 }  
	});
}

$(function() {
	pushHistory();
	window.addEventListener('popstate', function(e) {
	    //自定义操作
	    var maxc_id = getParam("zid");//检查子页面有没有返回maxc_id
		window.location.href = "/weixin/log/sub_state.jsp?openId="+getParam("openId")+"&id="+maxc_id+"&number="+Math.random();
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
//跳转到详情页面
function getInfo(id){
	var openId = getParam("openId");
	var zid = getParam("id");
	var maxc_id = getParam("zid");
	var url="/weixin/log/sector_deviceInfo.jsp?openId="+openId+"&id="+id+"&zid="+zid+"&maxc_id="+maxc_id;
	window.location=url;
}
</script>
</head>
<body id="body">
</body>
</html>