package com.core.cluster;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.cluster.strategy.OrderLoopStrategy;
import com.core.cluster.strategy.Strategy;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 集群服务管理
 * 
 * @Description:
 * @author: hang
 * @date: 2018年9月10日下午5:23:16
 * @version V1.7
 */
public class ClusterManager {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	public int SESSION_TIME_OUT = 100;

	public ZkClient zkClient;//zk客户端
	public Strategy strategy;// 服务发现策略
	public Lock lock =  new ReentrantLock();

	public static String ROOT_PATH = "/service";// 服务注册根路径

	//已发现的服务资源
	public static Map<Integer, Set<Service>> services = Maps.newConcurrentMap();
	// 本实例 注册的服务缓存 断线重连后 重新注册
	public static Set<ServiceRegister> registeredServices = Sets.newConcurrentHashSet();

	/**
	 * 
	 * @param address  zk服务器地址 
	 *  address= "192.168.31.150:3001";//单台配置  
	 *  address="192.168.31.150:3001,192.168.31.150:3002,192.168.31.150:3003";多台集群  
	 * @param strategy 服务发现策略  目前 有随机  还有轮训  可自由定义实现{@link Strategy}
	 */
	public ClusterManager(String address,Strategy strategy) {
		this.strategy = strategy;
		zkClient = new ZkClient(address, SESSION_TIME_OUT, Integer.MAX_VALUE);
		zkClient.subscribeStateChanges(new HAStateListener());
	}
	public ClusterManager(String address) {
		this(address,new OrderLoopStrategy());//默认轮训策略
	}


	/**
	 * 创建集群配置信息
	 * 
	 * @param path
	 * @param data
	 * @param mode
	 */
	public void createPath(String path, Object data, CreateMode mode) {
		if (!zkClient.exists(path)) {
			zkClient.create(path, data, mode);
		} else {
			zkClient.writeData(path, data);
		}
		registeredServices.add(new ServiceRegister(path, data, mode));
	}

	/**
	 * 断线重连 重新注册服务
	 */
	public void reRegister() {
		if (!registeredServices.isEmpty()) {
			for (ServiceRegister s : registeredServices) {
				createPath(s.getPath(), s.getData(), s.getMode());
			}
		}
	}

	/**
	 * 注册服务
	 * 
	 * @param path
	 * @param data
	 * @param mode
	 */
	public void registerService(String path, Object data) {
		try {
			if (!zkClient.exists(ROOT_PATH)) {
				createPath(ROOT_PATH, null, CreateMode.PERSISTENT);
			}
			path = ROOT_PATH + "/" + path;
			this.createPath(path, data, CreateMode.EPHEMERAL);
		} catch (Exception e) {
			throw new RuntimeException("registerService",e);
		}
		
	}
	/**
	 * 服务发现
	 * 
	
	 * @param serviceType
	 *            
	 * @return
	 */
	public Service serviceDiscovery(int serviceType) {
		return serviceDiscovery(serviceType,null);
	}

	/**
	 * 服务发现
	 * @param serviceType 服务类型
	 * @param serviceId  服务id
	 * @return
	 */
	public Service serviceDiscovery(int serviceType,String serviceId) {
		if (services.isEmpty() || services.get(serviceType) == null || services.get(serviceType).isEmpty()) {
			if (zkClient.exists(ROOT_PATH)) {
				servicesInit();
				// 监听child改变
				zkClient.subscribeChildChanges(ROOT_PATH, new HAChildListener());
			}
		}
		if (!services.isEmpty()&&services.get(serviceType) != null && !services.get(serviceType).isEmpty()) {
			if(serviceId == null) {
				return strategy.discovery(services.get(serviceType));
			}else {
				Set<Service> set= services.get(serviceType);
				if(!set.isEmpty()) {
					for(Service service : set) {
						if(service.getServiceId().equals(serviceId)){
							return service;
						}
					}
				}
			}
			 
		}
		return null;
	}

	public void servicesInit() {
		try {
			lock.lock();
			services.clear();
			List<String> childs = zkClient.getChildren(ROOT_PATH);
			if (childs != null && !childs.isEmpty()) {
				for (String childPath : childs) {
					Service service = zkClient.readData(ROOT_PATH + "/" + childPath);
					if (!services.containsKey(service.getType())) {
						services.put(service.getType(), Sets.newConcurrentHashSet());
					}
					services.get(service.getType()).add(service);
				}
				log.info("services init Childs=[{}]",childs);
			}
		} finally {
			lock.unlock();
		}
		
	}

	public ZkClient getZkClient() {
		return zkClient;
	}

	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}

	// 状态监听
	class HAStateListener implements IZkStateListener {

		@Override
		public void handleStateChanged(KeeperState state) throws Exception {

			if (state == KeeperState.Disconnected) {
				log.warn("Disconnected connection to the zkServer ");
                //services.clear();//zkServer 断开 暂不清理缓存的服务
			} else if (state == KeeperState.SyncConnected) {
				// 重新连接zk服务器 重新注册缓存信息
				reRegister();
				log.warn("reConnection to the zkServer  reRegister");
			}

		}

		@Override
		public void handleNewSession() throws Exception {

		}

		@Override
		public void handleSessionEstablishmentError(Throwable error) throws Exception {

		}

	}

	class HAChildListener implements IZkChildListener {

		@Override
		public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
			log.info("listener handleChildChange parentPath=[{}]  Childs=[{}]",parentPath,currentChilds);
			if(parentPath.equals(ROOT_PATH)) {
				servicesInit();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
//		HACluster ha = new HACluster("127.0.0.1:2100");
		ClusterManager ha = new ClusterManager("127.0.0.1:2100",new OrderLoopStrategy());

		ha.registerService("s1",new Service("s1id",1));
		ha.registerService("s2", new Service("s2id",1));
		ha.registerService("s3", new Service("s3id",1));
		ha.registerService("s4", new Service("s4id",1));
		ha.registerService("s5", new Service("s5id",1));
		for(int i=0;i<10;i++) {
			Object o1= ha.serviceDiscovery(1);
			System.err.println("main="+o1);
		}
		
		while(true) {
			Thread.sleep(2000l);
			Object o1= ha.serviceDiscovery(1);
			System.err.println("main="+o1);
		}

	}

}
