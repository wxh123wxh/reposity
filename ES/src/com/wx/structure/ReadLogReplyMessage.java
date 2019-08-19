package com.wx.structure;

@PackType(typeNo = 0x91)
public class ReadLogReplyMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int style;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	public ReadLogReplyMessage() {
		
	}

	public ReadLogReplyMessage(String maxc, int style) {
		super(maxc, 0x91);
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

	public void setValid(int valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "ReadLogReplyMessage [style=" + style + ", valid=" + valid + ", getLen()=" + getLen() + ", getMaxc()="
				+ getMaxc() + ", getCenter_number()=" + getCenter_number() + ", getOrder_number()=" + getOrder_number()
				+ "]";
	}
	
	
}
