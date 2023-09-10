package com.programming.techie.orderservice.ddd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication( scanBasePackages = {"com.programming.techie"} )
@EnableJpaRepositories( basePackages = {"com.programming.techie"} )
@EntityScan( basePackages = {"com.programming.techie"} )
@EnableWebMvc
@EnableFeignClients
public class OrderServiceMSPTApplicationDDD
{
	public static void main(String[] args)
	{
		SpringApplication.run(OrderServiceMSPTApplicationDDD.class, args);
	}
	
}
