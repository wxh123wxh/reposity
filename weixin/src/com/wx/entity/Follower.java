package com.wx.entity;

/**
 * Follower（操作者表）表映射
 * @author Administrator
 *
 */
public class Follower {

	private Long id; //表id
	private String MAXC_id; //可操作的设备maxc_id
	private String openid; //操作者openId
	private String nickname; //操作者昵称
	private String headimgurl; //操作者头像路径
	private String city; //操作者所在城市
	
	private String glid; //保存管理员openId
	
	public Follower() {
		
	}
	public Follower(String mAXC_id, String openid, String nickname, String headimgurl, String city) {
		MAXC_id = mAXC_id;
		this.openid = openid;
		this.nickname = nickname;
		this.headimgurl = headimgurl;
		this.city = city;
	}
	
	public String getGlid() {
		return glid;
	}
	public void setGlid(String glid) {
		this.glid = glid;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMAXC_id() {
		return MAXC_id;
	}
	public void setMAXC_id(String mAXC_id) {
		MAXC_id = mAXC_id;
	}
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	@Override
	public String toString() {
		return "Follower [id=" + id + ", MAXC_id=" + MAXC_id + ", openid=" + openid + ", nickname=" + nickname
				+ ", headimgurl=" + headimgurl + ", city=" + city + ", glid=" + glid + "]";
	}
	
	
}
