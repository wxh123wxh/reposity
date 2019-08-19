package com.wx.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wx.entity.KeyBox1;
import com.wx.service.ServletService;
import com.wx.structure.BrokenBackMessage;
import com.wx.structure.BrokenBackReplyMessage;
import com.wx.structure.BrokenMessage;
import com.wx.structure.BrokenReplyMessage;
import com.wx.structure.KeyAllStateMessage;
import com.wx.structure.KeyAllStateReplyMessage;
import com.wx.structure.KeyStateChangeMessage;
import com.wx.structure.KeyStateChangeReplyMessage;
import com.wx.structure.OnlineReplyMessage;
import com.wx.structure.OperateLogIndexMessage;
import com.wx.structure.OperateLogIndexReplyMessage;
import com.wx.structure.Pack;
import com.wx.structure.SwingCardLogMessage;
import com.wx.structure.SwingCardLogReplyMessage;
import com.wx.structure.UserOperateLogMessage;
import com.wx.structure.UserOperateLogReplyMessage;
import com.wx.utils.SpringContextUtil;
import com.wx.wx.ServletContextLTest;

public class ReceiveQuery implements Runnable{
	private static int front ;//队头
	private static int back;  //队尾
	public static int  theSize;  //队伍长度
	
	private Log log = LogFactory.getLog(ReceiveQuery.class);
	private byte [] data = new byte[70];
	private static DatagramPacket[] theQueue = new DatagramPacket[30000]; //数组
	public static final Lock lock = new ReentrantLock();
	private static ServletService servletService = (ServletService)SpringContextUtil.getBean("servletService");
	
	/**
	 * 把监听的信息加入队列
	 * @param o
	 */
	public static void addList(DatagramPacket o) {
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
	
	/**
	 * 获取对头信息
	 * @return
	 */
	public static DatagramPacket get() {
		lock.lock();
		try {
			if (theSize==0) {
				return null;
			}else {
				DatagramPacket dequeueVal=theQueue[front];//不为空，返回队列的对头
				theSize--;
				front=(front+1)%theQueue.length;
				return dequeueVal;
			}
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 清空队列
	 */
	public static void clear() {
		lock.lock();
		try {
			theSize = 0;
			front = back;
		} finally {
			lock.unlock();
		}
	}
	

	public void run() {
		log.info("开启信息处理线程");
		while(true){
			try {
				DatagramPacket p = get();
				if (p!=null) {//通讯必须先给应答，然后处理数据
					data = p.getData();
					log.info("接收信息："+Util.BinaryToHexString(p.getData())+":"+p.getAddress());
					Pack pack = Pack.deserialize(data);
					String maxc = pack.getMaxc();
					KeyBox1 search = ServletContextLTest.keyboxList.get(maxc);
					
					if (search!=null&&Util.valid(data)) {
						updataKeyBoxState(pack,search,p);
						if (pack.getOrder_number()==0x01) {//0x01	在线命令
							OnlineReplyMessage message = new OnlineReplyMessage(maxc);
							
							sendReply(message,p);
							log.info("在线命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (pack.getOrder_number()==0x03) {//0x03	设备故障命令
							BrokenMessage broken = (BrokenMessage)pack;
							BrokenReplyMessage message = new BrokenReplyMessage(maxc, broken.getSub_iid());
							
							sendReply(message,p);
							log.info("设备故障命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (pack.getOrder_number()==0x04) {//0x04	设备故障恢复命令
							BrokenBackMessage broken = (BrokenBackMessage)pack;
							BrokenBackReplyMessage message = new BrokenBackReplyMessage(maxc, broken.getSub_iid());
							
							sendReply(message,p);
							log.info("设备故障恢复命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (pack.getOrder_number()==0x85) {//0x85	单个锁控制命令的应答命令
							validFlag(search,pack);
							log.info("单个锁控制命令的应答命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (pack.getOrder_number()==0x86) {//0x86	卡配置命令的应答命令
							validFlag(search,pack);
							log.info("卡配置命令的应答命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (pack.getOrder_number()==0x87) {//0x87	用户配置的应答命令
							validFlag(search,pack);
							log.info("用户配置的应答命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (pack.getOrder_number()==0x88) {//0x88	锁配置命令的应答命令
							validFlag(search,pack);
							log.info("锁配置命令的应答命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (pack.getOrder_number()==0x90) {//0xA0	读钥匙状态应答命令
							validFlag(search,pack);
							log.info("读钥匙状态应答命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (pack.getOrder_number()==0x24) {//0x24	13.	钥匙所有状态上报命令
							KeyAllStateMessage allStateMessage = (KeyAllStateMessage)pack;
							KeyAllStateReplyMessage message = new KeyAllStateReplyMessage(maxc, allStateMessage.getCount());
							
							sendReply(message, p);
							//修改钥匙所有状态
							MessageHandle.updateAllKeyState(allStateMessage.getKey_all_state(), maxc);
							log.info("钥匙所有状态上报命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (pack.getOrder_number()==0x91) {//0x91	读操作记录应答命令
							validFlag(search, pack);
							log.info("读操作记录应答命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (pack.getOrder_number()==0x25) {//0x25	14.	操作记录索引上报命令
							OperateLogIndexMessage indexMessage = (OperateLogIndexMessage)pack;
							OperateLogIndexReplyMessage message = new OperateLogIndexReplyMessage(maxc, indexMessage.getCount());
							
							sendReply(message, p);
							ServletContextLTest.keyboxList.updateLogNumber(maxc, indexMessage.getCount());//设置远程箱上报的记录数
							log.info("操作记录索引上报命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (Byte.toUnsignedInt(data[8])==0x21) {//0x21	刷卡上报命令
							SwingCardLogMessage logMessage = (SwingCardLogMessage)pack;
							SwingCardLogReplyMessage message = new SwingCardLogReplyMessage(maxc, logMessage.getStyle(), logMessage.getCard_iid(), logMessage.getCard_number(), logMessage.getKey_iid());
							
							sendReply(message, p);
							//添加刷卡记录
							MessageHandle.addCardLog(search, logMessage);
							log.info("刷卡上报命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (Byte.toUnsignedInt(data[8])==0x22) {//0x22	用户操作上报命令
							UserOperateLogMessage logMessage = (UserOperateLogMessage)pack;
							UserOperateLogReplyMessage message = new UserOperateLogReplyMessage(maxc, logMessage.getStyle(), logMessage.getUser_iid(), logMessage.getKey_iid());
							
							sendReply(message, p);
							//添加用户操作记录
							MessageHandle.addUserLog(search, logMessage);
							log.info("用户操作上报命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (Byte.toUnsignedInt(data[8])==0x23) {//0x23	钥匙状态改变上报命令
							KeyStateChangeMessage changeMessage = (KeyStateChangeMessage)pack;
							KeyStateChangeReplyMessage message = new KeyStateChangeReplyMessage(maxc, changeMessage.getStyle(), changeMessage.getKey_iid());
							
							sendReply(message, p);
							//修改钥匙状态
							MessageHandle.updataKeyState(search, changeMessage);
							log.info("钥匙状态改变上报命令："+Util.BinaryToHexString(data)+":"+new Date());
						}else if (Byte.toUnsignedInt(data[8])==0xB1) {//0xB1	IP参数应答命令
							log.info("IP参数应答命令："+Util.BinaryToHexString(data)+":"+new Date());
							ServletContextLTest.keyboxList.updateGetAllBack(maxc, 1);
						}
					}
				}else {
					Thread.sleep(3);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} 
		}
	}
	/**
	 * 将要回复的信息映射成数组，然后发送到接收的信息所属ip及端口
	 * @param pack 回复信息的映射对象
	 * @param p 接收的数据包对象
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public void sendReply(Pack pack,DatagramPacket p) throws IOException {
		byte[] serialize = pack.serialize();
		pack.valid(serialize);
		UDPClient.sendReply(serialize,p);
	}
	
	/**
	 * 处理接收的消息时修改箱的在线状态
	 * @param pack 接收的信息映射对象
	 * @param search 接收的信息所属箱
	 * @param p	接收的数据包对象
	 * @throws IOException
	 */
	public void updataKeyBoxState(Pack pack,KeyBox1 search,DatagramPacket p) throws IOException {
		if(search.getOnlines().equals("掉线")) {//如果当前箱状态为掉线，则改为在线
			servletService.updateKeyBoxOnlineById(search.getId(), "在线");
		}
		if (pack.getOrder_number()==0xB1) {//如果为ip配置命令的应答 （0xB1），修改在线状态
			ServletContextLTest.keyboxList.update(search.getMaxc(), "在线", 999, System.currentTimeMillis(), 999,null,-1,-1);
		}else {//如果非ip配置命令的应答，修改在线状态及命令发送ip和端口
			ServletContextLTest.keyboxList.update(search.getMaxc(), "在线", 999, System.currentTimeMillis(), 999,p.getAddress(),p.getPort(),-1);
		}
	}
	
	/**
	 * 验证接收的信息是否为发送的信息的回复，只验证同一个箱的命令字是否相同，应答所带的属性不判断，
	 * 因为每一个箱的信息每隔1s发送一次，基本上不会出现同时接收多条应答；如果出现也只是导致信息没有重发没什么大关系
	 * @param search  信息所属箱对象,保存了发送的信息
	 * @param pack	接收的信息对象
	 * @throws IOException
	 */
	public void validFlag(KeyBox1 search,Pack pack) throws IOException {
		if (search.getFlag()==1) {//如果箱处于阻塞状态，及发送信息等待回复的状态
			boolean flag = search.getPack().getOrder_number()==pack.getOrder_number()-0x80;
			if (flag) {//如果验证成功则关闭箱的阻塞状态同时将步数设置为0
				ServletContextLTest.keyboxList.update(search.getMaxc(), "999", 0, 999, 0,null,-1,0);
				ServletContextLTest.keyboxList.updateGetAllBack(search.getMaxc(), search.getGetAllBack()-1);
				log.info("验证应答成功修改getallback:"+search.getGetAllBack());
			}else {
				log.info("验证应答失败");
			}
		}
	}
}
