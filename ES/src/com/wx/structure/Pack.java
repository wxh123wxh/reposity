package com.wx.structure;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * 通用父类
 * @author daidai
 */
public class Pack {
	 /**
     * 在进行tcp通讯时，根据此序列来对应请求包和返回包
     */
    @ColumnProperty(type = ColumnType.ONE_BYTE)
    private int len;

    /**
     * 在进行tcp通讯时，根据此序列来对应请求包和返回包
     */
    @ColumnProperty(type = ColumnType.MAXC)
    private String maxc;
    
    /**
     * 在进行tcp通讯时，根据此序列来对应请求包和返回包
     */
    @ColumnProperty(type = ColumnType.ONE_BYTE)
    private int center_number = 0xff;

    /**
     * 在进行tcp通讯时，根据此序列来对应请求包和返回包
     */
    @ColumnProperty(type = ColumnType.ONE_BYTE)
    private int order_number;
    

    
	public Pack(String maxc,int order_number) {
		this.maxc = maxc;
		this.order_number = order_number;
	}
	
	public Pack(String maxc) {
		this.maxc = maxc;
	}

	public Pack() {
		
	}

	public int getLen() {
		return len;
	}

	public String getMaxc() {
		return maxc;
	}

	public int getCenter_number() {
		return center_number;
	}
	
	public int getOrder_number() {
		return order_number;
	}

	/**
	 * 序列化对象成数组
	 * 设置大小端字序和byte缓冲区大小
	 * @return
	 */
    public byte[] serialize(){
    	try {
    		ByteBuffer bf = ByteBuffer.allocate(250);
    		bf.order(ByteOrder.BIG_ENDIAN);
    		return serialize(bf);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * 用反射获取类的属性
     * 序列化父类，将包对象转化为字节数组
     * getFields()只能获取public的字段，包括父类的。
     * 而getDeclaredFields()只能获取自己声明的各种字段，包括public，protected，private。
     * @return
     * @throws Exception
     */
    public byte[] serialize(ByteBuffer bf) throws Exception{
        Field[] fields = Pack.class.getDeclaredFields();//获取父类定义的所有属性
        
        for(Field field : fields){//遍历父类定义的所有属性
            ColumnProperty cp = field.getAnnotation(ColumnProperty.class);//获取ColumnProperty注释对象
            field.setAccessible(true);//设置可以操作私有属性
            Object obj = field.get(this);//获取字段值
            final ColumnType type = cp.type();//获取ColumnProperty注释对象的type值（type值为枚举ColumnType的实例）
            type.serialize(bf, obj);
        }
        serializeSub(bf);//序列化子类
        
        byte[] result = new byte[bf.position()];//position 为byte缓冲区的当前位置
        System.arraycopy(bf.array(), 0, result, 0, result.length);
        return result;
    }

    /**
     * 用反射获取类的属性
     * 序列化子类，将包对象转化为字节数组
     * getFields()只能获取public的字段，包括父类的。
     * 而getDeclaredFields()只能获取自己声明的各种字段，包括public，protected，private。
     * @return
     * @throws Exception
     */
    public void serializeSub(ByteBuffer bf) throws Exception{
    	Field[] fields = this.getClass().getDeclaredFields();
        
        for(Field field : fields){
            ColumnProperty cp = field.getAnnotation(ColumnProperty.class);
            field.setAccessible(true);
            Object obj = field.get(this);//获取字段值
            final ColumnType type = cp.type();
            type.serialize(bf, obj);
        }
    }
    /**
     * 返序列化，将字节数组转换为包对象
     * @param datas
     * @return
     * @throws Exception
     */
    public static final Pack deserialize(byte[] datas){
    	try {
    		ByteBuffer byteBuffer = ByteBuffer.wrap(datas);
    		byteBuffer.order(ByteOrder.BIG_ENDIAN);
    		return deserialize(byteBuffer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

    /**
     * 返序列化父类，将字节缓冲区转换为包对象
     * @param byteBuffer
     * @return
     * @throws Exception
     */
    public static final Pack deserialize(ByteBuffer byteBuffer) throws Exception{
    	@SuppressWarnings("rawtypes")
		Class clazz = Pack.class;
    	Pack pack = (Pack) clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        
        for(Field field : fields){
            ColumnProperty cp = field.getAnnotation(ColumnProperty.class);
            field.setAccessible(true);//是否可获取私有属性
            final ColumnType type = cp.type();
            field.set(pack, type.deserialize(byteBuffer));
        }
        int number = (int) clazz.getDeclaredField("order_number").get(pack);
        Class<?> cla = PackAuthorize.getInstance().getTypenoandpackmap().get(number);
        return getPack(byteBuffer,pack,clazz,cla);
    }

    /**
     * 返序列化子类
     * @param byteBuffer
     * @param Father 父类实例
     * @param father 父类类对象
     * @param child  子类类对象
     * @return
     * @throws Exception
     */
    private static final Pack getPack(ByteBuffer byteBuffer,Pack Father, Class<?> father,Class<?> child) throws Exception{
    	Pack Child = (Pack) child.newInstance();//子类对象
        Field[] fieldss = child.getDeclaredFields();
        for(Field field : fieldss){
            ColumnProperty cp = field.getAnnotation(ColumnProperty.class);
            field.setAccessible(true);//是否可获取私有属性
            final ColumnType type = cp.type();
            field.set(Child, type.deserialize(byteBuffer));
        }
        
        Field[] fields = father.getDeclaredFields();
        for(Field field : fields){
        	Object obj = field.get(Father);
        	field.setAccessible(true);
        	field.set(Child,obj);
        }
        return Child;
    }
    
    
    /**
     * 设置数组的最后一位为校验和,第一位为长度
     * @param bs
     */
    public static void valid(byte[] bs) {
		int num = 0;
		bs[0] = (byte) bs.length;
		
		for(int a=0;a<=bs.length-2;a++) {
			num += Byte.toUnsignedInt(bs[a]);
		}
		bs[bs.length-1] = (byte) (num&0xff);
	}
}
