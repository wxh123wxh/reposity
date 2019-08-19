package com.wx.entity;

import java.util.List;

/**
 * 保存openId和其对应的maxc_id集合
 * @author Administrator
 *
 */
public class Open {

	private String openId; //用户openId
	private String nickname;
	private long time;
	private int style;
	private List<String> maxc_id_list; //可操作的设备maxc_id集合
	
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public List<String> getMaxc_id_list() {
		return maxc_id_list;
	}

	public void setMaxc_id_list(List<String> maxc_id_list) {
		this.maxc_id_list = maxc_id_list;
	}

	@Override
	public String toString() {
		return "Open [openId=" + openId + ", nickname=" + nickname + ", time=" + time + ", style=" + style
				+ ", maxc_id_list=" + maxc_id_list + "]";
	}

	
}
