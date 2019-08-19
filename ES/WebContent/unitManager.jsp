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
	$("#top_bnt",window.parent.document).text(getParam("unit_name"));
	$.ajax({
		dataType:"json",
		type:"post", 
		data:{"unit_id":getParam("unit_id"),"start":start},
		url:"/ES/ESServlet/findUnitManager.action",
		success:function(data){
			if(jQuery.isEmptyObject(data)){
				
			}else{
				$.each(data,function(){
					if(user_name==this.name||style=="level"){
						$("#body").append("<div class=\"unit_info\"><p class=\"unit_p1 padding18\">"+number+"</p><p class=\"unit_p2-1\">"+this.iid+"</p><p class=\"cont_p3\">"+this.name+"</p><p class=\"cont_p4\">"+this.password+"</p><p class=\"unit_p3\" onclick=\"changeAuth('"+this.iid+"','"+this.name+"')\"><img class=\"padding_right_none\" src=\"./image/auth.png\" ></p><p class=\"unit_p3\"><img class=\"img1\" src=\"./image/discheck.png\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/edit.png\" onclick=\"change('"+this.id+"','"+this.name+"')\"></p></div>");
					}else{
						$("#body").append("<div class=\"unit_info\"><p class=\"unit_p1 padding18\">"+number+"</p><p class=\"unit_p2-1\">"+this.iid+"</p><p class=\"cont_p3\">"+this.name+"</p><p class=\"cont_p4\">******</p><p class=\"unit_p3\" onclick=\"changeAuth('"+this.iid+"','"+this.name+"')\"><img class=\"padding_right_none\" src=\"./image/auth.png\" ></p><p class=\"unit_p3\"><img class=\"img1\" src=\"./image/discheck.png\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/edit.png\" onclick=\"change('"+this.id+"','"+this.name+"')\"></p></div>");
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
    			data:{"unit_id":getParam("unit_id"),"start":++start},
    			url:"/ES/ESServlet/findUnitManager.action",
    			success:function(data){
    				if(jQuery.isEmptyObject(data)){
    					
    				}else{
    					$.each(data,function(){
    						if(user_name==this.name||style=="level"){
    							$("#body").append("<div class=\"unit_info\"><p class=\"unit_p1 padding18\">"+number+"</p><p class=\"unit_p2-1\">"+this.iid+"</p><p class=\"cont_p3\">"+this.name+"</p><p class=\"cont_p4\">"+this.password+"</p><p class=\"unit_p3\" onclick=\"changeAuth('"+this.iid+"','"+this.name+"')\"><img class=\"padding_right_none\" src=\"./image/auth.png\" ></p><p class=\"unit_p3\"><img class=\"img1\" src=\"./image/discheck.png\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/edit.png\" onclick=\"change('"+this.id+"','"+this.name+"')\"></p></div>");
    						}else{
    							$("#body").append("<div class=\"unit_info\"><p class=\"unit_p1 padding18\">"+number+"</p><p class=\"unit_p2-1\">"+this.iid+"</p><p class=\"cont_p3\">"+this.name+"</p><p class=\"cont_p4\">******</p><p class=\"unit_p3\" onclick=\"changeAuth('"+this.iid+"','"+this.name+"')\"><img class=\"padding_right_none\" src=\"./image/auth.png\" ></p><p class=\"unit_p3\"><img class=\"img1\" src=\"./image/discheck.png\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/edit.png\" onclick=\"change('"+this.id+"','"+this.name+"')\"></p></div>");
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

add_levelManager = function(){
	if(style=="level"){
		ShowDiv('MyDiv3','fade3')
	}else{
		alert("仅调试者可进行此操作")
	}
}
function CloseDiv2_3(show_div,bg_div){
	var iid = $("input[id='levelManager_add_iid']").val().trim()
	var name = $("input[id='levelManager_add_name']").val().trim()
	var password = $("input[id='levelManager_add_password']").val().trim()
	
	if(name!=""&&password!=""&&iid!=""){
		if(name!=user_name&&name!="administrator"){
			var reg = new RegExp("^[0-9]+$");
			if(!reg.test(iid)){
				alert("编号请输入数字！");
			}else {
				if(!(iid>=0&&iid<=31)){
					alert("授权卡编号在(0——31)之间！")
				}else{
					$.ajax({  
						type:"post", 
						dataType: "json",
						data:{"name":name,"password":password,"unit_id":getParam("unit_id"),"iid":iid},
						url:"/ES/ESServlet/addUnitManager.action",
						success:function(data){
							alert(data.data)
							if(data.data=="添加成功"){
								window.location="/ES/unitManager.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
							}
						}
					});
				}
			}
		}else if(name==user_name){
			alert("名称重复")
		}
	}else{
		alert("请输入管理员名称、密码和编号")
	}
	document.getElementById(show_div).style.display='none';
	document.getElementById(bg_div).style.display='none';
}

var change_id = null;
var change_name = null;
change = function(id,name){
	if(style=="level"||style=="unit"){
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
		if(style=="level"){
			if(name!=user_name){
				$.ajax({  
					type:"post", 
					dataType: "json",
					data:{"name":name,"password":password,"id":change_id,"unit_id":getParam("unit_id")},
					url:"/ES/ESServlet/changeUnitManager.action",
					success:function(data){
						alert(data.data)
						if(data.data=="修改成功"){
							window.location="/ES/unitManager.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
						}
					}
				});
			}else{
				alert("名称已存在")
			}
		}else if(style=="unit"&&name==""&&password!=""&&change_name==user_name){
			$.ajax({  
				type:"post", 
				dataType: "json",
				data:{"name":name,"password":password,"id":change_id,"unit_id":getParam("unit_id")},
				url:"/ES/ESServlet/changeUnitManager.action",
				success:function(data){
					alert(data.data)
					if(data.data=="修改成功"){
						window.location="/ES/unitManager.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
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
	if($(event).attr("src")=="./image/discheck.png"){
		$(event).attr("src","./image/dui.png")
		arr.push(iid)
	}else {
		$(event).attr("src","./image/discheck.png")
		var index = arr.indexOf(iid)
		arr.splice(index,1);
	}
}

delete_levelManager = function(){
	var bln = confirm("确定删除此主设备吗?");
	if(bln){
		if(style=="level"){
			if(arr.length>0){
				$.ajax({
					dataType:"json",
					type:"post", 
					data:{"iids":arr,"unit_id":getParam("unit_id")},
					traditional: true,
					url:"/ES/ESServlet/deleteUnitManager.action",
					success:function(data){
						alert(data.data)
						if(data.data=="删除成功"){
							window.location="/ES/unitManager.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
						}
					}
				})
			}else{
				alert("请先选择要删除的管理员")
			}
		}else{
			alert("仅调试者可进行此操作")
		}
	}
}
deleteUnit = function(manager_number){
	var bln = confirm("确定删除此主设备吗?");
	if(bln){
		if(style=="level"){
			arr.push(manager_number)
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"iids":arr,"unit_id":getParam("unit_id")},
				traditional: true,
				url:"/ES/ESServlet/deleteUnitManager.action",
				success:function(data){
					alert(data.data)
					if(data.data=="删除成功"){
						window.location="/ES/unitManager.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
					}
				}
			})
			arr.length = 0
		}else{
			alert("仅调试者可进行此操作")
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
			data:{"name":name,"unit_id":getParam("unit_id")},
			url:"/ES/ESServlet/findUnitManagerByName.action",
			success:function(data){
				if(data.data!=null){
					alert(data.data);
				}else{
					ShowDiv('MyDiv2','fade2')
					$("#mydiv2_cont").append("<div class=\"div_p_4 font\" id=\"show_close\"><p class=\"p_5\">编号</p><p class=\"p_5\">管理员名称</p><p class=\"p_5\">密码</p><p class=\"p_5\">分配开锁权限</p><p class=\"p_5_5\">操作</p></div>");
					
					if(user_name==data.name||style=="level"){
						$("#mydiv2_cont").append("<div class=\"div_p_4\" id=\"show_close\"><p class=\"p_5\">"+data.iid+"</p><p class=\"p_5\">"+data.name+"</p><p class=\"p_5\">"+data.password+"</p><p class=\"p_5 p_4_img\" onclick=\"changeAuth('"+data.iid+"','"+data.name+"')\"><img src=\"./image/auth.png\"></p><p class=\"p_5_5 p_4_img\"><img src=\"./image/delete.png\" onclick=\"deleteUnit('"+data.iid+"')\"><img src=\"./image/edit.png\"  onclick=\"change('"+data.id+"','"+data.name+"')\"></p></div>")
					}else{
						$("#mydiv2_cont").append("<div class=\"div_p_4\" id=\"show_close\"><p class=\"p_5\">"+data.iid+"</p><p class=\"p_5\">"+data.name+"</p><p class=\"p_5\">******</p><p class=\"p_5 p_4_img\" onclick=\"changeAuth('"+data.iid+"','"+data.name+"')\"><img src=\"./image/auth.png\"></p><p class=\"p_5_5 p_4_img\"><img src=\"./image/delete.png\" onclick=\"deleteUnit('"+data.iid+"')\"><img src=\"./image/edit.png\"  onclick=\"change('"+data.id+"','"+data.name+"')\"></p></div>")
					}
				}
			}
		});
	}else if(name==null){
		
	}else{
		alert("请先选择要查询的管理员名称")
	}
}
changeAuth = function(iid,name){
	if(style=="level"){
		var url = "/ES/unitManagerAuth.jsp?unit_manager_id="+iid+"&unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name")+"&unitManager_name="+name;
		window.location=url;
	}else{
		alert("仅调试者可进行此操作")
	}
}
</script>
</head>
<body id="body" class="mar_bot">
	<div id="show">
		<div id="cont_addsite">
			<div class="site_search_add_user"><button class="search_user" onclick="search_levelManager()">查询管理员</button><button class="add_user" onclick="add_levelManager()">添加管理员(编号0——31)</button><button class="delete_user" onclick="delete_levelManager()">删除管理员</button></div>
			<div class="div_p_6"><p class="unit_p1">序号</p><p class="unit_p2 margin_left-1">编号</p><p class="unit_p2">管理员名称</p><p class="unit_p2 margin_left5">密码</p><p class="unit_p2">分配开锁权限</p><p class="unit_pright"><span id="site_sp1">操作</span></p></div>
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
				<label class="lab_manager">编号：</label><input type="text" id="levelManager_add_iid"  class="inp_1"/><br/>
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