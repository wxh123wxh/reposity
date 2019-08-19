package com.wx.structure;


@PackType(typeNo = 0x08)
public class KeySetMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int style;

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int key_iid;
	
	@ColumnProperty(type = ColumnType.NAME_64BYTE)
	private String name;
	
	@ColumnProperty(type = ColumnType.CARD_NUMBER)
	private String card_id;

	@ColumnProperty(type = ColumnType.WAIT)
	private String wait;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	
	public KeySetMessage(String maxc, int style, int key_iid, String name,String card_id) {
		super(maxc, 0x08);
		this.style = style;
		this.key_iid = key_iid;
		this.name = name;
		this.card_id = card_id;
	}

	public KeySetMessage() {
		
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	@Override
	public String toString() {
		return "KeySetMessage [style=" + style + ", key_iid=" + key_iid + ", name=" + name + ", card_id=" + card_id
				+ ", wait=" + wait + ", valid=" + valid + ", getLen()=" + getLen() + ", getMaxc()=" + getMaxc()
				+ ", getCenter_number()=" + getCenter_number() + ", getOrder_number()=" + getOrder_number() + "]";
	}
	
}
