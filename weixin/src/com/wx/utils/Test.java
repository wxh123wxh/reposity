package com.wx.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wx.po.Template;
import com.wx.po.TemplateParam;
import com.wx.wx.WXServlet;


public class Test {


	    public static void main(String[] args) throws Exception {
	    	String str[] = {"UTF-8","ACCESS_TOKEN","errcode","errmsg","模板消息发送成功","模板消息发送失败:","40001重发","异步请求取消",
	        		"APPID","APPSECRET","expires_in","#00DD00","","yyyy-MM-dd  HH:mm:ss","first","您的设备报警了","#FF3333",
	        		"keyword1","#0044BB","keyword2","keyword3","#BBBBBB","请尽快处理","remark","keyword4","headimgurl",
	        		"nickname","city","openid","CODE","GET","access_token","OPENID","40001","SECRET"};
		    for(int i=0;i<str.length;i++) {
		    	System.out.println(i+":"+str[i]);
		    }
	    }

	    public static Template geTemplateAddUser(String first,String nickname,String fromUserName,String remark,String url) {
	    	String keyword2 = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date());
	    	Template tem=new Template();  
			tem.setTemplateId(WXServlet.TEMPLATE_ID_ADD_USER);  
			tem.setTopColor("#bbbbbb");
			tem.setUrl(url);  
			
			List<TemplateParam> paras=new ArrayList<TemplateParam>();  
			paras.add(new TemplateParam("first",first,"#FF3333"));  
			paras.add(new TemplateParam("keyword1",nickname,"#0044BB"));  
			paras.add(new TemplateParam("keyword2",keyword2,"#0044BB"));
			paras.add(new TemplateParam("remark",remark,"#BBBBBB")); 
			          
			tem.setTemplateParamList(paras);
			tem.setToUser(fromUserName);
			return tem;
	    }
	    
}
