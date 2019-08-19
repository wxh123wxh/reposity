package com.wx.entity;

public class Logs {
	private long id;
	private int keyss_id;
	private int users_id;
	private String users_name;
	private int manager_id;
	private String manager_name;
	private String application_time;
	private String approve_time;
	private String get_time;
	private String style;
	private String unit_name;
	private String team_name;
	private String keybox_name;
	private String keyss_name;
	private String unit_id;
	private int team_id;
	private String keybox_id;
	private String card_id;
	private int log_style;//设备故障及故障恢复时添加的记录目前没有被使用

	public Logs() {
		
	}
	public Logs(int keyss_id, int users_id, String users_name, int manager_id, String manager_name,
			String application_time, String approve_time, String get_time, String style, String unit_name,
			String team_name, String keybox_name, String keyss_name, String unit_id, int team_id, String keybox_id,
			String card_id, int log_style) {
		this.keyss_id = keyss_id;
		this.users_id = users_id;
		this.users_name = users_name;
		this.manager_id = manager_id;
		this.manager_name = manager_name;
		this.application_time = application_time;
		this.approve_time = approve_time;
		this.get_time = get_time;
		this.style = style;
		this.unit_name = unit_name;
		this.team_name = team_name;
		this.keybox_name = keybox_name;
		this.keyss_name = keyss_name;
		this.unit_id = unit_id;
		this.team_id = team_id;
		this.keybox_id = keybox_id;
		this.card_id = card_id;
		this.log_style = log_style;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getKeyss_id() {
		return keyss_id;
	}
	public void setKeyss_id(int keyss_id) {
		this.keyss_id = keyss_id;
	}
	public int getUsers_id() {
		return users_id;
	}
	public void setUsers_id(int users_id) {
		this.users_id = users_id;
	}
	public String getUsers_name() {
		return users_name;
	}
	public void setUsers_name(String users_name) {
		this.users_name = users_name;
	}
	public int getManager_id() {
		return manager_id;
	}
	public void setManager_id(int manager_id) {
		this.manager_id = manager_id;
	}
	public String getManager_name() {
		return manager_name;
	}
	public void setManager_name(String manager_name) {
		this.manager_name = manager_name;
	}
	public String getApplication_time() {
		return application_time;
	}
	public void setApplication_time(String application_time) {
		this.application_time = application_time;
	}
	public String getApprove_time() {
		return approve_time;
	}
	public void setApprove_time(String approve_time) {
		this.approve_time = approve_time;
	}
	public String getGet_time() {
		return get_time;
	}
	public void setGet_time(String get_time) {
		this.get_time = get_time;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
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
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public int getLog_style() {
		return log_style;
	}
	public void setLog_style(int log_style) {
		this.log_style = log_style;
	}
	@Override
	public String toString() {
		return "Logs [id=" + id + ", keyss_id=" + keyss_id + ", users_id=" + users_id + ", users_name=" + users_name
				+ ", manager_id=" + manager_id + ", manager_name=" + manager_name + ", application_time="
				+ application_time + ", approve_time=" + approve_time + ", get_time=" + get_time + ", style=" + style
				+ ", unit_name=" + unit_name + ", team_name=" + team_name + ", keybox_name=" + keybox_name
				+ ", keyss_name=" + keyss_name + ", unit_id=" + unit_id + ", team_id=" + team_id + ", keybox_id="
				+ keybox_id + ", card_id=" + card_id + ", log_style=" + log_style + "]";
	}

	

}
