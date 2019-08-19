package com.wx.structure;

@PackType(typeNo = 0x22)
public class UserOperateLogMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int style;

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int user_iid;
	
	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int key_iid;
	
	@ColumnProperty(type = ColumnType.WAIT)
	private String wait;

	@ColumnProperty(type = ColumnType.TIME)
	private String time;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	
	public UserOperateLogMessage(String maxc, int style, int user_iid, int key_iid) {
		super(maxc, 0x22);
		this.style = style;
		this.user_iid = user_iid;
		this.key_iid = key_iid;
	}


	public UserOperateLogMessage() {
		
	}


	public int getStyle() {
		return style;
	}


	public void setStyle(int style) {
		this.style = style;
	}


	public int getUser_iid() {
		return user_iid;
	}


	public void setUser_iid(int user_iid) {
		this.user_iid = user_iid;
	}


	public String getWait() {
		return wait;
	}


	public void setWait(String wait) {
		this.wait = wait;
	}


	public int getKey_iid() {
		return key_iid;
	}


	public void setKey_iid(int key_iid) {
		this.key_iid = key_iid;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public int getValid() {
		return valid;
	}


	public void setValid(int valid) {
		this.valid = valid;
	}


	@Override
	public String toString() {
		return "UserOperateLogReplyMessage [style=" + style + ", user_iid=" + user_iid + ", wait=" + wait + ", key_iid="
				+ key_iid + ", time=" + time + ", valid=" + valid + ", getLen()=" + getLen() + ", getMaxc()="
				+ getMaxc() + ", getCenter_number()=" + getCenter_number() + ", getOrder_number()=" + getOrder_number()
				+ "]";
	}
	
	
}
