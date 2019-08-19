package com.wx.structure;

@PackType(typeNo = 0x01)
public class OnlineMessage extends Pack {

	/**
     * 在进行tcp通讯时，根据此序列来对应请求包和返回包
     */
	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	public int getValid() {
		return valid;
	}
	

	public OnlineMessage(String maxc) {
		super(maxc, 0x01);
	}


	public OnlineMessage() {
		
	}


	public OnlineMessage(String maxc, int order_number) {
		super(maxc, order_number);
	}


	@Override
	public String toString() {
		return "OnlineMessage [valid=" + valid + ", getLen()=" + getLen() + ", getMaxc()=" + getMaxc()
				+ ", getCenter_number()=" + getCenter_number() + ", getOrder_number()=" + getOrder_number() + "]";
	}
	    
	
}
