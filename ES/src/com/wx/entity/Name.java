package com.wx.entity;

public class Name {

	private String unit_id;
	private int team_id;
	private String keybox_id;
	private int keyss_id;
	private String unit_name;
	private String team_name;
	private String keybox_name;
	private String keyss_name;
	
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public int getTeam_id() {
		return team_id;
	}
	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}
	public String getKeybox_id() {
		return keybox_id;
	}
	public void setKeybox_id(String keybox_id) {
		this.keybox_id = keybox_id;
	}
	public int getKeyss_id() {
		return keyss_id;
	}
	public void setKeyss_id(int keyss_id) {
		this.keyss_id = keyss_id;
	}
	public String getKeybox_name() {
		return keybox_name;
	}
	public void setKeybox_name(String keybox_name) {
		this.keybox_name = keybox_name;
	}
	public String getKeyss_name() {
		return keyss_name;
	}
	public void setKeyss_name(String keyss_name) {
		this.keyss_name = keyss_name;
	}
	public String getUnit_name() {
		return unit_name;
	}
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}
	public String getTeam_name() {
		return team_name;
	}
	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}
	@Override
	public String toString() {
		return "Name [unit_id=" + unit_id + ", team_id=" + team_id + ", keybox_id=" + keybox_id + ", keyss_id="
				+ keyss_id + ", unit_name=" + unit_name + ", team_name=" + team_name + ", keybox_name=" + keybox_name
				+ ", keyss_name=" + keyss_name + "]";
	}
	
}
