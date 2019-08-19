package com.wx.entity;

public class Card {
	
	private long id;
	private int iid;
	private int team_id;
	private String keybox_id;
	private String unit_id;
	private String card_number;
	
	public Card() {
		
	}
	
	public Card(String keybox_id, String card_number,String unit_id,int iid,int team_id) {
		this.keybox_id = keybox_id;
		this.card_number = card_number;
		this.unit_id = unit_id;
		this.iid = iid;
		this.team_id = team_id;
	}

	public int getTeam_id() {
		return team_id;
	}

	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
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
	public String getKeybox_id() {
		return keybox_id;
	}
	public void setKeybox_id(String keybox_id) {
		this.keybox_id = keybox_id;
	}
	public String getCard_number() {
		return card_number;
	}
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", iid=" + iid + ", team_id=" + team_id + ", keybox_id=" + keybox_id + ", unit_id="
				+ unit_id + ", card_number=" + card_number + "]";
	}

	

}
