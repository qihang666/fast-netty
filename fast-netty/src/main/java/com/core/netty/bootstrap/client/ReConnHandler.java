package com.core.netty.bootstrap.client;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
/**
 * 重连检测
 * @Description:   
 * @author: hang   
 * @date: 2018年9月3日下午5:44:05   
 * @version V1.7
 */
@Sharable
public abstract class ReConnHandler extends ChannelInboundHandlerAdapter implements TimerTask, HandlerHolder {

	private Bootstrap boot;
	private int renum;// 重连次数
	private boolean isReConn;// 是否重连
	private final Timer timer;
	private final int port;
	private final String host;
	private TcpClient tcpClient;

	public ReConnHandler(TcpClient tcpClient, String host, int port, boolean isReConn) {
		super();
		this.tcpClient = tcpClient;
		this.boot = tcpClient.getBootstrap();
		this.isReConn = isReConn;

		this.host = host;
		this.port = port;
		timer = new HashedWheelTimer();
	}



	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 注册
		renum = 0;
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (isReConn) {
			if (renum < 15) {
				renum++;
				long delay = renum;
				timer.newTimeout(this, delay, TimeUnit.SECONDS);
			}

		}
		ctx.fireChannelInactive();

	}

	@Override
	public void run(Timeout timeout) throws Exception {
		ChannelFuture future;
		// bootstrap已经初始化好了，只需要将handler填入就可以了
		synchronized (boot) {
			boot.handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {

					ch.pipeline().addLast(handlers());
				}
			});
			future = boot.connect(host, port);
		}
		// future对象
		future.addListener(new ChannelFutureListener() {

			public void operationComplete(ChannelFuture f) throws Exception {
				boolean succeed = f.isSuccess();
				// 如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
				if (!succeed) {
					System.out.println("重连失败");
					f.channel().pipeline().fireChannelInactive();
				} else {
					System.out.println("重连成功");
//					System.out.println(future.channel());
					tcpClient.setChannel(future.channel());
				}
			}
		});

	}

}
