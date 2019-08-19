package com.wx.structure;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 字段属性注解
 * @Retention可以用来修饰注解，是注解的注解，称为元注解。
 * Retention(保留)注解说明,这种类型的注解会被保留到那个阶段. 有三个值:
	1.RetentionPolicy.SOURCE —— 这种类型的Annotations只在源代码级别保留,编译时就会被忽略
	2.RetentionPolicy.CLASS —— 这种类型的Annotations编译时被保留,在class文件中存在,但JVM将会忽略
	3.RetentionPolicy.RUNTIME —— 这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用.
	@Retention的用法：指示注释类型的注释要保留多久。如果注释类型声明中不存在 Retention 注释，则保留策略默认为 RetentionPolicy.CLASS
 @interface ColumnProperty{ } 定义一个注解 @ColumnProperty，一个注解是一个类。
 注解@Target也是用来修饰注解的元注解，它有一个属性ElementType也是枚举类型，表示使用范围
值为：ANNOTATION_TYPE CONSTRUCTOR  FIELD LOCAL_VARIABLE METHOD PACKAGE PARAMETER TYPE
 	TYPE,    //表明此注解可以用在类或接口上
    FIELD,     //表明此注解可以用在域上(还没用过，)
    METHOD,     //表明此注解可以用在方法上
    PARAMETER,     //表明此注解可以用在参数上
    CONSTRUCTOR,     //表明此注解可以用在构造方法上
    LOCAL_VARIABLE,     //表明此注解可以用在局部变量上
    ANNOTATION_TYPE,     //表明此注解可以用在注解类型上
    PACKAGE,     //用于记录java文件的package文件信息，
                   不使用在一般的类中，而用在固定文件package-info.java中。
                   注意命名一定是“package-info”。
                   由于package- info.java并不是一个合法的类，
                  使用eclipse创建类的方式会提示不合法，所以需要以创建
                  文件的方式来创建package-info.java。

    TYPE_PARAMETER,     //类型参数声明
    TYPE_USE  //类型使用声明 (未使用过，也不知道怎么用)
 */
@Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value={java.lang.annotation.ElementType.FIELD})
public @interface ColumnProperty {

    // 字段类型
    public ColumnType type();

    // 字段长度限制，0为不限制
//  public int maxLen() default 0;
}
