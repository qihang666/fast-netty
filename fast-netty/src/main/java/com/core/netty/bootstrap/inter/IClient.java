package com.core.netty.bootstrap.inter;

public interface IClient {
	/**
	 * 初始化
	 */
	void init();

	/**
	 * 启动
	 */
	void start();

	/**
	 * 关闭
	 */
	void stop();

	/**
	 * 写数据
	 * @param msg
	 */
	void writeAndFlush(Object msg);	
}
