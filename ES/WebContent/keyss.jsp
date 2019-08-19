<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

var number = 0;//序号
var start = 0;
var state = null
window.onload=function(){
	document.getElementById("cont_addsite").style.display='block';
	$("#top_bnt",window.parent.document).text(getParam("keybox_name"));
	$.ajax({
		dataType:"json",
		type:"post", 
		data:{"start":start,"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id")},
		url:"/ES/ESServlet/findKey.action",
		success:function(data){
			if(jQuery.isEmptyObject(data)){
				
			}else{
				document.getElementById("log_foot").style.display='block';
				$.each(data,function(){
					if(this.keyss_state==0){
						state = "钥匙未启动"
					}else if(this.keyss_state==1){
						state = "钥匙在位"
					}else if(this.keyss_state==2){
						state = "钥匙取走"
					}else if(this.keyss_state==3){
						state = "设备断线"
					}else{
						state = "开锁"
					}
					$("#body").append("<div class=\"div_p_9\"><p class=\"p_9_1\">"+number+"</p><p class=\"p_9_2\">"+this.iid+"</p><p class=\"p_9_3\">"+this.name+"</p><p class=\"p_9_4\">"+this.card_id+"</p><p class=\"p_9_5\">"+state+"</p><p class=\"p_9_6\" onclick=\"lock('"+this.iid+"','"+this.name+"')\"><img src=\"./image/lock.png\"><span class=\"blue\">开锁</span></p><p class=\"p_9_7 red\" onclick=\"usering('"+this.id+"','"+this.usering+"','"+this.card_id+"','"+this.keybox_id+"','"+this.iid+"','"+this.name+"')\">"+this.usering+"</p><p class=\"p_9_8\" onclick=\"findLogs('"+this.iid+"')\"><img src=\"./image/logs.png\" class=\"img2\"></p><p class=\"p_9_9\"><img src=\"./image/discheck.png\" class=\"img1\" onclick=\"check('"+this.iid+"','"+this.card_id+"','"+this.usering+"',this)\"><img src=\"./image/edit.png\" onclick=\"changeUser('"+this.iid+"','"+this.card_id+"','"+this.name+"','"+this.usering+"')\"></p></div>");
					number++;
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
    			data:{"start":++start,"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id")},
    			url:"/ES/ESServlet/findKey.action",
    			success:function(data){
    				if(jQuery.isEmptyObject(data)){
    					
    				}else{
    					$.each(data,function(){
    						if(this.keyss_state==0){
    							state = "钥匙未启动"
    						}else if(this.keyss_state==1){
    							state = "钥匙在位"
    						}else if(this.keyss_state==2){
    							state = "钥匙取走"
    						}else if(this.keyss_state==3){
    							state = "设备断线"
    						}else{
    							state = "开锁"
    						}
    						$("#body").append("<div class=\"div_p_9\"><p class=\"p_9_1\">"+number+"</p><p class=\"p_9_2\">"+this.iid+"</p><p class=\"p_9_3\">"+this.name+"</p><p class=\"p_9_4\">"+this.card_id+"</p><p class=\"p_9_5\">"+state+"</p><p class=\"p_9_6\" onclick=\"lock('"+this.iid+"','"+this.name+"')\"><img src=\"./image/lock.png\"><span class=\"blue\">开锁</span></p><p class=\"p_9_7 red\" onclick=\"usering('"+this.id+"','"+this.usering+"','"+this.card_id+"','"+this.keybox_id+"','"+this.iid+"','"+this.name+"')\">"+this.usering+"</p><p class=\"p_9_8\" onclick=\"findLogs('"+this.iid+"')\"><img src=\"./image/logs.png\" class=\"img2\"></p><p class=\"p_9_9\"><img src=\"./image/discheck.png\" class=\"img1\" onclick=\"check('"+this.iid+"','"+this.card_id+"','"+this.usering+"',this)\"><img src=\"./image/edit.png\" onclick=\"changeUser('"+this.iid+"','"+this.card_id+"','"+this.name+"','"+this.usering+"')\"></p></div>");
    						number++;
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
		window.location.href = "/ES/keybox.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&number="+Math.random();
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
var method = null
search_user = function(){
	method = "search";
	ShowDiv('MyDiv3','fade3')
}
add_user = function(){
	if(validAuth()!="no"){
		method = "add"; 
		ShowDiv('MyDiv3','fade3')
	}else{
		alert("权限不足")
	}
}
function ShowDiv(show_div,bg_div){
	document.getElementById(show_div).style.display='block';
	document.getElementById(bg_div).style.display='block' ;
	var bgdiv = document.getElementById(bg_div);
	bgdiv.style.width = document.body.scrollWidth; 
	$("#"+bg_div).height($(document).height());
};
//关闭弹出层
function CloseDiv1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
	 method = null;
}
function CloseDiv2_3(show_div,bg_div){
	var name = $("input[id='levelManager_add_name']").val().trim()
	var iid = $("input[id='levelManager_add_']").val().trim()
	var card_id = $("input[id='levelManager_add_iid']").val().trim()
	
	if(method=="add"){
		if(name!=""&&iid!=""&&card_id!=""){
			var reg = new RegExp("^[0-9]+$");
			if(!reg.test(iid)){
				alert("编号请输入数字！");
			}else {
				if(!(iid>=0&&iid<=148)){
					alert("编号在0——148之间");
				}else{
					$.ajax({
						dataType:"json",
						type:"post", 
						data:{"name":name,"keybox_id":getParam("keybox_id"),"iid":iid,"unit_id":getParam("unit_id"),"team_id":getParam("team_id"),"card_id":card_id},
						url:"/ES/ESServlet/addKey.action",
						success:function(data){
							alert(data.data)
							if(data.data=="添加成功"){
								window.location="/ES/keyss.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+getParam("keybox_name");
							}
						}
					})
				}
			}
		}else{
			alert("请输入完整的钥匙信息")
		}
	}else if(method=="search"){
		if(name==""&&iid==""&&card_id==""){
			alert("至少输入一项查询条件")
		}else{
			if(iid==""){
				iid=-1
			}
			if(card_id==""){
				card_id = null;
			}
			$.ajax({  
				type:"post", 
				dataType: "json",
				data:{"keybox_id":getParam("keybox_id"),"name":name,"iid":iid,"unit_id":getParam("unit_id"),"team_id":getParam("team_id"),"card_id":card_id},
				url:"/ES/ESServlet/findkeyByName.action",
				success:function(data){
					if(data.data!=null){
						alert(data.data)
					}else{
						ShowDiv('MyDiv2','fade2')
						if(data.keyss_state==0){
							state = "钥匙未启动"
						}else if(data.keyss_state==1){
							state = "钥匙在位"
						}else if(data.keyss_state==2){
							state = "钥匙取走"
						}else if(data.keyss_state==3){
							state = "设备断线"
						}else{
							state = "开锁"
						}
						$("#mydiv2_cont").append("<div class=\"div_p_8\" id=\"show_close\"><p class=\"p_8_1 width_8\">编号</p><p class=\"p_8_2 width_37\">钥匙名称</p><p class=\"p_8_3\">卡id</p><p class=\"p_8_4 width_8\">状态</p><p class=\"p_8_5 width_8\">开锁</p><p class=\"p_8_6 width_8\">启用/禁用</p><p class=\"p_8_7 width_8\">记录</p><p class=\"p_8_8 width_8\"><span>操作</span></p></div>")
						$("#mydiv2_cont").append("<div class=\"div_p_8_2\" id=\"show_close\"><p class=\"p_8_1 width_8\">"+data.iid+"</p><p class=\"p_8_2 width_37\">"+data.name+"</p><p class=\"p_8_3\">"+data.card_id+"</p><p class=\"p_8_4 width_8\">"+state+"</p><p class=\"padding14 width_8\" onclick=\"lock('"+data.iid+"','"+data.name+"')\"><img src=\"./image/lock.png\"><span class=\"blue\">开锁</span></p><p class=\"p_8_6 red width_8\" onclick=\"usering('"+data.id+"','"+data.usering+"','"+data.card_id+"','"+data.keybox_id+"','"+data.iid+"','"+data.name+"')\">"+data.usering+"</p><p class=\"padding14 width_8\"  onclick=\"findLogs('"+data.iid+"')\"><img src=\"./image/logs.png\" class=\"margin_14\"></p><p class=\"padding14 width_8\"><img src=\"./image/delete.png\" class=\"img12\" onclick=\"deleteUnit('"+data.iid+"','"+data.card_id+"','"+data.usering+"')\"><img src=\"./image/edit.png\" onclick=\"changeUser('"+data.iid+"','"+data.card_id+"','"+data.name+"','"+data.usering+"')\"></p></div>");
					}
				}
			});
		}
	}
	document.getElementById(show_div).style.display='none';
	document.getElementById(bg_div).style.display='none';
	method = null;
}
function CloseDiv2_1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
	 $("div[id='show_close']").remove()
	 $("div[id='show_close']").remove()
}
var arr = new Array;
var arr2 = new Array
var arr3 = new Array
check = function(iid,card_id,usering,event){
	if($(event).attr("src")=="./image/discheck.png"){
		$(event).attr("src","./image/dui.png")
		arr.push(iid)
		arr2.push(card_id)
		arr3.push(usering)
	}else {
		$(event).attr("src","./image/discheck.png")
		var index =arr.indexOf(iid)
		arr.splice(index,1);
		arr2.splice(index,1)
		arr3.splice(index,1)
	}
}
delete_user = function(){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(validAuth()!="no"){
			if(arr.length>0){
				 $.ajax({
					dataType:"json",
					type:"post", 
					data:{"iids":arr,"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id"),"card_ids":arr2,"userings":arr3},
					traditional: true,
					url:"/ES/ESServlet/deleteKeyByIds.action",
					success:function(data){
						alert(data.data)
						if(data.data=="删除成功"){
							window.location="/ES/keyss.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+getParam("keybox_name");
						}
					}
				}) 
			}else{
				alert("请先选择要删除的钥匙")
			}
		}else{
			alert("权限不足")
		}
	}
}

deleteUnit = function(iid,card_id,usering){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(validAuth()!="no"){
			arr.push(iid)
			arr2.push(card_id)
			arr3.push(usering)
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"iids":arr,"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id"),"card_ids":arr2,"userings":arr3},
				traditional: true,
				url:"/ES/ESServlet/deleteKeyByIds.action",
				success:function(data){
					alert(data.data)
					if(data.data=="删除成功"){
						window.location="/ES/keyss.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+getParam("keybox_name");
					}
				}
			}) 
			arr.length = 0
			arr2.length = 0
			arr3.length = 0
		}else{
			alert("权限不足")
		}
	}
}

var change_iid = null;
var old_name = null;
var old_card_id = null
var change_usering = null
changeUser = function(iid,card_id,name,usering){
	ShowDiv('MyDiv33','fade33')
	old_name = name;
	old_card_id = card_id
	change_id = iid
	change_usering = usering
}


function CloseDiv13(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
	 $("#change_name").html("");
	 $("#change_card").html("");
	 old_name = null;
	 old_card_id = null
	 change_iid = null
	 change_usering = null
}
function CloseDiv23(show_div,bg_div){
	var new_name = $("#change_name").val().trim();
	var new_card = $("#change_card").val().trim();

	if(validAuth()!="no"){
		if(new_name==""&&new_card==""){
			alert("最少输入一个修改信息")
		}else{
			$.ajax({  
				type:"post", 
				dataType: "json",
				data:{"old_name":old_name,"iid":change_id,"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id"),"old_card_id":old_card_id,"new_name":new_name,"new_card":new_card,"usering":change_usering},
				url:"/ES/ESServlet/changeKeyById.action",
				success:function(data){
					alert(data.data)
					if(data.data=="修改成功"){
						window.location="/ES/keyss.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+getParam("keybox_name");
					}
				}
			});
		}
	}else{
		alert("权限不足")
	}
	document.getElementById(show_div).style.display='none';
	document.getElementById(bg_div).style.display='none';
	$("#change_name").html("");
	$("#change_card").html("");
	old_name = null;
	old_card_id = null
	change_iid = null
	change_usering = null
}

var keyss_iid = null
var keyss_name = null
lock = function(iid,name){
	var style = "<%=request.getSession().getAttribute("style")%>"
	var managername = "<%=request.getSession().getAttribute("user")%>"
	
	if(style=="level"){
		alert("管理员才能开锁")
	}else if(style=="unit"||style=="team"){
		if(validAuth()!="no"){
			$.ajax({  
				type:"post", 
				dataType: "json",
				data:{"team_id":getParam("team_id"),"style":style,"unit_id":getParam("unit_id"),"managername":managername},
				url:"/ES/ESServlet/getLockAuth.action",
				success:function(data){
					if(data.data=="no"){
						alert("您没有此权限")
					}else{
						if(data.length>0){
							$.each(data,function(){
								$("#lock_username").append("<option value=\""+this.iid+"\">"+this.name+"</option>");   
							})
						}else{
							$("#lock_username").append("<option value=\"-1\">请先添加使用者</option>");   
						}
						keyss_iid = iid;
						keyss_name = name
						$("#lock_managername").val(managername)
						ShowDiv('MyDiv4','fade4_2')
					}
				}
			});
		}else{
			alert("权限不足")
		}
	}
}
function CloseDiv1_4(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
	 $("#lock_managername").html("");
	 $("#lock_username").html("");
	 keyss_iid = null;
	 keyss_name = null
}
function CloseDiv2_4(show_div,bg_div){
	var manager_name = "<%=request.getSession().getAttribute("user")%>"
	var users_iid = $("#lock_username option:selected").val().trim();
	var users_name = $("#lock_username option:selected").text().trim();

	if(users_iid!=-1&&users_name!=null&&users_name!="请先添加使用者"){
		$.ajax({  
			type:"post", 
			dataType: "json",
			data:{"users_iid":users_iid,"keyss_iid":keyss_iid,"manager_name":manager_name,"users_name":users_name,"keyss_name":keyss_name,"keybox_id":getParam("keybox_id"),"team_id":getParam("team_id"),"unit_id":getParam("unit_id")},
			url:"/ES/ESServlet/addLog.action",
			success:function(data){
				alert(data.data)
			}
		});
	}else{
		alert("请先添加使用者")
	}
	document.getElementById(show_div).style.display='none';
	document.getElementById(bg_div).style.display='none';
	$("#lock_managername").html("");
	$("#lock_username").html("");
	keyss_iid = null;
	keyss_name = null
}
validAuth = function(){
	var style = "<%=request.getSession().getAttribute("style")%>"
	var managername = "<%=request.getSession().getAttribute("user")%>"
	var auth = null;
	
	if(style=="team"){
		$.ajax({  
			type:"post", 
			async: false,
			dataType: "json",
			data:{"team_id":getParam("team_id"),"unit_id":getParam("unit_id"),"managername":managername},
			url:"/ES/ESServlet/validAuth.action",
			success:function(data){
				auth = data.data
			}
		});
	}
	return auth;
}

var lasttimestamp = 0;
getKeyState = function(){
	$.ajax({
		dataType:"json",
		type:"post", 
		data:{"keybox_id":getParam("keybox_id")},
		url:"/ES/ESServlet/getRemotesKeyState.action",
		success:function(data){
			if(data.data=="命令已发送"){
				lasttimestamp = (new Date()).getTime();
				time = setInterval("getBack(1)", 1000);
				$("#loading").addClass("loadingWrap");
			}else{
				alert(data.data)
			}
		}
	});
}
var time = null;
getLogs = function(e){
	var ret = true;
	if(e==2){//2为删除远程记录
		var bln = confirm("确定删除吗?");
		if(!bln){
			ret = false;
		}
	}
	if(ret){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"keybox_id":getParam("keybox_id"),"style":e},
			url:"/ES/ESServlet/getRemotesLogs.action",
			success:function(data){
				if(data.data=="命令已发送"){
					lasttimestamp = (new Date()).getTime();
					time = setInterval("getBack(1)", 1000);
					$("#loading").addClass("loadingWrap");
				}else{
					alert(data.data)
				}
			}
		});
	}
}

sendAllKey = function(){
	$.ajax({  
		type:"post", 
		data:{"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id")},
		url:"/ES/ESServlet/sendAllKey.action",
		dataType:"json",
		success:function(data){
			if(data.data=="命令已发送"){
				lasttimestamp = (new Date()).getTime();
				time = setInterval("getBack(1)", 1000);
				$("#loading").addClass("loadingWrap");
			}else{
				alert(data.data)
			}
		}  
	});
}
usering = function(id,usering,card_id,keybox_id,iid,name){
	var reg = new RegExp("^[0-9]+$");
	if(reg.test(card_id)&&card_id.length==10){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"id":id,"usering":usering,"card_id":card_id,"keybox_id":keybox_id,"iid":iid,"name":name},
			traditional: true,
			url:"/ES/ESServlet/usering.action",
			success:function(data){
				alert(data.data)
				if(data.data=="设置成功"){
					window.location="/ES/keyss.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+getParam("keybox_name");
				}
			}
		}) 
	}else {
		alert("卡id请改为10位数字！");
	}
}
findLogs = function(iid){
	var url = "/ES/logs.jsp?keyss_id="+iid+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
	window.location=url;
}
function getBack(size){
	$.ajax({  
		type:"post", 
		data:{"keybox_id":getParam("keybox_id")},
		url:"/ES/ESServlet/getBack.action",
		dataType:"json",
		success:function(data){
			var timestamp = (new Date()).getTime();
			
			if(data.data=="配置成功"){
				clearTimeout(time);
				time = null;
				$("#loading").removeClass("loadingWrap");
				alert("操作成功")
				window.location="/ES/keyss.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+getParam("keybox_name");
			}else if(timestamp-lasttimestamp>60*1000*size){
				clearTimeout(time);
				time = null;
				$("#loading").removeClass("loadingWrap");
				alert("操作失败")
			}
		 }  
	});
} 
</script>
</head>
<body id="body"  class="mar_bot">
	<div id="show">
		<div id="cont_addsite">
			<div class="site_search_add_user"><button class="search_user" onclick="search_user()">查询钥匙</button><button class="add_user" onclick="add_user()">添加钥匙(编号0——148)</button><button class="delete_user" onclick="delete_user()">删除钥匙</button></div>
			<div class="div_p_9 margin_4"><p class="p_9_1">序号</p><p class="p_9_2">编号</p><p class="p_9_3">钥匙名称</p><p class="p_9_4">卡id</p><p class="p_9_5">状态</p><p>开锁</p><p class="p_9_7">启用/禁用</p><p>记录</p><p class="no_right"><span id="site_sp1">操作</span></p></div>
		</div>
	</div>
	
	<div id="fade2" class="black_overlay"></div>
	<div id="MyDiv2" class="white_content width100">
		<div>
			<P class="tip_p margin_30">查询成功!</P>
			<div id="mydiv2_cont">
			</div>
			<div>
				<div class="center"
					onclick="CloseDiv2_1('MyDiv2','fade2')">
					<input type="button" value="取消" class="border_none">
				</div>
				<div class="center" onclick="CloseDiv2_1('MyDiv2','fade2')">
					<input type="button" value="确定" class="border_none">
				</div>
			</div>
		</div>
	</div>
	
	<div id="fade3" class="black_overlay"></div>
	<div id="MyDiv3" class="white_content">
		<div>
			<P class="tip_p">请输入钥匙信息！</P>
			<div id="cont" class="cente">
				<label class="lab_manager">名称：</label><input type="text" id="levelManager_add_name" class="inp_1"/><br/>
				<label class="lab_manager">编号：</label><input type="text" id="levelManager_add_"  class="inp_1"/><br/>
				<label class="lab_manager">id号：</label><input type="text" id="levelManager_add_iid"  class="inp_1"/><br/>
			</div>
			<div>
				<div class="center"
					onclick="CloseDiv1('MyDiv3','fade3')">
					<input type="button" value="取消" class="border_none">
				</div>
				<div class="center" onclick="CloseDiv2_3('MyDiv3','fade3')">
					<input type="button" value="确定" class="border_none">
				</div>
			</div>
		</div>
	</div>
	
	<div id="fade4_2" class="black_overlay"></div>
	<div id="MyDiv4" class="white_content">
		<div>
			<P class="tip_p margin_24_10">请输入开锁信息</P>
			<div id="cont" class="cente">
				<label class="lab_manager">申请人名称：</label><select id="lock_username" class="select1"></select><br/>
				<label class="lab_manager">批准人名称：</label><input id="lock_managername" class="select2" readonly="readonly"/><br/>
			</div>
			<div>
				<div class="center"
					onclick="CloseDiv1_4('MyDiv4','fade4_2')">
					<input type="button" value="取消" class="border_none">
				</div>
				<div class="center" onclick="CloseDiv2_4('MyDiv4','fade4_2')">
					<input type="button" value="开锁" class="border_none">
				</div>
			</div>
		</div>
	</div>
	
	
	<div id="fade33" class="black_overlay"></div>
	<div id="MyDiv33" class="white_content">
		<div>
			<P class="tip_p">请输入钥匙信息！</P>
			<div id="cont" class="cente">
				<label class="lab_manager">名称：</label><input type="text" id="change_name" class="inp_1"/><br/>
				<label class="lab_manager">id号：</label><input type="text" id="change_card"  class="inp_1"/><br/>
			</div>
			<div>
				<div class="center"
					onclick="CloseDiv13('MyDiv33','fade33')">
					<input type="button" value="取消" class="border_none">
				</div>
				<div class="center" onclick="CloseDiv23('MyDiv33','fade33')">
					<input type="button" value="确定" class="border_none">
				</div>
			</div>
		</div>
	</div>
	
	<div id="log_foot" class="creatTable">
		<p class="table_p1" onclick="getLogs(2)">删除远程操作记录</p>
		<p class="table_p1" onclick="getLogs(1)">获取远程操作记录</p>
		<p class="table_p2" onclick="getKeyState()">获取远程钥匙状态</p>
		<p class="table_p2" onclick="sendAllKey()">配置所有钥匙</p>
	</div>
	<div class="" id="loading"></div>
</body>
</html>