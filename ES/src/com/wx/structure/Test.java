package com.wx.structure;


import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;

import com.wx.client.SendInfo;

import java.util.Arrays;
import java.util.Timer;




public class Test {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		
		/*StringBuilder sb = new StringBuilder();
		sb.append("0F").append("5410ECF4D404").append("FF05").append(Util.addNumber(5&0xff)).append(Util.addNumber(5>>8)).append("01").append("0000");
		byte[] valid = Util.valid(sb.toString());
		
		KeyOperateMessage message = new KeyOperateMessage("5410ECF4D404", 5, 1);
		byte[] serialize = message.serialize();
		valid(serialize);
		System.out.println("添加单个锁控制命令："+Util.BinaryToHexString(valid));
		System.out.println("添加单个锁控制命令："+Util.BinaryToHexString(serialize));
		
		KeyOperateMessage deserialize = (KeyOperateMessage)Pack.deserialize(serialize);
		System.out.println(message);
		System.out.println(deserialize);*/
		
		/*StringBuilder sb = new StringBuilder();
		sb.append("15").append("5410ECF4D404").append("FF06").append(Util.addNumber(1)).append(Util.addNumber(33%256)).append(Util.addNumber(33/256));
		sb.append(Util.addIC_num("10754124")).append("000000");
		byte[] valid = Util.valid(sb.toString());
		
		CardSetMessage message = new CardSetMessage("5410ECF4D404", 1, 33, "10754124");
		byte[] serialize = message.serialize();
		valid(serialize);
		System.out.println("添加卡配置命令："+Util.BinaryToHexString(valid));
		System.out.println("添加卡配置命令："+Util.BinaryToHexString(serialize));
		
		CardSetMessage deserialize = (CardSetMessage)Pack.deserialize(serialize);
		System.out.println(message);
		System.out.println(deserialize);*/
		
		/*StringBuilder sb = new StringBuilder();
		sb.append("20").append("5410ECF4D404").append("FF07").append(Util.addNumber(2)).append(Util.addNumber(66&0xff)).append(Util.addNumber(66>>8));
		sb.append(Util.addName("安身", 32)).append("000000");
		byte[] valid = Util.valid(sb.toString());
		
		UserSetMessage message = new UserSetMessage("5410ECF4D404", 2, 66, "安身");
		byte[] serialize = message.serialize();
		valid(serialize);
		System.out.println("添加用户配置命令："+Util.BinaryToHexString(valid));
		System.out.println("添加用户配置命令："+Util.BinaryToHexString(serialize));
		
		UserSetMessage deserialize = (UserSetMessage)Pack.deserialize(serialize);
		System.out.println(message);
		System.out.println(deserialize);*/
		
		
		/*StringBuilder sb = new StringBuilder();
		sb.append("20").append("5410ECF4D404").append("FF08").append(Util.addNumber(2)).append(Util.addNumber(223&0xff)).append(Util.addNumber(223>>8));
		sb.append(Util.addName("大门钥匙",32)).append("000000");
		byte[] valid = Util.valid(sb.toString());
		
		KeySetMessage message = new KeySetMessage("5410ECF4D404", 2, 223, "大门钥匙");
		byte[] serialize = message.serialize();
		valid(serialize);
		System.out.println("添加锁配置命令："+Util.BinaryToHexString(valid));
		System.out.println("添加锁配置命令："+Util.BinaryToHexString(serialize));
		
		KeySetMessage deserialize = (KeySetMessage)Pack.deserialize(serialize);
		System.out.println(message);
		System.out.println(deserialize);*/
		
		
		/*StringBuilder sb = new StringBuilder();
		sb.append("0F").append("5410ECF4D404").append("FF10FFFF").append("000000");
		byte[] valid = Util.valid(sb.toString());
		
		ReadKeyStateMessage message = new ReadKeyStateMessage("5410ECF4D404");
		byte[] serialize = message.serialize();
		valid(serialize);
		System.out.println("添加送读钥匙状态命令："+Util.BinaryToHexString(valid));
		System.out.println("添加送读钥匙状态命令："+Util.BinaryToHexString(serialize));
		
		ReadKeyStateMessage deserialize = (ReadKeyStateMessage)Pack.deserialize(serialize);
		System.out.println(message);
		System.out.println(deserialize);*/
		
		
		/*StringBuilder sb = new StringBuilder();
		sb.append("0E").append("5410ECF4D404").append("FF11").append("01000000");
		byte[] valid = Util.valid(sb.toString());
		
		ReadLogMessage message = new ReadLogMessage("5410ECF4D404", 1);
		byte[] serialize = message.serialize();
		valid(serialize);
		System.out.println("添加送读操作记录命令："+Util.BinaryToHexString(valid));
		System.out.println("添加送读钥匙状态命令："+Util.BinaryToHexString(serialize));
		
		Pack deserialize = Pack.deserialize(serialize);
		System.out.println(message);
		System.out.println(deserialize);
		System.out.println(Util.BinaryToHexString(deserialize.serialize()));
		*/
		/*
		StringBuilder sb = new StringBuilder();
		String password = "12345";
		int length = password.length();
		
		sb.append("40").append("5410ECF4D404").append("FF31").append(Util.addNumber(1)).append(password);
		while(length<6) {
			sb.append("F");
			length++;
		}
		sb.append(Util.addIp("10.0.0.107")).append(Util.addIp("10.0.0.0")).append(Util.addIp("255.255.255.0")).append(Util.addIp("10.0.0.29")).append(Util.addIp("0.0.0.0"))
		.append(Util.addIp("0.0.0.0")).append(Util.addIp("0.0.0.0")).append(Util.addNumber(4015&0xff)).append(Util.addNumber(4015>>8)).append(Util.addNumber(4015&0xff)).append(Util.addNumber(4015>>8))
		.append(Util.addNumber(65535&0xff)).append(Util.addNumber(65535>>8)).append(Util.addNumber(65535&0xff)).append(Util.addNumber(65535>>8))
		.append(Util.addNumber(65535&0xff)).append(Util.addNumber(65535>>8)).append(Util.addNumber(65535&0xff)).append(Util.addNumber(65535>>8))
		.append(Util.addNumber(65535&0xff)).append(Util.addNumber(65535>>8)).append(Util.addNumber(65535&0xff)).append(Util.addNumber(65535>>8)).append("000000000000");
		
		byte[] valid = Util.valid(sb.toString());
		
		IpSetMessage message = new IpSetMessage("5410ECF4D404",1, password, "10.0.0.107", "10.0.0.0", "255.255.255.0", "10.0.0.29", "0.0.0.0", "0.0.0.0", "0.0.0.0", 4015, 4015, 65535, 65535, 65535, 65535, 65535, 65535);
		byte[] serialize = message.serialize();
		valid(serialize);
		System.out.println("添加IP参数命令："+Util.BinaryToHexString(valid));
		System.out.println("添加IP参数命令："+Util.BinaryToHexString(serialize));
		
		IpSetMessage deserialize = (IpSetMessage)Pack.deserialize(serialize);
		System.out.println(deserialize);
		System.out.println(message);*/
		
		byte arr [] = {10,0,0,24};
		String join = StringUtils.join(arr, '.');
		System.out.println(join);
	}
	
	public static void valid(byte[] bs) {
		int num = 0;
		bs[0] = (byte) bs.length;
		
		for(int a=0;a<=bs.length-2;a++) {
			num += Byte.toUnsignedInt(bs[a]);
		}
		bs[bs.length-1] = (byte) (num&0xff);
		
	}
	
	public static String getPassword(int users_iid,int keyss_iid,int manager_iid) {
		int maxc = Integer.parseInt("D404");
		int number = 8;
		
		String x = String.format("%03d", users_iid+number);
		String y = String.format("%03d", keyss_iid-number+199);
		String z = String.format("%03d",manager_iid);
		
		String parseLong = Long.parseLong(x+y)+maxc+"";
		while(parseLong.length()<6) {
			parseLong = "0"+parseLong;
		}
		if (parseLong.length()>6) {
			parseLong = parseLong.substring(parseLong.length()-5, parseLong.length()-1);
		}
		return z+parseLong;
	}
}

/**
 * 
 * @author Administrator
 *
 */
class Queue2{
	public static final Lock lock = new ReentrantLock();
	public static Timer timer;
	public static final String QUERY_EMPTY = "empty";
	private static int theSize;  //队伍长度
	private static SendInfo[] theQueue = new SendInfo[2100]; //数组
	
	public static void add(String maxc,String data) {
		lock.lock();
		try {
			for(int a=0;a<theSize;a++) {
				System.out.print(theQueue[a].getMaxc()+"\t");
			}
			System.out.println();
			int search = Arrays.binarySearch(theQueue, 0, theSize, new SendInfo(maxc));
			if (search>=0) {
				theQueue[search].addList(data);
			}else if(theSize<2100){
				SendInfo sendInfo = new SendInfo(maxc);
				sendInfo.addList(data);
				theQueue[theSize] = sendInfo;
				
				if(theSize>0&&theQueue[theSize].compareTo(theQueue[theSize-1])<0){//注意[0,i-1]都是有序的。如果待插入元素比arr[i-1]还大则无需再与[i-1]前面的元素进行比较了，反之则进入if语句
					SendInfo temp = theQueue[theSize];
			        int j;
			        for(j = theSize-1; j >= 0 && theQueue[j].compareTo(temp)>0; j --){                
			        	theQueue[j+1] = theQueue[j];//把比temp大或相等的元素全部往后移动一个位置            
			        }
			        theQueue[j+1] = temp;//把待排序的元素temp插入腾出位置的(j+1)
			    } 
				theSize++;
			}else {
				System.out.println("数组长度大于2100 丢弃");
			}
		} finally {
			lock.unlock();
		}
	}
	
	public static void get(Integer id) {
		lock.lock();
		try {
			SendInfo sendInfo = theQueue[id];
			if(sendInfo!=null) {
				Object object = sendInfo.get();
				if (object!=null) {
					System.out.println((String)object);
				}else {
				}
			}else {
			}
		} finally {
			lock.unlock();
		}
	}
	
}
