package com.wx.wx;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wx.po.Template;
import com.wx.utils.CommonUtil;
import com.wx.utils.TokenThread;
/**
 * 发送模板消息的线程
 * @author Administrator
 *
 */
public class SendTemp implements Runnable{
	private String string[] = {"CHANGE","该用户已是一个管理员，不能再添加！","请重新添加其他用户","","ADD","/","您成功添加了一个用户，请为他分配设备！",
			"请点击该消息分配设备","http://ajbtest.com/weixin/user/addUser.jsp?glid=","PANEL","DISCONNECT","&gzid="};
	private Log log = LogFactory.getLog(SendTemp.class);
	private StringBuilder sb = new StringBuilder();
	
	@Override
	public void run() {
		Timer timer = new Timer();
		InitAccessTokenServlet.LIST.add(timer);
	    timer.scheduleAtFixedRate(new TimerTask() {
	    	public void run() {
	    		try {
	    			if (!OpenIdList.OpenId_map.isEmpty()) {
	    				for(int a=1;a<=OpenIdList.OpenId_map.size();a++) {
	    					if (!OpenIdList.OpenId_map.get(a).equals(OpenIdList.QUERY_EMPTY)) {
	    						String message = OpenIdList.get(a);
	    						if (message!=null) {
	    							if (message.indexOf(string[0])==0) {//该用户已是一个管理员，不能再添加
	    	    						String[] split = message.split(string[5]);
	    	    						Template template = CommonUtil.geTemplateAddUser(string[1], split[1], OpenIdList.OpenId_map.get(a), string[2], string[3]);
	    	    						CommonUtil.acynPost(TokenThread.accessToken, template);
	    	    					}else if (message.indexOf(string[4])==0) {//添加用户模板信息
	    	    						String[] split = message.split(string[5]);
	    	    						Template template = CommonUtil.geTemplateAddUser(string[6], split[2], OpenIdList.OpenId_map.get(a), string[7],sb.append(string[8]).append(OpenIdList.OpenId_map.get(a)).append(string[11]).append(split[1]).toString());
	    	    						CommonUtil.acynPost(TokenThread.accessToken, template);
	    	    						sb.setLength(0);
	    	    					}else if (message.indexOf(string[9])==0) {
	    	    						String[] split = message.split(string[5]);
    	    							Template template = null;
    	    							
	    	    						if (split[2].contains("——报警")) {
	    	    							template = CommonUtil.geTemplate(split[1],split[2],OpenIdList.OpenId_map.get(a),"#FF3333");
										}else if (split[2].contains("——撤防")) {
											template = CommonUtil.geTemplate(split[1],split[2],OpenIdList.OpenId_map.get(a),"#0044BB");
										}else if (split[2].contains("——布防")) {
											template = CommonUtil.geTemplate(split[1],split[2],OpenIdList.OpenId_map.get(a),"#00BB32");
										}else {
											template = CommonUtil.geTemplate(split[1],split[2],OpenIdList.OpenId_map.get(a),"#0E0E0E");
										}
	    	    						CommonUtil.acynPost(TokenThread.accessToken, template);
	    	    					}
		    					}
							}
	    				}
	    			}
	    		} catch (Exception e) {
	    			log.error(e.getMessage(),e);
	    		}
	    	}
	    }, 0, 1500);
	}
}
