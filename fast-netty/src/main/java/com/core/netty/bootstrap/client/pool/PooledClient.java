package com.core.netty.bootstrap.client.pool;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

import com.core.netty.bootstrap.inter.IClient;
/**
 * 连接池客户端
 * @Description:   
 * @author: hang   
 * @date: 2018年9月10日上午11:23:48   
 * @version V1.7
 */
public class PooledClient implements IClient{
    //连接池对象
	private TcpClientChannelPool pools;

	public PooledClient(String hosts,int port) {
		this.pools = new TcpClientChannelPool(null, port, hosts, null);
	}
	
	public PooledClient(String hosts,int port,Config poolConfig) {
		this.pools = new TcpClientChannelPool(poolConfig, port, hosts, null);
	}
	
	@Override
	 public void writeAndFlush(Object msg) {
		 TcpClientChannel  resource = this.pools.getResource();
		 try {
			 resource.getChannelFuture().channel().writeAndFlush(msg);
		} catch (Exception e) {
			throw e;
		}finally {
			this.pools.returnResource(resource);
		}
	}

	public TcpClientChannelPool getPools() {
		return pools;
	}

	@Override
	public void init() {
		
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}
	
	

}
