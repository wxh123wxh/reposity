package com.wx.entity;

/**
 * operateLog（操作记录） 表映射
 * @author Administrator
 *
 */
public class OperateLog {

	private Long id; //表id
	private String MAXC_id; // 记录所属设备maxc_id
	private int sub_id; //记录所属子设备编码
	private String openId;//操作者openId
	private String time; //操作时间
	private String content; //操作内容
	private String nickname;//操作者昵称
	private String czid; //操作者所属城市
	
	public OperateLog() {
	}
	public OperateLog(String mAXC_id, int sub_id, String openId, String time, String content,
			String nickname) {
		MAXC_id = mAXC_id;
		this.sub_id = sub_id;
		this.openId = openId;
		this.time = time;
		this.content = content;
		this.nickname = nickname;
	}
	public String getCzid() {
		return czid;
	}
	public void setCzid(String czid) {
		this.czid = czid;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "OperateLog [id=" + id + ", MAXC_id=" + MAXC_id + ", sub_id=" + sub_id + ", openId=" + openId + ", time="
				+ time + ", content=" + content + ", nickname=" + nickname + "]";
	}
	
}
