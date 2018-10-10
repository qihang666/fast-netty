package com.core.dispatcher.disruptor;

import com.core.netty.coder.Message;
import com.core.thread.NameThreadFactory;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public abstract class DisruptorManager {

	private Disruptor<MsgEvent<Message>> disruptor;

	@SuppressWarnings("unchecked")
	public DisruptorManager() {

		EventFactory<MsgEvent<Message>> eventFactory = new MsgEventFactory<Message>();
		int bufferSize = 1024 * 16;
		disruptor = new Disruptor<MsgEvent<Message>>(eventFactory, bufferSize,
				new NameThreadFactory("Disruptor"));
		disruptor.handleEventsWith(new EventHandler<MsgEvent<Message>>() {

			@Override
			public void onEvent(MsgEvent<Message> event, long sequence, boolean endOfBatch) throws Exception {
				eventHandler(event.get(),sequence);
			}
			
		});
		disruptor.start();

	}

	public void publishEvent(Message m) {
		RingBuffer<MsgEvent<Message>> rb = disruptor.getRingBuffer();
		long index = rb.next();
		MsgEvent<Message> event = rb.get(index);
		event.set(m);
		rb.publish(index);
	}
	
	
	public abstract void eventHandler(Message msg,long sequence);
	

}
