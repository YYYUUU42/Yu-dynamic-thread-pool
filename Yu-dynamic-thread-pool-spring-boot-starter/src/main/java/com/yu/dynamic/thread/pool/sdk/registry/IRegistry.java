package com.yu.dynamic.thread.pool.sdk.registry;


import com.yu.dynamic.thread.pool.sdk.domain.model.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @author yu
 * @description 注册中心接口
 * @date 2024-05-24
 */
public interface IRegistry {

    /**
     * 池化配置列表
     */
    void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities);

    /**
     * 池化配置参数
     */
    void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);

}
