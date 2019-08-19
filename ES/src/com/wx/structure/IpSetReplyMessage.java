package com.wx.structure;

@PackType(typeNo=0xB1)
public class IpSetReplyMessage extends Pack{

	@ColumnProperty(type=ColumnType.ONE_BYTE)
	private int style;
	
	@ColumnProperty(type=ColumnType.ONE_BYTE)
	private int valid;
	
	public IpSetReplyMessage() {
		
	}
	
	public IpSetReplyMessage(String maxc, int style) {
		super(maxc, 0xb1);
		this.style = style;
	}



	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public int getValid() {
		return valid;
	}

	@Override
	public String toString() {
		return "IpSetReplyMessage [style=" + style + ", valid=" + valid + ", getLen()=" + getLen() + ", getMaxc()="
				+ getMaxc() + ", getCenter_number()=" + getCenter_number() + ", getOrder_number()=" + getOrder_number()
				+ "]";
	}
	
	
}
