package com.yu.dynamic.thread.pool.admin.model;

public interface RedisConstant {
	/**
	 * 线程池配置列表 key
	 */
	String THREAD_POOL_CONFIG_LIST_KEY = "thread-pool-config-list";

	/**
	 * 线程池配置参数列表 key
	 */
	String THREAD_POOL_CONFIG_PARAMETER_LIST_KEY = "thread-pool-config-parameter-list";

	/**
	 * 动态线程池 redis topic
	 */
	String DYNAMIC_THREAD_POOL_REDIS_TOPIC = "thread-pool-topic";
}
