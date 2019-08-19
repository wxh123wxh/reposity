package com.wx.structure;

@PackType(typeNo=0xA5)
public class OperateLogIndexReplyMessage extends Pack{

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
    private int count;
	
	@ColumnProperty(type = ColumnType.ONE_BYTE)
    private int valid;

	public OperateLogIndexReplyMessage() {
		
	}

	public OperateLogIndexReplyMessage(String maxc, int count) {
		super(maxc, 0xa5);
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getValid() {
		return valid;
	}

	@Override
	public String toString() {
		return "OperateLogIndexReplyMessage [count=" + count + ", valid=" + valid + ", getLen()=" + getLen()
				+ ", getMaxc()=" + getMaxc() + ", getCenter_number()=" + getCenter_number() + ", getOrder_number()="
				+ getOrder_number() + "]";
	}
	
	
}
