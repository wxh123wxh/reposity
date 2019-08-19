package com.wx.entity;

public class Manager {
	
	private String name;
	private int iid;
	private String style;
	
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public int getIid() {
		return iid;
	}
	public void setIid(int iid) {
		this.iid = iid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Manager [name=" + name + ", iid=" + iid + ", style=" + style + "]";
	}
	
}
