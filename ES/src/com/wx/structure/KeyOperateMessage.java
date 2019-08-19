package com.wx.structure;

@PackType(typeNo = 0x05)
public class KeyOperateMessage extends Pack{

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int key_iid;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int state;
	
	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int wait = 0;//动作时间目前为0 两个字节

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	
	public KeyOperateMessage() {
		
	}

	public KeyOperateMessage(String maxc, int key_iid, int state) {
		super(maxc, 0x05);
		this.key_iid = key_iid;
		this.state = state;
	}


	public int getKey_iid() {
		return key_iid;
	}

	public void setKey_iid(int key_iid) {
		this.key_iid = key_iid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public 	int getWait() {
		return wait;
	}

	public void setWait(int wait) {
		this.wait = wait;
	}

	public int getValid() {
		return valid;
	}


	@Override
	public String toString() {
		return "KeyOperateMessage [key_iid=" + key_iid + ", state=" + state + ", wait=" + wait + ", valid=" + valid
				+ ", getLen()=" + getLen() + ", getMaxc()=" + getMaxc() + ", getCenter_number()=" + getCenter_number()
				+ ", getOrder_number()=" + getOrder_number() + "]";
	}
	
	
}
