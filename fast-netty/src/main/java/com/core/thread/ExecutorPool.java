package com.core.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorPool {
	
	public static ExecutorPool instance = new ExecutorPool();
	public static ExecutorPool getInstance() {
		return instance;
	}
	//定时服务线程
	private ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(3);
	
	// 执行事件处理 线程池
//	private  ExecutorService threadPool = 	Executors.newFixedThreadPool(2); 
	private  ExecutorService threadPool = new ThreadPoolExecutor(
			4,
			4,
			0L,
			TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(),
			new NameThreadFactory("dispatcher msg thread")
		    ) ;
	
	public ScheduledExecutorService getScheduledPool() {
		return scheduledPool;
	}

	public ExecutorService getThreadPool() {
		return threadPool;
	}
	
	
	
}
