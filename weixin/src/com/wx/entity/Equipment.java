package com.wx.entity;

import java.util.List;
/**
 * equipment（设备）表映射
 * @author Administrator
 *
 */
public class Equipment {
	
	private String id; //maxc_id
	private String e6u; //设备e6u默认"F00"
	private String name; // 设备名称
	private String managerId; //管理员openId
	private Integer online; // 在线状态默认0 掉线     1在线
	private String nickname;//管理员昵称
	
	private String openId; //当前用户openId或者管理员id
	private int one_arming; //针对具体用户设置的一键布防状态
	private int one_broken; //针对具体用户设置的一键撤防状态
	private int overtime;
	private List<Sub_Equipment> list;//子设备集合
	
	
	public List<Sub_Equipment> getList() {
		return list;
	}
	public void setList(List<Sub_Equipment> list) {
		this.list = list;
	}
	
	public int getOvertime() {
		return overtime;
	}
	public void setOvertime(int overtime) {
		this.overtime = overtime;
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Integer getOnline() {
		return online;
	}
	public void setOnline(Integer online) {
		this.online = online;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getE6u() {
		return e6u;
	}
	public void setE6u(String e6u) {
		this.e6u = e6u;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	
	@Override
	public String toString() {
		return "Equipment [id=" + id + ", e6u=" + e6u + ", name=" + name + ", managerId=" + managerId + ", online="
				+ online + ", nickname=" + nickname + ", openId=" + openId + ", one_arming=" + one_arming
				+ ", one_broken=" + one_broken + ", overtime=" + overtime + ", list=" + list + "]";
	}
}
