package com.example;

import com.core.dispatcher.MessageDispatcher;
import com.core.netty.bootstrap.client.TcpClient;
import com.core.netty.coder.Message;
import com.protobuf.Protobuf;

public class DemoClient2 extends TcpClient {

	public DemoClient2(String hosts, int port) {
		super(hosts, port);
	}


	@Override
	public void init() {
		MessageDispatcher.register(DomeClientHandler.class);
	}


	public static void main(String[] args) {
		DemoClient2 c  = new DemoClient2("127.0.0.1",9000);
		c.start();
		int i=0;
	     Protobuf.TestData.Builder data = Protobuf.TestData.newBuilder();
			while(i<20001) {
				data.clear();
				data.setName("name"+i++);
				data.setId(i);
				Message m2 = new Message((short)10,data.build().toByteArray());
				c.getChannel().writeAndFlush(m2);
//				c.getChannel().write(m2);
//				if(i%2000==0) {
//					c.getChannel().flush();
//				}
		   }
		
		
	}
}
