package com.core.dispatcher;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.dispatcher.annotation.HandlerMsg;
import com.core.netty.coder.Message;
import com.google.common.collect.Maps;

/**
 * 消息注册与派发
 * 
 * @Description:
 * @author: hang
 * @date: 2018年9月6日上午11:52:46
 * @version V1.7
 */
public class MessageDispatcher {
	protected static Logger log = LoggerFactory.getLogger(MessageDispatcher.class);

	private static Map<Integer, Method> methodMap = Maps.newConcurrentMap();
	private static Map<Method, Object> listenersMap = Maps.newConcurrentMap();
	private static Map<Class<?>, Object> classMap = Maps.newConcurrentMap();

	/**
	 * 注册handler类
	 * @param classz
	 */
	public static void register(Class<?> classz) {
		try {
			if (classMap.containsKey(classz)) {
				return;
			}
			Object ben = classz.newInstance();
			classMap.put(classz, ben);
			HandlerMsg listen;
			for (Method m : classz.getDeclaredMethods()) {
				if ((listen = m.getAnnotation(HandlerMsg.class)) != null) {
					int id = listen.id();
					methodMap.put(id, m);
					listenersMap.put(m, ben);
					log.info("[register handler] msgId=[{}] method=[{}]", id, m.getName());
				}
			}
		} catch (InstantiationException e) {
			  throw new RuntimeException("MessageDispatcher register error", e);
		} catch (IllegalAccessException e) {
			 throw new RuntimeException("MessageDispatcher register error", e);
		}

	}

	public static void dispatcher(Message msg) {
		try {
			int id = msg.getId();
			Method method = methodMap.get(id);
			Object ben = listenersMap.get(method);
			method.invoke(ben, msg);
		} catch (Exception e) {
			log.error(" MessageDispatcher dispatcher error id=[{}]",msg.getId());
		}
	}

}
