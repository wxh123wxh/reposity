package com.wx.structure;


/**
 * 测试请求包
 * 3．	设备故障恢复命令（主机发送）
 * @author daidai
 */
@PackType(typeNo = 0x04)
public class BrokenBackMessage extends Pack {

    /**
     * 在进行tcp通讯时，根据此序列来对应请求包和返回包
     */
    @ColumnProperty(type = ColumnType.ONE_BYTE)
    private int sub_iid;

    @ColumnProperty(type = ColumnType.TIME)
    private String time;
    /**
     * 在进行tcp通讯时，根据此序列来对应请求包和返回包
     */
    @ColumnProperty(type = ColumnType.ONE_BYTE)
    private int valid;
    

	public BrokenBackMessage(String maxc, int sub_iid) {
		super(maxc, 0x04);
		this.sub_iid = sub_iid;
	}


	public BrokenBackMessage() {
		
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

	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public void setValid(int valid) {
		this.valid = valid;
	}


	@Override
	public String toString() {
		return "BrokenBackMessage [sub_iid=" + sub_iid + ", time=" + time + ", valid=" + valid + ", getLen()="
				+ getLen() + ", getMaxc()=" + getMaxc() + ", getCenter_number()=" + getCenter_number()
				+ ", getOrder_number()=" + getOrder_number() + "]";
	}
}
