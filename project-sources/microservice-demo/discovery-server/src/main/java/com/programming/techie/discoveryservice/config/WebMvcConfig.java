package com.programming.techie.discoveryservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer
{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
    	// register Eureka static files (JS, CSS, etc)
        registry
          .addResourceHandler("/eureka/**")
          .addResourceLocations("/eureka/", "classpath:/static/eureka/");
    }
    
}
