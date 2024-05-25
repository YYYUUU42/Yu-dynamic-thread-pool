package com.yu.dynamic.thread.pool.admin.config;

import com.yu.dynamic.thread.pool.admin.model.RedisClientConfigProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RedisClientConfigProperties.class)
public class RedisClientConfig {

	@Bean("redissonClient")
	public RedissonClient redissonClient(ConfigurableApplicationContext applicationContext, RedisClientConfigProperties properties) {
		Config config = new Config();
		config.setCodec(JsonJacksonCodec.INSTANCE);

		config.useSingleServer()
				.setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
				.setConnectionPoolSize(properties.getPoolSize())
				.setConnectionMinimumIdleSize(properties.getMinIdleSize())
				.setIdleConnectionTimeout(properties.getIdleTimeout())
				.setConnectTimeout(properties.getConnectTimeout())
				.setRetryAttempts(properties.getRetryAttempts())
				.setRetryInterval(properties.getRetryInterval())
				.setPingConnectionInterval(properties.getPingInterval())
				.setKeepAlive(properties.isKeepAlive());

		return Redisson.create(config);
	}

}