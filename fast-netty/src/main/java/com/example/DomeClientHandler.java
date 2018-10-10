package com.example;

import com.core.dispatcher.annotation.HandlerMsg;
import com.core.netty.coder.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.protobuf.Protobuf;
import com.protobuf.Protobuf.TestData;

public class DomeClientHandler {
	public static DomeClientHandler instance = new DomeClientHandler();

	public static DomeClientHandler getInstance() {
		return instance;
	}
	
	@HandlerMsg(id = 10)
	public void test1(Message msg) {
		try {
			TestData data = Protobuf.TestData.parseFrom(msg.getBody());
			System.err.println(data.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@HandlerMsg(id = 11)
	public void test2(Message msg) {
		try {
			TestData data = Protobuf.TestData.parseFrom(msg.getBody());
			System.err.println("消息11:"+data.getName());
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		
	}
}
