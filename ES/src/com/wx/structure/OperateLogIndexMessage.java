package com.wx.structure;

@PackType(typeNo=0x25)
public class OperateLogIndexMessage extends Pack{

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
    private int count;
	
	@ColumnProperty(type = ColumnType.WAIT)
    private String wait1;
	
	@ColumnProperty(type = ColumnType.WAIT)
    private String wait2;
	
	@ColumnProperty(type = ColumnType.ONE_BYTE)
    private int valid;
	
	public OperateLogIndexMessage(String maxc, int count) {
		super(maxc, 0x25);
		this.count = count;
	}

	public OperateLogIndexMessage() {
		
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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
		return "OperateLogIndexMessage [count=" + count + ", wait1=" + wait1 + ", wait2=" + wait2 + ", valid=" + valid
				+ ", getLen()=" + getLen() + ", getMaxc()=" + getMaxc() + ", getCenter_number()=" + getCenter_number()
				+ ", getOrder_number()=" + getOrder_number() + "]";
	}
	
	
}
