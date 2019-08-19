package com.wx.client;

public class SendInfo implements Comparable<SendInfo> {
	private int front ;//队头
	private int back;  //队尾
	private int  theSize;  //队伍长度
	private String maxc;
	private Object[] theQueue = new Object[1000]; //数组
	
	
	public SendInfo(String maxc) {
		this.maxc = maxc;
	}

	public void addList(Object o) {
		 //判断队列是否已满
		if (theSize==theQueue.length) {
			
		}else {
			theQueue[back]=o;//在队列尾部添加新成员
			back=(1+back)%theQueue.length;
			theSize++;
		}
	}
	
	public Object get() {
		if (theSize==0) {
			return null;
		}else {
			Object dequeueVal=theQueue[front];//不为空，返回队列的对头
			theSize--;
			front=(front+1)%theQueue.length;
			return dequeueVal;
		}
	}
	
	public void clear() {
		theSize = 0;
		front = back;
	}
	

	public int getTheSize() {
		return theSize;
	}

	public void setTheSize(int theSize) {
		this.theSize = theSize;
	}

	public String getMaxc() {
		return maxc;
	}

	public void setMaxc(String maxc) {
		this.maxc = maxc;
	}

	@Override
	public int compareTo(SendInfo o) {
		 return this.maxc.compareTo(o.maxc);
	}
	
}