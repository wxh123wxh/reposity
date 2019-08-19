package com.wx.structure;

import java.util.Arrays;

@PackType(typeNo=0x24)
public class KeyAllStateMessage extends Pack{

	@ColumnProperty(type = ColumnType.TWO_BYTE_LITTER)
    private int count;
	
	@ColumnProperty(type = ColumnType.KEY_ALL_STATE)
    private byte[] key_all_state;
	
	@ColumnProperty(type = ColumnType.ONE_BYTE)
    private int valid;

	public KeyAllStateMessage() {
		
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public byte[] getKey_all_state() {
		return key_all_state;
	}

	public int getValid() {
		return valid;
	}


	@Override
	public String toString() {
		return "KeyAllStateMessage [count=" + count + ", key_all_state=" + Arrays.toString(key_all_state) + ", valid="
				+ valid + ", getLen()=" + getLen() + ", getMaxc()=" + getMaxc() + ", getCenter_number()="
				+ getCenter_number() + ", getOrder_number()=" + getOrder_number() + "]";
	}
	
	
}
