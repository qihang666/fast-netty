package com.core.netty.bootstrap.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
/**
 * 读取数据超时处理
 * @Description:   
 * @author: hang   
 * @date: 2018年9月6日下午5:55:47   
 * @version V1.7
 */
public class ServerIdleHandler extends ChannelDuplexHandler{
	
	public static final int READER_TIME = 60*30;//多久没有数据读取了 关闭通道  避免消耗服务器资源 单位(秒)
//	public static final int READER_TIME = 6;
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
            	log.info("Write idle on channel:" + ctx.channel() + " is timeout");
            } else if (e.state() == IdleState.READER_IDLE) {
            	log.info("Read idle on channel:" + ctx.channel() + " is timeout on "
                        + ctx.channel().remoteAddress() + ", so close it");
                ctx.close();
            }
        }
	
	}

}
