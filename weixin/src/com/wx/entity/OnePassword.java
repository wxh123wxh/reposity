package com.wx.entity;

/**
 * onePassword（一键操作密码表）表映射
 * @author Administrator
 *
 */
public class OnePassword {

	private String openId;  //用户openId
	private String password; //一键操作密码
	private String remember; //是否记住一键操作密码
	
	public String getRemember() {
		return remember;
	}
	public void setRemember(String remember) {
		this.remember = remember;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "OnePassword [openId=" + openId + ", password=" + password + ", remember=" + remember + "]";
	}
	
}
