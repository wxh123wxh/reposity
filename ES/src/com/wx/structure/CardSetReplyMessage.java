package com.wx.structure;

@PackType(typeNo = 0x86)
public class CardSetReplyMessage extends Pack{

	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int style;

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
	private int card_iid;
	
	@ColumnProperty(type = ColumnType.ONE_BYTE)
	private int valid;

	
	public CardSetReplyMessage() {
		
	}

	public CardSetReplyMessage(String maxc, int style, int card_iid) {
		super(maxc, 0x86);
		this.style = style;
		this.card_iid = card_iid;
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

	public int getValid() {
		return valid;
	}

	@Override
	public String toString() {
		return "CardSetReplyMessage [style=" + style + ", card_iid=" + card_iid + ", valid=" + valid + ", getLen()="
				+ getLen() + ", getMaxc()=" + getMaxc() + ", getCenter_number()=" + getCenter_number()
				+ ", getOrder_number()=" + getOrder_number() + "]";
	}
	
	
}
