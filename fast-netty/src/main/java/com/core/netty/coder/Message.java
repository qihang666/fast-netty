package com.core.netty.coder;

import com.core.session.Session;

public class Message {

	public static final int HEADER_SIZE = 4;
	
	private short id;//协议id
	private int length;//长度
	private byte[] body; //内容
	private Session session;//会话

	public Message() {
	}
	
	public Message(short id, byte[] body) {
		this.id = id;
		this.body = body;
	}

	public short getId() {
		return id;
	}

	public Message setId(short id) {
		this.id = id;
		return this;
	}

	public byte[] getBody() {
		return body;
	}

	public Message setBody(byte[] body) {
		this.body = body;
		return this;
	}

	public int getLength() {
		return length;
	}

	public Message setLength(int length) {
		this.length = length;
		return this;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	

}
