package com.wx.entity;

public class UnitManager {
	private long id;
	private int iid;
	private String unit_id;
	private String openId;
	private String name;
	private String password;
	private int remember;
	
	public UnitManager() {
		
	}
	
	public UnitManager(int iid,String unit_id, String name, String password) {
		this.iid = iid;
		this.unit_id = unit_id;
		this.name = name;
		this.password = password;
	}

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
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

	@Override
	public String toString() {
		return "UnitManager [id=" + id + ", iid=" + iid + ", unit_id=" + unit_id + ", openId=" + openId + ", name="
				+ name + ", password=" + password + ", remember=" + remember + "]";
	}
	
	
}
