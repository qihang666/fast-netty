package com.core.netty.bootstrap.client.pool;

import com.core.dispatcher.HandlerRegister;
import com.core.dispatcher.MessageDispatcher;
import com.core.netty.bootstrap.client.TcpClient;

public class SimpleTcpClient extends TcpClient {
	public SimpleTcpClient(String hosts,int port ) {
		super(hosts, port);
	}
	public SimpleTcpClient(String hosts,int port,HandlerRegister register) {
		super(hosts, port);
		this.register = register;
		init();
	}

	@Override
	public void init() {
		if(register!= null && !register.getAlasszs().isEmpty()) {
			for (Class<?> c : register.getAlasszs()) {
				MessageDispatcher.register(c);
			}
		}
	}

}
