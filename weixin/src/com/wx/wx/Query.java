package com.wx.wx;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 保存报警或操作记录的队列
 * @author Administrator
 *
 */
public class Query {
	private static int front ;//队头
	private static int back;  //队尾
	private static int  theSize;  //队伍长度
	private static Object[] theQueue = new Object[10000]; //数组
	public static final Lock lock = new ReentrantLock();
	

	public static void addList(Object o) {
		lock.lock();
		try {
			 //判断队列是否已满
			if (theSize==theQueue.length) {
				
			}else {
				boolean flag = true;
				for(int a = 0;a<theSize;a++) {
					Object object = theQueue[(front+a)%theQueue.length];
					if(object.toString().equals(o.toString())) {
						flag = false;
						break;
					}
				}
				if (flag) {
					theQueue[back]=o;//在队列尾部添加新成员
					back=(1+back)%theQueue.length;
					theSize++;
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
	public static Object get() {
		lock.lock();
		try {
			if (theSize==0) {
				return null;
			}else {
				Object dequeueVal=theQueue[front];//不为空，返回队列的对头
				theSize--;
				front=(front+1)%theQueue.length;
				return dequeueVal;
			}
		} finally {
			lock.unlock();
		}
	}
}
