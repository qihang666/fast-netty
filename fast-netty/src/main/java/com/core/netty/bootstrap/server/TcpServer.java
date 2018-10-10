package com.core.netty.bootstrap.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.netty.bootstrap.inter.IServer;
import com.core.netty.coder.Decoder;
import com.core.netty.coder.Encoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public abstract class TcpServer implements IServer {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	private ServerBootstrap boot;
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workGroup;
	private int port;

	@Override
	public IServer listen(int port) {
		this.port = port;
		return this;
	}

	@Override
	public void start() {
		init();
		boot = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup();
		workGroup = new NioEventLoopGroup();
		try {
			boot.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					// .handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(new Decoder());
							ch.pipeline().addLast(new Encoder());
							ch.pipeline().addLast(new ServerHandler());
							ch.pipeline().addLast(new IdleStateHandler(ServerIdleHandler.READER_TIME, 0, 0));
							ch.pipeline().addLast(new ServerIdleHandler());
						}
					});
			ChannelFuture future = boot.bind(port).sync();
			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						log.info("--------------------------------------------------------------------------------------");
						log.info("--------------server Satrt----listenPort="+port+"-----------------------------------------");
						log.info("--------------------------------------------------------------------------------------");
					}
				}
			});

			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// add shutdown hook
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					stop();
				}
			}));

		}

	}

	@Override
	public void stop() {
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}
		if (workGroup != null) {
			workGroup.shutdownGracefully();
		}
	}
}
