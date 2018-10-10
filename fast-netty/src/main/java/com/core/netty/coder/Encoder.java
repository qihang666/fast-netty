package com.core.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		if (null == msg) {
			throw new Exception("msg is null");
		}
		int length = 2;
		if (msg.getBody() != null) {
			length =length+ msg.getBody().length;
		}
		out.writeInt(length);
		out.writeShort(msg.getId());
		if (msg.getBody() != null) {
			out.writeBytes(msg.getBody());// 2进制数据
		}

	}

}
