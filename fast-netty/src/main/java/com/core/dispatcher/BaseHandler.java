package com.core.dispatcher;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.netty.coder.Message;
import com.core.session.Session;
import com.google.protobuf.GeneratedMessage;

public class BaseHandler {
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	public void sendMsg(short id,GeneratedMessage msg, Session session) {
		if(session.getChannel()!= null&&session.getChannel().isActive()) {
			Message m = new Message(id, msg.toByteArray());
			session.getChannel().writeAndFlush(m);
		}
	}
	
	public void sendMsg(short id,GeneratedMessage msg, Message message) {
		sendMsg(id,msg,message.getSession());
	}
	
	/**
	 * 转化消息对象
	 * @param clazz
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends GeneratedMessage> T parseFromMsg(Class<T> clazz,
			byte[] data) {
		if(data == null){
			return null;
		}
		GeneratedMessage msg = null;
		try {
			Method method = clazz.getMethod("parseFrom", byte[].class);
			msg = (GeneratedMessage) method.invoke(null, data);
		} catch (Exception e) {
			log.error("parseFrom error", e);
		}
		return (T) msg;
	}

}
