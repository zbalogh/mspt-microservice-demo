package com.programming.techie.discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
@EnableEurekaServer
public class DiscoveryServiceMSPTApplication
{
	/**
	 * The main entry point of the Spring Boot Application
	 */
    public static void main(String[] args)
    {
        SpringApplication.run(DiscoveryServiceMSPTApplication.class, args);
    }
    
}
