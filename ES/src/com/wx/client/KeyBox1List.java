package com.wx.client;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.wx.entity.KeyBox1;
import com.wx.structure.Pack;

public class KeyBox1List {

	private volatile ArrayList<KeyBox1> list = new ArrayList<KeyBox1>(2000);//定义一个缓存箱的集合
	public static final Lock lock = new ReentrantLock();

	public KeyBox1List(ArrayList<KeyBox1> list) {
		this.list = list;
	}
	
	/**
	 * 通过箱maxc地址二分查询箱，查询不到返回一个负数
	 * @param maxc 箱maxc地址
	 * @return
	 */
	public KeyBox1 get(String maxc) {
		lock.lock();
		try {
			KeyBox1 binarySearch = (KeyBox1) Util.BinarySearch(list, new KeyBox1(maxc));
			return binarySearch;
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 把箱信息添加到箱的缓存集合，并排序
	 * @param keyBox1 箱信息
	 */
	public void add(KeyBox1 keyBox1) {
		lock.lock();
		try {
			list.add(keyBox1);
			Queue.add(keyBox1.getMaxc(), null);
			Collections.sort(list);
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 将发送的信息保存起来，等待应答验证
	 * @param maxc 箱maxc地址
	 * @param packet 发送的信息
	 */
	public void addPack(String maxc,Pack pack) {
		lock.lock();
		try {
			KeyBox1 binarySearch = (KeyBox1) Util.BinarySearch(list, new KeyBox1(maxc));
			binarySearch.setPack(pack);
		}finally {
			lock.unlock();
		}
	}
	
	
	/**
	 * 修改指定箱的还要上报的历史记录条数
	 * @param maxc
	 * @param number
	 */
	public void updateLogNumber(String maxc,int number) {
		lock.lock();
		try {
			KeyBox1 binarySearch = (KeyBox1) Util.BinarySearch(list, new KeyBox1(maxc));
			binarySearch.setLogNumber(number);
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 修改指定箱的状态等信息
	 * @param maxc   箱maxc地址
	 * @param online 箱在线状态
	 * @param number 重发次数
	 * @param time	 最后的在线时间
	 * @param flag	是否处于等待应答的状态
	 * @param ip	接收的信息的发送端ip
	 * @param port	接收的信息的发送端端口
	 * @param cftime	上一次重发时间
	 */
	public void update(String maxc,String online,int number,long time,int flag,InetAddress ip,int port,long cftime) {
		lock.lock();
		try {
			KeyBox1 binarySearch = (KeyBox1) Util.BinarySearch(list, new KeyBox1(maxc));
			if (!online.equals("999")) {
				binarySearch.setOnlines(online);
			}
			if (number!=999) {
				binarySearch.setNumber(number);
			}
			if (time!=999) {
				binarySearch.setLastTime(time);
			}
			if (flag!=999) {
				binarySearch.setFlag(flag);
			}
			if (ip!=null) {
				binarySearch.setSendIp(ip);
			}
			if (port!=-1) {
				binarySearch.setSendPort(port);
			}
			if(cftime!=-1) {
				binarySearch.setTime(cftime);
			}
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 修改箱下子设备的故障状态
	 * @param maxc 箱maxc地址
	 * @param fault_number 箱的故障子设备编号通过逗号连接成的字符串
	 */
	public void updateFault_number(String maxc,String fault_number) {
		lock.lock();
		try {
			KeyBox1 binarySearch = (KeyBox1) Util.BinarySearch(list, new KeyBox1(maxc));
			binarySearch.setFault_number(fault_number);
		}finally {
			lock.unlock();
		}
	}
	
	
	/**
	 * 修改箱的getAllBack值，当箱发送多条信息命令时back设置为消息条数，当配置回复应答时back设置为1，此时用户等待的回调定时器函数停止
	 * @param maxc
	 * @param getAllBack
	 */
	public void updateGetAllBack(String maxc,int getAllBack) {
		lock.lock();
		try {
			KeyBox1 binarySearch = (KeyBox1) Util.BinarySearch(list, new KeyBox1(maxc));
			binarySearch.setGetAllBack(getAllBack);
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 修改对箱（所有箱公用）的最后操作时间
	 * @param time 对箱（所有箱公用）的最后操作时间
	 */
	public void updateLogTime(String maxc,long operateTime) {
		lock.lock();
		try {
			KeyBox1 binarySearch = (KeyBox1) Util.BinarySearch(list, new KeyBox1(maxc));
			binarySearch.setOperateTime(operateTime);
		}finally {
			lock.unlock();
		}
	}
	
	
	/**
	 * 在箱的缓存中删除箱
	 * @param maxcs 箱maxc地址数组
	 */
	public void remove(String[] maxcs) {
		lock.lock();
		try {
			for(int a=0;a<maxcs.length;a++) {
				int binarySearch = Collections.binarySearch(list, new KeyBox1(maxcs[a]));
				list.remove(binarySearch);
			}
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 在箱缓存集合中删除指定班组下的箱
	 * @param team_ids 班组编号数组
	 */
	public void remove(int[] team_ids) {
		lock.lock();
		try {
			Iterator<KeyBox1> iterator = list.iterator();
			while(iterator.hasNext()) {
				KeyBox1 next = iterator.next();
				if (Arrays.binarySearch(team_ids, next.getTeam_id())>=0) {
					iterator.remove();
				}
			}
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 清空箱缓存集合
	 */
	public void clear() {
		lock.lock();
		try {
			list.clear();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public String toString() {
		return "KeyBox1List [list=" + list + "]";
	}
	
}
