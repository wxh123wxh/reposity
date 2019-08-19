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
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<title>authCard</title>
<link rel="stylesheet" href="css/layui.css">
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

window.onload=function(){
	document.getElementById("cont_addsite").style.display='block';
	$("#top_bnt",window.parent.document).text(getParam("keybox_name"));
	$.ajax({
		dataType:"json",
		type:"post", 
		data:{"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id")},
		url:"/ES/ESServlet/findCard.action",
		success:function(data){
			if(jQuery.isEmptyObject(data)){
				
			}else{
				$.each(data,function(){
					$("#body").append("<div class=\"div_p_5 width_25\"><p class=\"p_5\">"+number+"</p><p class=\"p_5\">"+this.iid+"</p><p class=\"p_5\">"+this.card_number+"</p><p class=\"p_5_5 p_5_img\"><img src=\"./image/discheck.png\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/edit.png\" onclick=\"changeCard('"+this.card_number+"','"+this.iid+"')\"></p></div>");
					number++;
				})
			}
		}
	});
}
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
search_unit = function(){
	var card_number = prompt("请输入要查询的卡号")
	if(card_number!=null&&card_number.trim()!=""){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"card_number":card_number,"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id")},
			url:"/ES/ESServlet/seachCard.action",
			success:function(data){
				if(data.data!=null){
					alert(data.data);
				}else{
					ShowDiv('MyDiv2','fade2')
					$("#mydiv2_cont").append("<div class=\"div_p_3 margin_4 border_top\" id=\"show_close\"><p class=\"p_3_1\">编号</p><p class=\"p_3_2\">卡号</p><p class=\"p_3_3\">操作</p></div>");
					$("#mydiv2_cont").append("<div class=\"div_p_3\" id=\"show_close\"><p class=\"p_3_1\">"+data.iid+"</p><p class=\"p_3_2\">"+data.card_number+"</p><p class=\"p_3_3\"><img src=\"./image/delete.png\" class=\"img1_1\" onclick=\"deleteCard('"+data.iid+"')\"><img src=\"./image/edit.png\" onclick=\"changeCard('"+data.card_number+"','"+data.iid+"')\"></p></div>")
				}
			}
		});
	}else if(card_number==null){
		
	}else{
		alert("请输入要查询的卡号")
	}
}

add_unit = function(){
	if(validAuth()!="no"){
		ShowDiv('MyDiv3','fade3')
	}else{
		alert("权限不足")
	}
}

changeCard = function(change_number,iid){
	if(validAuth()!="no"){
		var card_number = prompt("请输入卡号")
		if(card_number!=null&&card_number!=""){
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"card_number":card_number,"keybox_id":getParam("keybox_id"),"change_number":change_number,"unit_id":getParam("unit_id"),"team_id":getParam("team_id"),"iid":iid},
				traditional: true,
				url:"/ES/ESServlet/changeCard.action",
				success:function(data){
					alert(data.data)
					if(data.data=="修改成功"){
						window.location="/ES/card.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+getParam("keybox_name");
					}
				}
			})
		}else if(card_number==null){
			
		}else{
			alert("请输入要修改成的卡号")
		}
	}else{
		alert("权限不足")
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
deleteCard = function(iid){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(validAuth()!="no"){
			arr.push(iid)
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"iids":arr,"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id")},
				traditional: true,
				url:"/ES/ESServlet/deleteCards.action",
				success:function(data){
					alert(data.data)
					if(data.data=="删除成功"){
						window.location="/ES/card.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+getParam("keybox_name");
					}
				}
			})
			arr.length = 0
		}else{
			alert("权限不足")
		}
	}
}
delete_unit = function(){
	if(validAuth()!="no"){
		if(arr.length>0){
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"iids":arr,"keybox_id":getParam("keybox_id"),"unit_id":getParam("unit_id"),"team_id":getParam("team_id")},
				traditional: true,
				url:"/ES/ESServlet/deleteCards.action",
				success:function(data){
					alert(data.data)
					if(data.data=="删除成功"){
						window.location="/ES/card.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+getParam("keybox_name");
					}
				}
			})
		}else{
			alert("请先选择要删除的卡")
		}
	}else{
		alert("权限不足")
	}
}
var change_id = null;

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
function CloseDiv1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
}
function CloseDiv2_3(show_div,bg_div){
	var iid = $("#iid").val()
	var card_number = $("#number").val().trim()
	var keybox_id = getParam("keybox_id");
	
	if(iid!=""&&card_number!=""){
		var reg = new RegExp("^[0-9]+$");
		if(!reg.test(iid)){
			alert("编号请输入数字！");
		}else {
			if(!(iid>=0&&iid<=2)){
				alert("普通卡编号在(0-2)之间！")
			}else{
				$.ajax({
					dataType:"json",
					type:"post", 
					data:{"card_number":card_number,"keybox_id":keybox_id,"unit_id":getParam("unit_id"),"iid":iid,"team_id":getParam("team_id")},
					url:"/ES/ESServlet/addCard.action",
					success:function(data){
						alert(data.data)
						if(data.data=="添加成功"){
							window.location="/ES/card.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&keybox_id="+getParam("keybox_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")+"&keybox_name="+getParam("keybox_name");
						}
					}
				})
			}
		}
	}else{
		alert("请输入完整卡信息")
	}
	document.getElementById(show_div).style.display='none';
	document.getElementById(bg_div).style.display='none';
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
</script>
</head>
<body id="body" class="mar_bot">
	<div id="show">
		<div id="cont_addsite">
			<div class="site_search_add_user"><button class="search_user" onclick="search_unit()">查询普通卡</button><button class="add_user" onclick="add_unit()">添加普通卡(编号0-2)</button><button class="delete_user" onclick="delete_unit()">删除普通卡</button></div>
			<div class="div_p_5 margin_4 width_25"><p class="p_5">序号</p><p class="p_5">编号</p><p class="p_5">卡号</p><p class="p_5_5">操作</p></div>
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
			<P class="tip_p">请输入卡信息！</P>
			<div id="cont" class="cente">
				<label class="lab_manager">编号：</label><input type="text" id="iid" class="inp_1"/><br/>
				<label class="lab_manager">卡号：</label><input type="text" id="number"  class="inp_1"/><br/>
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