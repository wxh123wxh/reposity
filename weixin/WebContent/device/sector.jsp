<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width, initial-scale=1 user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>防区管理</title>
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
						$("#body").append("<div id=\"device\"><div id=\"changeColor\"><div id=\"device_info\" ><div><p>编号："+this.sec_id+"</p><p>名称："+this.name+"</p></div><div id=\"device_change\"><img src=\"../image/edit.png\" onclick=\"updateSector('"+this.id+"','"+this.name+"')\"></div></div><div id=\"device_operate\"><p><img src=\"../image/offline2.png\"><button id=\"but1\">掉线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but2\">撤防</button></p><p onclick=\"getInfo('"+this.id+"')\"><img src=\"../image/info.png\"><button id=\"but3\">详情</button></p><p onclick=\"deleteSector('"+this.id+"')\"><img src=\"../image/delete.png\"><button id=\"but4\">删除</button><div></p></div></div></div>");
					}else if(this.arming_status==1&&this.online==1){
						$("#body").append("<div id=\"device\"><div id=\"changeColor\"><div id=\"device_info\" ><div><p>编号："+this.sec_id+"</p><p>名称："+this.name+"</p></div><div id=\"device_change\"><img src=\"../image/edit.png\" onclick=\"updateSector('"+this.id+"','"+this.name+"')\"></div></div><div id=\"device_operate\"><p><img src=\"../image/online2.png\"><button id=\"but1\">在线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but2\">撤防</button></p><p onclick=\"getInfo('"+this.id+"')\"><img src=\"../image/info.png\"><button id=\"but3\">详情</button></p><p onclick=\"deleteSector('"+this.id+"')\"><img src=\"../image/delete.png\"><button id=\"but4\">删除</button></p></div></div></div>");
					}else if(this.arming_status==0&&this.online==0){
						$("#body").append("<div id=\"device\"><div id=\"changeColor\"><div id=\"device_info\" ><div><p>编号："+this.sec_id+"</p><p>名称："+this.name+"</p></div><div id=\"device_change\"><img src=\"../image/edit.png\" onclick=\"updateSector('"+this.id+"','"+this.name+"')\"></div></div><div id=\"device_operate\"><p><img src=\"../image/offline2.png\"><button id=\"but1\">掉线</button></p><p><img src=\"../image/arming.png\"><button id=\"but2\">布防</button></p><p onclick=\"getInfo('"+this.id+"')\"><img src=\"../image/info.png\"><button id=\"but3\">详情</button></p><p onclick=\"deleteSector('"+this.id+"')\"><img src=\"../image/delete.png\"><button id=\"but4\">删除</button></p></div></div></div>");
					}else if(this.arming_status==0&&this.online==1){
						$("#body").append("<div id=\"device\"><div id=\"changeColor\"><div id=\"device_info\" ><div><p>编号："+this.sec_id+"</p><p>名称："+this.name+"</p></div><div id=\"device_change\"><img src=\"../image/edit.png\" onclick=\"updateSector('"+this.id+"','"+this.name+"')\"></div></div><div id=\"device_operate\"><p><img src=\"../image/online2.png\"><button id=\"but1\">在线</button></p><p><img src=\"../image/arming.png\"><button id=\"but2\">布防</button></p><p onclick=\"getInfo('"+this.id+"')\"><img src=\"../image/info.png\"><button id=\"but3\">详情</button></p><p onclick=\"deleteSector('"+this.id+"')\"><img src=\"../image/delete.png\"><button id=\"but4\">删除</button></p></div></div></div>");
					}
			    })
			}
			$("#body").append("<div id=\"addDevice\" onclick=\"addSector()\"><p><img src=\"../image/add.png\"><button>添加防区</button></p></div>");
		 }  
	});
}

$(function() {
	pushHistory();
	window.addEventListener('popstate', function(e) {
	    //自定义操作
	    var maxc_id = getParam("zid");//检查子页面有没有返回maxc_id
		window.location.href = "/weixin/device/SubDevice.jsp?openId="+getParam("openId")+"&id="+maxc_id+"&number="+Math.random();
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
var flag = true;
function deleteSector(id){
	var openId = getParam("openId");
	var bln = confirm("确定删除此防区吗?"); 
	if(bln&&flag){
		flag = false;
		$.ajax({  
			dataType:"json",    //数据类型为json格式
			type:"post", 
			data:{"id":id,"openId":openId},
			url:"/weixin/servlet/deleteSector.action",
			success:function(data){ 
				if(data.date!=null&&data.date!=""){
					alert(data.date)
				}else{
					window.location.href = data.url+"&id="+getParam("id")+"&zid="+getParam("zid");
				}
			},
			complete:function(){
				flag = true;
			}
		});
	}
}
//跳转到操作记录界面
function getCZ(id,name){
	var zid = getParam("id");
	var maxc_id = getParam("zid");
	var openId = getParam("openId");
	var url = "/weixin/device/sectorOperate.jsp?id="+id+"&openId="+openId+"&zid="+zid+"&oldname="+name+"&maxc_id="+maxc_id;
	window.location=url;
}
//跳转到详情界面
function getInfo(id){
	var openId = getParam("openId");
	var zid = getParam("id");
	var maxc_id = getParam("zid");
	var url="/weixin/device/sector_deviceInfo.jsp?openId="+openId+"&id="+id+"&zid="+zid+"&maxc_id="+maxc_id;
	window.location=url;
}
function addSector(){
	var maxc_id = getParam("zid");
	var openId = getParam("openId");
	var zid = getParam("id");
	var url = "/weixin/device/addSector.jsp?openId="+openId+"&zId="+zid+"&maxc_id="+maxc_id;
	window.location=url;
}
function updateSector(id,name){
	var maxc_id = getParam("zid");
	var openId = getParam("openId");
	var zid = getParam("id");
	var url = "/weixin/device/updateSector.jsp?id="+id+"&openId="+openId+"&zId="+zid+"&oldname="+name+"&maxc_id="+maxc_id;
	window.location=url;
}
</script>
</head>
<body id="body">

</body>
</html>