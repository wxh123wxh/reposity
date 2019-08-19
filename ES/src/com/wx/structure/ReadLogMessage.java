package com.wx.structure;

@PackType(typeNo = 0x11)
public class ReadLogMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int style;

	@ColumnProperty(type = ColumnType.WAIT)
	private String wait;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	public ReadLogMessage() {
		
	}

	public ReadLogMessage(String maxc, int style) {
		super(maxc, 0x11);
		this.style = style;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
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

	public void setValid(int valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "ReadLogMessage [style=" + style + ", wait=" + wait + ", valid=" + valid + ", getLen()=" + getLen()
				+ ", getMaxc()=" + getMaxc() + ", getCenter_number()=" + getCenter_number() + ", getOrder_number()="
				+ getOrder_number() + "]";
	}
	
	
}
