package com.wx.structure;

/**
 * 8.	送读钥匙状态命令每次都是读所有钥匙状态所以  key_iid=65535
 * @author Administrator
 *
 */
@PackType(typeNo = 0x10)
public class ReadKeyStateMessage extends Pack{

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int key_iid = 65535;

	@ColumnProperty(type = ColumnType.WAIT)
	private String wait;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	public ReadKeyStateMessage() {
		
	}

	public ReadKeyStateMessage(String maxc) {
		super(maxc, 0x10);
	}

	public int getKey_iid() {
		return key_iid;
	}

	public void setKey_iid(int key_iid) {
		this.key_iid = key_iid;
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

	@Override
	public String toString() {
		return "ReadKeyStateMessage [key_iid=" + key_iid + ", wait=" + wait + ", valid=" + valid + ", getLen()="
				+ getLen() + ", getMaxc()=" + getMaxc() + ", getCenter_number()=" + getCenter_number()
				+ ", getOrder_number()=" + getOrder_number() + "]";
	}

	
}
