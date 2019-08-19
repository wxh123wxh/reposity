package com.wx.structure;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 包授权管理      通过包类型获取其对应的反射类类型
 * @author daidai
 */
public class PackAuthorize {

    /**
     * 存放通讯包类型号和通讯包对应关系
     */
    private static final ConcurrentMap<Integer, Class<?>> typeNoAndPackMap ;

    static{

        typeNoAndPackMap = new ConcurrentHashMap<Integer, Class<?>>();

        registerPack(
        		com.wx.structure.BrokenBackMessage.class,
        		com.wx.structure.BrokenBackReplyMessage.class,
        		com.wx.structure.BrokenMessage.class,
        		com.wx.structure.BrokenReplyMessage.class,
        		com.wx.structure.CardSetMessage.class,
        		com.wx.structure.CardSetReplyMessage.class,
        		com.wx.structure.IpSetMessage.class,
        		com.wx.structure.IpSetReplyMessage.class,
        		com.wx.structure.KeyAllStateMessage.class,
        		com.wx.structure.KeyAllStateReplyMessage.class,
        		com.wx.structure.KeyOperateMessage.class,
        		com.wx.structure.KeyOperateReplyMessage.class,
        		com.wx.structure.KeySetMessage.class,
        		com.wx.structure.KeySetReplyMessage.class,
        		com.wx.structure.KeyStateChangeMessage.class,
        		com.wx.structure.KeyStateChangeReplyMessage.class,
        		com.wx.structure.OnlineMessage.class,
        		com.wx.structure.OnlineReplyMessage.class,
        		com.wx.structure.OperateLogIndexMessage.class,
        		com.wx.structure.OperateLogIndexReplyMessage.class,
        		com.wx.structure.ReadKeyStateMessage.class,
        		com.wx.structure.ReadKeyStateReplyMessage.class,
        		com.wx.structure.ReadLogMessage.class,
        		com.wx.structure.ReadLogReplyMessage.class,
        		com.wx.structure.SwingCardLogMessage.class,
        		com.wx.structure.SwingCardLogReplyMessage.class,
        		com.wx.structure.UserOperateLogMessage.class,
        		com.wx.structure.UserOperateLogReplyMessage.class,
        		com.wx.structure.UserSetMessage.class,
        		com.wx.structure.UserSetReplyMessage.class
                );

    }

    private PackAuthorize(){};

    private static PackAuthorize instance;

    public static PackAuthorize getInstance(){
        if(instance == null){
            synchronized(PackAuthorize.class){
                if(instance == null){
                    instance = new PackAuthorize();
                }
            }
        }
        return instance;
    }

    /**
     * 注册通讯包和包类型号的对应关系管理
     * @param pack
     */
    public static void registerPack(Class<?>... classes){

        for(Class<?> clazz : classes){
            PackType packType = (PackType) clazz.getAnnotation(PackType.class);
            typeNoAndPackMap.put(packType.typeNo(), clazz);
        }

    }

    public ConcurrentMap<Integer, Class<?>> getTypenoandpackmap() {
        return typeNoAndPackMap;
    }

}
