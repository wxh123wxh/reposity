package com.wx.wx;

/**
 * openId 的信息队列
 * @author Administrator
 *
 */
public class OpenIdQuery {

	private int num = 2; //信息合并时的循环次数
	private int front ;//队头
	private int back;  //队尾
	private int  theSize;  //队伍长度
	private String[] theQueue = new String[1000]; //信息数组
	private static final StringBuilder sb1 = new StringBuilder();
	private static final StringBuilder sb2 = new StringBuilder();
	public int count = 0; //一定时间内获取信息的条数
	public long time = 0; //最后一次获取信息的时间
	
	/**
	 * 添加信息到队列
	 * @param o  信息
	 */
	public  void addList(String o) {
		 //判断队列是否已满
		if (theSize==theQueue.length) {
			
		}else {
			theQueue[back]=o;//在队列尾部添加新成员
			back=(1+back)%theQueue.length;
			theSize++;
		}
	}
	
	/**
	 * 在队列中获取一条信息
	 * @return
	 */
	public String getString() {
		String dequeueVal=theQueue[front];//不为空，返回队列的对头
        theSize--;
        front=(front+1)%theQueue.length;
        return dequeueVal;
	}
	
	/**
	 * 在队列中获取一条合并的信息
	 * @return
	 */
	public String get() {
		if (theSize==0) {//判断队列是否为空
			if(time!=0&&System.currentTimeMillis()-time>=5*60*1000) {//队列曾经被获取过信息且过去了5分钟 返回1表示可以置空
				return "1";
			}
			return "";
		}else {
			if (time==0) {//没有获取过信息，即第一次进入这个队列获取信息
				time = System.currentTimeMillis();
				count++;
			}else if (System.currentTimeMillis()-time>=5*60*1000) {//上一次在这个队列获取信息是超过了5分钟
				count = 1;
				time = System.currentTimeMillis();
			}else if (System.currentTimeMillis()-time<=5*60*1000&&count<100) {
				count++;
			}else if (System.currentTimeMillis()-time<=5*60*1000&&count>=100) {//5分钟内从这个队列获取信息到到了100条
				return "";
			}
			
			String string = getString();
			if (string.indexOf("PANEL")==0) {//判断信息是否以PANEL开头（这个开头的才可以合并）
				sb1.append(string.substring(string.lastIndexOf("/")));//发送的内容
				sb2.append(string.substring(0, string.lastIndexOf("/")));//主题
				
				while(theSize!=0&&num>0&&theQueue[front].indexOf("PANEL")==0) {
					String[] split = getString().split("/");
					if (sb2.indexOf(split[1])==-1) {//不是同一个设备
						sb2.append(",");
						sb2.append(split[1]);
					}
					sb1.append("\\n");
					sb1.append(split[2]);
					num--;
				}
				num = 2;
				String string2 = sb2.append(sb1).toString();
				sb1.setLength(0);
				sb2.setLength(0);
				return string2;
			}else {
				return string;
			}
		}
	}
}
