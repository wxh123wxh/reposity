package com.wx.client;

import java.net.DatagramPacket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReceiveQuery2 implements Runnable{
	private Log log = LogFactory.getLog(ReceiveQuery2.class);
	
	public void run() {
		log.info("开启监听广播线程");
		while(UDPClient.running){
			try {
				byte [] buf = new byte[70];
				DatagramPacket p = new DatagramPacket(buf, buf.length);
				
				UDPClient.socket2.receive(p);//监听用户端口,端口由用户设置，每次设置后要重启服务器
				ReceiveQuery.addList(p);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} 
		}
	}
}
