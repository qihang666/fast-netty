package com.core.netty.bootstrap.client.pool;

import io.netty.channel.ChannelFuture;

public class TcpClientChannel {
	ChannelFuture channelFuture;

	
	public TcpClientChannel(ChannelFuture channelFuture) {
		super();
		this.channelFuture = channelFuture;
	}

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	public void setChannelFuture(ChannelFuture channelFuture) {
		this.channelFuture = channelFuture;
	}

	
}
