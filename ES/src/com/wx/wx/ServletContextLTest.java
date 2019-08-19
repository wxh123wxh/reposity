package com.wx.wx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wx.client.KeyBox1List;
import com.wx.client.Queue;
import com.wx.entity.Config;
import com.wx.entity.KeyBox1;
import com.wx.service.ServletService;
import com.wx.utils.DatabaseBackup;


public class ServletContextLTest implements ServletContextListener{

	public static KeyBox1List keyboxList;
	private Log log = LogFactory.getLog(ServletContextListener.class);
	ServletService servlet = null;
	public static Config config;
	Timer timer = new Timer();
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if(servlet!=null) {
			servlet.stop();
		}
		timer.cancel();
		log.info("关闭程序");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
        Object obj = context.getBean( "servletService" );
        servlet = (ServletService)obj;
        
        ArrayList<KeyBox1> list = (ArrayList<KeyBox1>) servlet.findAllKeyBox();
        Collections.sort(list);
        keyboxList = new KeyBox1List(list);//初始化加载所有箱的缓存集合
        Queue.addAll(list);
       
        config = servlet.findConfig();
        servlet.connect(config.getReceivePort());//初始化时开启监听、信息处理等线程
    	
        log.info("初始化程序");
    	timer.scheduleAtFixedRate(new TimerTask() {//定时器定时检测备份
    		public void run() {
    			try {
    				if (config!=null) {
			        	long millis = System.currentTimeMillis();
			        	long b = millis-config.getSetTime();//当前时间和设置备份时间的间隔
			        	log.info("查询备份："+b+":"+config.getIntervalDay()*24*60*60*1000);	
			        	
			        	if (b>=config.getIntervalDay()*24*60*60*1000) {//间隔时间大于设置的备份天数
			        		DatabaseBackup bak = servlet.getDatabaseBackup();
			        		bak.backup("lock_db");//备份数据
			        		servlet.setConfigTime(millis,config.getIntervalDay());
			        		config.setSetTime(millis);//更新备份的设置时间
			        		log.info("自动备份数据库");
						}
			        }
    			} catch (Exception e) {
    				log.error(e.getMessage(),e);
    			}
    		}
    	}, 0, 1000*60*60);
	}
}
