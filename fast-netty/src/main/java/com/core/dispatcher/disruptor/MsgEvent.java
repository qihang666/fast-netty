package com.core.dispatcher.disruptor;

public class MsgEvent<T> {
	private T value;

	public T get() {
		return value;
	}

	public void set(T value) {
		this.value = value;
	}
}
