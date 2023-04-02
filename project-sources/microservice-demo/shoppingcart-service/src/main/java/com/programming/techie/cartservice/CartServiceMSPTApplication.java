package com.programming.techie.cartservice;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableRedisRepositories
@EnableWebMvc
@Slf4j
public class CartServiceMSPTApplication  implements ApplicationRunner
{
	/**
	 * The main method to start up the application
	 */
	public static void main(String[] args)
	{
		SpringApplication.run(CartServiceMSPTApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		log.info("ShoppingCart Service has successfully started up.");
	}

}
