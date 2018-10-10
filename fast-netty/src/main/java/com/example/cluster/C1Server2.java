package com.example.cluster;

import com.core.cluster.ClusterManager;
import com.core.cluster.Service;
import com.core.dispatcher.MessageDispatcher;
import com.core.netty.bootstrap.server.TcpServer;
import com.example.DomeServerHandler;

public class C1Server2 extends TcpServer {



	@Override
	public void init() {
		
	}
	
	public static void main(String[] args) {
		MessageDispatcher.register(DomeServerHandler.class);
		ClusterManager ha = new ClusterManager("127.0.0.1:2100");
		Service s = new Service("mate2","127.0.0.1",9001,2);
		ha.registerService(s.getServiceId(), s);
		new C1Server2().listen(9001).start();
		
	}

}
