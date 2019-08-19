package com.wx.entity;

public class Config {
	
	private long id;
	private String name;
	private String password;
	private int remember;
	public volatile long setTime;
	public volatile int intervalDay;
	public volatile int receivePort;
	
	
	public int getReceivePort() {
		return receivePort;
	}
	public void setReceivePort(int receivePort) {
		this.receivePort = receivePort;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRemember() {
		return remember;
	}
	public void setRemember(int remember) {
		this.remember = remember;
	}
	public long getSetTime() {
		return setTime;
	}
	public void setSetTime(long setTime) {
		this.setTime = setTime;
	}
	public int getIntervalDay() {
		return intervalDay;
	}
	public void setIntervalDay(int intervalDay) {
		this.intervalDay = intervalDay;
	}
	@Override
	public String toString() {
		return "Config [id=" + id + ", name=" + name + ", password=" + password + ", remember=" + remember
				+ ", setTime=" + setTime + ", intervalDay=" + intervalDay + ", receivePort=" + receivePort + "]";
	}
	
}
