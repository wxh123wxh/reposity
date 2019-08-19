package com.wx.structure;

/**
 * 3.设备故障恢复应答命令（监控中心应答）
 * @author Administrator
 *
 */
@PackType(typeNo = 0x84)
public class BrokenBackReplyMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int sub_iid;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	
	public BrokenBackReplyMessage() {
		
	}

	public BrokenBackReplyMessage(String maxc, int sub_iid) {
		super(maxc, 0x84);
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
		return "BrokenBackReplyMessage [sub_iid=" + sub_iid + ", valid=" + valid + ", getLen()=" + getLen()
				+ ", getMaxc()=" + getMaxc() + ", getCenter_number()=" + getCenter_number() + ", getOrder_number()="
				+ getOrder_number() + "]";
	}
	
	
}
