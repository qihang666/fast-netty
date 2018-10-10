package com.core.cluster;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.cluster.strategy.Strategy;
import com.core.netty.bootstrap.client.pool.PooledClient;
import com.core.netty.bootstrap.inter.IClient;
import com.core.netty.coder.Message;
import com.google.common.collect.Maps;
import com.google.protobuf.GeneratedMessage;

/**
 * 集群消息分发器 
 * 
 * @Description:
 * @author: hang
 * @date: 2018年9月11日下午4:07:00
 * @version V1.7
 */
public class ClusterDispatcher {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	// 服务信息 连接池客户端
	private static Map<Service, IClient> pooledClientMap = Maps.newConcurrentMap();
	// key 唯一标识 value 服务
	private static Map<Object, Service> serviceMap = Maps.newConcurrentMap();

	private ClusterManager clusterManager;// 服务管理者

	/**
	 * 
	 * @param address
	 *            zk地址
	 */
	public ClusterDispatcher(String address) {
		this.clusterManager = new ClusterManager(address);
	}

	/**
	 * 
	 * @param address
	 *            zkserver 地址
	 * @param strategy
	 *            发现策略
	 */
	public ClusterDispatcher(String address, Strategy strategy) {
		this.clusterManager = new ClusterManager(address, strategy);
	}

	/**
	 *  策略发现服务 发送消息      ( 第一次调用绑定 uuid 与 服务的连接, 后 续发送不需再次发现)
	 * @param uuid
	 * @param serviceType
	 * @param msgId
	 * @param msg
	 */
	public void sendFirstMsgByDiscovery(String uuid, int serviceType, short msgId, GeneratedMessage msg) {
		Message message = new Message(msgId, msg.toByteArray());
		try {
			IClient clientPool = getClientByDiscovery(uuid, serviceType);
			if (clientPool != null) {
				clientPool.writeAndFlush(message);
			}

		} catch (Exception e) {
			log.error("sendFirstDiscoveryMsg",e);
		}
	}
	
	
	/**
	 *   指定发现服务 发送消息      ( 第一次调用绑定 uuid 与 服务的连接, 后 续发送不需再次发现)
	 * @param uuid  
	 * @param serviceType
	 * @param msgId
	 * @param msg
	 */
	public void sendFirstMsgByServiceId(String uuid, int serviceType, String serviceId, short msgId, GeneratedMessage msg) {
		Message message = new Message(msgId, msg.toByteArray());
		try {
			IClient clientPool = getClientByServiceId(uuid, serviceType, serviceId);
			if (clientPool != null) {
				clientPool.writeAndFlush(message);
			}

		} catch (Exception e) {
			log.error("sendFirstMsgByServiceId",e);
		}
	}

	/**
	 * 根据uuid 找到 服务 发送消息     在第一次发现后 后续用这个方法发送消息（之前已经绑定过无需发现）
	 * @param uuid
	 * @param serviceType
	 * @param msgId
	 * @param msg
	 */
	public void sendMsg(String uuid, int serviceType, short msgId, byte[] msg) {
		Message message = new Message(msgId, msg);
		try {
			IClient clientPool = getClient(uuid, serviceType);
			if (clientPool != null) {
				clientPool.writeAndFlush(message);
			}

		} catch (Exception e) {
			log.error("sendMsg",e);
		}
	}

	/**
	 * 根据uuid 找到 服务 发送消息     在第一次发现后 后续用这个方法发送消息（之前已经绑定过无需发现）
	 * @param uuid
	 * @param serviceType
	 * @param msgId
	 * @param msg
	 */
	public void sendMsg(String uuid, int serviceType, short msgId, GeneratedMessage msg) {
		sendMsg(uuid, serviceType,msgId,msg.toByteArray());
	}



	/**
	 * 根据服务ID 发现服务
	 * 
	 * @param uuid
	 * @param serviceType
	 * @param serviceId
	 * @return
	 */
	public IClient getClientByServiceId(String uuid, int serviceType, String serviceId) {
		
		String uuidKey = uuid + "-type-" + serviceType;
		if (serviceId != null) {
			// 根据服务ID 发现服务
			// 去发现服务
			Service service = clusterManager.serviceDiscovery(serviceType, serviceId);
			// System.err.println(service);
			if (service == null) {
				throw new RuntimeException("no service is Discovery");
			}

			if (!pooledClientMap.containsKey(service)) {
				IClient clientPool2 = new PooledClient(service.getHosts(), service.getPort());
				pooledClientMap.put(service, clientPool2);
			}
			serviceMap.put(uuidKey, service);
//			System.err.println(" 11 " + service);
		}
		return getClient(uuid,serviceType);

	}

	/**
	 * 根据次略发现服务
	 * 
	 * @param uuid
	 * @param serviceType
	 * @param isDiscovery
	 * @return
	 */
	public IClient getClientByDiscovery(String uuid, int serviceType) {
		String uuidKey = uuid + "-type-" + serviceType;
		// 发现服务
		Service service = clusterManager.serviceDiscovery(serviceType);
		// System.err.println(service);
		if (service == null) {
			throw new RuntimeException("no service is Discovery");
		}

		if (!pooledClientMap.containsKey(service)) {
			IClient clientPool2 = new PooledClient(service.getHosts(), service.getPort());
			pooledClientMap.put(service, clientPool2);
		}
		serviceMap.put(uuidKey, service);
//		System.err.println(" 22 " + service);
		return getClient(uuid,serviceType);
	}

	public IClient getClient(String uuid, int serviceType) {
		IClient clientPool = null;
		String uuidKey = uuid + "-type-" + serviceType;
		try {
			if (!serviceMap.containsKey(uuidKey)) {
				log.error("no service ");
				return clientPool;
			}
			Service service = serviceMap.get(uuidKey);
			clientPool = pooledClientMap.get(service);
//			System.err.println(" 33 " + service);

		} catch (Exception e) {
			log.error("ClusterHolder clientPool writeAndFlush ", e);
		}
		return clientPool;
	}

	public ClusterManager getClusterManager() {
		return clusterManager;
	}

	public void setClusterManager(ClusterManager clusterManager) {
		this.clusterManager = clusterManager;
	}

	public void registerService(String path, Object data) {
		this.clusterManager.registerService(path, data);
	}

	public static void main(String[] args) {

	}

}
