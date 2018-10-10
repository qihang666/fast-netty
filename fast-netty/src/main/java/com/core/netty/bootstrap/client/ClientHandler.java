package com.core.netty.bootstrap.client;

import com.core.dispatcher.MessageDispatcher;
import com.core.dispatcher.disruptor.DisruptorManager;
import com.core.netty.coder.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Message>{

	private static ThreadLocal<DisruptorManager> LOCAL = new ThreadLocal<DisruptorManager>() {
		@Override
		protected DisruptorManager initialValue() {
			return new DisruptorManager() {
				@Override
				public void eventHandler(Message msg, long sequence) {
					MessageDispatcher.dispatcher(msg);
				}
			};
		}
	};
		
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		LOCAL.get().publishEvent(msg);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}

}
