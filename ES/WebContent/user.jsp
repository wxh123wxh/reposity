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
window.onload=function(){
	document.getElementById("cont_addsite").style.display='block';
	$("#top_bnt",window.parent.document).text(getParam("team_name"));
	$.ajax({
		dataType:"json",
		type:"post", 
		data:{"team_id":getParam("team_id"),"start":start,"unit_id":getParam("unit_id")},
		url:"/ES/ESServlet/findUser.action",
		success:function(data){
			if(jQuery.isEmptyObject(data)){
				
			}else{
				$.each(data,function(){
					$("#body").append("<div class=\"div_p_5\"><p class=\"p_5\">"+number+"</p><p class=\"p_5\">"+this.iid+"</p><p class=\"p_5\">"+this.name+"</p><p class=\"p_5 p_5_img\" onclick=\"changeUser('"+this.id+"','"+this.iid+"')\"><img src=\"./image/edit.png\"></p><p class=\"p_5_5 p_5_img\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/discheck.png\"></p></div>");
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
    			url:"/ES/ESServlet/findUser.action",
    			success:function(data){
    				if(jQuery.isEmptyObject(data)){
    					
    				}else{
    					$.each(data,function(){
    						$("#body").append("<div class=\"div_p_5\"><p class=\"p_5\">"+number+"</p><p class=\"p_5\">"+this.iid+"</p><p class=\"p_5\">"+this.name+"</p><p class=\"p_5 p_5_img\" onclick=\"changeUser('"+this.id+"','"+this.iid+"')\"><img src=\"./image/edit.png\"></p><p class=\"p_5_5 p_5_img\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/discheck.png\"></p></div>");
    						number++;
    					})
    				}
    			}
    		});
  		 }
	});
});

add_user = function(){
	if(validAuth()!="no"){
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
function CloseDiv2_1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
	 $("#mydiv2_cont").html("")
}
search_user = function(){
	var name = prompt("请输入使用者名称")

	if(name!=null&&name!=""){
		$.ajax({  
			type:"post", 
			dataType: "json",
			data:{"team_id":getParam("team_id"),"name":name,"unit_id":getParam("unit_id")},
			url:"/ES/ESServlet/searchUser.action",
			success:function(data){
				if(data.data!=null){
					alert(data.data)
				}else{
					ShowDiv('MyDiv2','fade2')
					$("#mydiv2_cont").append("<div class=\"div_p_4 font\" id=\"show_close\"><p class=\"p_4\">编号</p><p class=\"p_4\">使用者名称</p><p class=\"p_4\">修改</p><p class=\"p_4_4\">删除</p></div>");
					$("#mydiv2_cont").append("<div class=\"div_p_4\" id=\"show_close\"><p class=\"p_4\">"+data.iid+"</p><p class=\"p_4\">"+data.name+"</p><p class=\"p_4 p_4_img\" onclick=\"changeUser('"+data.id+"','"+data.iid+"')\"><img src=\"./image/edit.png\"></p><p class=\"p_4_4 p_4_img\" onclick=\"deleteUsers('"+data.iid+"')\"><img src=\"./image/delete.png\"></p></div>")
				}
			}
		});
	}
}
changeUser = function(id,iid){
	if(validAuth()!="no"){
		var name = prompt("请输入要修改成的名称")
	
		if(name!=null&&name!=""){
			$.ajax({  
				type:"post", 
				dataType: "json",
				data:{"name":name,"id":id,"team_id":getParam("team_id"),"unit_id":getParam("unit_id"),"iid":iid},
				url:"/ES/ESServlet/changeUser.action",
				success:function(data){
					alert(data.data)
					if(data.data=="修改成功"){
						window.location="/ES/user.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name");
					}
				}
			});
		}
	}else{
		alert("权限不足")
	}
}
var arr = new Array;
check = function(iid,event){
	if($(event).children().eq(0).attr("src")=="./image/discheck.png"){
		$(event).children().eq(0).attr("src","./image/dui.png")
		arr.push(iid)
	}else {
		$(event).children().eq(0).attr("src","./image/discheck.png")
		var index =arr.indexOf(iid)
		arr.splice(index,1);
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
					data:{"iids":arr,"team_id":getParam("team_id"),"unit_id":getParam("unit_id")},
					traditional: true,
					url:"/ES/ESServlet/deleteUsers.action",
					success:function(data){
						alert(data.data)
						if(data.data=="删除成功"){
							window.location="/ES/user.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name");
						}
					}
				}) 
			}
		}else{
			alert("权限不足")
		}
	}
}
deleteUsers = function(iid){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(validAuth()!="no"){
			arr.push(iid)
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"iids":arr,"team_id":getParam("team_id"),"unit_id":getParam("unit_id")},
				traditional: true,
				url:"/ES/ESServlet/deleteUsers.action",
				success:function(data){
					alert(data.data)
					if(data.data=="删除成功"){
						window.location="/ES/user.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name");
					}
				}
			}) 
			arr.length = 0
		}else{
			alert("权限不足")
		}
	}
}
function CloseDiv1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
}

function CloseDiv2_3(show_div,bg_div){
	var unit_id = getParam("unit_id");
	var iid = $("#add_user_iid").val().trim();
	var name = $("#add_user_name").val().trim();
	
	if(name!=""&&iid!=""){
		var reg = new RegExp("^[0-9]+$");
		if(!reg.test(iid)){
			alert("编号请输入数字！");
		}else {
			if(!(iid>=35&&iid<=98)){
				alert("使用者编号在(35——98)之间！")
			}else{
				$.ajax({  
					type:"post", 
					dataType: "json",
					data:{"team_id":getParam("team_id"),"name":name,"iid":iid,"unit_id":unit_id},
					url:"/ES/ESServlet/addUser.action",
					success:function(data){
						alert(data.data)
						if(data.data=="添加成功"){
							window.location="/ES/user.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name");
						}
					}
				});
			}
		}
	}else{
		alert("请输入要添加的使用者名称和编号")
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
<body id="body"  class="mar_bot">
	<div id="show">
		<div id="cont_addsite">
			<div class="site_search_add_user"><button class="search_user" onclick="search_user()">查询使用者</button><button class="add_user" onclick="add_user()">添加使用者(编号35——98)</button><button class="delete_user" onclick="delete_user()">删除使用者</button></div>
			<div class="div_p_5 margin_4"><p class="p_5">序号</p><p class="p_5">编号</p><p class="p_5">使用者名称</p><p class="p_5">修改</p><p class="p_5_5">删除</p></div>
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
			<P class="tip_p">添加班组</P>
			<div id="cont" class="cente">
				<label class="lab_manager">编号：</label><input type="text" id="add_user_iid" class="inp_1"/><br/>
				<label class="lab_manager">名称：</label><input type="text" id="add_user_name"  class="inp_1"/><br/>
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