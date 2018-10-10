package com.core.netty.bootstrap.client.pool;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.dispatcher.HandlerRegister;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class TcpClientFactory extends BasePoolableObjectFactory {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	private SimpleTcpClient client;

	public TcpClientFactory(int port, String hosts,HandlerRegister register, boolean isReConn) {
		client = new SimpleTcpClient(hosts,port,register);
	}

	@Override
	public Object makeObject() throws Exception {
		ChannelFuture future = client.connect();
		future.awaitUninterruptibly();
		if (!future.isSuccess()) {
			log.error("Making new connection on " + client.getHosts() + client.getPort() + " not success", future.cause());
		}
		TcpClientChannel channel = new TcpClientChannel(future);
		return channel;
	}

	@Override
	public void destroyObject(Object obj) throws Exception {
		if (obj instanceof TcpClientChannel) {
			TcpClientChannel c = (TcpClientChannel) obj;
			Channel channel = c.getChannelFuture().channel();
			if (!channel.isOpen() || channel.isActive()) {
				channel.close();
			}
			log.info("Closing channel and destroy connection from pool done");
		}
	}

	@Override
	public boolean validateObject(Object obj) {
	    if (obj instanceof TcpClientChannel) {
            final TcpClientChannel ch = (TcpClientChannel) obj;
            Channel channel = ch.getChannelFuture().channel();
            return channel.isOpen() && channel.isActive();
        }
        return false;
	}

}
