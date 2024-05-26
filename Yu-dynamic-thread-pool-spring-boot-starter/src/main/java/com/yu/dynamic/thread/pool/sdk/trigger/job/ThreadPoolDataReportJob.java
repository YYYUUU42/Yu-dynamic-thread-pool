package com.yu.dynamic.thread.pool.sdk.trigger.job;

import com.alibaba.fastjson.JSON;
import com.yu.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.yu.dynamic.thread.pool.sdk.domain.model.ThreadPoolConfigEntity;
import com.yu.dynamic.thread.pool.sdk.registry.IRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @author yu
 * @description 定时上报线程池消息
 * @date 2024-05-26
 */
@Slf4j
public class ThreadPoolDataReportJob {


	private final IDynamicThreadPoolService dynamicThreadPoolService;

	private final IRegistry registry;

	public ThreadPoolDataReportJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
		this.dynamicThreadPoolService = dynamicThreadPoolService;
		this.registry = registry;
	}

	/**
	 * 每 10 秒上报一次线程池信息
	 */
	@Scheduled(cron = "0/10 * * * * ?")
	public void execReportThreadPoolList() {
		List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
		registry.reportThreadPool(threadPoolConfigEntities);
		log.info("动态线程池，上报线程池信息：{}", JSON.toJSONString(threadPoolConfigEntities));

		for (ThreadPoolConfigEntity threadPoolConfigEntity : threadPoolConfigEntities) {
			registry.reportThreadPoolConfigParameter(threadPoolConfigEntity);
			log.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntity));
		}

	}

}