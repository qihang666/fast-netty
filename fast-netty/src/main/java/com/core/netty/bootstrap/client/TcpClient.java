package com.core.netty.bootstrap.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.dispatcher.HandlerRegister;
import com.core.netty.bootstrap.inter.IClient;
import com.core.netty.coder.Decoder;
import com.core.netty.coder.Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public abstract class TcpClient implements IClient {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	private Channel channel;
	private Bootstrap bootstrap;
	private NioEventLoopGroup workGroup;
	protected HandlerRegister register;// 注册的handlers
	private int port;//主机端口
	private String hosts;//主机地址
	private boolean isReConn;// 重连
	
	public TcpClient(String hosts, int port) {
		this(hosts,port,false);
	}
	public TcpClient(String hosts, int port,boolean isReConn) {
		this.hosts = hosts;
		this.port = port;
		this.isReConn = isReConn;
		init();
		bootstrap = new Bootstrap();
		workGroup = new NioEventLoopGroup();
		bootstrap.group(workGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		// 重连检测handler
		ReConnHandler reConnHandler = new ReConnHandler(this, this.hosts,this.port, this.isReConn) {

			@Override
			public ChannelHandler[] handlers() {
				return new ChannelHandler[] { this, new Decoder(), new Encoder(), new ClientHandler() };
			}
		};
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(reConnHandler.handlers());
			}
		});
	}
	

	@Override
	public void start() {

	}

	@Override
	public void stop() {
		if (workGroup != null) {
			workGroup.shutdownGracefully();
		}
	}

	/**
	 * 连接
	 * 
	 * @return
	 */
	public ChannelFuture connect() {
		try {
			ChannelFuture future = bootstrap.connect(this.hosts, this.port);
			this.channel = future.channel();
			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture channelFuture) throws Exception {
					if (channelFuture.isSuccess()) {
						log.info("Connection " + channelFuture.channel() + " is well established");
					} else {
						log.warn(String.format("Connection get failed on %s due to %s",
								channelFuture.cause().getMessage(), channelFuture.cause()));
					}
				}
			});
			return future;

		} catch (Exception e) {
			log.error("Failed to connect to " + this.hosts + this.port + " due to " + e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeAndFlush(Object msg) {
		if (channel != null) {
			if (channel.isActive()) {
				channel.writeAndFlush(msg);
			}
		}
	}
	

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}

	public void setBootstrap(Bootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	
}
