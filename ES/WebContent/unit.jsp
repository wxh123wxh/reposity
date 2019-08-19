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

var number = 0;
window.onload=function(){
	document.getElementById("cont_addsite").style.display='block';
	$.ajax({
		dataType:"json",
		type:"post", 
		url:"/ES/ESServlet/findUnit.action",
		success:function(data){
			if(data.data!=null){
				$("#top_bnt",window.parent.document).text("请添加"+'<%=prop.get("unit")%>'+"信息");//子页面给父页面元素赋值
				$("#u_name",window.parent.document).val("请添加"+'<%=prop.get("unit")%>'+"信息");//子页面给父页面元素赋值
			}else{
				$("#body").append("<div class=\"unit_info\"><p class=\"unit_p1 width25\">"+data.unit_number+"</p><p class=\"unit_p2-1 width26\">"+data.name+"</p><p class=\"unit_p3_2 width12\" onclick=\"findCards('"+data.unit_number+"','"+data.name+"')\"><img src=\"./image/card.png\"><span class=\"blue\">授权卡</span></p><p class=\"unit_p3_5 width12\" onclick=\"findUnitSubById('"+data.unit_number+"','"+data.name+"')\"><img src=\"./image/sub_blue.png\"><span class=\"blue\">"+'<%=prop.get("team")%>'+"</span></p><p class=\"unit_p3 width12\" onclick=\"findUnitManagerById('"+data.unit_number+"','"+data.name+"')\"><img src=\"./image/manager.png\" ><span class=\"deept_blue\">管理员</span></p><p class=\"unit_p3 width12\"><img src=\"./image/discheck.png\" class=\"img1\" onclick=\"check(this)\"><img src=\"./image/edit.png\" onclick=\"changeUnitById('"+data.id+"')\"></p></div>");
				$("#top_bnt",window.parent.document).text(data.name);
				$("#u_name",window.parent.document).val(data.name);
				number++;
			}
		}
	});
}
search_unit = function(){
	var name = prompt("请输入名称")
	
	if(name!=null&&name.trim()!=""){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"name":name},
			url:"/ES/ESServlet/findUnitByName.action",
			success:function(data){
				if(data.data!=null){
					alert(data.data);
				}else{
					ShowDiv('MyDiv2','fade2')
					$("#mydiv2_cont").append("<div class=\"div_p_6 border_top\"><p class=\"unit_p1 width25\">编号</p><p class=\"unit_p2 margin_left-1 width26\">名称</p><p class=\"unit_p2 width12\">授权卡</p><p class=\"unit_p2 margin_left5 width12\">"+'<%=prop.get("team")%>'+"</p><p class=\"unit_p2 width12\">管理员</p><p class=\"unit_pright width12\"><span id=\"site_sp1\">操作</span></p></div>")
					$("#mydiv2_cont").append("<div class=\"unit_info\"><p class=\"unit_p1 margin_left0 width25\">"+data.unit_number+"</p><p class=\"unit_p2-1 width26\">"+data.name+"</p><p class=\"unit_p3_2 width12\" onclick=\"findCards('"+data.unit_number+"','"+data.name+"')\"><img src=\"./image/card.png\"><span class=\"blue\">授权卡</span></p><p class=\"unit_p3_5 width12\" onclick=\"findUnitSubById('"+data.unit_number+"','"+data.name+"')\"><img src=\"./image/sub_blue.png\"><span class=\"blue\">"+'<%=prop.get("team")%>'+"</span></p><p class=\"unit_p3 width12\" onclick=\"findUnitManagerById('"+data.unit_number+"','"+data.name+"')\"><img src=\"./image/manager.png\" ><span class=\"deept_blue\">管理员</span></p><p class=\"unit_p3 width12\"><img src=\"./image/delete.png\" class=\"img1\" onclick=\"deleteUnit('"+data.unit_number+"')\"><img src=\"./image/edit.png\" onclick=\"changeUnitById('"+data.id+"')\"></p></div>");
				}
			}
		});
	}else if(name==null){
		
	}else{
		alert("请输入名称")
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
function CloseDiv2_1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
	 $("#mydiv2_cont").html("")
}
$(function() {
	pushHistory();
	window.addEventListener('popstate', function(e) {
		 //自定义操作
		window.location.href = "/ES/unit.jsp?&number="+Math.random();
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
	var style = "<%=request.getSession().getAttribute("style")%>"
	if(style=="level"){
		if(number>0){
			alert("只能存在一个"+'<%=prop.get("unit")%>')
		}else{
			var name = prompt("请输入要添加的"+'<%=prop.get("unit")%>'+"名称")
			if(name!=null&&name.trim()!=""){
				$.ajax({
					dataType:"json",
					type:"post", 
					data:{"name":name},
					url:"/ES/ESServlet/addUnit.action",
					success:function(data){
						alert(data.data)
						if(data.data="添加成功"){
							window.location="/ES/unit.jsp";
						}
					}
				})
			}else if(name==null){
				
			}else{
				alert("请先输入要添加的"+'<%=prop.get("unit")%>'+"名称")
			}
		}
	}else{
		alert("权限不足")
	}
}

var arr = new Array();
check = function(event){
	if($(event).attr("src")=="./image/discheck.png"){
		$(event).attr("src","./image/dui.png")
		var unit_id = $(event).parent().parent().children().eq(0).text()
		arr.push(unit_id)
	}else {
		$(event).attr("src","./image/discheck.png")
		var unit_id = $(event).parent().parent().children().eq(0).text()
		var index =arr.indexOf(unit_id)
		arr.splice(index,1);
	}
}

delete_unit = function(){
	var bln = confirm("确定删除吗?");
	if(bln){
		var style = "<%=request.getSession().getAttribute("style")%>"
		if(style=="level"){
			if(arr.length>0){
				$.ajax({
					dataType:"json",
					type:"post", 
					data:{"unit_numbers":arr},
					traditional: true,
					url:"/ES/ESServlet/deleteUnitByIds.action",
					success:function(data){
						alert(data.data)
						if(data.data=="删除成功"){
							window.location="/ES/unit.jsp";
							arr.length = 0
						}
					}
				})
			}else{
				alert("请先选择要删除的"+'<%=prop.get("unit")%>')
			}
		}else{
			alert("权限不足")
		}
	}
}
deleteUnit = function(unit_id){
	var bln = confirm("确定删除吗?");
	if(bln){
		var style = "<%=request.getSession().getAttribute("style")%>"
		if(style=="level"){
			arr.push(unit_id)
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"unit_numbers":arr},
				traditional: true,
				url:"/ES/ESServlet/deleteUnitByIds.action",
				success:function(data){
					alert(data.data)
					if(data.data=="删除成功"){
						window.location="/ES/unit.jsp";
					}
				}
			})
			arr.length = 0
		}else{
			alert("权限不足")
		}
	}
}
changeUnitById = function(id){
	var style = "<%=request.getSession().getAttribute("style")%>"
	if(style=="level"){
		var name = prompt("请输入要修改的名称");
		
		if(name!=null&&name.trim()!=""){
			$.ajax({  
				type:"post", 
				dataType: "json",
				data:{"change_name":name,"change_id":id},
				url:"/ES/ESServlet/changeUnitById.action",
				success:function(data){
					alert(data.data)
					if(data.data=="修改成功"){
						window.location="/ES/unit.jsp";
					}
				}
			});
		}else if(name==null){
			
		}else{
			alert("请输入要修改成的名称")
		}
	}else{
		alert("权限不足")
	}
}

findUnitSubById = function(unit_number,name){
	var url = "/ES/team.jsp?unit_id="+unit_number+"&unit_name="+name;
	window.location=url;
}
findUnitManagerById = function(unit_number,name){
	var url = "/ES/unitManager.jsp?unit_id="+unit_number+"&unit_name="+name;
	window.location=url;
}
findCards = function(unit_number,name){
	var url = "/ES/authCard.jsp?unit_id="+unit_number+"&unit_name="+name;
	window.location=url;
}
</script>
</head>
<body id="body">
	<div id="show">
		<div id="cont_addsite">
			<div class="site_search_add_user"><button class="search_user" onclick="search_unit()">查询<%=prop.get("unit")%></button><button class="add_user" onclick="add_unit()">添加<%=prop.get("unit")%></button><button class="delete_user" onclick="delete_unit()">删除<%=prop.get("unit")%></button></div>
			<div class="div_p_6"><p class="unit_p1 width25">编号</p><p class="unit_p2 margin_left-1 width26"><%=prop.get("unit")%>名称</p><p class="unit_p2 width12">授权卡</p><p class="unit_p2 margin_left5 width12"><%=prop.get("team")%></p><p class="unit_p2 width12">管理员</p><p class="unit_pright width12"><span id="site_sp1">操作</span></p></div>
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
</body>
</html>