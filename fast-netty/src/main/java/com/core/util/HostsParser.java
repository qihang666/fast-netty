package com.core.util;
/**
 * 主机地址解析器
 * @Description:   
 * @author: hang   
 * @date: 2018年9月11日下午6:55:02   
 * @version V1.7
 */
public class HostsParser {
	private String hosts;
	private int port;

	public HostsParser(String address){
		String []sp =  address.split(":");
		if(sp.length==2) {
			hosts = sp[0];
			port = Integer.valueOf(sp[1]);
		}
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
	
	
	
}
