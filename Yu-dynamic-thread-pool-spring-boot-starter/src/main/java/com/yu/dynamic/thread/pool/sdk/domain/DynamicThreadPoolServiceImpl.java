package com.yu.dynamic.thread.pool.sdk.domain;

import com.alibaba.fastjson.JSON;
import com.yu.dynamic.thread.pool.sdk.domain.model.ThreadPoolConfigEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yu
 * @description 动态线程池服务实现
 * @date 2024-05-25
 */
@Slf4j
public class DynamicThreadPoolServiceImpl implements IDynamicThreadPoolService {

	/**
	 * 服务名称
	 */
	private final String applicationName;

	/**
	 * 线程池集合
	 */
	private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;

	public DynamicThreadPoolServiceImpl(String applicationName, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
		this.applicationName = applicationName;
		this.threadPoolExecutorMap = threadPoolExecutorMap;
	}

	/**
	 * 查询线程池列表
	 */
	@Override
	public List<ThreadPoolConfigEntity> queryThreadPoolList() {
		Set<String> threadPoolBeanNames = threadPoolExecutorMap.keySet();
		List<ThreadPoolConfigEntity> threadPoolVOS = new ArrayList<>(threadPoolBeanNames.size());
		for (String beanName : threadPoolBeanNames) {
			ThreadPoolConfigEntity threadPoolConfigVO = getThreadPoolConfig(beanName);
			threadPoolVOS.add(threadPoolConfigVO);
		}
		return threadPoolVOS;
	}

	/**
	 * 根据线程池名称查询线程池配置
	 */
	@Override
	public ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName) {
		ThreadPoolConfigEntity threadPoolConfigVO = getThreadPoolConfig(threadPoolName);

		if (log.isDebugEnabled()) {
			log.info("动态线程池，配置查询 应用名:{} 线程名:{} 池化配置:{}", applicationName, threadPoolName, JSON.toJSONString(threadPoolConfigVO));
		}

		return threadPoolConfigVO;
	}

	/**
	 * 更新线程池配置
	 */
	@Override
	public void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity) {
		if (threadPoolConfigEntity == null || !applicationName.equals(threadPoolConfigEntity.getAppName())) return;
		ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolConfigEntity.getThreadPoolName());
		if (threadPoolExecutor == null) {
			return;
		}

		// 设置参数 「调整核心线程数和最大线程数」
		threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
		threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
	}

	/**
	 * 获取线程池配置
	 */
	private ThreadPoolConfigEntity getThreadPoolConfig(String beanName) {
		ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(beanName);
		if (threadPoolExecutor == null) {
			return new ThreadPoolConfigEntity(applicationName, beanName);
		}

		ThreadPoolConfigEntity threadPoolConfigVO = new ThreadPoolConfigEntity(applicationName, beanName);
		threadPoolConfigVO.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
		threadPoolConfigVO.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
		threadPoolConfigVO.setActiveCount(threadPoolExecutor.getActiveCount());
		threadPoolConfigVO.setPoolSize(threadPoolExecutor.getPoolSize());
		threadPoolConfigVO.setQueueType(threadPoolExecutor.getQueue().getClass().getSimpleName());
		threadPoolConfigVO.setQueueSize(threadPoolExecutor.getQueue().size());
		threadPoolConfigVO.setRemainingCapacity(threadPoolExecutor.getQueue().remainingCapacity());

		return threadPoolConfigVO;
	}
}
