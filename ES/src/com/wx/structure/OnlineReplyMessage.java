package com.wx.structure;

@PackType(typeNo = 0x81)
public class OnlineReplyMessage extends Pack {

	 /**
     * 在进行tcp通讯时，根据此序列来对应请求包和返回包
     */
    @ColumnProperty(type = ColumnType.TIME)
    private String time;

    /**
     * 在进行tcp通讯时，根据此序列来对应请求包和返回包
     */
    @ColumnProperty(type = ColumnType.ONE_BYTE)
    private int valid;

    
	public OnlineReplyMessage() {
		
	}

	public OnlineReplyMessage(String maxc) {
		super(maxc, 0x81);
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "OnlineReplyMessage [time=" + time + ", valid=" + valid + ", getLen()=" + getLen() + ", getMaxc()="
				+ getMaxc() + ", getCenter_number()=" + getCenter_number() + ", getOrder_number()=" + getOrder_number()
				+ "]";
	}

}
