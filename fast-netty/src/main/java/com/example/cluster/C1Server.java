package com.example.cluster;

import com.core.cluster.ClusterManager;
import com.core.cluster.Service;
import com.core.dispatcher.MessageDispatcher;
import com.core.netty.bootstrap.server.TcpServer;
import com.example.DomeServerHandler;

public class C1Server extends TcpServer {



	@Override
	public void init() {
		
	}
	
	public static void main(String[] args) {
		MessageDispatcher.register(DomeServerHandler.class);
		ClusterManager ha = new ClusterManager("127.0.0.1:2100");
		Service s = new Service("mate1","127.0.0.1",9000,2);
		ha.registerService(s.getServiceId(), s);
		new C1Server().listen(9000).start();
		
		
	}

}
