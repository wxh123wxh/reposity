package com.wx.structure;

@PackType(typeNo = 0x85)
public class KeyOperateReplyMessage extends Pack{

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int key_iid;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;
	

	public KeyOperateReplyMessage(String maxc, int key_iid) {
		super(maxc, 0x85);
		this.key_iid = key_iid;
	}

	public KeyOperateReplyMessage() {
		
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


	@Override
	public String toString() {
		return "KeyOperateReplyMessage [key_iid=" + key_iid + ", valid=" + valid + ", getLen()=" + getLen()
				+ ", getMaxc()=" + getMaxc() + ", getCenter_number()=" + getCenter_number() + ", getOrder_number()="
				+ getOrder_number() + "]";
	}
	
	
}
