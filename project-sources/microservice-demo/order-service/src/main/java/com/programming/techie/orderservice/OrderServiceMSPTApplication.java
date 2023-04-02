package com.programming.techie.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableFeignClients
public class OrderServiceMSPTApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(OrderServiceMSPTApplication.class, args);
	}

}
