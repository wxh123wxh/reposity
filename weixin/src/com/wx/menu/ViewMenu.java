package com.wx.menu;

/**
 * 这个类型的按钮一般为视图
 * @author Administrator
 *
 */
public class ViewMenu extends Button {

	private String url;//按钮连接的页面路径（调用code码的路径里面包含页面路径）

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
