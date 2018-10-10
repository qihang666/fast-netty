package com.core.dispatcher.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 消息处理注解
 * @Description:   
 * @author: hang   
 * @date: 2018年9月5日下午3:11:13   
 * @version V1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HandlerMsg {
	
    public short id();//协议Id号

}
