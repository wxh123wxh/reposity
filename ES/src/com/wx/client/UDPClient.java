package com.wx.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wx.entity.KeyBoxInfo;
import com.wx.structure.CardSetMessage;
import com.wx.structure.IpSetMessage;
import com.wx.structure.KeyOperateMessage;
import com.wx.structure.KeySetMessage;
import com.wx.structure.ReadKeyStateMessage;
import com.wx.structure.ReadLogMessage;
import com.wx.structure.UserSetMessage;


public class UDPClient {

	public static DatagramSocket socket;
	public static DatagramSocket socket2;
	private static Log log = LogFactory.getLog(UDPClient.class);
	public static int port;
	public static boolean running = false;
	private static Thread rThread;//读取信息的线程
	private static Thread qThread;//发送命令的线程
	private static Thread rqThread;//监听用户端口的线程
	private static Thread rrqThread;//监听广播端口的线程
	
	
	public static void connect(int pot){
		try {
			if(running) {
				return;
			}else {
				socket = new DatagramSocket(pot);//监听用户端口
				socket2 = new DatagramSocket(4035);//监听广播端口
				port = pot;
				running=true;
				rThread = new Thread(new ReceiveWatchDog()); //开启监听用户端口的线程
				rThread.start();
				qThread = new Thread(new Queue());//开启发送命令的线程
				qThread.start();
				rqThread = new Thread(new ReceiveQuery());//开启信息处理的线程
				rqThread.start();
				rrqThread = new Thread(new ReceiveQuery2());//开启监听广播端口的线程
				rrqThread.start();
				log.info("连接成功:"+port);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 断开连接
	 * @throws IOException 
	 */
	public static String stop(){
		if(running) {
			running=false;
			socket.close();
			rThread.interrupt();//关闭信息处理的线程
			Queue.timer.cancel();//停止发送信息的定时器
			qThread.interrupt();//关闭发送命令的线程
			rqThread.interrupt();//关闭监听用户端口的线程
			rrqThread.interrupt();//关闭监听广播端口的线程
			log.info("断开连接："+port);
			return "已断开连接";
		}else {
			return "连接不存在";
		}
	}
	
	/**
	 * UDP发送回复
	 * @throws IOException
	 */
	public static void sendReply(byte [] bs,DatagramPacket p){
		try {
			DatagramPacket packet = new DatagramPacket(bs, bs.length,p.getAddress(),p.getPort());
			socket.send(packet);
			log.info("发送应答命令："+Util.BinaryToHexString(bs)+":"+new Date()+":"+packet.getAddress()+":"+packet.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 4．	单个锁控制命令
	 * @param address1 箱maxc地址
	 * @param iid 箱下钥匙编号
	 * @throws IOException
	 */
	public static void sendLockOperate(String address1,int iid,int state){
		KeyOperateMessage message = new KeyOperateMessage(address1, iid, state);
		Queue.add(address1, message);
	}
	
	/**
	 * 5．	卡配置命令
	 * @param address1 箱maxc地址
	 * @param style 1：注册；2：注销；3：暂停；4：暂停终止
	 * @param iid 卡iid
	 * @param card_number	卡card_number
	 * @throws IOException
	 */
	public static void sendCardDeploy(String address1,int style,int iid,String card_number){
		CardSetMessage message = new CardSetMessage(address1, style, iid, card_number);
		Queue.add(address1, message);
	}
	

	/**
	 * 6．	用户配置命令
	 * @param address1 箱maxc地址
	 * @param style 1：增加；2：删除；3：更新； 
	 * @param iid 用户编号
	 * @param name 用户汉字名称
	 * @throws IOException
	 */
	public static void sendUserDeploy(String address1,int style,int iid,String name){
		UserSetMessage message = new UserSetMessage(address1, style, iid, name);
		Queue.add(address1, message);
	}
	
	/**
	 * 7.	锁配置命令
	 * @param address1 箱maxc地址
	 * @param style 1：增加；2：删除；3：更新； 
	 * @param iid 箱下钥匙编号
	 * @param name 箱下钥匙名称
	 * @throws IOException
	 */
	public static void sendLockDeploy(String address1,int style,int iid,String name,String card_id){
		log.info("sc"+address1+":"+style+":"+iid+":"+name+":"+card_id);
		KeySetMessage message = new KeySetMessage(address1, style, iid, name, card_id);
		log.info("锁配置命令:"+message);
		Queue.add(address1, message);
	}
	
	
	/**
	 * 8.	送读钥匙状态命令
	 * @param address1 箱maxc地址
	 * @param iid 箱下钥匙编号 如果=0xFFFF：读取所有锁状态
	 * @throws IOException
	 */
	public static void sendGetLockState(String address1){
		ReadKeyStateMessage message = new ReadKeyStateMessage(address1);
		Queue.add(address1, message);
	}
	
	/**
	 * 10.	送读操作记录命令
	 * @param address1 设备的通讯机地址
	 * @param style 操作类型:02删除远程记录 01读取远程记录
	 * @throws IOException
	 */
	public static void sendGetOperateLog(String address1,int style){
		ReadLogMessage message = new ReadLogMessage(address1, style);
		Queue.add(address1, message);
	}
	
	
	
	/**
	 * 15.IP参数命令
	 * @param KeyBoxInfo 箱信息对象
	 * @param style 01：配置；02：读取；03：上报
	 * @throws IOException
	 */
	public static void sendSetIpConfige(KeyBoxInfo keyBox,int style){
		IpSetMessage message = new IpSetMessage(keyBox.getMaxc(), style, keyBox.getPassword(), keyBox.getIp(), keyBox.getIp_mask(), keyBox.getIp_gatewey(), keyBox.getCent1_ip(), keyBox.getCent2_ip(), keyBox.getCent3_ip(), 
				keyBox.getCent4_ip(), keyBox.getCent1_sourcePort(), keyBox.getCent1_destPort(), keyBox.getCent2_sourcePort(), keyBox.getCent3_destPort(), keyBox.getCent3_sourcePort(), keyBox.getCent3_destPort(), keyBox.getCent4_sourcePort(), keyBox.getCent4_destPort());
		Queue.add(keyBox.getMaxc(), message);
	}
}
