package com.example;

import com.core.dispatcher.MessageDispatcher;
import com.core.netty.bootstrap.client.pool.PooledClient;
import com.core.netty.bootstrap.inter.IClient;
import com.core.netty.coder.Message;
import com.protobuf.Protobuf;

public class PoolClient3 {



	public static void main(String[] args) {
		MessageDispatcher.register(DomeClientHandler.class);
		IClient c = new PooledClient("127.0.0.1", 9000);
		int i = 0;
	     Protobuf.TestData.Builder data = Protobuf.TestData.newBuilder();
			while(i<20001) {
				data.clear();
				data.setName("name"+i++);
				data.setId(i);
				Message m2 = new Message((short)10,data.build().toByteArray());
				c.writeAndFlush(m2);
//				c.getChannel().write(m2);
//				if(i%2000==0) {
//					c.getChannel().flush();
//				}
		   }
		
		
	}
}
