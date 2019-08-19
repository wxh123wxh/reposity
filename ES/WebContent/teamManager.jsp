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
var start = 0; //查询的开始位置
var user_name = "<%=request.getSession().getAttribute("user")%>"
var style = "<%=request.getSession().getAttribute("style")%>"

window.onload=function(){
	document.getElementById("cont_addsite").style.display='block';
	$("#top_bnt",window.parent.document).text(getParam("team_name"));
	$.ajax({
		dataType:"json",
		type:"post", 
		data:{"team_id":getParam("team_id"),"start":start,"unit_id":getParam("unit_id")},
		url:"/ES/ESServlet/findTeamManager.action",
		success:function(data){
			if(jQuery.isEmptyObject(data)){
				
			}else{
				$.each(data,function(){
					if(style=="team"&&user_name!=this.name){
						$("#body").append("<div class=\"unit_info\"><p class=\"unit_p1 padding18\">"+number+"</p><p class=\"unit_p2-1\">"+this.iid+"</p><p class=\"cont_p3\">"+this.name+"</p><p class=\"cont_p4\">******</p><p class=\"unit_p3\" onclick=\"change('"+this.id+"','"+this.name+"')\"><img class=\"padding_right_none\" src=\"./image/edit.png\"></p><p class=\"unit_p3\" onclick=\"check('"+this.iid+"',this)\"><img class=\"img1\" src=\"./image/discheck.png\"></p></div>");
					}else{
						$("#body").append("<div class=\"unit_info\"><p class=\"unit_p1 padding18\">"+number+"</p><p class=\"unit_p2-1\">"+this.iid+"</p><p class=\"cont_p3\">"+this.name+"</p><p class=\"cont_p4\">"+this.password+"</p><p class=\"unit_p3\" onclick=\"change('"+this.id+"','"+this.name+"')\"><img class=\"padding_right_none\" src=\"./image/edit.png\"></p><p class=\"unit_p3\" onclick=\"check('"+this.iid+"',this)\"><img class=\"img1\" src=\"./image/discheck.png\"></p></div>");
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
    			data:{"team_id":getParam("team_id"),"start":++start},
    			url:"/ES/ESServlet/findTeamManager.action",
    			success:function(data){
    				if(jQuery.isEmptyObject(data)){
    					
    				}else{
    					$.each(data,function(){
    						if(style=="team"&&user_name!=this.name){
    							$("#body").append("<div class=\"unit_info\"><p class=\"unit_p1 padding18\">"+number+"</p><p class=\"unit_p2-1\">"+this.iid+"</p><p class=\"cont_p3\">"+this.name+"</p><p class=\"cont_p4\">******</p><p class=\"unit_p3\" onclick=\"change('"+this.id+"','"+this.name+"')\"><img class=\"padding_right_none\" src=\"./image/edit.png\"></p><p class=\"unit_p3\" onclick=\"check('"+this.iid+"',this)\"><img class=\"img1\" src=\"./image/discheck.png\"></p></div>");
    						}else{
    							$("#body").append("<div class=\"unit_info\"><p class=\"unit_p1 padding18\">"+number+"</p><p class=\"unit_p2-1\">"+this.iid+"</p><p class=\"cont_p3\">"+this.name+"</p><p class=\"cont_p4\">"+this.password+"</p><p class=\"unit_p3\" onclick=\"change('"+this.id+"','"+this.name+"')\"><img class=\"padding_right_none\" src=\"./image/edit.png\"></p><p class=\"unit_p3\" onclick=\"check('"+this.iid+"',this)\"><img class=\"img1\" src=\"./image/discheck.png\"></p></div>");
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

add_levelManager = function(){
	if(style!="team"){
		ShowDiv('MyDiv3','fade3')
	}else{
		alert("权限不足")
	}
}
function CloseDiv2_3(show_div,bg_div){
	var iid = $("input[id='levelManager_add_iid']").val().trim()
	var name = $("input[id='levelManager_add_name']").val().trim()
	var password = $("input[id='levelManager_add_password']").val().trim()
	
	if(name!=""&&password!=""&&iid!=""){
		if(name!="administrator"&&name!=user_name){
			var reg = new RegExp("^[0-9]+$");
			if(!reg.test(iid)){
				alert("编号请输入数字！");
			}else {
				if(!(iid>=32&&iid<=34)){
					alert("编号在(32——34)之间！")
				}else{
					$.ajax({  
						type:"post", 
						dataType: "json",
						data:{"iid":iid,"name":name,"password":password,"team_id":getParam("team_id"),"unit_id":getParam("unit_id")},
						url:"/ES/ESServlet/addTeamManager.action",
						success:function(data){
							alert(data.data)
							if(data.data=="添加成功"){
								window.location="/ES/teamManager.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")
							}
						}
					});
				}
			}
		}else{
			alert("名称重复")
		}
	}else{
		alert("请输入管理员名称和密码")
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

var change_id = null;
var change_name = null;
change = function(id,name){
	if(validAuth()!="no"){
		change_id = id;
		change_name = name;
		ShowDiv('MyDiv3_2','fade3_2')
	}else{
		alert("权限不足")
	}
}
function CloseDiv2_3_2(show_div,bg_div){
	var name = $("input[id='levelManager_add_name_2']").val().trim()
	var password = $("input[id='levelManager_add_password_2']").val().trim()
	
	if(name!=""||password!=""){
		if(style!="team"){
			if(name!="administrator"){
				$.ajax({  
					type:"post", 
					dataType: "json",
					data:{"name":name,"password":password,"id":change_id,"unit_id":getParam("unit_id")},
					url:"/ES/ESServlet/changeTeamManager.action",
					success:function(data){
						alert(data.data)
						if(data.data=="修改成功"){
							window.location="/ES/teamManager.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name")
						}
					}
				});
			}else{
				alert("名称已存在")
			}
		}else if(style=="team"&&name==""&&password!=""&&change_name==user_name){
			$.ajax({  
				type:"post", 
				dataType: "json",
				data:{"name":name,"password":password,"id":change_id,"unit_id":getParam("unit_id")},
				url:"/ES/ESServlet/changeTeamManager.action",
				success:function(data){
					alert(data.data)
					if(data.data=="修改成功"){
						window.location="/ES/teamManager.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name");
					}
				}
			});
		}else{
			alert("只能修改自己的密码")
		}
	}else{
		alert("最少修改一项信息")
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
}

var arr = new Array();
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

delete_levelManager = function(){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(style!="team"){
			if(arr.length>0){
				$.ajax({
					dataType:"json",
					type:"post", 
					data:{"iids":arr,"team_id":getParam("team_id"),"unit_id":getParam("unit_id")},
					traditional: true,
					url:"/ES/ESServlet/deleteTeamManager.action",
					success:function(data){
						alert(data.data)
						if(data.data=="删除成功"){
							window.location="/ES/teamManager.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name");
						}
					}
				})
			}else{
				alert("请先选择要删除的管理员")
			}
		}else{
			alert("权限不足")
		}
	}
}
deleteUnit = function(iid){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(style=="level"){
			arr.push(iid)
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"iids":arr,"team_id":getParam("team_id"),"unit_id":getParam("unit_id")},
				traditional: true,
				url:"/ES/ESServlet/deleteTeamManager.action",
				success:function(data){
					alert(data.data)
					if(data.data=="删除成功"){
						window.location="/ES/teamManager.jsp?unit_id="+getParam("unit_id")+"&team_id="+getParam("team_id")+"&unit_name="+getParam("unit_name")+"&team_name="+getParam("team_name");
					}
				}
			})
			arr.length = 0
		}else{
			alert("权限不足")
		}
	}
}

//关闭弹出层
function CloseDiv2_1(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
	 $("#mydiv2_cont").html("")
}
search_levelManager = function(){
	var name = prompt("请输入要查询的管理员名称")
	
	if(name!=null&&name.trim()!=""){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"name":name,"team_id":getParam("team_id"),"unit_id":getParam("unit_id")},
			url:"/ES/ESServlet/findTeamManagerByName.action",
			success:function(data){
				if(data.data!=null){
					alert(data.data);
				}else{
					ShowDiv('MyDiv2','fade2')
					$("#mydiv2_cont").append("<div class=\"div_p_4 font\" id=\"show_close\"><p class=\"p_5\">编号</p><p class=\"p_5\">管理员名称</p><p class=\"p_5\">密码</p><p class=\"p_5\">修改</p><p class=\"p_5_5\">删除</p></div>");
					if(style=="team"&&user_name!=data.name){
						$("#mydiv2_cont").append("<div class=\"div_p_4\" id=\"show_close\"><p class=\"p_5\">"+data.iid+"</p><p class=\"p_5\">"+data.name+"</p><p class=\"p_5\">******</p><p class=\"p_5 p_4_img\" onclick=\"deleteUnit('"+data.iid+"')\"><img src=\"./image/delete.png\"></p><p class=\"p_5_5 p_4_img\" onclick=\"change('"+data.id+"','"+data.name+"')\"><img src=\"./image/edit.png\"></p></div>")
					}else{
						$("#mydiv2_cont").append("<div class=\"div_p_4\" id=\"show_close\"><p class=\"p_5\">"+data.iid+"</p><p class=\"p_5\">"+data.name+"</p><p class=\"p_5\">"+data.password+"</p><p class=\"p_5 p_4_img\" onclick=\"deleteUnit('"+data.iid+"')\"><img src=\"./image/delete.png\"></p><p class=\"p_5_5 p_4_img\" onclick=\"change('"+data.id+"','"+data.name+"')\"><img src=\"./image/edit.png\"></p></div>")
					}
				}
			}
		});
	}else if(name==null){
		
	}else{
		alert("请先选择要查询的管理员名称")
	}
}
</script>
</head>
<body id="body" class="mar_bot">
	<div id="show">
		<div id="cont_addsite">
			<div class="site_search_add_user"><button class="search_user" onclick="search_levelManager()">查询管理员</button><button class="add_user" onclick="add_levelManager()">添加管理员(编号32——34)</button><button class="delete_user" onclick="delete_levelManager()">删除管理员</button></div>
			<div class="div_p_6"><p class="unit_p1">序号</p><p class="unit_p2 margin_left-1">编号</p><p class="unit_p2">管理员名称</p><p class="unit_p2 margin_left5">密码</p><p class="unit_p2">修改</p><p class="unit_pright"><span id="site_sp1">删除</span></p></div>
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
			<P class="tip_p">请输入管理员信息！</P>
			<div id="cont" class="cente">
				<label class="lab_manager">编号：</label><input type="text" id="levelManager_add_iid" class="inp_1"/><br/>
				<label class="lab_manager">名称：</label><input type="text" id="levelManager_add_name" class="inp_1"/><br/>
				<label class="lab_manager">密码：</label><input type="text" id="levelManager_add_password"  class="inp_1"/><br/>
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
	
	<div id="fade3_2" class="black_overlay"></div>
	<div id="MyDiv3_2" class="white_content">
		<div>
			<P class="tip_p">请输入要修改的信息！</P>
			<div id="cont" class="cente">
				<label class="lab_manager">名称：</label><input type="text" id="levelManager_add_name_2" class="inp_1"/><br/>
				<label class="lab_manager">密码：</label><input type="text" id="levelManager_add_password_2"  class="inp_1"/><br/>
			</div>
			<div>
				<div class="center"
					onclick="CloseDiv1('MyDiv3_2','fade3_2')">
					<input type="button" value="取消" class="border_none">
				</div>
				<div class="center" onclick="CloseDiv2_3_2('MyDiv3_2','fade3_2')">
					<input type="button" value="确定" class="border_none">
				</div>
			</div>
		</div>
	</div>
</body>
</html>