package com.core.cluster.strategy;

import java.util.ArrayList;
import java.util.Set;

import com.core.cluster.Service;

/**
 * 轮训策略
 * 
 * @Description:
 * @author: hang
 * @date: 2018年9月10日下午6:51:55
 * @version V1.7
 */
public class OrderLoopStrategy implements Strategy {

	private int index = 0;

	@Override
	public Service discovery(Set<Service> set) {
		if (index >= set.size()) {
			index = 0;
		}
		Service service = new ArrayList<>(set).get(index);
		index++;
		return service;
	}

}
