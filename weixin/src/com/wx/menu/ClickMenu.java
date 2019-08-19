package com.wx.menu;

/**
 * 这个类型的按钮一般为扫码
 * @author Administrator
 *
 */
public class ClickMenu extends Button{

	private String key; //按钮的key值（自定义）

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
