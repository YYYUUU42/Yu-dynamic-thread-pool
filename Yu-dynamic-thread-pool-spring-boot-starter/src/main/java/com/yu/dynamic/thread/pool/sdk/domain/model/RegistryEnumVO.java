package com.yu.dynamic.thread.pool.sdk.domain.model;

import lombok.Getter;

/**
 * @author yu
 * @description 注册中心枚举值对象
 * @date 2024-05-24
 */
@Getter
public enum RegistryEnumVO {

	THREAD_POOL_CONFIG_LIST_KEY("thread-pool-config-list", "池化配置列表"),
	THREAD_POOL_CONFIG_PARAMETER_LIST_KEY("thread-pool-config-parameter-list", "池化配置参数"),
	DYNAMIC_THREAD_POOL_REDIS_TOPIC("thread-pool-topic", "动态线程池监听主题配置");

	private final String key;
	private final String desc;

	RegistryEnumVO(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}
}
