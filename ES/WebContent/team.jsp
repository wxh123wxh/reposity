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

var start = 0; //查询的开始位置
var style = "<%=request.getSession().getAttribute("style")%>"

window.onload=function(){
	document.getElementById("cont_addsite").style.display='block';
	$("#top_bnt",window.parent.document).text(getParam("unit_name"));
	$.ajax({
		dataType:"json",
		type:"post", 
		data:{"start":start,"unit_id":getParam("unit_id")},
		url:"/ES/ESServlet/findTeam.action",
		success:function(data){
			if(jQuery.isEmptyObject(data)){
				
			}else{
				$.each(data,function(){
					$("#body").append("<div class=\"div_p_7_2\"><p class=\"p_7\">"+this.iid+"</p><p class=\"p_7 width_22\">"+this.name+"</p><p class=\"p_7_2\" onclick=\"findTeamSubById('"+this.iid+"','"+this.name+"')\"><img src=\"./image/sub_blue.png\"><span class=\"blue\">"+'<%=prop.get("keybox")%>'+"</span></p><p class=\"p_7_2\" onclick=\"findTeamManager('"+this.iid+"','"+this.name+"')\"><img src=\"./image/manager.png\"><span class=\"deept_blue\">管理员</span></p><p class=\"p_7_2\" onclick=\"findUsers('"+this.iid+"','"+this.name+"')\"><img src=\"./image/user.png\" ><span class=\"blue\">使用者</span></p><p class=\"p_7_2\" onclick=\"findLogs('"+this.iid+"')\"><img src=\"./image/logs.png\" class=\"margin_14\"></p><p class=\"p_7_7\"><img src=\"./image/discheck.png\" class=\"img1_1\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/edit.png\" onclick=\"changeTeamById('"+this.id+"',this)\"></p></div>");
				})
			}
		}
	});
}
/**
 * 页面下拉时查询更多的unit
 */
$(function(){
	$(window).scroll(function() {
   		var scrollTop = $(this).scrollTop(),scrollHeight = $(document).height(),windowHeight = $(this).height();
    	var positionValue = (scrollTop + windowHeight) - scrollHeight;
    	
    	if (positionValue >= 0) {
        	$.ajax({  
    			dataType:"json",    //数据类型为json格式
    			type:"post",
    			data:{"start":++start,"unit_id":getParam("unit_id")},
    			url:"/ES/ESServlet/findTeam.action",
    			success:function(data){ 
    				if(jQuery.isEmptyObject(data)){
    					
    				}else{
    					$.each(data,function(){
    						$("#body").append("<div class=\"div_p_7_2\"><p class=\"p_7\">"+this.iid+"</p><p class=\"p_7 width_22\">"+this.name+"</p><p class=\"p_7_2\" onclick=\"findTeamSubById('"+this.iid+"','"+this.name+"')\"><img src=\"./image/sub_blue.png\"><span class=\"blue\">"+'<%=prop.get("keybox")%>'+"</span></p><p class=\"p_7_2\" onclick=\"findTeamManager('"+this.iid+"','"+this.name+"')\"><img src=\"./image/manager.png\"><span class=\"deept_blue\">管理员</span></p><p class=\"p_7_2\" onclick=\"findUsers('"+this.iid+"','"+this.name+"')\"><img src=\"./image/user.png\" ><span class=\"blue\">使用者</span></p><p class=\"p_7_2\" onclick=\"findLogs('"+this.iid+"')\"><img src=\"./image/logs.png\" class=\"margin_14\"></p><p class=\"p_7_7\"><img src=\"./image/discheck.png\" class=\"img1_1\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/edit.png\" onclick=\"changeTeamById('"+this.id+"',this)\"></p></div>");
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
		window.location.href = "/ES/unit.jsp?number="+Math.random();
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}
add_unit = function(){
	if(style!="team"){
		ShowDiv('MyDiv3','fade3')
	}else{
		alert("没有此权限")
	}
}

var arr = new Array();
check = function(iid,event){
	if($(event).attr("src")=="./image/discheck.png"){
		$(event).attr("src","./image/dui.png")
		arr.push(iid)
	}else {
		$(event).attr("src","./image/discheck.png")
		var index =arr.indexOf(iid)
		arr.splice(index,1);
	}
}

delete_unit = function(){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(style!="team"){
			if(arr.length>0){
				$.ajax({
					dataType:"json",
					type:"post", 
					data:{"iids":arr,"unit_id":getParam("unit_id")},
					traditional: true,
					url:"/ES/ESServlet/deleteTeamByIds.action",
					success:function(data){
						alert(data.data)
						if(data.data=="删除成功"){
							window.location="/ES/team.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
						}
					}
				})
			}else{
				alert("请先选择要删除的"+'<%=prop.get("team")%>')
			}
		}else{
			alert("没有此权限")
		}
	}
}
deleteTeam = function(iid){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(style!="team"){
			arr.push(iid)
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"iids":arr,"unit_id":getParam("unit_id")},
				traditional: true,
				url:"/ES/ESServlet/deleteTeamByIds.action",
				success:function(data){
					alert(data.data)
					if(data.data=="删除成功"){
						window.location="/ES/team.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
					}
				}
			})
			arr.length = 0
		}else{
			alert("没有此权限")
		}
	}
}
var change_id = null//要修改名称的单位id

changeTeamById = function(id,event){
	if(style!="team"){
		var name = prompt("请输入要修改的"+'<%=prop.get("team")%>'+"名称");
		if(name!=null&&name.trim()!=""){
			$.ajax({  
				type:"post", 
				dataType: "json",
				data:{"change_name":name,"id":id,"unit_id":getParam("unit_id")},
				url:"/ES/ESServlet/changeTeamById.action",
				success:function(data){
					alert(data.data)
					if(data.data=="修改成功"){
						window.location="/ES/team.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
					}
				}
			});
		}else if(name==null){
			
		}else{
			alert("请输入要修改成的"+'<%=prop.get("team")%>'+"名称")
		}
	}else{
		alert("没有此权限")
	}
}
search_unit = function(){
	var name = prompt("请输入要查询的"+'<%=prop.get("team")%>'+"名称")
	
	if(name!=null&&name.trim()!=""){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"name":name,"unit_id":getParam("unit_id")},
			url:"/ES/ESServlet/findTeamByName.action",
			success:function(data){
				if(data.data!=null){
					alert(data.data);
				}else{
					ShowDiv('MyDiv2','fade2')
					$("#mydiv2_cont").append("<div class=\"div_p_7_1\" id=\"show_close\"><p class=\"p_7\">编号</p><p class=\"p_7 width_22\">名称</p><p class=\"p_7\">"+'<%=prop.get("keybox")%>'+"</p><p class=\"p_7\">管理员</p><p class=\"p_7\">使用者</p><p class=\"p_7\"><span>记录</span></p><p class=\"p_7_3\"><span>操作</span></p></div>")
					$("#mydiv2_cont").append("<div class=\"div_p_7_2\" id=\"show_close\"><p class=\"p_7\">"+data.iid+"</p><p class=\"p_7 width_22\">"+data.name+"</p><p class=\"p_7_2\" onclick=\"findTeamSubById('"+data.iid+"','"+data.name+"')\"><img src=\"./image/sub_blue.png\"><span class=\"blue\">"+'<%=prop.get("keybox")%>'+"</span></p><p class=\"p_7_2\"  onclick=\"findTeamManager('"+data.iid+"','"+data.name+"')\"><img src=\"./image/manager.png\"><span class=\"deept_blue\">管理员</span></p><p class=\"p_7_2\" onclick=\"findUsers('"+data.iid+"','"+data.name+"')\"><img src=\"./image/user.png\" ><span class=\"blue\">使用者</span></p><p class=\"p_7_2\" onclick=\"findLogs('"+data.iid+"')\"><img src=\"./image/logs.png\" class=\"margin_14\"></p><p class=\"p_7_7\"><img src=\"./image/delete.png\" class=\"img1_1\" onclick=\"deleteTeam('"+data.iid+"')\"><img src=\"./image/edit.png\" onclick=\"changeTeamById('"+data.id+"',this)\"></p></div>");
				}
			}
		});
	}else if(name==null){
		
	}else{
		alert("请输入要查询的"+'<%=prop.get("team")%>'+"名称")
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
	 change_id = null;
	 $("div[id='show_close']").remove()
}
function CloseDiv2_2(show_div,bg_div){
	document.getElementById(show_div).style.display='none';
	document.getElementById(bg_div).style.display='none';
	 $("div[id='show_close']").remove()
}

function CloseDiv1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
}

function CloseDiv2_3(show_div,bg_div){
	var unit_id = getParam("unit_id");
	var iid = $("#add_team_iid").val().trim();
	var name = $("#add_team_name").val().trim();
	
	if(name!=""&&iid!=""){
		var reg = new RegExp("^[0-9]+$");
		if(!reg.test(iid)){
			alert("编号请输入数字！");
		}else {
			if(!(iid>=0&&iid<=31)){
				alert("编号在(0-31)之间！")
			}else{
				$.ajax({
					dataType:"json",
					type:"post", 
					data:{"name":name,"unit_id":unit_id,"iid":iid},
					url:"/ES/ESServlet/addTeam.action",
					success:function(data){
						if(data.data!=null){
							alert(data.data)
						}else{
							alert("添加成功")
							window.location="/ES/team.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
						}
					}
				})
			}
		}
	}else{
		alert("请输入要添加的"+'<%=prop.get("team")%>'+"名称和编号")
	}
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
}

findUsers = function(iid,name){
	var url = "/ES/user.jsp?unit_id="+getParam("unit_id")+"&team_id="+iid+"&unit_name="+getParam("unit_name")+"&team_name="+name;
	window.location=url;
}
findTeamSubById = function(iid,name){
	var url = "/ES/keybox.jsp?unit_id="+getParam("unit_id")+"&team_id="+iid+"&unit_name="+getParam("unit_name")+"&team_name="+name;
	window.location=url;
}
findTeamManager = function(iid,name){
	var url = "/ES/teamManager.jsp?unit_id="+getParam("unit_id")+"&team_id="+iid+"&unit_name="+getParam("unit_name")+"&team_name="+name;
	window.location=url;
}
findLogs = function(iid){
	var url = "/ES/logs.jsp?unit_id="+getParam("unit_id")+"&team_id="+iid+"&unit_name="+getParam("unit_name")
	window.location=url;
}
</script>
</head>
<body id="body" class="mar_bot">
	<div id="show">
		<div id="cont_addsite">
			<div class="site_search_add_user"><button class="search_user" onclick="search_unit()">查询<%=prop.get("team")%></button><button class="add_user" onclick="add_unit()">添加<%=prop.get("team")%>(编号0-31)</button><button class="delete_user" onclick="delete_unit()">删除<%=prop.get("team")%></button></div>
			<div class="div_p_7_1 margin_4"><p class="p_7">编号</p><p class="p_7 width_22"><%=prop.get("team")%>名称</p><p class="p_7">下属<%=prop.get("keybox")%></p><p class="p_7">管理员</p><p class="p_7">使用者</p><p class="p_7"><span id="site_sp1">记录</span></p><p class="p_7_3"><span id="site_sp1">操作</span></p></div>
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
			<P class="tip_p">添加<%=prop.get("team")%></P>
			<div id="cont" class="cente">
				<label class="lab_manager">编号：</label><input type="text" id="add_team_iid" class="inp_1"/><br/>
				<label class="lab_manager">名称：</label><input type="text" id="add_team_name"  class="inp_1"/><br/>
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