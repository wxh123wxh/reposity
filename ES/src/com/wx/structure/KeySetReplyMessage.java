package com.wx.structure;

@PackType(typeNo = 0x88)
public class KeySetReplyMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int style;

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int key_iid;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;
	

	public KeySetReplyMessage(String maxc, int style, int key_iid) {
		super(maxc, 0x88);
		this.style = style;
		this.key_iid = key_iid;
	}

	public KeySetReplyMessage() {
		
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

	public int getValid() {
		return valid;
	}

	@Override
	public String toString() {
		return "KeySetReplyMessage [style=" + style + ", key_iid=" + key_iid + ", valid=" + valid + "]";
	}
	
	
}
