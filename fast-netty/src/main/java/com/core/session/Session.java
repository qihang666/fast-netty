package com.core.session;

import com.core.netty.coder.Message;
import com.google.protobuf.GeneratedMessage;

import io.netty.channel.Channel;
/**
 * 会话
 * @Description:   
 * @author: hang   
 * @date: 2018年9月6日下午6:53:00   
 * @version V1.7
 */
public class Session {
	
	private Channel channel;
	
	public Session() {}
	public Session(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public void write(Object msg) {
		if (channel != null) {
			if (channel.isActive()) {
				channel.write(msg);
			}
		}
	}
	
	public void writeMessage(short id,GeneratedMessage msg) {
		if (channel != null) {
			if (channel.isActive()) {
				Message m = new Message(id,msg.toByteArray());
				channel.writeAndFlush(m);
			}
		}
	}
	
	public void writeAndFlush(Object msg) {
		if (channel != null) {
			if (channel.isActive()) {
				channel.writeAndFlush(msg);
			}
		}
	}

	
}
