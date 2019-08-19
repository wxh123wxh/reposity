package com.wx.structure;

@PackType(typeNo = 0x83)
public class BrokenReplyMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int sub_iid;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	
	public BrokenReplyMessage() {
		
	}

	public BrokenReplyMessage(String maxc, int sub_iid) {
		super(maxc, 0x83);
		this.sub_iid = sub_iid;
	}

	public int getSub_iid() {
		return sub_iid;
	}

	public void setSub_iid(int sub_iid) {
		this.sub_iid = sub_iid;
	}

	public int getValid() {
		return valid;
	}


	@Override
	public String toString() {
		return "BrokenReplyMessage [sub_iid=" + sub_iid + ", valid=" + valid + ", getLen()=" + getLen() + ", getMaxc()="
				+ getMaxc() + ", getCenter_number()=" + getCenter_number() + ", getOrder_number()=" + getOrder_number()
				+ "]";
	}
	 
	 
}
