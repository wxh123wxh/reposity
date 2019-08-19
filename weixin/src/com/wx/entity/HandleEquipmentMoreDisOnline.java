package com.wx.entity;

public class HandleEquipmentMoreDisOnline implements Comparable<HandleEquipmentMoreDisOnline>{

	private String maxc;
	private int overTime;//间隔时间，当当前时间-time>=overTime(即超过间隔时间)发送上下线的模板信息
	private long time;//设备在新一轮间隔时间内发生上下线的最早时间
	private int statu = 0;//0正常没有变化，大于0上线，小于0掉线
	private String name;
	
	public HandleEquipmentMoreDisOnline() {
		
	}
	public HandleEquipmentMoreDisOnline(String maxc) {
		this.maxc = maxc;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMaxc() {
		return maxc;
	}
	public void setMaxc(String maxc) {
		this.maxc = maxc;
	}
	public int getOverTime() {
		return overTime;
	}
	public void setOverTime(int overTime) {
		this.overTime = overTime;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getStatu() {
		return statu;
	}
	public void setStatu(int statu) {
		this.statu = statu;
	}
	
	
	@Override
	public String toString() {
		return "HandleEquipmentMoreDisOnline [maxc=" + maxc + ", overTime=" + overTime + ", time=" + time + ", statu="
				+ statu + ", name=" + name + "]";
	}
	@Override
	public int compareTo(HandleEquipmentMoreDisOnline o) {
		return this.maxc.compareTo(o.maxc);
	}
}
