package com.wx.wx;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wx.po.MqttConfig;
import com.wx.utils.MqttUtil;

/**
 *保存订阅、取消订阅和mqtt发送的消息 然后通过线程发送
 * @author Administrator
 *
 */
public class SendQuery implements Runnable{

	private static int front ;//队头
	private static int back;  //队尾
	private static int  theSize;  //队伍长度
	private static  String[] theQueue = new String[10000]; //数组
	
	private static Log log = LogFactory.getLog(SendQuery.class);
	public static final Lock lock = new ReentrantLock();
	private static final StringBuilder sb = new StringBuilder();
	private static final String str[] = {"{\"Pass\":\"","\"}","PANEL/","UNSUB"};
	

	public static void addList(String o) {
		lock.lock();
		try {
			 //判断队列是否已满
			if (theSize==theQueue.length) {
				
			}else {
				theQueue[back]=o;//在队列尾部添加新成员
				back=(1+back)%theQueue.length;
				theSize++;
			}
		} finally {
			lock.unlock();
		}
	}
	
	public static String get() {
		lock.lock();
		try {
			 //判断队列是否为空
			if (theSize==0) {
				return null;
			}else {
				String dequeueVal=theQueue[front];//不为空，返回队列的对头
				theSize--;
				front=(front+1)%theQueue.length;
				return dequeueVal;
			}
		} finally {
			lock.unlock();
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		while(!Thread.currentThread().interrupted()) {
			try {
				if(MqttConfig.getMqttClient()!=null&&MqttConfig.getMqttClient().isConnected()){//获取消息前先判断mqtt是否连接
					String string = SendQuery.get();
					if (string!=null) {
						if(string.contains(str[2])) {//判断是否为订阅、取消订阅的消息
							if (string.contains(str[3])) {
								MqttConfig.getMqttClient().unsubscribe(string.substring(string.indexOf("/")+1));
								log.info("取消订阅："+string);
							}else {
								MqttConfig.getMqttClient().subscribe(string,1);
								log.info("订阅："+string);
							}
							Thread.sleep(100);
						}else if (string.contains("SysTimer")) {//判断是否为配置时间的信息
							String[] split = string.split("&");
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							String data = format.format(new Date());
							MqttUtil.send("{\"Time\":\""+data+"\"}","PANEL/"+split[1]+"/Cfg/SysTimer");
						}else {
							String[] split = string.split("&");
							MqttUtil.send(sb.append(str[0]).append(split[0]).append(str[1]).toString(),str[2]+split[1]);
							sb.setLength(0);
							Thread.sleep(30);
						}
					}else {
						Thread.sleep(1000);
					}
				}else {
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				if (e instanceof InterruptedException) {
					log.error("关闭SendQuery线程");
					break;
				}
				log.error(e.getMessage(),e);
			}
		}
	}
}
