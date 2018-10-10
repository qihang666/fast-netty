package com.example;

import java.util.Scanner;

import com.core.dispatcher.MessageDispatcher;
import com.core.netty.bootstrap.client.TcpClient;
import com.core.netty.coder.Message;
import com.protobuf.Protobuf;

public class DemoClient extends TcpClient {

	public DemoClient(String hosts, int port) {
		super(hosts, port);
	}



	@Override
	public void init() {
		MessageDispatcher.register(DomeClientHandler.class);

	}



	public static void main(String[] args) {
		 DemoClient client = new DemoClient("127.0.0.1",9000);
	     client.start();
	     Protobuf.TestData.Builder data = Protobuf.TestData.newBuilder();
	     data.setName("我是客户端");
		 client.writeAndFlush(new Message((short)11,data.build().toByteArray()));
		 for (; ; ) {
	            Scanner scan = new Scanner(System.in);
	            String msg = scan.nextLine();
	            if ("exit".equals(msg)) {
	                System.exit(0);
	            }
	            Protobuf.TestData.Builder data2 = Protobuf.TestData.newBuilder();
	            data2.setName(msg);
	            client.writeAndFlush(new Message((short)11,data2.build().toByteArray()));
	        }
		
	}
}
