package com.wx.structure;

@PackType(typeNo=0xA4)
public class KeyAllStateReplyMessage extends Pack{

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
    private int count;
	
	@ColumnProperty(type = ColumnType.ONE_BYTE)
    private int valid;

	public KeyAllStateReplyMessage(String maxc, int count) {
		super(maxc, 0xa4);
		this.count = count;
	}

	public KeyAllStateReplyMessage() {
		
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
		return "KeyAllStateReplyMessage [count=" + count + ", valid=" + valid + ", getLen()=" + getLen()
				+ ", getMaxc()=" + getMaxc() + ", getCenter_number()=" + getCenter_number() + ", getOrder_number()="
				+ getOrder_number() + "]";
	}
	
	
}
