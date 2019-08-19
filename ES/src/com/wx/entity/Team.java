package com.wx.entity;

public class Team {
	private long id;
	private int iid;
	private String unit_id;
	private String name;
	private boolean flage;
	
	public Team() {
		
	}
	
	public Team(String unit_id, String name, int iid) {
		this.unit_id = unit_id;
		this.name = name;
		this.iid = iid;
	}

	public boolean isFlage() {
		return flage;
	}

	public void setFlage(boolean flage) {
		this.flage = flage;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", iid=" + iid + ", unit_id=" + unit_id + ", name=" + name + ", flage=" + flage + "]";
	}

	
}
