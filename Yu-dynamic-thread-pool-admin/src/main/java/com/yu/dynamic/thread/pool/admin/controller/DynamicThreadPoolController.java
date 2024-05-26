package com.yu.dynamic.thread.pool.admin.controller;

import com.alibaba.fastjson.JSON;
import com.yu.dynamic.thread.pool.admin.model.ResponseResult;
import com.yu.dynamic.thread.pool.admin.model.ThreadPoolConfigEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.yu.dynamic.thread.pool.admin.model.AppHttpCodeEnum.SERVER_ERROR;
import static com.yu.dynamic.thread.pool.admin.model.RedisConstant.*;

/**
 * @author yu
 * @description 线程池接口
 * @date 2024-05-24
 */
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/dynamic/thread/pool/")
public class DynamicThreadPoolController {

	@Resource
	private RedissonClient redissonClient;

	/**
	 * 查询线程池数据
	 */
	@GetMapping("query_thread_pool_list")
	public ResponseResult queryThreadPoolList() {
		try {
			RList<Object> list = redissonClient.getList(THREAD_POOL_CONFIG_LIST_KEY);
			return ResponseResult.okResult(list.readAll());
		} catch (Exception e) {
			log.error("查询线程池数据异常", e);
			return ResponseResult.errorResult(SERVER_ERROR);
		}
	}

	/**
	 * 查询线程池配置
	 */
	@GetMapping("query_thread_pool_config")
	public ResponseResult queryThreadPoolConfig(@RequestParam String appName, @RequestParam String threadPoolName) {
		try {
			String cacheKey = THREAD_POOL_CONFIG_PARAMETER_LIST_KEY + ":" + appName + ":" + threadPoolName;
			ThreadPoolConfigEntity threadPoolConfigEntity = redissonClient.<ThreadPoolConfigEntity>getBucket(cacheKey).get();
			return ResponseResult.okResult(threadPoolConfigEntity);
		} catch (Exception e) {
			log.error("查询线程池配置异常", e);
			return ResponseResult.errorResult(SERVER_ERROR);
		}
	}


	/**
	 * 更新线程池配置
	 */
	@GetMapping("update_thread_pool_config")
	public ResponseResult updateThreadPoolConfig(@RequestBody ThreadPoolConfigEntity request) {
		try {
			log.info("修改线程池配置开始 {} {} {}", request.getAppName(), request.getThreadPoolName(), JSON.toJSONString(request));

			RTopic topic = redissonClient.getTopic(DYNAMIC_THREAD_POOL_REDIS_TOPIC+ "-" + request.getAppName());
			topic.publish(request);

			log.info("修改线程池配置完成 {} {}", request.getAppName(), request.getThreadPoolName());

			return ResponseResult.okResult();
		}catch (Exception e){
			log.error("修改线程池配置异常", e);
			return ResponseResult.errorResult(SERVER_ERROR);
		}
	}
}
