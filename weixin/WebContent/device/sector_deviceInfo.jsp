<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>防区详情</title>
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
window.onload=function(){
	var id = getParam("id");
	var arr1 = ["sec_id","name"];
	var arr2 = ["arming_status","online","alarm","tamper","DC","AC"];
	$.ajax({  
		dataType:"json",    //数据类型为json格式
		type:"post", 
		data:{"id":id},
		url:"/weixin/servlet/findSerctorById.action", 
		success:function(data){ 
			for (var key in data) {
				var index1 = $.inArray(key,arr1);
				var index2 = $.inArray(key,arr2);
				
				if(index1>=0){
					$("#"+arr1[index1]).val(data[key]);
				}else if(index2>=0){
					if(key!="arming_status"){
						if(data[key]==0){
							$("#"+key+"1").attr("src","../image/check.png");
							$("#"+key+"2").attr("src","../image/discheck.png");
						}else{
							$("#"+key+"2").attr("src","../image/check.png");
							$("#"+key+"1").attr("src","../image/discheck.png");
						}
					}else{
						if(data[key]==0){
							$("#"+key+"1").attr("src","../image/discheck.png");
							$("#"+key+"2").attr("src","../image/check.png");
						}else{
							$("#"+key+"2").attr("src","../image/discheck.png");
							$("#"+key+"1").attr("src","../image/check.png");
						}
					}
				}
			}
		}  
	});
}
function submit_from(){
	var zid = getParam("zid");
	var maxc_id = getParam("maxc_id");
	var openId = getParam("openId");
	var url = "/weixin/device/sector.jsp?id="+zid+"&openId="+openId+"&zid="+maxc_id;
	window.location=url;
}

</script>
</head>
<body>
	<div id="subDeviceLog">
		<div id="deviceinfo">
			<div>
				<p>防区名称</p>
			</div>
			<div>
				<input id="name" readonly="readonly" />
			</div>
		</div>
		<div id="deviceinfo">
			<div>
				<p>防区编号</p>
			</div>
			<div>
				<input id="sec_id" readonly="readonly" />
			</div>
		</div>
		<div id="deviceinfo">
			<div>
				<p>在线状态</p>
			</div>
			<div id="check">
				<div>
					<img src="../image/discheck.png" id="online1">
					<button>掉线</button>
				</div>
				<div>
					<img src="../image/check.png" id="online2">
					<button>在线</button>
				</div>
			</div>
		</div>
		<div id="deviceinfo">
			<div>
				<p>布防状态</p>
			</div>
			<div id="check">
				<div>
					<img src="../image/discheck.png" id="arming_status1">
					<button>撤防</button>
				</div>
				<div>
					<img src="../image/check.png" id="arming_status2">
					<button>布防</button>
				</div>
			</div>
		</div>
		<div id="deviceinfo">
			<div>
				<p>报警状态</p>
			</div>
			<div id="check">
				<div>
					<img src="../image/discheck.png" id="alarm1">
					<button>正常</button>
				</div>
				<div>
					<img src="../image/check.png" id="alarm2">
					<button>报警</button>
				</div>
			</div>
		</div>
		<div id="deviceinfo">
			<div>
				<p>被撬状态</p>
			</div>
			<div id="check">
				<div>
					<img src="../image/discheck.png" id="tamper1">
					<button>正常</button>
				</div>
				<div>
					<img src="../image/check.png" id="tamper2">
					<button>被撬</button>
				</div>
			</div>
		</div>
		<div id="deviceinfo">
			<div>
				<p>欠压状态</p>
			</div>
			<div id="check">
				<div>
					<img src="../image/discheck.png" id="DC1">
					<button>正常</button>
				</div>
				<div>
					<img src="../image/check.png" id="DC2">
					<button>欠压</button>
				</div>
			</div>
		</div>
		<div id="deviceinfo">
			<div>
				<p>断电状态</p>
			</div>
			<div id="check">
				<div>
					<img src="../image/discheck.png" id="AC1">
					<button>正常</button>
				</div>
				<div>
					<img src="../image/check.png" id="AC2">
					<button>断电</button>
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
</body>
</html>