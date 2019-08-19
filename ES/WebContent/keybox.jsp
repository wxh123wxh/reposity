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
var start = 0; //查询的开始位置

window.onload=function(){
	document.getElementById("cont_addsite").style.display='block';
	$("#top_bnt",window.parent.document).text(getParam("team_name"));
	$.ajax({
		dataType:"json",
		type:"post", 
		data:{"team_id":getParam("team_id"),"start":start,"unit_id":getParam("unit_id")},
		url:"/ES/ESServlet/findKeyBox.action",
		success:function(data){
			if(jQuery.isEmptyObject(data)){
				
			}else{
				$.each(data,function(){
					if(this.onlines=="掉线"){
						$("#body").append("<div class=\"div_p_8_2\"><p class=\"p_8_1\">"+number+"</p><p class=\"width_22\">"+this.name+"</p><p class=\"padding14\"><img src=\"./image/cuo.png\"><span class=\"red\">"+this.onlines+"</span></p><p class=\"padding14\" onclick=\"findKeyBoxSubById('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/sub_blue.png\"><span class=\"blue\">钥匙</span></p><p class=\"padding14\" onclick=\"findCard('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/card.png\"><span class=\"blue\">普通卡</span></p><p class=\"padding14\" onclick=\"findInfo('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/info.png\" ><span class=\"blue\">详情</span></p><p class=\"padding14\" onclick=\"findLogs('"+this.maxc+"')\"><img src=\"./image/logs.png\" class=\"margin_14\"></p><p class=\"padding14\"><img src=\"./image/discheck.png\" class=\"img1_1\" onclick=\"check('"+this.maxc+"',this)\"><img src=\"./image/edit.png\" onclick=\"changeTeamById('"+this.id+"',this)\"></p></div>");
					}else{
						$("#body").append("<div class=\"div_p_8_2\"><p class=\"p_8_1\">"+number+"</p><p class=\"width_22\">"+this.name+"</p><p class=\"padding14\"><img src=\"./image/dui.png\"><span class=\"red\">"+this.onlines+"</span></p><p class=\"padding14\" onclick=\"findKeyBoxSubById('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/sub_blue.png\"><span class=\"blue\">钥匙</span></p><p class=\"padding14\" onclick=\"findCard('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/card.png\"><span class=\"blue\">普通卡</span></p><p class=\"padding14\" onclick=\"findInfo('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/info.png\" ><span class=\"blue\">详情</span></p><p class=\"padding14\" onclick=\"findLogs('"+this.maxc+"')\"><img src=\"./image/logs.png\" class=\"margin_14\"></p><p class=\"padding14\"><img src=\"./image/discheck.png\" class=\"img1_1\" onclick=\"check('"+this.maxc+"',this)\"><img src=\"./image/edit.png\" onclick=\"changeTeamById('"+this.id+"',this)\"></p></div>");
					}
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
    			data:{"team_id":getParam("team_id"),"start":++start,"unit_id":getParam("unit_id")},
    			url:"/ES/ESServlet/findKeyBox.action",
    			success:function(data){
    				if(jQuery.isEmptyObject(data)){
    					
    				}else{
    					$.each(data,function(){
    						if(this.onlines=="掉线"){
    							$("#body").append("<div class=\"div_p_8_2\"><p class=\"p_8_1\">"+number+"</p><p class=\"width_22\">"+this.name+"</p><p class=\"padding14\"><img src=\"./image/cuo.png\"><span class=\"red\">"+this.onlines+"</span></p><p class=\"padding14\" onclick=\"findKeyBoxSubById('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/sub_blue.png\"><span class=\"blue\">钥匙</span></p><p class=\"padding14\" onclick=\"findCard('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/card.png\"><span class=\"blue\">普通卡</span></p><p class=\"padding14\" onclick=\"findInfo('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/info.png\" ><span class=\"blue\">详情</span></p><p class=\"padding14\" onclick=\"findLogs('"+this.maxc+"')\"><img src=\"./image/logs.png\" class=\"margin_14\"></p><p class=\"padding14\"><img src=\"./image/discheck.png\" class=\"img1_1\" onclick=\"check('"+this.maxc+"',this)\"><img src=\"./image/edit.png\" onclick=\"changeTeamById('"+this.id+"',this)\"></p></div>");
    						}else{
    							$("#body").append("<div class=\"div_p_8_2\"><p class=\"p_8_1\">"+number+"</p><p class=\"width_22\">"+this.name+"</p><p class=\"padding14\"><img src=\"./image/dui.png\"><span class=\"red\">"+this.onlines+"</span></p><p class=\"padding14\" onclick=\"findKeyBoxSubById('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/sub_blue.png\"><span class=\"blue\">钥匙</span></p><p class=\"padding14\" onclick=\"findCard('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/card.png\"><span class=\"blue\">普通卡</span></p><p class=\"padding14\" onclick=\"findInfo('"+this.maxc+"','"+this.name+"')\"><img src=\"./image/info.png\" ><span class=\"blue\">详情</span></p><p class=\"padding14\" onclick=\"findLogs('"+this.maxc+"')\"><img src=\"./image/logs.png\" class=\"margin_14\"></p><p class=\"padding14\"><img src=\"./image/discheck.png\" class=\"img1_1\" onclick=\"check('"+this.maxc+"',this)\"><img src=\"./image/edit.png\" onclick=\"changeTeamById('"+this.id+"',this)\"></p></div>");
    						}
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
		window.location.href = "/ES/team.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name")+"&number="+Math.random();
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
search_unit = function(){
	var name = prompt("请输入要查询名称")
	
	if(name!=null&&name.trim()!=""){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"name":name,"team_id":getParam("team_id"),"unit_id":getParam("unit_id")},
			url:"/ES/ESServlet/findKeyBoxByName.action",
			success:function(data){
				if(data.data!=null){
					alert(data.data);
				}else{
					ShowDiv('MyDiv2','fade2')
					$("#mydiv2_cont").append("<div class=\"div_p_7_1\" id=\"show_close\"><p class=\"p_7 width_22\">名称</p><p class=\"p_7\">在线状态</p><p class=\"p_7\">钥匙</p><p class=\"p_7\">普通卡</p><p class=\"p_7\">详情</p><p class=\"p_7\"><span>记录</span></p><p class=\"p_7_3\"><span>操作</span></p></div>")
					if(data.onlines=="掉线"){
						$("#mydiv2_cont").append("<div class=\"div_p_7_2\" id=\"show_close\"><p class=\"p_7 width_22\">"+data.name+"</p><p class=\"p_7_2\"><img src=\"./image/cuo.png\"><span class=\"red\">"+data.onlines+"</span></p><p class=\"p_7_2\" onclick=\"findKeyBoxSubById('"+data.maxc+"','"+data.name+"')\"><img src=\"./image/sub_blue.png\"><span class=\"blue\">钥匙</span></p><p class=\"p_7_2\"  onclick=\"findCard('"+data.maxc+"','"+data.name+"')\"><img src=\"./image/card.png\"><span class=\"blue\">普通卡</span></p><p class=\"p_7_2\" onclick=\"findInfo('"+data.maxc+"','"+data.name+"')\"><img src=\"./image/info.png\" ><span class=\"blue\">详情</span></p><p class=\"p_7_2\" onclick=\"findLogs('"+data.maxc+"')\"><img src=\"./image/logs.png\" class=\"margin_14\"></p><p class=\"p_7_7\"><img src=\"./image/delete.png\" class=\"img1_1\" onclick=\"deleteTeam('"+data.maxc+"')\"><img src=\"./image/edit.png\" onclick=\"changeTeamById('"+data.id+"',this)\"></p></div>");
					}else{
						$("#mydiv2_cont").append("<div class=\"div_p_7_2\" id=\"show_close\"><p class=\"p_7 width_22\">"+data.name+"</p><p class=\"p_7_2\"><img src=\"./image/dui.png\"><span class=\"red\">"+data.onlines+"</span></p><p class=\"p_7_2\" onclick=\"findKeyBoxSubById('"+data.maxc+"','"+data.name+"')\"><img src=\"./image/sub_blue.png\"><span class=\"blue\">钥匙</span></p><p class=\"p_7_2\"  onclick=\"findCard('"+data.maxc+"','"+data.name+"')\"><img src=\"./image/card.png\"><span class=\"blue\">普通卡</span></p><p class=\"p_7_2\" onclick=\"findInfo('"+data.maxc+"','"+data.name+"')\"><img src=\"./image/info.png\" ><span class=\"blue\">详情</span></p><p class=\"p_7_2\" onclick=\"findLogs('"+data.maxc+"')\"><img src=\"./image/logs.png\" class=\"margin_14\"></p><p class=\"p_7_7\"><img src=\"./image/delete.png\" class=\"img1_1\" onclick=\"deleteTeam('"+data.maxc+"')\"><img src=\"./image/edit.png\" onclick=\"changeTeamById('"+data.id+"',this)\"></p></div>");
					}
				}
			}
		});
	}else if(name==null){
		
	}else{
		alert("请输入要查询的名称")
	}
}

add_unit = function(){
	if(validAuth()!="no"){
		ShowDiv('MyDiv3','fade3')
	}else{
		alert("权限不足")
	}
}
function CloseDiv1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
}
function CloseDiv2_3(show_div,bg_div){
	var name = $("input[id='levelManager_add_name']").val().trim()
	var maxc = $("input[id='levelManager_add_iid']").val().trim()
	var team_id = getParam("team_id");
	
	if(name!=""&&maxc!=""&&maxc.length==12){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"name":name,"team_id":team_id,"maxc":maxc,"unit_id":getParam("unit_id")},
			url:"/ES/ESServlet/addKeyBox.action",
			success:function(data){
				alert(data.data)
				if(data.data=="添加成功"){
					window.location="/ES/keybox.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name");
				}
			}
		})
	}else if(name==null){
		
	}else{
		alert("请输入完整的"+'<%=prop.get("keybox")%>'+"信息")
	}
	document.getElementById(show_div).style.display='none';
	document.getElementById(bg_div).style.display='none';
}

var arr = new Array();
check = function(maxc,event){
	if($(event).attr("src")=="./image/discheck.png"){
		$(event).attr("src","./image/dui.png")
		arr.push(maxc)
	}else {
		$(event).attr("src","./image/discheck.png")
		var index =arr.indexOf(maxc)
		arr.splice(index,1);
	}
}

delete_unit = function(){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(validAuth()!="no"){
			if(arr.length>0){
				$.ajax({
					dataType:"json",
					type:"post", 
					data:{"maxcs":arr},
					traditional: true,
					url:"/ES/ESServlet/deleteKeyBoxByIds.action",
					success:function(data){
						alert(data.data)
						if(data.data=="删除成功"){
							window.location="/ES/keybox.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name");
						}
					}
				})
			}else{
				alert("请先选择要删除的"+'<%=prop.get("keybox")%>')
			}
		}else{
			alert("权限不足")
		}
	}
}
deleteTeam = function(maxc){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(validAuth()!="no"){
			arr.push(maxc)
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"maxcs":arr},
				traditional: true,
				url:"/ES/ESServlet/deleteKeyBoxByIds.action",
				success:function(data){
					alert(data.data)
					if(data.data=="删除成功"){
						window.location="/ES/keybox.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name");
					}
				}
			})
			arr.length = 0
		}else{
			alert("权限不足")
		}
	}
}

changeTeamById = function(id,event){
	if(validAuth()!="no"){
		var name = prompt("请输入要修改的"+'<%=prop.get("keybox")%>'+"名称");
		if(name!=null&&name.trim()!=""){
			$.ajax({  
				type:"post", 
				dataType: "json",
				data:{"name":name,"id":id,"team_id":getParam("team_id"),"unit_id":getParam("unit_id")},
				url:"/ES/ESServlet/changeKeyBoxById.action",
				success:function(data){
					alert(data.data)
					if(data.data=="修改成功"){
						window.location="/ES/keybox.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name");
					}
				}
			});
		}else if(name==null){
			
		}else{
			alert("请输入要修改成的"+'<%=prop.get("keybox")%>'+"名称")
		}
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

function CloseDiv2_1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
	 $("div[id='show_close']").remove()
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
findCard = function(maxc,name){
	var url = "/ES/card.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+maxc+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+name;
	window.location=url;
}
findKeyBoxSubById = function(maxc,name){
	var url = "/ES/keyss.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+maxc+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+name;
	window.location=url;
}
findInfo = function(maxc,name){
	var url = "/ES/keybox_info.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+maxc+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+name;
	window.location=url;
}
findLogs = function(maxc){
	var url = "/ES/logs.jsp?keybox_id="+maxc+"&unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")
	window.location=url;
}
</script>
</head>
<body id="body" class="mar_bot">
	<div id="show">
		<div id="cont_addsite">
			<div class="site_search_add_user"><button class="search_user" onclick="search_unit()">查询<%=prop.get("keybox")%></button><button class="add_user" onclick="add_unit()">添加<%=prop.get("keybox")%></button><button class="delete_user" onclick="delete_unit()">删除<%=prop.get("keybox")%></button></div>
			<div class="div_p_8 margin_4"><p class="p_8_1">序号</p><p class="width_22"><%=prop.get("keybox")%>名称</p><p class="p_8_4">在线状态</p><p class="p_8_4">下属钥匙</p><p class="p_8_5">普通卡</p><p class="p_8_6">详情</p><p class="p_8_7"><span id="site_sp1">记录</span></p><p class="p_8_8"><span id="site_sp1">操作</span></p></div>
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
			<P class="tip_p">请输入<%=prop.get("keybox")%>信息！</P>
			<div id="cont" class="cente">
				<label class="lab_manager">名称：</label><input type="text" id="levelManager_add_name" class="inp_1"/><br/>
				<label class="lab_manager">地址：</label><input type="text" id="levelManager_add_iid"  class="inp_1"/><br/>
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
</body>
</html>