package com.wx.po;

/**
 * user类通过openId获取的用户信息
 * @author Administrator
 *
 */
public class User {

	private String openid; //用户openId
	private String nickname;//用户昵称
	private String headimgurl;//用户头像路径
	private String city;//用户所在城市
	
	private String glid;//保存管理的
	
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	@Override
	public String toString() {
		return "User [openid=" + openid + ", nickname=" + nickname + ", headimgurl=" + headimgurl + ", city=" + city
				+ ", glid=" + glid + "]";
	}
	
}
