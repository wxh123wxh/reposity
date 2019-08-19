package com.wx.entity;

public class Key {

	private long id;
	private int iid;
	private int team_id;
	private String unit_id;
	private String keybox_id;
	private String name;
	private int keyss_state;//default 1 钥匙在位
	private String card_id;
	private String usering;
	
	public Key() {
		
	}
	
	public Key(int iid, String keybox_id, String name,String unit_id,int team_id,String card_id) {
		this.iid = iid;
		this.keybox_id = keybox_id;
		this.name = name;
		this.unit_id = unit_id;
		this.team_id = team_id;
		this.card_id = card_id;
	}

	
	public String getUsering() {
		return usering;
	}

	public void setUsering(String usering) {
		this.usering = usering;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public int getTeam_id() {
		return team_id;
	}

	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getIid() {
		return iid;
	}
	public void setIid(int iid) {
		this.iid = iid;
	}
	public String getKeybox_id() {
		return keybox_id;
	}
	public void setKeybox_id(String keybox_id) {
		this.keybox_id = keybox_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getKeyss_state() {
		return keyss_state;
	}
	public void setKeyss_state(int keyss_state) {
		this.keyss_state = keyss_state;
	}

	@Override
	public String toString() {
		return "Key [id=" + id + ", iid=" + iid + ", team_id=" + team_id + ", unit_id=" + unit_id + ", keybox_id="
				+ keybox_id + ", name=" + name + ", keyss_state=" + keyss_state + ", card_id=" + card_id + ", usering="
				+ usering + "]";
	}

}
