package com.yu.dynamic.thread.pool.admin;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Configurable
public class AdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class);
	}
}
