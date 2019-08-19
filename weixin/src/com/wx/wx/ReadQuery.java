package com.wx.wx;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wx.entity.ReadMessage;
import com.wx.po.MyMqttCallback;

public class ReadQuery implements Runnable{
	private static int front ;//队头
	private static int back;  //队尾
	private static int  theSize;  //队伍长度
	private static ReadMessage[] theQueue = new ReadMessage[10000]; //数组
	public static final Lock lock = new ReentrantLock();
	private static final Log log = LogFactory.getLog(ReadQuery.class);
	

	public static void addList(ReadMessage o) {
		lock.lock();
		try {
			 //判断队列是否已满
			if (theSize==theQueue.length) {
				log.info("接收队列达到极限");
			}else {
				theQueue[back]=o;//在队列尾部添加新成员
				back=(1+back)%theQueue.length;
				theSize++;
			}
		} finally {
			lock.unlock();
		}
	}
	
	public static ReadMessage get() {
		lock.lock();
		try {
			if (theSize==0) {
				return null;
			}else {
				ReadMessage dequeueVal=theQueue[front];//不为空，返回队列的对头
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
				ReadMessage readMessage = get();
				if (readMessage!=null) {
					MyMqttCallback.hand(readMessage.getTopic(), readMessage.getMessage());
				}else {
					Thread.sleep(90);
				}
				Thread.sleep(10);
			} catch (Exception e) {
				if (e instanceof InterruptedException) {
					log.error("关闭ReadQuery线程");
					break;
				}
				log.error(e.getMessage(),e);
			}
		}
	}
}
