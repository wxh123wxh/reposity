package com.wx.structure;

@PackType(typeNo = 0x03)
public class BrokenMessage extends Pack{

	 /**
     * 在进行tcp通讯时，根据此序列来对应请求包和返回包
     */
    @ColumnProperty(type = ColumnType.ONE_BYTE)
    private int sub_iid;
    
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

	public BrokenMessage() {
		
	}

	public BrokenMessage(String maxc, int sub_iid) {
		super(maxc, 0x03);
		this.sub_iid = sub_iid;
	}


	public int getSub_iid() {
		return sub_iid;
	}

	public void setSub_iid(int sub_iid) {
		this.sub_iid = sub_iid;
	}

	public int getValid() {
		return valid;
	}


	@Override
	public String toString() {
		return "BrokenMessage [sub_iid=" + sub_iid + ", valid=" + valid + ", getLen()=" + getLen() + ", getMaxc()="
				+ getMaxc() + ", getCenter_number()=" + getCenter_number() + ", getOrder_number()=" + getOrder_number()
				+ "]";
	}
    
    
}
