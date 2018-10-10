package com.core.cluster;

import java.io.Serializable;

/**
 * 服务模型
 * 
 * @Description:
 * @author: hang
 * @date: 2018年9月10日下午5:44:30
 * @version V1.7
 */
public class Service implements Serializable {
	private static final long serialVersionUID = 3946270881719849852L;
	
	private String serviceId;//服务的唯一id（业务自定义）
	private String hosts;//主机地址
	private int port;//主机端口号
	private int type;//服务类型(业务自定义)

	public Service() {
	}

	public Service(String id) {
		super();
		this.serviceId = id;
	}
	
	public Service(String id,int type) {
		super();
		this.serviceId = id;
		this.type = type;
	}


	public Service(String id, String hosts, int port, int type) {
		super();
		this.serviceId = id;
		this.hosts = hosts;
		this.port = port;
		this.type = type;
	}
	
	

	@Override
	public String toString() {
		return "Service [id=" + serviceId + ", hosts=" + hosts + ", port=" + port + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Service other = (Service) obj;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		return true;
	}


	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
