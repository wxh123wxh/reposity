package com.wx.structure;


import java.nio.ByteBuffer;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import com.wx.client.Util;

/**
 * 字段类型
 * @author daidai
 */
public enum ColumnType {
	
	ONE_BYTE{//0——255
		@Override
        public void serialize(ByteBuffer bf, Object obj) throws Exception {
			int n = (int)obj;
            bf.put((byte) n);
        }

        @Override
        public Object deserialize(ByteBuffer bf) throws Exception {
            return Byte.toUnsignedInt(bf.get());
        }
	},
	
	
	
	MAXC{//固定6个字节   12个16进制字符串
		 @Override
	     public void serialize(ByteBuffer bf, Object obj) throws Exception {
			String string = (String)obj;
			byte[] bs = Util.hexStringToBytes(string);
			int len = bs.length;
			
			while(len<6) {//不足6个字节前面补0
				bf.put((byte) 0);
				len++;
			}
	        bf.put(bs);
	     }

	     @Override
	     public Object deserialize(ByteBuffer bf) throws Exception {
	        byte[] bs = new byte[6];
	        bf.get(bs);
	        return Util.BinaryToHexString(bs);
	     }
	},
	
	TIME{
		 @Override
	     public void serialize(ByteBuffer bf, Object obj) throws Exception {
			 Calendar calendar = Calendar.getInstance();
			 byte [] arr = new byte[6];
			 
			 arr[0] = (byte) (calendar.get(Calendar.YEAR)-2000);
			 arr[1] = (byte) (calendar.get(Calendar.MONTH)+1);
			 arr[2] = (byte) calendar.get(Calendar.DAY_OF_MONTH);
			 arr[3] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
			 arr[4] = (byte) calendar.get(Calendar.MINUTE);
			 arr[5] = (byte) calendar.get(Calendar.SECOND);
			 
			 bf.put(arr);
	     }

	     @Override
	     public Object deserialize(ByteBuffer bf) throws Exception {
	         byte[] bs = new byte[6];
	         bf.get(bs);
	            
	         StringBuilder sb = new StringBuilder();
	         sb.append(Byte.toUnsignedInt(bs[0])+2000).append("-").append(Byte.toUnsignedInt(bs[1])).append("-").append(Byte.toUnsignedInt(bs[2])).append(" ").append(Byte.toUnsignedInt(bs[3])).append(":").append(Byte.toUnsignedInt(bs[4])).append(":").append(Byte.toUnsignedInt(bs[5]));
	         return sb.toString();
	     }
	},
	
	
	TWO_BYTE_LITTER{
		@Override
        public void serialize(ByteBuffer bf, Object obj) throws Exception {
            int n = (int)obj;
       	 	
       	 	bf.put((byte) (n&0xff));
    	 	bf.put((byte) (n>>8));
        }

        @Override
        public Object deserialize(ByteBuffer bf) throws Exception {
        	int number1 = Byte.toUnsignedInt(bf.get());
        	int number2 = Byte.toUnsignedInt(bf.get());
            return (number2<<8)+number1;
        }
	},
	
	NAME_16BYTE{
		 @Override
	     public void serialize(ByteBuffer bf, Object obj) throws Exception {
			String string = (String)obj;
			StringBuffer sb = new StringBuffer();
			sb.append(Util.BinaryToHexString(string.getBytes("gb2312")));
			
			while(sb.length()<32) {// name16个字节   32个16进制字符
				sb.append("00");
			}
				
			byte[] bs = Util.hexStringToBytes(sb.toString());
	        bf.put(bs);
	     }

	     @Override
	     public Object deserialize(ByteBuffer bf) throws Exception {
	        byte[] bs = new byte[16];
	        bf.get(bs);
	        return new String(bs,"gb2312");
	     }
	},
	
	NAME_64BYTE{
		 @Override
	     public void serialize(ByteBuffer bf, Object obj) throws Exception {
			String string = (String)obj;
			StringBuffer sb = new StringBuffer();
			sb.append(Util.BinaryToHexString(string.getBytes("gb2312")));
			
			while(sb.length()<128) {// name16个字节   32个16进制字符
				sb.append("00");
			}
				
			byte[] bs = Util.hexStringToBytes(sb.toString());
	        bf.put(bs);
	     }

	     @Override
	     public Object deserialize(ByteBuffer bf) throws Exception {
	        byte[] bs = new byte[64];
	        bf.get(bs);
	        return new String(bs,"gb2312");
	     }
	},
	
	//因为此字段的字节数由钥匙个数字段决定，所以要先得到钥匙个数。因此钥匙个数对应的位置改变，此映射关系改变
	KEY_ALL_STATE{//这个不需要序列化
		 @Override
	     public void serialize(ByteBuffer bf, Object obj) throws Exception {
			
	     }

	     @Override
	     public Object deserialize(ByteBuffer bf) throws Exception {
        	byte[] bs = new byte[50];//上报200个钥匙状态
	        bf.get(bs);
	        return bs;
	     }
	},
	
	CARD_NUMBER{
		 @Override
	     public void serialize(ByteBuffer bf, Object obj) throws Exception {
			String IC_num = (String)obj;
			String hexString = Long.toHexString(Long.parseLong(IC_num));
			
			byte[] bytes = Util.hexStringToBytes(hexString);
			Util.reverse(bytes);
			bf.put(bytes);
				
			int len = bytes.length;
			while(len<5) {
				bf.put((byte) 0);
				len++;
			}
	     }

	     @Override
	     public Object deserialize(ByteBuffer bf) throws Exception {
	    	 StringBuffer sb = new StringBuffer();
	    	 byte[] bs = new byte[5];
		     bf.get(bs);
		     
		     Util.reverse(bs);
		     String hexString = Util.BinaryToHexString(bs);
		     String parseInt = Long.parseLong(hexString, 16)+"";
		     
		     int len = parseInt.length();
		     while(len<10) {
		    	 sb.append("0");
		    	 len++;
		     }
		     sb.append(parseInt);
	         return sb.toString();
	     }
	},
	
	WAIT{//预留3个字节
		 @Override
	     public void serialize(ByteBuffer bf, Object obj) throws Exception {
			byte[] bs = {0,0,0};
			bf.put(bs);
	     }

	     @Override//这个不需要解析
	     public Object deserialize(ByteBuffer bf) throws Exception {
	    	 byte[] bs = new byte[3];
		     bf.get(bs);
		     return Util.BinaryToHexString(bs);
	     }
	},
	
	PASSWORD{
		 @Override
	     public void serialize(ByteBuffer bf, Object obj) throws Exception {
			 StringBuffer sb = new StringBuffer();
			 String password = (String)obj;
			 sb.append(password);
			 
			 while(sb.length()<6) {
				 sb.append("F");
			 }
			 
			 byte[] bs = Util.hexStringToBytes(sb.toString());
		     bf.put(bs);
	     }

	     @Override//这个不需要解析
	     public Object deserialize(ByteBuffer bf) throws Exception {
	    	 byte[] bs = new byte[3];
		     bf.get(bs);
		     return Util.BinaryToHexString(bs);
	     }
	},
	
	IP{
		 @Override
	     public void serialize(ByteBuffer bf, Object obj) throws Exception {
			 String ip = (String)obj;
			 byte [] bs = new byte[4];
			 String[] split = ip.split("\\.");
				
			 for(int a=0;a<split.length;a++) {
				 bs[a] = (byte) Integer.parseInt(split[split.length-1-a]);
			 }
			 bf.put(bs);
	     }

	     @Override//这个不需要解析
	     public Object deserialize(ByteBuffer bf) throws Exception {
	    	 byte[] bs = new byte[4];
		     bf.get(bs);
		     String join = StringUtils.join(bs, '.');
		     return join;
	     }
	};
	
    /**
     * 枚举类型添加抽象方法，其所有实例都要重写
     * 序列化
     * @param bf
     * @param obj
     * @throws Exception
     */
    public abstract void serialize(ByteBuffer bf, Object obj) throws Exception;

    /**
     * 枚举类型添加抽象方法，其所有实例都要重写
     * 返序列化
     * @param bf
     * @return
     * @throws Exception
     */
    public abstract Object deserialize(ByteBuffer bf) throws Exception;
}
