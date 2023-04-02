package com.programming.techie.cartservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig
{
	private final RedisSerializer<?> keySerializer = new StringRedisSerializer();
	private final RedisSerializer<?> valueSeralizer = new GenericToStringSerializer<Object>(Object.class);
	//private final RedisSerializer<?> jsonSerializer = new GenericJackson2JsonRedisSerializer();

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory)
	{
		// create RedisTemplate instance
		final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		
		// set connection factory, it is automatically created by Spring Boot based on configuration from the application.properties file.
		template.setConnectionFactory(connectionFactory);
		
		// set keySerializer and valueSeralizer
		template.setKeySerializer(keySerializer);
		template.setValueSerializer(valueSeralizer);
		template.setHashKeySerializer(keySerializer);
		template.setHashValueSerializer(valueSeralizer);
		
		return template;
	}
	
}
