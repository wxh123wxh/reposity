package com.wx.structure;

@PackType(typeNo = 0xA1)
public class SwingCardLogReplyMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int style;

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int card_iid;
	
	@ColumnProperty(type = ColumnType.CARD_NUMBER)
	private String card_number;

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int key_iid;
	
	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	public SwingCardLogReplyMessage() {
		
	}

	public SwingCardLogReplyMessage(String maxc, int style, int card_iid, String card_number,int key_iid) {
		super(maxc, 0xa1);
		this.style = style;
		this.card_iid = card_iid;
		this.card_number = card_number;
		this.key_iid = key_iid;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public int getCard_iid() {
		return card_iid;
	}

	public void setCard_iid(int card_iid) {
		this.card_iid = card_iid;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
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

	public void setValid(int valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "SwingCardLogReplyMessage [style=" + style + ", card_iid=" + card_iid + ", card_number=" + card_number
				+ ", key_iid=" + key_iid + ", valid=" + valid + "]";
	}
	
	
}
