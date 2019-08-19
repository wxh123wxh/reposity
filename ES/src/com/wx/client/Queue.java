package com.wx.client;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wx.entity.KeyBox1;
import com.wx.service.ServletService;
import com.wx.structure.Pack;
import com.wx.utils.SpringContextUtil;
import com.wx.client.SendInfo;
import com.wx.wx.ServletContextLTest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;

/**
 * 
 * @author Administrator
 *
 */
public class Queue implements Runnable{
	
	public static final Lock lock = new ReentrantLock();
	private static Log log = LogFactory.getLog(Queue.class);
	private static ServletService servletService = (ServletService)SpringContextUtil.getBean("servletService");
	private DatagramPacket packet = null;
	
	public static Timer timer;
	private static int theSize;  //队伍长度
	private static SendInfo[] theQueue = new SendInfo[2100]; //数组
	
	/**
	 * 初始化时把所有的箱添加到这个发送线程，用于检测是否在线
	 * @param list
	 */
	public static void addAll(ArrayList<KeyBox1> list) {
		lock.lock();
		try {
			Iterator<KeyBox1> iterator = list.iterator();
			while(iterator.hasNext()) {
				String maxc = iterator.next().getMaxc();
				if(theSize<2100){//不存在当数组长度未达到极限
					SendInfo sendInfo = new SendInfo(maxc);
					sendInfo.addList(null);
					theQueue[theSize++] = sendInfo;
				}else {//不存在且数组长度达到极限
					log.info("数组长度大于2100 丢弃");
				}
			}
		} finally {
			lock.unlock();
		}
	}
	/**
	 * 添加数据到数组
	 * @param maxc 数据所属箱的maxc地址
	 * @param data 要添加的数据
	 */
	public static void add(String maxc,Pack pack) {
		lock.lock();
		try {
			int search = Util.search(theQueue, theSize, maxc);//遍历数组查询指定箱
			if (search>=0) {//指定的箱的队列存在
				theQueue[search].addList(pack);
			}else if(theSize<2100){//不存在当数组长度未达到极限
				SendInfo sendInfo = new SendInfo(maxc);
				sendInfo.addList(pack);
				theQueue[theSize++] = sendInfo;
			}else {//不存在且数组长度达到极限
				log.info("数组长度大于2100 丢弃");
			}
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 获取数组指定索引处的箱maxc
	 * @param id
	 * @return
	 */
	public static String getMaxc(Integer id) {
		return theQueue[id].getMaxc();
	}
	/**
	 * 获取数组指定索引处的箱信息
	 * @param id
	 * @return
	 */
	public static Pack get(Integer id) {
		lock.lock();
		try {
			SendInfo sendInfo = theQueue[id];
			Object object = sendInfo.get();//获取指定索引处队列中的数据
			
			if (object!=null) {
				return (Pack) object;
			}else {
				return null;
			}
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 清空指定所有处的箱信息队列
	 * @param id
	 */
	public static void setEmpty(Integer id) {
		lock.lock();
		try {
			SendInfo sendInfo = theQueue[id];
			sendInfo.clear();
		} finally {
			lock.unlock();
		}
	}
	
	
	@Override
	public void run() {
		log.info("开启信息发送线程");
		while(true){
			try {
    			for(int a=0;a<theSize;a++) {//遍历数组
					String maxc = getMaxc(a);//获取指定所有处箱maxc地址
					KeyBox1 keyBox1 = ServletContextLTest.keyboxList.get(maxc);//在箱缓存集合中查询箱是否存在
					if (keyBox1!=null) {
						handleMessage(keyBox1, a, maxc);
					}else {
						setEmpty(a);//该索引不在箱的缓存集合中，表示其被删除，所以清空该箱的信息队列
					}
				}
    			Thread.sleep(100);
    		} catch (Exception e) {
    			log.error(e.getMessage(), e);
    		}
	    }
	}
	
	public void handleMessage(KeyBox1 keyBox1,int a,String maxc) throws IOException{
		if (System.currentTimeMillis()-keyBox1.getLastTime()>=60*1000) {//判断箱不在线
			Pack pack = get(a);
			if (pack!=null) {//获取到了箱要发送的信息
				send(keyBox1,pack,0);
			}
			//不管是否阻塞都要进行的操作
			if (keyBox1.getOnlines().equals("在线")) {
				servletService.updateKeyBoxOnlineById(keyBox1.getId(),"掉线");
			}
			ServletContextLTest.keyboxList.update(maxc, "掉线", 0, 999, 0,null,-1,0);//掉线则设置，掉线状态、上一次阻塞时间为0、等待状态关闭
		}else {
			if (keyBox1.getFlag()==1) {//非掉线，但处于等待状态
				if(System.currentTimeMillis()-keyBox1.getTime()>=1000&&keyBox1.getNumber()<=3) {//如果定时器步数>=3，表示等待超过3秒，无须再等待
					Pack pack = keyBox1.getPack();
					send(keyBox1,pack,1);
					ServletContextLTest.keyboxList.update(maxc, "999", keyBox1.getNumber()+1, 999, 999,null,-1,System.currentTimeMillis());//定时器步数加一，等待状态不变
				}else if(keyBox1.getNumber()>3){//如果定时器步数为1、2则重复发送命令
					ServletContextLTest.keyboxList.update(maxc, "999", 0, 999, 0,null,-1,0);//超过等待3s，上一次阻塞时间为0，等待状态关闭
				}
			}else {
				Pack pack = get(a);
				if (pack!=null) {//非掉线，非等待状态，且获取到箱要发送的信息
					ServletContextLTest.keyboxList.addPack(maxc, pack);//保存在未阻塞时发送的信息
					ServletContextLTest.keyboxList.update(maxc, "999", 1, 999, 1,null,-1,System.currentTimeMillis());//非等待状态发送信息后设置为等待
					send(keyBox1,pack,1);
				}
			}
		}
	}
	
	public void send(KeyBox1 keyBox1,Pack p,int style) throws IOException {
		byte[] data = p.serialize();
		log.info(Util.BinaryToHexString(data));
		Pack.valid(data);
		
		if (style==1) {
			if (p.getOrder_number()==0x31) {
				packet = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), 4035);
				UDPClient.socket2.send(packet);
			}else {
				packet = new DatagramPacket(data, data.length, keyBox1.getSendIp(), keyBox1.getSendPort());
				UDPClient.socket.send(packet);
			}
			log.info("在线发送命令："+Util.BinaryToHexString(data)+":"+new Date());
		}else {
			if (p.getOrder_number()==0x31) {//如果是ip配置信息则发送
				packet = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), 4035);
				UDPClient.socket2.send(packet);//掉线配置两次ip，因为一般掉线配置都是第一次配置，这时箱需要配置两次才有应答
				UDPClient.socket2.send(packet);
				log.info("掉线发送ip配置命令："+Util.BinaryToHexString(data)+":"+new Date());
			}
		}
	}
}
