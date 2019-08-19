package com.wx.structure;

@PackType(typeNo = 0x87)
public class UserSetReplyMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int style;

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int user_iid;
	
	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	public UserSetReplyMessage() {
		
	}

	public UserSetReplyMessage(String maxc, int style, int user_iid) {
		super(maxc, 0x87);
		this.style = style;
		this.user_iid = user_iid;
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

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "UserSetReplyMessage [style=" + style + ", user_iid=" + user_iid + ", valid=" + valid + ", getLen()="
				+ getLen() + ", getMaxc()=" + getMaxc() + ", getCenter_number()=" + getCenter_number()
				+ ", getOrder_number()=" + getOrder_number() + "]";
	}

	
}
