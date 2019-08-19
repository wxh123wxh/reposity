package com.wx.entity;


public class KeyBoxInfo {
	private String name;
	private String maxc;
	private String ip;
	private String ip_mask;
	private String ip_gatewey;
	private String password;
	private String cent1_ip;
	private String cent2_ip;
	private String cent3_ip;
	private String cent4_ip;
	private int cent1_sourcePort;
	private int cent1_destPort;
	private int cent2_sourcePort;
	private int cent2_destPort;
	private int cent3_sourcePort;
	private int cent3_destPort;
	private int cent4_sourcePort;
	private int cent4_destPort;
	private String onlines;
	private String fault_number;
	
	private long id;
	private int team_id;
	private String unit_id;
	
	
	public KeyBoxInfo() {
		
	}
	
	public KeyBoxInfo(int team_id, String name, String maxc, String unit_id) {
		this.team_id = team_id;
		this.name = name;
		this.maxc = maxc;
		this.unit_id = unit_id;
	}

	
	public KeyBoxInfo(int id, int team_id, String unit_id, String maxc) {
		this.id = id;
		this.team_id = team_id;
		this.unit_id = unit_id;
		this.maxc = maxc;
	}

	
	public KeyBoxInfo(String maxc, String ip, String ip_mask, String ip_gatewey, String password, String cent1_ip,
			String cent2_ip, String cent3_ip, String cent4_ip, int cent1_sourcePort, int cent1_destPort,
			int cent2_sourcePort, int cent2_destPort, int cent3_sourcePort, int cent3_destPort, int cent4_sourcePort,
			int cent4_destPort, int team_id, String unit_id) {
		this.maxc = maxc;
		this.ip = ip;
		this.ip_mask = ip_mask;
		this.ip_gatewey = ip_gatewey;
		this.password = password;
		this.cent1_ip = cent1_ip;
		this.cent2_ip = cent2_ip;
		this.cent3_ip = cent3_ip;
		this.cent4_ip = cent4_ip;
		this.cent1_sourcePort = cent1_sourcePort;
		this.cent1_destPort = cent1_destPort;
		this.cent2_sourcePort = cent2_sourcePort;
		this.cent2_destPort = cent2_destPort;
		this.cent3_sourcePort = cent3_sourcePort;
		this.cent3_destPort = cent3_destPort;
		this.cent4_sourcePort = cent4_sourcePort;
		this.cent4_destPort = cent4_destPort;
		this.team_id = team_id;
		this.unit_id = unit_id;
	}

	public KeyBoxInfo(String maxc) {
		this.maxc = maxc;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp_mask() {
		return ip_mask;
	}

	public void setIp_mask(String ip_mask) {
		this.ip_mask = ip_mask;
	}

	public String getIp_gatewey() {
		return ip_gatewey;
	}

	public void setIp_gatewey(String ip_gatewey) {
		this.ip_gatewey = ip_gatewey;
	}

	public String getCent1_ip() {
		return cent1_ip;
	}

	public void setCent1_ip(String cent1_ip) {
		this.cent1_ip = cent1_ip;
	}

	public int getCent1_sourcePort() {
		return cent1_sourcePort;
	}

	public void setCent1_sourcePort(int cent1_sourcePort) {
		this.cent1_sourcePort = cent1_sourcePort;
	}

	public int getCent1_destPort() {
		return cent1_destPort;
	}

	public void setCent1_destPort(int cent1_destPort) {
		this.cent1_destPort = cent1_destPort;
	}

	public String getCent2_ip() {
		return cent2_ip;
	}

	public void setCent2_ip(String cent2_ip) {
		this.cent2_ip = cent2_ip;
	}

	public int getCent2_sourcePort() {
		return cent2_sourcePort;
	}

	public void setCent2_sourcePort(int cent2_sourcePort) {
		this.cent2_sourcePort = cent2_sourcePort;
	}

	public int getCent2_destPort() {
		return cent2_destPort;
	}

	public void setCent2_destPort(int cent2_destPort) {
		this.cent2_destPort = cent2_destPort;
	}

	public String getCent3_ip() {
		return cent3_ip;
	}

	public void setCent3_ip(String cent3_ip) {
		this.cent3_ip = cent3_ip;
	}

	public int getCent3_sourcePort() {
		return cent3_sourcePort;
	}

	public void setCent3_sourcePort(int cent3_sourcePort) {
		this.cent3_sourcePort = cent3_sourcePort;
	}

	public int getCent3_destPort() {
		return cent3_destPort;
	}

	public void setCent3_destPort(int cent3_destPort) {
		this.cent3_destPort = cent3_destPort;
	}

	public String getCent4_ip() {
		return cent4_ip;
	}

	public void setCent4_ip(String cent4_ip) {
		this.cent4_ip = cent4_ip;
	}

	public int getCent4_sourcePort() {
		return cent4_sourcePort;
	}

	public void setCent4_sourcePort(int cent4_sourcePort) {
		this.cent4_sourcePort = cent4_sourcePort;
	}

	public int getCent4_destPort() {
		return cent4_destPort;
	}

	public void setCent4_destPort(int cent4_destPort) {
		this.cent4_destPort = cent4_destPort;
	}

	public String getFault_number() {
		return fault_number;
	}

	public void setFault_number(String fault_number) {
		this.fault_number = fault_number;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getTeam_id() {
		return team_id;
	}
	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOnlines() {
		return onlines;
	}
	public void setOnlines(String onlines) {
		this.onlines = onlines;
	}
	public String getMaxc() {
		return maxc;
	}
	public void setMaxc(String maxc) {
		this.maxc = maxc;
	}
	

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	@Override
	public String toString() {
		return "KeyBoxInfo [id=" + id + ", team_id=" + team_id + ", unit_id=" + unit_id + ", name=" + name + ", ip="
				+ ip + ", onlines=" + onlines + ", maxc=" + maxc + ", fault_number=" + fault_number + ", password="
				+ password + ", ip_mask=" + ip_mask + ", ip_gatewey=" + ip_gatewey + ", cent1_ip=" + cent1_ip
				+ ", cent1_sourcePort=" + cent1_sourcePort + ", cent1_destPort=" + cent1_destPort + ", cent2_ip="
				+ cent2_ip + ", cent2_sourcePort=" + cent2_sourcePort + ", cent2_destPort=" + cent2_destPort
				+ ", cent3_ip=" + cent3_ip + ", cent3_sourcePort=" + cent3_sourcePort + ", cent3_destPort="
				+ cent3_destPort + ", cent4_ip=" + cent4_ip + ", cent4_sourcePort=" + cent4_sourcePort
				+ ", cent4_destPort=" + cent4_destPort + "]";
	}

	
}
