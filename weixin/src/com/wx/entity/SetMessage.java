package com.wx.entity;

/**
 * setMessage（消息接收设置表）表映射
 * @author Administrator
 *
 */
public class SetMessage {

	private Long id; //表id
	private String openId;// 用户openId
	private long sub_id;// 子设备id
	private int set_sub_arming;//子设备报警消息  1拒收 0接收 默认0
	private int set_sub_statu;//子设备状态消息 1拒收 0接收 默认0
	private int set_sub_alarm;//子设备布撤防消息 1拒收 0接收 默认0
	
	
	public long getSub_id() {
		return sub_id;
	}
	public void setSub_id(long sub_id) {
		this.sub_id = sub_id;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getSet_sub_arming() {
		return set_sub_arming;
	}
	public void setSet_sub_arming(int set_sub_arming) {
		this.set_sub_arming = set_sub_arming;
	}
	public int getSet_sub_statu() {
		return set_sub_statu;
	}
	public void setSet_sub_statu(int set_sub_statu) {
		this.set_sub_statu = set_sub_statu;
	}
	public int getSet_sub_alarm() {
		return set_sub_alarm;
	}
	public void setSet_sub_alarm(int set_sub_alarm) {
		this.set_sub_alarm = set_sub_alarm;
	}
	
	public SetMessage() {
		
	}
	public SetMessage(String openId, long sub_id, Integer set_sub_arming, Integer set_sub_statu,
			Integer set_sub_alarm) {
		this.openId = openId;
		this.sub_id = sub_id;
		this.set_sub_arming = set_sub_arming;
		this.set_sub_statu = set_sub_statu;
		this.set_sub_alarm = set_sub_alarm;
	}
	
	public SetMessage(Long id, Integer set_sub_arming, Integer set_sub_statu, Integer set_sub_alarm) {
		this.id = id;
		this.set_sub_arming = set_sub_arming;
		this.set_sub_statu = set_sub_statu;
		this.set_sub_alarm = set_sub_alarm;
	}
	@Override
	public String toString() {
		return "SetMessage [id=" + id + ", openId=" + openId + ", sub_id=" + sub_id + ", set_sub_arming="
				+ set_sub_arming + ", set_sub_statu=" + set_sub_statu + ", set_sub_alarm=" + set_sub_alarm + "]";
	}
	
	
}
