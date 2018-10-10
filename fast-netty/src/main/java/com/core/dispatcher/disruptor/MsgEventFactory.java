package com.core.dispatcher.disruptor;

import com.lmax.disruptor.EventFactory;

public class MsgEventFactory<T> implements EventFactory<MsgEvent<T>>{

	@Override
	public MsgEvent<T> newInstance() {
		return new MsgEvent<T>();
	}

}
