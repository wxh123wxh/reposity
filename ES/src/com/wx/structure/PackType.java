package com.wx.structure;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 包类型
 * @author daidai
 *
 */
@Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value={java.lang.annotation.ElementType.TYPE})
public @interface PackType {

    /**
     * 包类型号
     * @return
     */
    public int typeNo();

}
