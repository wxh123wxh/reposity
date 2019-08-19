package com.wx.entity;

/**
 * sector（防区）表映射
 * @author Administrator
 *
 */
public class Sector {

	private Long id; //表id
	private Long sub_id;//所属子设备id
	private int sec_id;//防区编码
	private Integer arming_status;//撤布防状态 1撤防 0布防默认0
	private Integer online;//掉线状态 1在线 0掉线 默认0
	private Integer Alarm;// 报警状态 1报警 0正常 默认0
	private Integer Tamper;//被撬状态 1被撬 0正常 默认0
	private Integer DC;//欠压状态 1欠压 0正常  默认0
	private Integer AC;//断电状态 1断电 0正常  默认0
	private String name;//防区名称
	private String sub_name;//子设备名称
	private String maxc_id;//设备maxc_id
	private String password;//子设备密码
	
	public String getSub_name() {
		return sub_name;
	}
	public void setSub_name(String sub_name) {
		this.sub_name = sub_name;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMaxc_id() {
		return maxc_id;
	}
	public void setMaxc_id(String maxc_id) {
		this.maxc_id = maxc_id;
	}
	public int getSec_id() {
		return sec_id;
	}
	public void setSec_id(int sec_id) {
		this.sec_id = sec_id;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSub_id() {
		return sub_id;
	}
	public void setSub_id(Long sub_id) {
		this.sub_id = sub_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getArming_status() {
		return arming_status;
	}
	public void setArming_status(Integer arming_status) {
		this.arming_status = arming_status;
	}
	public Integer getOnline() {
		return online;
	}
	public void setOnline(Integer online) {
		this.online = online;
	}
	@Override
	public String toString() {
		return "Sector [id=" + id + ", sub_id=" + sub_id + ", sec_id=" + sec_id + ", arming_status=" + arming_status
				+ ", online=" + online + ", Alarm=" + Alarm + ", Tamper=" + Tamper + ", DC=" + DC + ", AC=" + AC
				+ ", name=" + name + ", maxc_id=" + maxc_id + ", password=" + password + "]";
	}
	
	
}
