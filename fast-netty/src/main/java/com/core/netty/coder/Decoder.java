package com.core.netty.coder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Decoder extends ByteToMessageDecoder{


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in == null) {
			return;
        }
        if (in.readableBytes() < Message.HEADER_SIZE) {
			return;
        }
        in.markReaderIndex();
        int lenght = in.readInt();
        if (in.readableBytes() < lenght) {
        	in.resetReaderIndex();
			return;
        }
        
        short id = in.readShort();
        byte[] content = null;
		lenght = lenght-2;
		if(lenght > 0){
			content  =  new byte[lenght];
			in.readBytes(content,0,lenght);
		}
        Message msg = new Message(id,content);
        out.add(msg);

		
	}

	
	
}
