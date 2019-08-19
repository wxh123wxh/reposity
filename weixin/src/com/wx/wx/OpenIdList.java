package com.wx.wx;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OpenIdList {

	private static Lock lock = new ReentrantLock();
	//这个map存放	数字——信息队列  键值对		每个数字必须在OpenId_map中存在指向唯一的openId  OpenIdQuery对应某个openID的信息队列
	public static final LinkedHashMap<Integer, OpenIdQuery> map = new LinkedHashMap<Integer, OpenIdQuery>(1000);
	//这个map存放	数字——openID 键值对		每个openId对应一个唯一的数字
	public static final LinkedHashMap<Integer, String> OpenId_map = new LinkedHashMap<Integer, String>(1000);
	public static final String QUERY_EMPTY = "empty";
	private static final Log log = LogFactory.getLog(OpenIdList.class);
	private static int num = 0;
	
	/**
	 * 把信息添加到对应openiD的信息队列中去
	 * @param openId  用户openId
	 * @param message 对应openId的信息
	 */
	public static void add(String openId,String message) {
		lock.lock();
		try {
			if (OpenId_map.containsValue(openId)) {//判断该openID是否已有信息队列
				for(Integer getKey: OpenId_map.keySet()){//如果有找出这个队列并把信息添加进去
					if(OpenId_map.get(getKey).equals(openId)){
						map.get(getKey).addList(message);
						break;
					}
				}
			}else {
				if (OpenId_map.containsValue(QUERY_EMPTY)) {//判断是否有空的信息队列
					for(Integer getKey: OpenId_map.keySet()){//如果有找出这个队列并把他分配给当前openId，然后添加信息
						if(OpenId_map.get(getKey).equals(QUERY_EMPTY)){
							OpenId_map.put(getKey, openId);
							map.get(getKey).addList(message);
							break;
						}
					}
				}else if (OpenId_map.size()>=1000) {//如果没有空的队列判断总队列长度是否达到极限
					log.info("大于1000 丢弃:"+message);
				}else {//当没有达到极限时开辟新队列并在map和OpenId_map添加对应信息
					OpenId_map.put((++num),openId);
					OpenIdQuery openIdQuery = new OpenIdQuery();
					openIdQuery.addList(message);
					map.put(num,openIdQuery);
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 从队列中获取一个信息，并删除
	 * @param id   map中的数字
	 * @return
	 */
	public static String get(Integer id) {
		lock.lock();
		try {
			String string = map.get(id).get();
			if (!string.equals("")&&!string.equals("1")) {
				return string;
			}else if(string.equals("1")){//等于1表示该队列一定时间内都为空可以置空了
				OpenId_map.put(id, QUERY_EMPTY);
			}
			return null;
		} finally {
			lock.unlock();
		}
	}
}


