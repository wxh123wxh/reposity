package com.wx.entity;

/**
 * sub_equipment(子设备）表映射
 * @author Administrator
 *
 */
public class Sub_Equipment {
	
	private Long id; //表id
	private int sub_id;//子设备编码
	private String MAXC_id;//设备maxc_id
	private Integer arming_status;//撤布防状态 1撤防 0布防默认0
	private Integer online;//掉线状态 1在线 0掉线 默认0
	private Integer Alarm;// 报警状态 1报警 0正常 默认0
	private Integer Tamper;//被撬状态 1被撬 0正常 默认0
	private Integer DC;//欠压状态 1欠压 0正常  默认0
	private Integer AC;//断电状态 1断电 0正常  默认0
	private String password;//子设备密码
	private String name;//子设备名称
	
	private String fPassword;// 保存密码同时把password设为******使非管理员看不到真实的密码
	private int one_arming;//针对具体用户设置的一键布防状态
	private int one_broken;//针对具体用户设置的一键撤防状态
	
	
	
	public String getfPassword() {
		return fPassword;
	}
	public void setfPassword(String fPassword) {
		this.fPassword = fPassword;
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
	public Integer getAlarm() {
		return Alarm;
	}
	public void setAlarm(Integer alarm) {
		Alarm = alarm;
	}
	public Integer getTamper() {
		return Tamper;
	}
	public void setTamper(Integer tamper) {
		Tamper = tamper;
	}
	public Integer getDC() {
		return DC;
	}
	public void setDC(Integer dC) {
		DC = dC;
	}
	public Integer getAC() {
		return AC;
	}
	public void setAC(Integer aC) {
		AC = aC;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getSub_id() {
		return sub_id;
	}
	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}
	public String getMAXC_id() {
		return MAXC_id;
	}
	public void setMAXC_id(String mAXC_id) {
		MAXC_id = mAXC_id;
	}
	public Integer getArming_status() {
		return arming_status;
	}
	public void setArming_status(Integer arming_status) {
		this.arming_status = arming_status;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getOnline() {
		return online;
	}
	public void setOnline(Integer online) {
		this.online = online;
	}
	@Override
	public String toString() {
		return "Sub_Equipment [id=" + id + ", sub_id=" + sub_id + ", MAXC_id=" + MAXC_id + ", arming_status="
				+ arming_status + ", online=" + online + ", Alarm=" + Alarm + ", Tamper=" + Tamper + ", DC=" + DC
				+ ", AC=" + AC + ", password=" + password + ", name=" + name + ", fPassword=" + fPassword
				+ ", one_arming=" + one_arming + ", one_broken=" + one_broken + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	
}
