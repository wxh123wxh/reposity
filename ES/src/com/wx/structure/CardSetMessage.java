package com.wx.structure;

@PackType(typeNo = 0x06)
public class CardSetMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int style;

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int card_iid;
	
	@ColumnProperty(type = ColumnType.CARD_NUMBER)
	private String card_number;

	@ColumnProperty(type = ColumnType.WAIT)
	private String wait;

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;
	
	

	public CardSetMessage(String maxc, int style, int card_iid, String card_number) {
		super(maxc, 0x06);
		this.style = style;
		this.card_iid = card_iid;
		this.card_number = card_number;
	}

	public CardSetMessage() {
		
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

	public String getWait() {
		return wait;
	}

	public void setWait(String wait) {
		this.wait = wait;
	}

	public int getValid() {
		return valid;
	}

	@Override
	public String toString() {
		return "CardSetMessage [style=" + style + ", card_iid=" + card_iid + ", card_number=" + card_number + ", wait="
				+ wait + ", valid=" + valid + ", getLen()=" + getLen() + ", getMaxc()=" + getMaxc()
				+ ", getCenter_number()=" + getCenter_number() + ", getOrder_number()=" + getOrder_number() + "]";
	}
	
	
}
