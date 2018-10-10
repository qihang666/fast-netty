package com.core.cluster.strategy;

import java.util.Set;

import com.core.cluster.Service;

/**
 * 服务发现策略 接口
 * @Description:   
 * @author: hang   
 * @date: 2018年9月10日下午5:58:21   
 * @version V1.7
 */
public interface Strategy {

	Service discovery(Set<Service> set);
}
