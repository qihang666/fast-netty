package com.core.cluster.strategy;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import com.core.cluster.Service;
/**
 * 随机策略
 * @Description:   
 * @author: hang   
 * @date: 2018年9月10日下午6:51:55   
 * @version V1.7
 */
public class RandomStrategy implements Strategy {

	private Random rand = new Random();
	
	@Override
	public Service discovery(Set<Service> set) {
		int index = rand.nextInt(set.size());
		Service service = new ArrayList<>(set).get(index);
		return service;
	}

}
