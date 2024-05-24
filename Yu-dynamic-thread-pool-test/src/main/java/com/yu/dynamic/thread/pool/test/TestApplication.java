package com.yu.dynamic.thread.pool.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
@Configurable
public class TestApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class);
	}


	@Bean
	public ApplicationRunner applicationRunner(ExecutorService threadPoolExecutor) throws InterruptedException {
		return args -> {
			while (true) {
				Random random = new Random();
				// 随机时间，用于模拟任务启动延迟  1~3秒
				int randomInitialDelay = random.nextInt(3) + 1;
				// 随机休眠时间，用于模拟任务执行时间  1~3秒
				int randomSleepTime = random.nextInt(3) + 1;

				threadPoolExecutor.submit(() -> {
					try {
						// 模拟任务启动延迟
						TimeUnit.SECONDS.sleep(randomInitialDelay);
						log.info("Task started after " + randomInitialDelay + " seconds.");

						// 模拟任务执行
						TimeUnit.SECONDS.sleep(randomSleepTime);
						log.info("Task executed for " + randomSleepTime + " seconds.");
					} catch (Exception ex) {
						// 报错就终止当前线程
						Thread.currentThread().interrupt();
					}
				});

				Thread.sleep(random.nextInt(10) + 1);
			}
		};
	}
}
