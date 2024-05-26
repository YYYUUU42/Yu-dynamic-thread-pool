package com.yu.dynamic.thread.pool.sdk.config;

import com.yu.dynamic.thread.pool.sdk.domain.DynamicThreadPoolServiceImpl;
import com.yu.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.yu.dynamic.thread.pool.sdk.domain.model.ThreadPoolConfigEntity;
import com.yu.dynamic.thread.pool.sdk.domain.model.RegistryEnumVO;
import com.yu.dynamic.thread.pool.sdk.registry.IRegistry;
import com.yu.dynamic.thread.pool.sdk.registry.redis.RedisRegistry;
import com.yu.dynamic.thread.pool.sdk.trigger.job.ThreadPoolDataReportJob;
import com.yu.dynamic.thread.pool.sdk.trigger.listener.ThreadPoolConfigAdjustListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yu
 * @description 自动装配
 * @date 2024-05-24
 */
@Configuration
@EnableConfigurationProperties(DynamicThreadPoolAutoProperties.class)
@EnableScheduling
@Slf4j
public class DynamicThreadPoolAutoConfig {
	private String applicationName;

	/**
	 * Redis 配置
	 */
	@Bean("redissonClient")
	public RedissonClient redissonClient(DynamicThreadPoolAutoProperties properties) {
		Config config = new Config();config.setCodec(JsonJacksonCodec.INSTANCE);

		config.useSingleServer()
				.setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
				.setPassword(properties.getPassword())
				.setConnectionPoolSize(properties.getPoolSize())
				.setConnectionMinimumIdleSize(properties.getMinIdleSize())
				.setIdleConnectionTimeout(properties.getIdleTimeout())
				.setConnectTimeout(properties.getConnectTimeout())
				.setRetryAttempts(properties.getRetryAttempts())
				.setRetryInterval(properties.getRetryInterval())
				.setPingConnectionInterval(properties.getPingInterval())
				.setKeepAlive(properties.isKeepAlive())
		;

		RedissonClient redissonClient = Redisson.create(config);

		log.info("动态线程池，注册器（redis）链接初始化完成。{} {} {}", properties.getHost(), properties.getPoolSize(), !redissonClient.isShutdown());

		return redissonClient;
	}


	/**
	 * 动态线程池服务
	 */
	@Bean("dynamicThreadPollService")
	public DynamicThreadPoolServiceImpl dynamicThreadPollService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap, RedissonClient redissonClient) {
		applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");

		if (StringUtils.isBlank(applicationName)) {
			applicationName = "null";
			log.warn("动态线程池，启动提示。SpringBoot 应用未配置 spring.application.name 无法获取到应用名称！");
		}

		// 获取缓存数据，设置本地线程池配置
		Set<String> threadPoolKeys = threadPoolExecutorMap.keySet();
		for (String threadPoolKey : threadPoolKeys) {
			ThreadPoolConfigEntity threadPoolConfigEntity = redissonClient.<ThreadPoolConfigEntity>getBucket(RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() + ":" + applicationName + ":" + threadPoolKey).get();
			if (null == threadPoolConfigEntity) continue;
			ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolKey);
			threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
			threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
		}

		return new DynamicThreadPoolServiceImpl(applicationName, threadPoolExecutorMap);
	}

	/**
	 * 线程池数据上报任务
	 */
	@Bean
	public ThreadPoolDataReportJob threadPoolDataReportJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
		return new ThreadPoolDataReportJob(dynamicThreadPoolService, registry);
	}

	/**
	 * 线程池配置调整监听
	 */
	@Bean
	public ThreadPoolConfigAdjustListener threadPoolConfigAdjustListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
		return new ThreadPoolConfigAdjustListener(dynamicThreadPoolService, registry);
	}

	/**
	 * Redis 订阅
	 */
	@Bean(name = "dynamicThreadPoolRedisTopic")
	public RTopic threadPoolConfigAdjustListener(RedissonClient redissonClient, ThreadPoolConfigAdjustListener threadPoolConfigAdjustListener) {
		RTopic topic = redissonClient.getTopic(RegistryEnumVO.DYNAMIC_THREAD_POOL_REDIS_TOPIC.getKey() + "-" + applicationName);
		topic.addListener(ThreadPoolConfigEntity.class, threadPoolConfigAdjustListener);
		return topic;
	}

	/**
	 * Redis 服务注册
	 */
	@Bean
	public IRegistry redisRegistry(RedissonClient redissonClient) {
		return new RedisRegistry(redissonClient);
	}
}
