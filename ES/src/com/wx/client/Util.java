package com.wx.client;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	/**
	 * 16进制字符串转字节数组
	 * @param hexString 
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		// toUpperCase将字符串中的所有字符转换为大写
		hexString = hexString.toUpperCase();
		int len = hexString.length();
		if (len % 2 == 1){  
	        //奇数  
			len++;  
			hexString = "0" + hexString;
		}
		int length = len / 2;
		// toCharArray将此字符串转换为一个新的字符数组。
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
    //charToByte返回在指定字符的第一个发生的字符串中的索引，即返回匹配字符
	public static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	//将字节数组转换为16进制字符串
	public static String BinaryToHexString(byte[] bytes) {
		String hexStr = "0123456789ABCDEF";
		StringBuffer result = new StringBuffer();
		
		for (byte b : bytes) {
			result.append(String.valueOf(hexStr.charAt((b & 0xF0) >> 4)));
			result.append(String.valueOf(hexStr.charAt(b & 0x0F)));
		}
		return result.toString();
	}
	//将字节数组转换为16进制字符串
	public static String BinaryToHexString(byte[] bytes,int start,int end) {
		String hexStr = "0123456789ABCDEF";
		StringBuffer result = new StringBuffer();
		
		for (int a=start;a<=end;a++) {
			result.append(String.valueOf(hexStr.charAt((bytes[a] & 0xF0) >> 4)));
			result.append(String.valueOf(hexStr.charAt(bytes[a] & 0x0F)));
		}
		return result.toString();
	}
	
	/**
	 * 把0——255之间的int类型转16进制字符串
	 * @param number
	 * @return
	 */
	public static String addNumber(int number){
		String hexStr = "0123456789ABCDEF";
		String hex = String.valueOf(hexStr.charAt((number & 0xF0) >> 4));
		hex += String.valueOf(hexStr.charAt(number & 0x0F));
		return hex;
	}
	
	/**
	 * 把10进制字符串表示的卡号转为小端16进制的字符串
	 * 卡物理编号前3位10进制为一个字节  后面的2个字节不足的前面补0,最后预留2个字节
	 * @param IC_num
	 * @return
	 * @throws IOException
	 */
	/*public static String addIC_num(String IC_num) throws IOException {
		StringBuffer sb = new StringBuffer();
		String substring = IC_num.substring(0, 3);
		String addNumber1 = addNumber(Integer.parseInt(substring));
		
		int parseLong = Integer.parseInt(IC_num.substring(3));
		String addNumber2 = addNumber(parseLong>>8);
		String addNumber3 = addNumber(parseLong&0xff);
		
		sb.append(addNumber3).append(addNumber2).append(addNumber1).append("0000");
		return sb.toString();
	}*/
	
	public static String addIC_num2(String IC_num) throws IOException {
		StringBuffer sb = new StringBuffer();
		
		String hexString = Long.toHexString(Long.parseLong(IC_num));
		
		byte[] bytes = Util.hexStringToBytes(hexString);
		Util.reverse(bytes);
		String hexString2 = Util.BinaryToHexString(bytes);
		
		int length = bytes.length;
		sb.append(hexString2);
		while(length<5) {
			sb.append("00");
			length++;
		}
		return sb.toString();
	}
	
	/**
	 * 解析卡编号
	 * 第3个字节的10进制为卡编号前3位不足补0
	 * 第二个字节做高8位，加第3个字节做低8位；然后转为10进制作为卡编号的剩余部分
	 * @param data 卡编号对应的16进制字符串（10个字符，5个字节）
	 * @return
	 */
	public static String card_numberReserver(String data) {
		StringBuffer sb = new StringBuffer();
		byte[] bytes = Util.hexStringToBytes(data);
		Util.reverse(bytes);
		String hexString = Util.BinaryToHexString(bytes);
		String parseInt = Long.parseLong(hexString, 16)+"";
		
		int len = parseInt.length();
		while(len<10) {
			sb.append("0");
			len++;
		}
		
		sb.append(parseInt);
		return sb.toString();
	}
	
	/**
	 * 把字节数组的高低位互转
	 * @param bs
	 */
	public static void reverse(byte [] bs) {
		for (int i=0, mid=bs.length>>1; i<mid; i++) {
			byte temp = bs[i];
			bs[i] = bs[bs.length - i - 1];
			bs[bs.length - i - 1] = temp;
		}
	}
	
	/**
	 * 把字符串转为指定字节长度的16进制字符串
	 * @param string 要转为16进制字符串的字符串
	 * @param len  所占16进制字符数（字节个数*2）
	 * @return
	 * @throws IOException
	 */
	public static String addName(String string,int len) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(BinaryToHexString(string.getBytes("gb2312")));
		
		while(sb.length()<len) {//maxc12位   name32位    
			sb.append("00");
		}
		return sb.toString();
	}
	
	/**
	 * 把ip格式的字符串转为低位在前的16进制字符串
	 * @param ip ip格式字符串（例：255.255.255.255）
	 * @return
	 */
	public static String addIp(String ip){
		StringBuffer sb = new StringBuffer();
		String[] split = ip.split("\\.");
		
		for(int a=0;a<split.length;a++) {
			int parseInt = Integer.parseInt(split[split.length-1-a]);
			sb.append(addNumber(parseInt));
		}
		return sb.toString();
	}
	
	/**
	 * 通过二分法在对象集合中查找指定对象（对象必须实现comparable接口，重写compareTo方法）
	 * @param list 排序好的对象集合
	 * @param key  要查找的对象
	 * @return
	 */
	public static <T> Object BinarySearch(List<? extends Comparable<? super T>> list, T key) {
		 int low = 0;
	     int high = list.size()-1;

	     while (low <= high) {
	         int mid = (low + high) >>> 1;
	         Comparable<? super T> midVal = list.get(mid);
	         int cmp = midVal.compareTo(key);

	         if (cmp < 0)
	             low = mid + 1;
	         else if (cmp > 0)
	             high = mid - 1;
	         else
	             return midVal; // key found
	     }
	     return null;  // key not found
    }
	
	/**
	 * 在对象数组中查询某属性与指定属性相同的对象
	 * @param objects 对象数组
	 * @param size 数组长度
	 * @param maxc 指定属性
	 * @return
	 */
	public static int search(SendInfo [] objects,int size,String maxc){
		for (int i = 0; i < size; i++) {
			if (maxc.equals(objects[i].getMaxc())) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 把字节数组的指定长度（从0开始）转为16进制字符串并在其后添加时间的16进制字符串
	 * @param bs 要转为16进制字符串的字节数组
	 * @param start 指定的数组长度
	 * @return
	 */
	public static String addTime(byte[] bs,int start) {
		StringBuilder sb = new StringBuilder();
		String hexString = Util.BinaryToHexString(bs, 0, start);
		
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR)-2000;//时间后3位，因为现在是2019年减2000便得到时间后3位
		int month = calendar.get(Calendar.MONTH)+1;//有分从0开始所以加1，才是正确月份
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		sb.append(hexString).append(Util.addNumber(year)).append(Util.addNumber(month)).append(Util.addNumber(day)).append(Util.addNumber(hour)).append(Util.addNumber(minute)).append(Util.addNumber(second));
		return sb.toString();
	}
	
	/**
	 * 在16进制字符串后添加     校验和（字符串对应字节数组每个元素之和余256）
	 * @param data 16进制字符串
	 * @return 返回添加校验和后的16进制字节数组
	 */
	public static byte[] valid(String data) {
		byte[] bs = Util.hexStringToBytes(data);
		int num = 0;
		
		for(int a=0;a<=bs.length-1;a++) {
			num += Byte.toUnsignedInt(bs[a]);
		}
		data += Util.addNumber(num&0xff);
		return Util.hexStringToBytes(data);
	}
	
	/**
	 * 算出校验和并保存到数组最后一个位置
	 * @param bs
	 */
	public static void addLenAndValid(byte[] bs) {
		int num = 0;
		bs[0] = (byte) bs.length;//把数组长度放到数组的第一个元素
		
		for(int a=0;a<=bs.length-2;a++) {//数组从第一个到最后第二个字节的所有值相加的结果的后8（校验和）位作为数组的最后一个元素
			num += Byte.toUnsignedInt(bs[a]);
		}
		bs[bs.length-1] = (byte) (num&0xff);
	}
	
	/**
	 * 验证字节数组的    校验和(节数组每个元素之和余256)是否正确 
	 * @param bs 要验证校验和的字节数组
	 * @return
	 */
	public static boolean valid(byte [] bs) {
		int num = 0;
		int len = Byte.toUnsignedInt(bs[0])-1;//第一个字节表示要验证的数组长度，减1表示除去最后保存校验和后的数组长度
		
		for(int a=0;a<len;a++) {//把数组要验证的所有元素的值相加的后8位（校验和）和与要验证的数组的最后一个元素（校验和）对比
			num += Byte.toUnsignedInt(bs[a]);
		}
		return (num&0xff)==Byte.toUnsignedInt(bs[len]);//最后一个字节表示校验和
	}
	
	/**
	 * 过滤字符串中表情等特殊字符
	 */
	public static String filter(String str) {
		
		if(str.trim().isEmpty()){
			return str;
		}
		String pattern = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
		String reStr = "";
		Pattern emoji = Pattern.compile(pattern);
		Matcher  emojiMatcher = emoji.matcher(str);
		str = emojiMatcher.replaceAll(reStr);
		return str;
	}	
}
