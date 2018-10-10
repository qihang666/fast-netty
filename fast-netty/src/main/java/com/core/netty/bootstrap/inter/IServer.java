package com.core.netty.bootstrap.inter;

public interface IServer {
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
	 * 监听端口
	 * @param port
	 * @return
	 */
	IServer listen(int port);


}
