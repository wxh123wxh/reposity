<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>消息设置</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/weui.min.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
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
window.onload=function(){
	var arr = ["set_sub_arming","set_sub_statu","set_sub_alarm"];
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post",
		data:{"openId":getParam("openId"),"sub_id":getParam("sub_id")},
		url:"/weixin/servlet/getMessage.action", 
		success:function(data){ 
			if(jQuery.isEmptyObject(data)){
			}else {
				$.each(data,function(index){
					var inde = $.inArray(index,arr);
					if(index=="id"){
						$("#but").val(this);	
					}else if(index=="openId"){
						$("#openId").val(this);
					}else {
						if(this==0){
							$("img").eq(inde*2).attr("src","../image/check.png");
							$("img").eq(inde*2+1).attr("src","../image/discheck.png");
						}else if(this==1){
							$("img").eq(inde*2).attr("src","../image/discheck.png");
							$("img").eq(inde*2+1).attr("src","../image/check.png");
						}
					}
			    })
			}
		} 
	});
	checked();
}
function checked(){
	var len = $("div[id='check']").length;
	for(var i = 0; i < len; i++){
		$("div[id='check']")[i].onclick = function(){
			if($(this).children("div:first-child").children("img:first-child").attr("src")=="../image/check.png"){
				$(this).children("div:first-child").children("img:first-child").attr("src","../image/discheck.png");
				$(this).children("div:last-child").children("img:first-child").attr("src","../image/check.png");
			}else{
				$(this).children("div:first-child").children("img:first-child").attr("src","../image/check.png");
				$(this).children("div:last-child").children("img:first-child").attr("src","../image/discheck.png");
			}
		}
	}
}

$(function() {
	pushHistory();
	window.addEventListener('popstate', function(e) {
	    //自定义操作//检查子页面有没有返回maxc_id
		 window.location.href = "/weixin/operate/sub_information.jsp?openId="+getParam("openId")+"&id="+getParam("zId")+"&number="+Math.random();
	}, false);
})
function pushHistory() {
	var state = {
		title: "title",
	    url: "#"
	};
	window.history.pushState(state, "title", "#");
}


function submit_from(){
	if(flag){
		var id = $("#but").val();
		var string = "";
		var arr = new Array(0,2,4);
		for(var i = 0; i < arr.length; i++){
			if($("img").eq(arr[i]).attr("src")=="../image/check.png"){
				string = string+"0";
			}else{
				string = string+"1";
			}
		}
		flag = false;
		if(id!=""&&id>=0){//表示存在消息接收设置 则更新 否则添加
			$.ajax({  
				dataType:"json",    //数据类型为json格式
				type:"post",
				data:{"data":string,"id":id},
				url:"/weixin/servlet/setMessage.action", 
				complete:function(){
					flag = true;
					var url = "/weixin/operate/sub_information.jsp?id="+getParam("zId")+"&openId="+getParam("openId");
					window.location=url;
				}
			});
		}else{
			$.ajax({  
				dataType:"json",    //数据类型为json格式
				type:"post",
				data:{"data":string,"openId":getParam("openId"),"sub_id":getParam("sub_id")},
				url:"/weixin/servlet/insertMessage.action", 
				complete:function(){
					flag = true;
					var url = "/weixin/operate/sub_information.jsp?id="+getParam("zId")+"&openId="+getParam("openId");
					window.location=url;
				}
			});
		}
	}
}


</script>
</head>
<body>
	<div id="subDeviceLog">
		<div id="deviceinfo">
			<div>
				<p>设备报警消息</p>
			</div>
			<div id="check">
				<div>
					<img src="../image/check.png">
					<button>接收</button>
				</div>
				<div>
					<img src="../image/discheck.png">
					<button>拒收</button>
				</div>
			</div>
		</div>
		<div id="deviceinfo">
			<div>
				<p>设备状态消息</p>
			</div>
			<div id="check">
				<div>
					<img src="../image/check.png">
					<button>接收</button>
				</div>
				<div>
					<img src="../image/discheck.png">
					<button>拒收</button>
				</div>
			</div>
		</div>
		<div id="deviceinfo">
			<div>
				<p>设备布撤防消息</p>
			</div>
			<div id="check">
				<div>
					<img src="../image/check.png">
					<button>接收</button>
				</div>
				<div>
					<img src="../image/discheck.png">
					<button>拒收</button>
				</div>
			</div>
		</div>
		<div id="confrim" onclick="submit_from()">
			<p>
				<img src="../image/confrim.png" />
				<button type="button">确认</button>
			</p>
		</div>
	</div>
	<input type="hidden" id="but">
	<input type="hidden" id="openId">
</body>
</html>