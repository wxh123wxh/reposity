package com.wx.utils;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.wx.po.MqttConfig;
import com.wx.po.MyMqttCallback;
import com.wx.service.ServletService;
import com.wx.wx.InitAccessTokenServlet;
import com.wx.wx.SendQuery;


/**
 * MQTT客户端连接
 * @author Administrator
 *
 */
public class MqttUtil {
	
	private static final String str[] = {"utf-8","发送的消息：","PANEL/MAXC_IDF01/#","mqtt connect ok","MAXC_ID","UNSUB/PANEL/MAXC_IDF01/#"};
	private static final Log log = LogFactory.getLog(MqttUtil.class);
	public static final String clientName = "AjbWeixinPublic";
	public static final String endpoint = "ssl://es6100endpoint.mqtt.iot.gz.baidubce.com:1884";//输入创建endpoint返回的SSL地址
	public static final String username = "es6100endpoint/9e84e4c84089"; //输入创建thing返回的username
	public static final String password = "hKfb/CtTp2tJaYi5kCerVPVs8uxMLvkeuw6OYBXEQqY=";//输入创建principal返回的password
	private static ServletService servletService = (ServletService)SpringTool.getBean("servletService");
	
	public static MqttConnectOptions getOptions() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		// 创建SSL连接
		//TrustManagerFactory此类充当基于信任材料源的信任管理器的工厂。每个信任管理器管理特定类型的由安全套接字使用的信任材料
		//  X509支持的加密算法
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
		tmf.init((KeyStore)null);// 用证书授权源和相关的信任材料初始化此工厂
		TrustManager[] trustManagers = tmf.getTrustManagers();// 为每种信任材料返回一个信任管理器。
					 
		SSLContext ctx = SSLContext.getInstance("TLS");//SSLContext此类的实例表示安全套接字协议的实现,getInstance("TLS")生成实现指定安全套接字协议的 SSLContext 对象。
		ctx.init(null, trustManagers, null);// 初始化此上下文。
		// 配置MQTT连接
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		options.setUserName(username);
		options.setPassword(password.toCharArray());
		options.setSocketFactory(ctx.getSocketFactory());
		return options;
	}
	
	/**
	 * mqtt连接时订阅数据库中所有设备
	 * @param client mqtt客户端
	 */
	public static void subscribe(MqttClient client) {
		try {
			List<String> list = servletService.findAllMaxc_id();
			for(int a=0;a<list.size();a++) {
				SendQuery.addList(str[2].replace(str[4], list.get(a)));
			}
			read(client);//订阅后开启mqtt的回调类MqttCallback，用于读取接收的信息
			log.info(str[3]);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 通过设备maxc_id 订阅该设备
	 * @param maxc_id  设备maxc_id
	 */
	public static void subscribe2(String maxc_id) {
		try {
			SendQuery.addList(str[2].replace(str[4], maxc_id));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * mqtt连接
	 * @throws MqttException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	public static void connect() throws MqttException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		if(MqttConfig.getMqttClient()==null) {//如果当前没有mqtt客户端实例
			
		}else if(!MqttConfig.getMqttClient().isConnected()){//如果当前mqtt的tcp未连接
			MqttConfig.getMqttClient().close();//关闭当前mqtt客户端
			InitAccessTokenServlet.LIST.remove(MqttConfig.getMqttClient());//把这个关闭的客户端移出LIST
		}else if (MqttConfig.getMqttClient().isConnected()) {//如果当前mqtt的tcp已连接
			MqttConfig.getMqttClient().disconnect();//关闭当前mqtt的tcp连接
			MqttConfig.getMqttClient().close();//关闭当前mqtt客户端
			InitAccessTokenServlet.LIST.remove(MqttConfig.getMqttClient());//把这个关闭的客户端移出LIST
		}
		MqttClient client = new MqttClient(MqttUtil.endpoint,MqttUtil.clientName);//创建新的mqtt客户端
		client.connect(MqttUtil.getOptions());//设置mqtt连接配置
		subscribe(client);//订阅主题
		MqttConfig.setMqttClient(client);//保存mqtt客户端
		InitAccessTokenServlet.LIST.add(MqttConfig.getMqttClient());//把客户端加入LIST，用于关闭软件时进行摧毁
	}
	
	/**
	 * 发送信息到指定主题
	 * @param content	信息内容
	 * @param topic		要发送到的主题
	 */
	public static void send(String content,String topic){
		MqttMessage message = new MqttMessage();
		try {
			message.setPayload(content.getBytes(str[0]));//设置消息及信息字符格式
			message.setQos(1);//设置消息质量
			InitAccessTokenServlet.cachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						MqttConfig.getMqttClient().publish(topic, message);//发送信息到指定主题
					}  catch (Exception e) {
						log.error(e.getMessage(),e);
					}
				}
			});
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		log.info(new StringBuilder().append(str[1]).append(topic).append(content).toString());
	}
		
	/**
	 * 开启mqtt回调类，用于读取接收的消息
	 * @param client
	 * @throws MqttException
	 */
	public static void read(MqttClient client) throws MqttException {
		MyMqttCallback mqttCallback = new MyMqttCallback(client);
		client.setCallback(mqttCallback);
	}
}

