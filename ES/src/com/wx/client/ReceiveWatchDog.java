package com.wx.client;

import java.net.DatagramPacket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ReceiveWatchDog implements Runnable{
	private Log log = LogFactory.getLog(ReceiveWatchDog.class);
	
	public void run() {
		log.info("开启监听用户端口线程");
		while(UDPClient.running){
			try {
				byte [] buf = new byte[70];
				DatagramPacket p = new DatagramPacket(buf, buf.length);
				//在监听的同时通过数据包获取ip或者端口很耗时间，不要在监听线程中使用
				//log.info("接收信息1："+Util.BinaryToHexString(p.getData())+":"+p.getAddress().getHostName()+":"+p.getPort());
				
				UDPClient.socket.receive(p);//监听4015端口，此端口为广播端口，用于ip配置相关命令
				ReceiveQuery.addList(p);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} 
		}
	}
	
}
