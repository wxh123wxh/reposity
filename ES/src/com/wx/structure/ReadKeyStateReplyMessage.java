package com.wx.structure;

@PackType(typeNo = 0x90)
public class ReadKeyStateReplyMessage extends Pack{

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int key_iid = 65535;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	public ReadKeyStateReplyMessage() {
		
	}

	public ReadKeyStateReplyMessage(String maxc) {
		super(maxc, 0x90);
	}

	public int getKey_iid() {
		return key_iid;
	}

	public void setKey_iid(int key_iid) {
		this.key_iid = key_iid;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "ReadKeyStateReplyMessage [key_iid=" + key_iid + ", valid=" + valid + "]";
	}
	
	
}
