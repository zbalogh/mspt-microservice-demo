package com.programming.techie.notificationservice.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.programming.techie.notificationservice.messaging.events.OrderPlacedEvent;

@EnableKafka
@Configuration
public class KafkaConsumerConfig
{
	@Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
	
	@Value(value = "${spring.kafka.consumer.group-id}")
    private String orderNotificationGroupId;
	

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderPlacedEvent> orderEventKafkaListenerContainerFactory()
    {
        ConcurrentKafkaListenerContainerFactory<String, OrderPlacedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        
        factory.setConsumerFactory(orderEventConsumerFactory());
        
        return factory;
    }
    
    private ConsumerFactory<String, OrderPlacedEvent> orderEventConsumerFactory()
    {
        Map<String, Object> props = new HashMap<>();
        
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, orderNotificationGroupId);
        
        JsonDeserializer<OrderPlacedEvent> jsonDeserializer = new JsonDeserializer<>(OrderPlacedEvent.class);
        jsonDeserializer.trustedPackages("*");  // trust all java packages for the value types
        
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
    }
    
}
