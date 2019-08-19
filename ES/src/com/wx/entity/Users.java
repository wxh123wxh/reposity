package com.wx.entity;

public class Users {
	private long id;
	private int iid;
	private int team_id;
	private String name;
	private String unit_id;
	
	public Users() {
		
	}
	public Users(int iid,int team_id, String name,String unit_id) {
		this.iid = iid;
		this.team_id = team_id;
		this.name = name;
		this.unit_id = unit_id;
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
	public int getTeam_id() {
		return team_id;
	}
	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Users [id=" + id + ", iid=" + iid + ", team_id=" + team_id + ", name=" + name + ", unit_id=" + unit_id
				+ "]";
	}
	
}
