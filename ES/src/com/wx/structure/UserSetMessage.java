package com.wx.structure;

@PackType(typeNo = 0x07)
public class UserSetMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int style;

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int user_iid;
	
	@ColumnProperty(type = ColumnType.NAME_16BYTE)
	private String name;

	@ColumnProperty(type = ColumnType.WAIT)
	private String wait;
	
	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	public UserSetMessage() {
		
	}

	public UserSetMessage(String maxc, int style, int user_iid, String name) {
		super(maxc, 0x07);
		this.style = style;
		this.user_iid = user_iid;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWait() {
		return wait;
	}

	public void setWait(String wait) {
		this.wait = wait;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "UserSetMessage [style=" + style + ", user_iid=" + user_iid + ", name=" + name + ", wait=" + wait
				+ ", valid=" + valid + ", getLen()=" + getLen() + ", getMaxc()=" + getMaxc() + ", getCenter_number()="
				+ getCenter_number() + ", getOrder_number()=" + getOrder_number() + "]";
	}
	
	
}
