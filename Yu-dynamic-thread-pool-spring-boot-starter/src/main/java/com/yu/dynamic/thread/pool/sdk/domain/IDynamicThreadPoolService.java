package com.yu.dynamic.thread.pool.sdk.domain;

import com.yu.dynamic.thread.pool.sdk.domain.model.ThreadPoolConfigEntity;
import java.util.List;

/**
 * @author yu
 * @description 动态线程池服务
 * @date 2024-05-24
 */
public interface IDynamicThreadPoolService {

    /**
     * 查询线程池列表
     */
    List<ThreadPoolConfigEntity> queryThreadPoolList();

    /**
     * 根据线程池名称查询线程池配置
     */
    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);

    /**
     * 更新线程池配置
     */
    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);

}
