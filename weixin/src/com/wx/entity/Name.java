package com.wx.entity;
/**
 * 用来保存设备、子设备、防区名称
 * @author Administrator
 *
 */
public class Name {

	private String name; //设备名称
	private String sub_name; //子设备名称
	private String sec_name; //防区名称
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSub_name() {
		return sub_name;
	}
	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
	}
	public String getSec_name() {
		return sec_name;
	}
	public void setSec_name(String sec_name) {
		this.sec_name = sec_name;
	}
	@Override
	public String toString() {
		return "Name [name=" + name + ", sub_name=" + sub_name + ", sec_name=" + sec_name + "]";
	}
	
}
