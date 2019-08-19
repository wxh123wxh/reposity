package com.wx.wx;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import com.wx.utils.HttpAsynClient;
import com.wx.utils.MqttUtil;
import com.wx.utils.TokenThread;

/** 
 * 初始化各个线程并创建mqtt连接
 * @author 胡汉三 
 * 
 */  
public class InitAccessTokenServlet extends HttpServlet{  
    
	private static final long serialVersionUID = 1L;  
    private Log log = LogFactory.getLog(InitAccessTokenServlet.class);
    public static final ExecutorService cachedThreadPool =  Executors.newFixedThreadPool(8);//初始化线程池
    public static final CloseableHttpAsyncClient httpclient = HttpAsynClient.getAsynClient();//初始化异步http客户端
    public static final List<Object> LIST = new ArrayList<Object>();
    public static volatile boolean flag = true;//mqtt摧毁状态，为true表示未摧毁则可以重新连接
    
    public void init() throws ServletException { 
        //获取web.xml中配置的参数  
        TokenThread.appid = getInitParameter("appid");  
        TokenThread.appsecret = getInitParameter("appsecret"); 
       
        while (true){
            try {//如果没有发生异常说明连接成功，如果发生异常，则死循环
            	Thread.sleep(20000);
                MqttUtil.connect();
                break;
            }catch (Exception e){
                continue;
            }
        }
        
        if("".equals(TokenThread.appid) || "".equals(TokenThread.appsecret)){
            log.error("appid和appsecret未给出");  
        }else {  
            Thread thread2 = new Thread(new TokenThread(),"t"); 
            Thread thread3 = new Thread(new SendTemp(),"send"); 
            Thread thread4 = new Thread(new LogQuery(),"q"); 
            Thread thread5 = new Thread(new HandleEquipmentMoreDisOnlineQuery(),"h");
            Thread thread6 = new Thread(new OpenQuery(),"o");
            Thread thread7 = new Thread(new ReadQuery(),"r");
            thread7.setPriority(7);
            Thread thread = new Thread(new SendQuery(),"s");
            thread.setPriority(6);
            //开启各线程
            thread.start();
            thread2.start();
            thread3.start();
            thread4.start();
            thread5.start();
            thread6.start();
            thread7.start();
            //把需要关闭软件时摧毁的线程和对象加入LIST
            LIST.add(thread);
            LIST.add(thread2);
            LIST.add(thread3);
            LIST.add(thread4);
            LIST.add(thread5);
            LIST.add(thread6);
            LIST.add(thread7);
            LIST.add(cachedThreadPool);
            LIST.add(httpclient);
        }  
    }  
} 