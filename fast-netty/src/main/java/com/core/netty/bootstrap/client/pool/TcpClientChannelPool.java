package com.core.netty.bootstrap.client.pool;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

import com.core.dispatcher.HandlerRegister;
import com.core.util.Pool;

/**
 * 客户端连接池
 * 
 * @Description:
 * @author: hang
 * @date: 2018年9月7日下午2:39:08
 * @version V1.7
 */
public class TcpClientChannelPool extends Pool<TcpClientChannel> {

	/**
	 * 
	 * @param poolConfig
	 *            连接池配置
	 * @param port
	 *            主机端口号
	 * @param hosts
	 *            主机地址
	 * @param register
	 *            注册的处理类
	 */
	public TcpClientChannelPool(Config poolConfig, int port, String hosts, HandlerRegister register) {
		super(poolConfig == null ? new PooledConfig().getPoolConfig() : poolConfig,
				new TcpClientFactory(port, hosts, register, false));
	}

}
