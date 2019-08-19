package com.wx.entity;

import java.net.InetAddress;

import com.wx.structure.Pack;


public class KeyBox1 implements Comparable<KeyBox1>{
	private long id;
	private int team_id;
	private String unit_id;
	private String name;
	private String maxc;
	private String fault_number;
	private String onlines;
	private InetAddress sendIp;
	private int sendPort;
	
	private int logNumber;//送读操作记录命令时先判断LogNumber为0就发送，现在用发送间隔时间代替了
	private volatile long lastTime;//上一次在线时间
	private volatile long operateTime;//上一次操作的时间
	private int number;//重发次数
	private long time;//上一次重发时间
	private volatile int flag;//1等待，0发送,消息阻塞状态
	private Pack pack;
	private int getAllBack;//用户发送命令后的等待回复个数+1
	
	
	public KeyBox1() {
		
	}
	
	public KeyBox1(String maxc) {
		this.maxc = maxc;
	}

	
	public KeyBox1(long id, int team_id, String unit_id, String maxc,String onlines,String fault_number) {
		this.id = id;
		this.team_id = team_id;
		this.unit_id = unit_id;
		this.maxc = maxc;
		this.onlines = onlines;
		this.fault_number = fault_number;
	}

	public KeyBox1(int team_id, String unit_id, String name, String maxc) {
		this.team_id = team_id;
		this.unit_id = unit_id;
		this.name = name;
		this.maxc = maxc;
	}

	public long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(long operateTime) {
		this.operateTime = operateTime;
	}

	public int getSendPort() {
		return sendPort;
	}

	public void setSendPort(int sendPort) {
		this.sendPort = sendPort;
	}

	public int getGetAllBack() {
		return getAllBack;
	}

	public void setGetAllBack(int getAllBack) {
		this.getAllBack = getAllBack;
	}

	public InetAddress getSendIp() {
		return sendIp;
	}

	public void setSendIp(InetAddress sendIp) {
		this.sendIp = sendIp;
	}

	public int getLogNumber() {
		return logNumber;
	}

	public void setLogNumber(int logNumber) {
		this.logNumber = logNumber;
	}


	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Pack getPack() {
		return pack;
	}

	public void setPack(Pack pack) {
		this.pack = pack;
	}

	public String getOnlines() {
		return onlines;
	}

	public void setOnlines(String onlines) {
		this.onlines = onlines;
	}

	public String getFault_number() {
		return fault_number;
	}

	public void setFault_number(String fault_number) {
		this.fault_number = fault_number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getTeam_id() {
		return team_id;
	}
	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}
	public String getMaxc() {
		return maxc;
	}
	public void setMaxc(String maxc) {
		this.maxc = maxc;
	}
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "KeyBox1 [id=" + id + ", team_id=" + team_id + ", unit_id=" + unit_id + ", name=" + name + ", maxc="
				+ maxc + ", fault_number=" + fault_number + ", onlines=" + onlines + ", sendIp=" + sendIp
				+ ", sendPort=" + sendPort + ", logNumber=" + logNumber + ", lastTime=" + lastTime + ", operateTime="
				+ operateTime + ", number=" + number + ", time=" + time + ", flag=" + flag + ", pack=" + pack
				+ ", getAllBack=" + getAllBack + "]";
	}

	@Override
	public int compareTo(KeyBox1 o) {
		 return this.maxc.compareTo(o.maxc);
	}
}
