package com.programming.techie.productservice.ddd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication( scanBasePackages = {"com.programming.techie"} )
@EnableMongoRepositories( basePackages = {"com.programming.techie"} )
@EnableWebMvc
public class ProductServiceMSPTApplicationDDD
{
	public static void main(String[] args)
	{
		SpringApplication.run(ProductServiceMSPTApplicationDDD.class, args);
	}
	
}
