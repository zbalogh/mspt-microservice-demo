package com.programming.techie.orderservice.messaging.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.programming.techie.orderservice.domain.core.events.OrderPlacedEvent;

@Configuration
public class KafkaOrderProducerConfig
{
	@Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
	
	/**
	 * Creating KafkaTemplate for sending an 'OrderPlacedEvent' message.
	 */
	@Bean(name = "orderEventKafkaTemplate")
    public KafkaTemplate<String, OrderPlacedEvent> orderEventKafkaTemplate()
    {
        return new KafkaTemplate<>(orderEventProducerFactory());
    }
	
    private ProducerFactory<String, OrderPlacedEvent> orderEventProducerFactory()
    {
        Map<String, Object> configProps = new HashMap<>();
        
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        
        JsonSerializer<OrderPlacedEvent> jsonSerializer = new JsonSerializer<OrderPlacedEvent>();
        jsonSerializer.noTypeInfo();	// do not include (Java class) type info in the header
        
		return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), jsonSerializer);
    }
	
}
