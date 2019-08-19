package com.wx.structure;

@PackType(typeNo = 0x23)
public class KeyStateChangeMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int style;

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int key_iid;
	
	@ColumnProperty(type = ColumnType.WAIT)
	private String wait;

	@ColumnProperty(type = ColumnType.TIME)
	private String time;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	
	public KeyStateChangeMessage(String maxc, int style, int key_iid) {
		super(maxc, 0x23);
		this.style = style;
		this.key_iid = key_iid;
	}

	public KeyStateChangeMessage() {
		
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getValid() {
		return valid;
	}

	@Override
	public String toString() {
		return "KeyStateChangeMessage [style=" + style + ", key_iid=" + key_iid + ", wait=" + wait + ", time=" + time
				+ ", valid=" + valid + "]";
	}
	
	
}
