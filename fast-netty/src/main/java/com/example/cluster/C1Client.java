package com.example.cluster;

import com.core.cluster.ClusterDispatcher;
import com.core.cluster.ClusterManager;
import com.core.cluster.Service;
import com.core.cluster.strategy.RandomStrategy;
import com.core.dispatcher.MessageDispatcher;
import com.core.netty.bootstrap.server.TcpServer;
import com.core.netty.coder.Message;
import com.example.DemoClient;
import com.example.DomeClientHandler;
import com.example.DomeServerHandler;
import com.protobuf.Protobuf;

public class C1Client extends TcpServer {



	@Override
	public void init() {
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		MessageDispatcher.register(DomeClientHandler.class);
		ClusterDispatcher ha = new ClusterDispatcher("127.0.0.1:2100",new RandomStrategy());
		Protobuf.TestData.Builder data = Protobuf.TestData.newBuilder();
	    data.setName("name");
		data.setId(2);
		ha.sendFirstMsgByDiscovery("1_2233", 2, (short)10,data.build());
		for(int i=0;i<10;i++) {
			ha.sendMsg("1_2233", 2 ,(short)10,data.build());
			Thread.sleep(5000l);
			
		}
	}

}
