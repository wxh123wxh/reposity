package com.wx.entity;

/**
 * log（报警记录）表映射
 * @author Administrator
 *
 */
public class ArmingLog {
	
	private long id; //表id
	private String MAXC_id; //所属主设备maxc_id
	private Integer sub_id; //子设备编码
	private String time; //报警时间
	private String content; //报警内容
	
	private String openId;//保存当前用户openId
	
	
	
	public ArmingLog() {
	}
	public ArmingLog(String mAXC_id, Integer sub_id, String time, String content) {
		MAXC_id = mAXC_id;
		this.sub_id = sub_id;
		this.time = time;
		this.content = content;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Integer getSub_id() {
		return sub_id;
	}
	public void setSub_id(Integer sub_id) {
		this.sub_id = sub_id;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "ArmingLog [id=" + id + ", MAXC_id=" + MAXC_id + ", sub_id=" + sub_id + ", time=" + time + ", content="
				+ content + "]";
	}
	
}
