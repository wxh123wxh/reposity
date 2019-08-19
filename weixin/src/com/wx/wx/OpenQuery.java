package com.wx.wx;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wx.entity.Open;

/**
 * 保存open的队列
 * @author Administrator
 *
 */
public class OpenQuery implements Runnable{

	private final static ConcurrentHashMap <String, Open> map = new ConcurrentHashMap <String, Open>(5000);
	public static final Lock lock = new ReentrantLock();
	private Log log = LogFactory.getLog(OpenQuery.class);

	public static void add(Open open) {
		lock.lock();
		try {
			open.setTime(System.currentTimeMillis());
			map.put(open.getOpenId(), open);
		} finally {
			lock.unlock();
		}
	}
	
	public static Open get(String openId) {
		lock.lock();
		try {
			if (map.containsKey(openId)) {
				Open open = map.get(openId);
				return open;
			}else {
				return null;
			}
		} finally {
			lock.unlock();
		}
	}
	
	public static void delete(String openId) {
		lock.lock();
		try {
			if (map.containsKey(openId)) {
				map.remove(openId);
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
	    				Open open = OpenQuery.get(next);
	    				if (open!=null) {
	    					long time = System.currentTimeMillis() - open.getTime();
	    					if (time>1000*60*10) {
	    						iterator.remove();
							}
						}
	    			}
	    		} catch (Exception e) {
	    			log.error(e.getMessage(),e);
	    		}
	    	}
	    }, 0, 1000*60);
	}
}
