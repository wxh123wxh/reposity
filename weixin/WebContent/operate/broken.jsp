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
<title>撤防操作</title>
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
					if(this.one_broken==1){
						if(this.arming_status==1&&this.online==0){
							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/followed.png\" id=\""+this.id+"\"><button class=\"red\">取消</button></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
						}else if(this.arming_status==1&&this.online==1){
							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/followed.png\" id=\""+this.id+"\"><button class=\"red\">取消</button></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
						}else if(this.arming_status==0&&this.online==0){
							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/followed.png\" id=\""+this.id+"\"><button class=\"red\">取消</button></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
						}else if(this.arming_status==0&&this.online==1){
							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/followed.png\" id=\""+this.id+"\"><button class=\"red\">取消</button></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
						}
					}else{
						if(this.arming_status==1&&this.online==0){
							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/discheck.png\" id=\""+this.id+"\"><button class=\"red\">添加</button></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
						}else if(this.arming_status==1&&this.online==1){
							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/discheck.png\" id=\""+this.id+"\"><button class=\"red\">添加</button></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
						}else if(this.arming_status==0&&this.online==0){
							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/discheck.png\" id=\""+this.id+"\"><button class=\"red\">添加</button></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
						}else if(this.arming_status==0&&this.online==1){
							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/discheck.png\" id=\""+this.id+"\"><button class=\"red\">添加</button></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
						}
					}
					$("#body").append("<div id=\"addDevice\" onclick=\"ShowDiv('MyDiv','fade')\"><p><img src=\"../image/add.png\"><button>一键撤防</button></p></div>");
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
        					if(this.one_broken==1){
        						if(this.arming_status==1&&this.online==0){
        							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/followed.png\" id=\""+this.id+"\"><button class=\"red\">取消</button></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
        						}else if(this.arming_status==1&&this.online==1){
        							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/followed.png\" id=\""+this.id+"\"><button class=\"red\">取消</button></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
        						}else if(this.arming_status==0&&this.online==0){
        							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/followed.png\" id=\""+this.id+"\"><button class=\"red\">取消</button></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
        						}else if(this.arming_status==0&&this.online==1){
        							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/followed.png\" id=\""+this.id+"\"><button class=\"red\">取消</button></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
        						}
        					}else{
        						if(this.arming_status==1&&this.online==0){
        							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/discheck.png\" id=\""+this.id+"\"><button class=\"red\">添加</button></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
        						}else if(this.arming_status==1&&this.online==1){
        							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/discheck.png\" id=\""+this.id+"\"><button class=\"red\">添加</button></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/disarming.png\"><button id=\"but1\">撤防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
        						}else if(this.arming_status==0&&this.online==0){
        							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/discheck.png\" id=\""+this.id+"\"><button class=\"red\">添加</button></div></div><div id=\"device_operate\"><p><img src=\"../image/cuo.png\"><button id=\"but4\">掉线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
        						}else if(this.arming_status==0&&this.online==1){
        							$("#body").append("<div id=\"device_substate\"><div id=\"device_info\" ><div id=\"sub\"><p>编号："+this.sub_id+"</p><p>名称："+this.name+"</p></div><div id=\"check\" onclick=\"addSubOneBroken('"+this.id+"')\"><img src=\"../image/discheck.png\" id=\""+this.id+"\"><button class=\"red\">添加</button></div></div><div id=\"device_operate\"><p><img src=\"../image/dui.png\"><button id=\"but4\">在线</button></p><p><img src=\"../image/arming.png\"><button id=\"but1\">布防</button></p><p onclick=\"findAllSector('"+this.id+"','"+this.sub_id+"','"+this.fPassword+"')\"><img src=\"../image/sub.png\"><button id=\"but3\">防区</button></p><p onclick=\"ShowDiv2('"+this.id+"','"+this.sub_id+"','"+this.MAXC_id+"','"+this.fPassword+"','MyDiv','fade')\"><img src=\"../image/operate2.png\"><button id=\"but2\">撤防</button></p></div></div>");
        						}
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
		 window.location.href = "/weixin/operate/device_broken.jsp?openId="+getParam("openId")+"&number="+Math.random();
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
function disarming(){
	var openId = getParam("openId");
	var password = $("#password").val();
	var onePassword = $("#onePass").val();
	var arr = $("#method").val().split("/");
	if(password==""){
		alert("密码不能为空！")
	}else{
		if(arr[3]!=password){
			alert("密码错误!");
		}else{
			if(flag){
				flag = false;
				var remem = "not";
				if($("#cont_img").attr("src")!=img){
					if($("#cont_img").attr("src")=="../image/check.png"){
						remem = "true";
					}else if($("#cont_img").attr("src")=="../image/discheck.png"){
						remem = "false";
					}
				}
				$.ajax({  
					dataType:"json", 
					type:"post", 
					data:{"openId":openId,"sub_id":arr[1],"MAXC_id":arr[2],"password":password,"remember":remem,"method":onePassword,"id":arr[0]},
					url:"/weixin/servlet/disarming.action",
					complete:function(){
						flag = true;
					}
				});
				alert("已发送撤防命令！")
			}else{
				alert("不要频繁操作")
			}
		}
	}
}

function getPassword(id,fPassword){
	if(flag){
		flag = false;
		var openId = getParam("openId");
		var boo = null;
		$.ajax({  
			dataType:"json",    //数据类型为json格式
			async: false,
			type:"post", 
			data:{"openId":openId,"id":id},
			url:"/weixin/servlet/getPassword.action",
			success:function(data){
				if(data.remember!=null){
					if(data.remember=="true"){
						$("#password").val(fPassword);
						$("#cont_img").attr("src","../image/check.png")
					}else {
						$("#password").val("");
						$("#cont_img").attr("src","../image/discheck.png")
					}
					$("#onePass").val(fPassword);
				}else{
					$("#password").val("");
					$("#onePass").val("");
					$("#cont_img").attr("src","../image/discheck.png")
				}
			} ,
			complete:function(){
				flag = true;
			}
		});
	}else{
		alert("不要频繁操作")
	}
}

function findAllSector(id,sub_id,fPassword){
	var openId = getParam("openId");
	var zid = getParam("id");
	var url = "/weixin/operate/broken_sector.jsp?openId="+openId+"&id="+id+"&zid="+zid+"&sub_id="+sub_id+"&password="+fPassword;
	window.location=url;
}

function addSubOneBroken(id){
	if(flag){
		flag = false;
		var maxc_id = getParam("id");
		var name = getParam("name");
		var openId = getParam("openId");
		var one_broken;
		if($("#"+id).attr("src")=="../image/discheck.png"){
			$("#"+id).attr("src", "../image/followed.png");
			$("#"+id).parent().children("button:last-child").html("取消")
			one_broken = 1;
		}else if($("#"+id).attr("src")=="../image/followed.png"){
			$("#"+id).attr("src", "../image/discheck.png");
			$("#"+id).parent().children("button:last-child").html("添加")
			one_broken = 0;
		}
		$.ajax({  
			dataType:"json", 
			type:"post", 
			data:{"id":id,"one_broken":one_broken,"openId":openId,"maxc_id":maxc_id,"name":name},
			url:"/weixin/servlet/addSubOneBroken.action",
			complete:function(){
				flag = true;
			}
		});
	}else{
		alert("不要频繁操作")
	}
}
function oneSubBroken(){
	var openId = getParam("openId");
	var id = getParam("id");
	var name = getParam("name");
	var password = $("#password").val();
	var onePassword = $("#onePass").val();
	
	if(password==""){
		alert("密码不能为空！")
	}else{
		if(onePassword!=""&&onePassword!=password){
			alert("密码错误!");
		}else{
			if(flag){
				flag = false;
				var remem = "not";
				if($("#cont_img").attr("src")!=img){
					if($("#cont_img").attr("src")=="../image/check.png"){
						remem = "true";
					}else if($("#cont_img").attr("src")=="../image/discheck.png"){
						remem = "false";
					}
				}
				$.ajax({  
					dataType:"json", 
					type:"post", 
					data:{"openId":openId,"maxc_id":id,"name":name,"remember":remem,"onePassword":password,"method":onePassword},
					url:"/weixin/servlet/oneSubBroken.action",
					complete:function(){
						flag = true;
					}
				});
				alert("已发送撤防命令！")
			}else{
				alert("不要频繁操作")
			}
		}
	}
}

function getPasswordAll(){
	if(flag){
		flag = false;
		var openId = getParam("openId");
		$.ajax({  
			dataType:"json",    //数据类型为json格式
			async: false,
			type:"post", 
			data:{"openId":openId},
			url:"/weixin/servlet/getPasswordAll.action",
			success:function(data){ 
				if(data.remember!=null){
					if(data.remember=="true"){
						$("#password").val(data.password);
						$("#cont_img").attr("src","../image/check.png")
					}else {
						$("#password").val("");
						$("#cont_img").attr("src","../image/discheck.png")
					}
					$("#onePass").val(data.password);
				}else{
					$("#password").val("");
					$("#onePass").val("");
					$("#cont_img").attr("src","../image/discheck.png")
				}
			},
			complete:function(){
				flag = true;
			}
		});
	}else{
		alert("不要频繁操作")
	}
}
var img = null;
function ShowDiv2(id,sub_id,MAXC_id,fPassword,show_div,bg_div){
	$("#method").val(id+"/"+sub_id+"/"+MAXC_id+"/"+fPassword);
	getPassword(id,fPassword)
	img = $("#cont_img").attr("src");
	document.getElementById(show_div).style.display='block';
	document.getElementById(bg_div).style.display='block' ;
	var bgdiv = document.getElementById(bg_div);
	bgdiv.style.width = document.body.scrollWidth; 
	// bgdiv.style.height = $(document).height();
	$("#"+bg_div).height($(document).height());
};
function ShowDiv(show_div,bg_div){
	$("#method").val("ShowDiv");
	getPasswordAll()
	img = $("#cont_img").attr("src");
	document.getElementById(show_div).style.display='block';
	document.getElementById(bg_div).style.display='block' ;
	var bgdiv = document.getElementById(bg_div);
	bgdiv.style.width = document.body.scrollWidth; 
	// bgdiv.style.height = $(document).height();
	$("#"+bg_div).height($(document).height());
};
	//关闭弹出层
function CloseDiv1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
}
function CloseDiv2(show_div,bg_div){
	document.getElementById(show_div).style.display='none';
	document.getElementById(bg_div).style.display='none';
	
	if($("#method").val()=="ShowDiv"){
		oneSubBroken();
	}else{
		disarming();
	}
}
function check(){
	var a = $("#cont_img").attr("src");
	
	if(a=="../image/check.png"){
		$("#cont_img").attr("src","../image/discheck.png")
	}else{
		$("#cont_img").attr("src","../image/check.png")
	}
}
</script>
</head>
<body id="body">
	<input type="hidden" id="method">
	<input type="hidden" id="onePass">
	<!--弹出层时背景层DIV-->
	<div id="fade" class="black_overlay"></div>
	<div id="MyDiv" class="white_content">
		<div>
			<P id="p1">请输入密码！</P>
			<div id="cont">
				<input type="password" id="password" class="left_float" />
				<div class="left_float" onclick="check()" id="div1">
					<img src="../image/discheck.png" id="cont_img" />
					<button class="border_none fs">记住</button>
				</div>
			</div>
			<div class="top_border">
				<div class="left_float width center"
					onclick="CloseDiv1('MyDiv','fade')">
					<input type="button" value="取消" class="border_none">
				</div>
				<div class="center border_none" onclick="CloseDiv2('MyDiv','fade')">
					<input type="button" value="确认" class="border_none sblue">
				</div>
			</div>
		</div>
	</div>
</body>
</html>