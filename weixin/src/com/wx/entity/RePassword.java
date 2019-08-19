package com.wx.entity;
/**
 * rememberPassword（子设备密码记住状态表）表映射
 * @author Administrator
 *
 */
public class RePassword {

	private String openId; //用户openId
	private long id; //子设备id
	private String remember; //是否记住密码
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRemember() {
		return remember;
	}
	public void setRemember(String remember) {
		this.remember = remember;
	}
	@Override
	public String toString() {
		return "RePassword [openId=" + openId + ", id=" + id + ", remember=" + remember + "]";
	}
	
}
