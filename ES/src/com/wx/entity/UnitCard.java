package com.wx.entity;

public class UnitCard {
	
	private long id;
	private int iid;
	private String unit_id;
	private String card_number;
	private String keybox_id;
	private int auth_id;
	private String auth_name;
	private String style;
	
	public UnitCard() {
		
	}
	
	public UnitCard(String unit_id, String card_number,int iid) {
		this.unit_id = unit_id;
		this.card_number = card_number;
		this.iid = iid;
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
	
	public String getKeybox_id() {
		return keybox_id;
	}

	public void setKeybox_id(String keybox_id) {
		this.keybox_id = keybox_id;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getCard_number() {
		return card_number;
	}
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
	public int getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(int auth_id) {
		this.auth_id = auth_id;
	}
	public String getAuth_name() {
		return auth_name;
	}

	public void setAuth_name(String auth_name) {
		this.auth_name = auth_name;
	}

	@Override
	public String toString() {
		return "UnitCard [id=" + id + ", unit_id=" + unit_id + ", iid=" + iid + ", card_number=" + card_number
				+ ", auth_id=" + auth_id + ", auth_name=" + auth_name + ", keybox_id=" + keybox_id + ", style=" + style
				+ "]";
	}

	
}
