package com.wx.wx;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;


public class ServletContextLTest implements ServletContextListener{

	private Log log = LogFactory.getLog(ServletContextListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			List<Object> list = InitAccessTokenServlet.LIST;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof Thread) {
					Thread thread = (Thread)list.get(i);
					thread.interrupt();
				}else if (list.get(i) instanceof ExecutorService) {
					ExecutorService pool = (ExecutorService)list.get(i);
					try {  
						pool.shutdown();  
						// (所有的任务都结束的时候，返回TRUE)  
						if(!pool.awaitTermination(3, TimeUnit.SECONDS)){  
							// 超时的时候向线程池中所有的线程发出中断(interrupted)。  
							pool.shutdownNow();  
							if (!pool.awaitTermination(3, TimeUnit.SECONDS))
								log.error("Pool did not terminate");
						}  
					} catch (InterruptedException e) {  
						// awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。  
						log.error("awaitTermination interrupted: " + e);  
						pool.shutdownNow();  
					}  
				}else if (list.get(i) instanceof CloseableHttpAsyncClient) {
					CloseableHttpAsyncClient client = (CloseableHttpAsyncClient)list.get(i);
					try {
						client.close();
						log.info("关闭异步发送客户端");
					} catch (IOException e) {
						log.error(e.getMessage(),e);
					}
				}else if (list.get(i) instanceof Timer) {
					Timer timer = (Timer)list.get(i);
					timer.cancel();
					log.info("关闭定时器");
				}else if (list.get(i) instanceof MqttClient) {
					MqttClient mqttClient = (MqttClient)list.get(i);
					try {
						InitAccessTokenServlet.flag = false;
						mqttClient.disconnect();
						mqttClient.close();
						log.info("关闭mqtt连接");
					}catch (Exception e) {
						log.error(e.getMessage(),e);
					}
				}
				Thread.sleep(10);
			}
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		log.info("程序开始运行");
	}

}
