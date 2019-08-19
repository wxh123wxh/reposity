package com.wx.entity;

/**
 * setOperate(布撤防操作密码记住状态)表映射
 * @author Administrator
 *
 */
public class SetOperate {
	
	private String openId;//用户openId
	private String maxc_id;//设备maxc_id
	private int sub_id;//子设备id 为0时代表主设备
	private int one_arming;//一键布防操作状态 1布防 0不管 默认0
	private int one_broken;//一键撤防操作状态 1撤防 0不管 默认0
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getMaxc_id() {
		return maxc_id;
	}
	public void setMaxc_id(String maxc_id) {
		this.maxc_id = maxc_id;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public int getOne_arming() {
		return one_arming;
	}
	public void setOne_arming(int one_arming) {
		this.one_arming = one_arming;
	}
	public int getOne_broken() {
		return one_broken;
	}
	public void setOne_broken(int one_broken) {
		this.one_broken = one_broken;
	}
	@Override
	public String toString() {
		return "SetOperate [openId=" + openId + ", maxc_id=" + maxc_id + ", sub_id=" + sub_id + ", one_arming="
				+ one_arming + ", one_broken=" + one_broken + "]";
	}
	
}
