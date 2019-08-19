package com.wx.wx;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wx.entity.HandleEquipmentMoreDisOnline;
import com.wx.service.ServletService;
import com.wx.utils.SpringTool;


public class HandleEquipmentMoreDisOnlineQuery implements Runnable{
	
	public static final Lock lock = new ReentrantLock();
	private static Log log = LogFactory.getLog(HandleEquipmentMoreDisOnlineQuery.class);
	
	private static ServletService servletService = (ServletService)SpringTool.getBean("servletService");
	private final static ConcurrentHashMap <String, HandleEquipmentMoreDisOnline> map = new ConcurrentHashMap <String, HandleEquipmentMoreDisOnline>();

	static String [] statu = {"PANEL/","(",")","/","——上线","——掉线"};
	/**
	 * 通过箱maxc地址二分查询箱，查询不到返回一个负数
	 * @param maxc 箱maxc地址
	 * @return
	 */
	public static HandleEquipmentMoreDisOnline get(String maxc) {
		lock.lock();
		try {
			if (map.containsKey(maxc)) {
				HandleEquipmentMoreDisOnline hand = map.get(maxc);
				return hand;
			}else {
				return null;
			}
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 把箱信息添加到箱的缓存集合，并排序
	 * @param keyBox1 箱信息
	 */
	public static void add(HandleEquipmentMoreDisOnline hand) {
		lock.lock();
		try {
			HandleEquipmentMoreDisOnline online = map.get(hand.getMaxc());
			if (online!=null) {//要添加的已存在则修改
				if(online.getStatu()==0) {
					online.setTime(hand.getTime());
				}
				online.setStatu(online.getStatu()+hand.getStatu());
				online.setOverTime(hand.getOverTime());
				online.setName(hand.getName());
			}else {
				map.put(hand.getMaxc(), hand);
			}
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 修改指定对象的状态
	 * @param keyBox1 箱信息
	 */
	public static void updataStatu(String maxc) {
		lock.lock();
		try {
			HandleEquipmentMoreDisOnline online = map.get(maxc);
			if (online!=null) {//要添加的已存在则修改
				online.setStatu(0);
			}
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 修改指定对象的间隔时间
	 * @param keyBox1 箱信息
	 */
	public static void updataTime(String maxc,int time) {
		lock.lock();
		try {
			HandleEquipmentMoreDisOnline online = map.get(maxc);
			if (online!=null) {//要添加的已存在则修改
				online.setOverTime(time);
			}
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 删除指定对象
	 * @param maxc
	 */
	public static void delete(String maxc) {
		lock.lock();
		try {
			if (map.containsKey(maxc)) {
				map.remove(maxc);
			}
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void run() {
		Timer timer = new Timer();
		InitAccessTokenServlet.LIST.add(timer);
	    timer.scheduleAtFixedRate(new TimerTask() {
	    	public void run() {
	    		try {
	    			Iterator<String> iterator = map.keySet().iterator();
	    			while(iterator.hasNext()) {//使用迭代器，在遍历时添加的新元素可能遍历不到
	    				String next = iterator.next();
	    				HandleEquipmentMoreDisOnline hand = get(next);
	    				if (hand!=null) {
	    					boolean flag = (System.currentTimeMillis() - hand.getTime())>hand.getOverTime()*1000;
	    					if (flag&&hand.getStatu()!=0) {
	    						List<String> list = servletService.findAllFollowerAndManager(hand.getMaxc());
	    						StringBuilder sb = new StringBuilder();
	    						
	    						for(int a =0;a<list.size();a++) {//遍历该信息要发送到的用户集合
	    							if(hand.getStatu()>0) {
	    								OpenIdList.add(list.get(a),sb.append(statu[0]).append(hand.getName()).append(statu[1]).append(hand.getMaxc()).append(statu[2]).append(statu[3]).append(hand.getName()).append(statu[4]).toString());
	    							}else if(hand.getStatu()<0){
	    								OpenIdList.add(list.get(a),sb.append(statu[0]).append(hand.getName()).append(statu[1]).append(hand.getMaxc()).append(statu[2]).append(statu[3]).append(hand.getName()).append(statu[5]).toString());
	    							}
	    							sb.setLength(0);
	    						}
	    						updataStatu(hand.getMaxc());
							}else if((System.currentTimeMillis() - hand.getTime())>1000*60*10) {
								delete(hand.getMaxc());
							}
						}
	    			}
	    		} catch (Exception e) {
	    			log.error(e.getMessage(),e);
	    		}
	    	}
	    }, 0, 100);
	}
}
