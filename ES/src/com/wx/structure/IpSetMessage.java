package com.wx.structure;

@PackType(typeNo=0x31)
public class IpSetMessage extends Pack{

	@ColumnProperty(type=ColumnType.ONE_BYTE)
	private int style;
	
	@ColumnProperty(type=ColumnType.PASSWORD)
	private String password;
	
	@ColumnProperty(type=ColumnType.IP)
	private String ip="0.0.0.0";
	
	@ColumnProperty(type=ColumnType.IP)
	private String ip_mask="0.0.0.0";
	
	@ColumnProperty(type=ColumnType.IP)
	private String ip_gatewey="0.0.0.0";
	
	@ColumnProperty(type=ColumnType.IP)
	private String cent1_ip="0.0.0.0";
	
	@ColumnProperty(type=ColumnType.IP)
	private String cent2_ip="0.0.0.0";
	
	@ColumnProperty(type=ColumnType.IP)
	private String cent3_ip="0.0.0.0";
	
	@ColumnProperty(type=ColumnType.IP)
	private String cent4_ip="0.0.0.0";
	
	@ColumnProperty(type=ColumnType.TWO_BYTE_LITTER)
	private int cent1_sourcePort=65535;
	
	@ColumnProperty(type=ColumnType.TWO_BYTE_LITTER)
	private int cent1_destPort=65535;
	
	@ColumnProperty(type=ColumnType.TWO_BYTE_LITTER)
	private int cent2_sourcePort=65535;
	
	@ColumnProperty(type=ColumnType.TWO_BYTE_LITTER)
	private int cent2_destPort=65535;
	
	@ColumnProperty(type=ColumnType.TWO_BYTE_LITTER)
	private int cent3_sourcePort=65535;
	
	@ColumnProperty(type=ColumnType.TWO_BYTE_LITTER)
	private int cent3_destPort=65535;
	
	@ColumnProperty(type=ColumnType.TWO_BYTE_LITTER)
	private int cent4_sourcePort=65535;
	
	@ColumnProperty(type=ColumnType.TWO_BYTE_LITTER)
	private int cent4_destPort=65535;
	
	@ColumnProperty(type=ColumnType.WAIT)
	private String wait1;
	
	@ColumnProperty(type=ColumnType.WAIT)
	private String wait2;
	
	@ColumnProperty(type=ColumnType.ONE_BYTE)
	private int valid;

	public IpSetMessage() {
		
	}

	public IpSetMessage(String maxc, int style,String password, String ip, String ip_mask, String ip_gatewey,
			String cent1_ip, String cent2_ip, String cent3_ip, String cent4_ip, int cent1_sourcePort,
			int cent1_destPort, int cent2_sourcePort, int cent2_destPort, int cent3_sourcePort, int cent3_destPort,
			int cent4_sourcePort, int cent4_destPort) {
		super(maxc, 0x31);
		this.style = style;
		this.password = password;
		this.ip = ip;
		this.ip_mask = ip_mask;
		this.ip_gatewey = ip_gatewey;
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
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public String getCent2_ip() {
		return cent2_ip;
	}

	public void setCent2_ip(String cent2_ip) {
		this.cent2_ip = cent2_ip;
	}

	public String getCent3_ip() {
		return cent3_ip;
	}

	public void setCent3_ip(String cent3_ip) {
		this.cent3_ip = cent3_ip;
	}

	public String getCent4_ip() {
		return cent4_ip;
	}

	public void setCent4_ip(String cent4_ip) {
		this.cent4_ip = cent4_ip;
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

	public String getWait1() {
		return wait1;
	}

	public void setWait1(String wait1) {
		this.wait1 = wait1;
	}

	public String getWait2() {
		return wait2;
	}

	public void setWait2(String wait2) {
		this.wait2 = wait2;
	}

	public int getValid() {
		return valid;
	}

	@Override
	public String toString() {
		return "IpSetMessage [password=" + password + ", ip=" + ip + ", ip_mask=" + ip_mask + ", ip_gatewey="
				+ ip_gatewey + ", cent1_ip=" + cent1_ip + ", cent2_ip=" + cent2_ip + ", cent3_ip=" + cent3_ip
				+ ", cent4_ip=" + cent4_ip + ", cent1_sourcePort=" + cent1_sourcePort + ", cent1_destPort="
				+ cent1_destPort + ", cent2_sourcePort=" + cent2_sourcePort + ", cent2_destPort=" + cent2_destPort
				+ ", cent3_sourcePort=" + cent3_sourcePort + ", cent3_destPort=" + cent3_destPort
				+ ", cent4_sourcePort=" + cent4_sourcePort + ", cent4_destPort=" + cent4_destPort + ", wait1=" + wait1
				+ ", wait2=" + wait2 + ", valid=" + valid + ", getLen()=" + getLen() + ", getMaxc()=" + getMaxc()
				+ ", getCenter_number()=" + getCenter_number() + ", getOrder_number()=" + getOrder_number() + "]";
	}
	
	
}
