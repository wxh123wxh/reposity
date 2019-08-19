package com.wx.entity;


public class KeyBox{
	private long id;
	private int team_id;
	private String unit_id;
	private String name;
	private String maxc;
	private String onlines;
	
	
	public KeyBox() {
		
	}
	
	public KeyBox(String maxc) {
		this.maxc = maxc;
	}

	
	public KeyBox(int id, int team_id, String unit_id, String maxc) {
		this.id = id;
		this.team_id = team_id;
		this.unit_id = unit_id;
		this.maxc = maxc;
	}

	public KeyBox(int team_id, String unit_id, String name, String maxc) {
		this.team_id = team_id;
		this.unit_id = unit_id;
		this.name = name;
		this.maxc = maxc;
	}



	public String getOnlines() {
		return onlines;
	}

	public void setOnlines(String onlines) {
		this.onlines = onlines;
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

	@Override
	public String toString() {
		return "KeyBox [id=" + id + ", team_id=" + team_id + ", unit_id=" + unit_id + ", name=" + name + ", maxc="
				+ maxc + ", onlines=" + onlines + "]";
	}



	


	
}
