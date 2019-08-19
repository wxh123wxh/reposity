package com.wx.menu;

/**
 * 自定义菜单的按钮类（ClickMenu、ViewMenu类的父类）
 * @author Administrator
 *
 */
public class Button {

	private String name; //按钮名称
	private String Type; //按钮类型
	private Button[] sub_button; //子按钮数组
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public Button[] getSub_button() {
		return sub_button;
	}
	public void setSub_button(Button[] sub_button) {
		this.sub_button = sub_button;
	}
	
}
