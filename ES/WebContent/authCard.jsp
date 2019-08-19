<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
var start = 0; //查询的开始位置
var style = "<%=request.getSession().getAttribute("style")%>"

window.onload=function(){
	document.getElementById("cont_addsite").style.display='block';
	$("#top_bnt",window.parent.document).text(getParam("unit_name"));
	$.ajax({
		dataType:"json",
		type:"post", 
		data:{"unit_id":getParam("unit_id"),"start":start},
		url:"/ES/ESServlet/findUnitCard.action",
		success:function(data){
			if(jQuery.isEmptyObject(data)){
				
			}else{
				$.each(data,function(){
					$("#body").append("<div class=\"unit_info\"><p class=\"unit_p1 padding18\">"+number+"</p><p class=\"unit_p2-1\">"+this.iid+"</p><p class=\"cont_p3\">"+this.card_number+"</p><p class=\"cont_p4\">"+this.auth_name+"</p><p class=\"unit_p3\" onclick=\"changeAuth('"+this.iid+"','"+this.card_number+"','"+this.auth_name+"')\"><img class=\"padding_right_none\" src=\"./image/auth.png\" ></p><p class=\"unit_p3\"><img class=\"img1\" src=\"./image/discheck.png\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/edit.png\" onclick=\"change('"+this.card_number+"','"+this.iid+"')\"></p></div>");
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
    			url:"/ES/ESServlet/findUnitCard.action",
    			success:function(data){
    				if(jQuery.isEmptyObject(data)){
    					
    				}else{
    					$.each(data,function(){
    						$("#body").append("<div class=\"unit_info\"><p class=\"unit_p1 padding18\">"+number+"</p><p class=\"unit_p2-1\">"+this.iid+"</p><p class=\"cont_p3\">"+this.card_number+"</p><p class=\"cont_p4\">"+this.auth_name+"</p><p class=\"unit_p3\" onclick=\"changeAuth('"+this.iid+"','"+this.card_number+"','"+this.auth_name+"')\"><img class=\"padding_right_none\" src=\"./image/auth.png\" ></p><p class=\"unit_p3\"><img class=\"img1\" src=\"./image/discheck.png\" onclick=\"check('"+this.iid+"',this)\"><img src=\"./image/edit.png\" onclick=\"change('"+this.card_number+"','"+this.iid+"')\"></p></div>");
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
search_unit = function(){
	var card_number = prompt("请输入要查询的卡号")
	if(card_number!=null&&card_number.trim()!=""){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"card_number":card_number,"unit_id":getParam("unit_id")},
			url:"/ES/ESServlet/findUnitCardByNumber.action",
			success:function(data){
				if(data.data!=null){
					alert(data.data);
				}else{
					ShowDiv('MyDiv2','fade2')
					$("#mydiv2_cont").append("<div class=\"div_p_4 font\" id=\"show_close\"><p class=\"p_5\">编号</p><p class=\"p_5\">卡号</p><p class=\"p_5\">授权人</p><p class=\"p_5\">授权</p><p class=\"p_5_5\">操作</p></div>");
					$("#mydiv2_cont").append("<div class=\"div_p_4\" id=\"show_close\"><p class=\"p_5\">"+data.iid+"</p><p class=\"p_5\">"+data.card_number+"</p><p class=\"p_5\">"+data.auth_name+"</p><p class=\"p_5 p_4_img\" onclick=\"changeAuth('"+data.iid+"','"+data.card_number+"','"+data.auth_id+"')\"><img src=\"./image/auth.png\"></p><p class=\"p_5_5 p_4_img\"><img src=\"./image/delete.png\" onclick=\"deleteUnitCard('"+data.iid+"')\"><img src=\"./image/edit.png\"  onclick=\"change('"+data.card_number+"','"+data.iid+"')\"></p></div>")
				}
			}
		});
	}else if(card_number==null){
		
	}else{
		alert("请输入要查询的卡号")
	}
}

add_unit = function(){
	if(style=="level"){
		ShowDiv('MyDiv3','fade3')
	}else{
		alert("仅调试者可进行此操作")
	}
}
change = function(change_number,iid){
	if(style=="level"){
		var card_number = prompt("请输入卡号")
		if(card_number!=null&&card_number!=""){
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"card_number":card_number,"unit_id":getParam("unit_id"),"change_number":change_number,"iid":iid},
				url:"/ES/ESServlet/changeUnitCard.action",
				success:function(data){
					alert(data.data)
					if(data.data=="修改成功"){
						window.location="/ES/authCard.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name")+"&number="+Math.random();
					}
				}
			})
		}else if(card_number==null){
			
		}else{
			alert("请输入要修改成的卡号")
		}
	}else{
		alert("仅调试者可进行此操作")
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
deleteUnitCard = function(iid){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(style=="level"){
			arr.push(iid)
			$.ajax({
				dataType:"json",
				type:"post", 
				data:{"iids":arr,"unit_id":getParam("unit_id")},
				traditional: true,
				url:"/ES/ESServlet/deleteUnitCards.action",
				success:function(data){
					alert(data.data)
					if(data.data=="删除成功"){
						window.location="/ES/authCard.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
					}
				}
			})
			arr.length = 0
		}else{
			alert("仅调试者可进行此操作")
		}
	}
}
delete_unit = function(){
	var bln = confirm("确定删除吗?");
	if(bln){
		if(style=="level"){
			if(arr.length>0){
				$.ajax({
					dataType:"json",
					type:"post", 
					data:{"iids":arr,"unit_id":getParam("unit_id")},
					traditional: true,
					url:"/ES/ESServlet/deleteUnitCards.action",
					success:function(data){
						alert(data.data)
						if(data.data=="删除成功"){
							window.location="/ES/authCard.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
						}
					}
				})
			}else{
				alert("请先选择要删除的卡")
			}
		}else{
			alert("仅调试者可进行此操作")
		}
	}
}

var change_iid = null;
var change_number = null;
var change_auth_name = null;
changeAuth = function(iid,card_number,auth_name){
	if(style=="level"){
		change_iid = iid;
		change_number = card_number
		change_auth_name = auth_name
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"unit_id":getParam("unit_id")},
			url:"/ES/ESServlet/findAllManager.action",
			success:function(data){
				ShowDiv('MyDiv4','fade4_2')
				if(jQuery.isEmptyObject(data)){
					$("#manager_name").append("<option value=\"-1\">没有未绑定卡的管理员了</option>");   
				}else{
					$.each(data,function(){
						$("#manager_name").append("<option value=\""+this.iid+"\" style=\""+this.style+"\">"+this.name+"</option>");   
					})
				}
			}
		});
	}else{
		alert("仅调试者可进行此操作")
	}
}

function ShowDiv(show_div,bg_div){
	document.getElementById(show_div).style.display='block';
	document.getElementById(bg_div).style.display='block' ;
	var bgdiv = document.getElementById(bg_div);
	bgdiv.style.width = document.body.scrollWidth; 
	$("#"+bg_div).height($(document).height());
};
function CloseDiv1_4(show_div,bg_div){
	 document.getElementById(show_div).style.display='none';
	 document.getElementById(bg_div).style.display='none';
	 $("#manager_name").html("")
	 change_iid = null;
	 change_number = null;
	 change_auth_name = null;
}
function CloseDiv2_4(show_div,bg_div){
	var auth_id = $("#manager_name option:selected").val().trim();
	var auth_name = $("#manager_name option:selected").text().trim();
	var style = $("#manager_name option:selected").attr("style");
	
	if(auth_id!=-1&&auth_name!=change_auth_name){
		$.ajax({
			dataType:"json",
			type:"post", 
			data:{"iid":change_iid,"auth_id":auth_id,"auth_name":auth_name,"card_number":change_number,"style":style,"unit_id":getParam("unit_id")},
			url:"/ES/ESServlet/changeAuth.action",
			success:function(data){
				alert(data.data)
				if(data.data=="设置成功"){
					window.location="/ES/authCard.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
				}
			}
		});
	}
	document.getElementById(show_div).style.display='none';
	document.getElementById(bg_div).style.display='none';
	$("#manager_name").html("")
	change_id = null;
	change_number = null;
	change_auth_id = null;
}

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
	var unit_id = getParam("unit_id");
	
	if(iid!=""&&card_number!=""){
		var reg = new RegExp("^[0-9]+$");
		if(!reg.test(iid)){
			alert("编号请输入数字！");
		}else {
			if(!(iid>=101&&iid<=180)){
				alert("授权卡编号在(101-180)之间！")
			}else{
				$.ajax({
					dataType:"json",
					type:"post", 
					data:{"card_number":card_number,"unit_id":unit_id,"iid":iid},
					url:"/ES/ESServlet/addUnitCard.action",
					success:function(data){
						alert(data.data)
						if(data.data=="添加成功"){
							window.location="/ES/authCard.jsp?unit_id="+getParam("unit_id")+"&unit_name="+getParam("unit_name");
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
</script>
</head>
<body id="body" class="mar_bot">
	<div id="show">
		<div id="cont_addsite">
			<div class="site_search_add_user"><button class="search_user" onclick="search_unit()">查询授权卡</button><button class="add_user" onclick="add_unit()">添加授权卡(编号101-180)</button><button class="delete_user" onclick="delete_unit()">删除授权卡</button></div>
			<div class="div_p_6"><p class="unit_p1">序号</p><p class="unit_p2 margin_left-1">编号</p><p class="unit_p2">卡号</p><p class="unit_p2 margin_left5">授权人</p><p class="unit_p2">授权</p><p class="unit_pright"><span id="site_sp1">操作</span></p></div>
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
	
	<div id="fade4_2" class="black_overlay"></div>
	<div id="MyDiv4" class="white_content">
		<div>
			<P class="tip_p margin_24_10">请选择管理员</P>
			<div id="cont" class="cente">
				<label class="lab_manager">管理员名称：</label><select id="manager_name" class="select1"></select><br/>
			</div>
			<div>
				<div class="center"
					onclick="CloseDiv1_4('MyDiv4','fade4_2')">
					<input type="button" value="取消" class="border_none">
				</div>
				<div class="center" onclick="CloseDiv2_4('MyDiv4','fade4_2')">
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