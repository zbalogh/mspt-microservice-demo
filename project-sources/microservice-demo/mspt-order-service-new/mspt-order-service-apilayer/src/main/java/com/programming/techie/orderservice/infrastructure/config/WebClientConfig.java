package com.programming.techie.orderservice.infrastructure.config;

import java.nio.charset.Charset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	
	// Register the WebClient Builder with the given name
	@Bean(name = "webClientBuilder")
	// Use Spring Cloud LoadBalancer along with WebClient
	// this LoadBalancer will automatically cooperate with Eureka Client
	// NOTE: we do not use Eureka in Kubernetes!
	//@LoadBalanced
	public WebClient.Builder configureWebClientBuilder()
	{
		return WebClient.builder();
	}
	
	
	// Register RestTemplate instance with the given name
    @Bean("restTemplate")
    // Use Spring Cloud LoadBalancer along with RestTemplate
 	// this LoadBalancer will automatically cooperate with Eureka Client
    // NOTE: we do not use Eureka in Kubernetes!
    //@LoadBalanced
    RestTemplate configureRestTemplate()
	{
    	// create instance of REST Template
    	RestTemplate restTemplate = new RestTemplate();
    	
    	// https://stackoverflow.com/questions/27603782/java-spring-resttemplate-character-encoding
    	restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    	
        return restTemplate;
    }
	
}
