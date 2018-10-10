package com.core.dispatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 需要注册的处理类
 * @Description:   
 * @author: hang   
 * @date: 2018年9月7日下午2:19:04   
 * @version V1.7
 */
public class HandlerRegister {
	
	private  List<Class<?>> alasszs = new ArrayList<>();
	
	public  void register(Class<?> classz){
		alasszs.add(classz);
	}
	
	public  void register(Class<?>... classz){
		for(Class<?> c: classz) {
			alasszs.add(c);
		}
	}

	public  List<Class<?>> getAlasszs() {
		return alasszs;
	}
	

}
