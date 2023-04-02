package com.programming.techie.orderservice.messaging.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.programming.techie.orderservice.messaging.events.OrderPlacedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("kafkaOrderPlacedEventSender")
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderPlacedEventSender implements IKafkaMessageBusSender<OrderPlacedEvent> {

	private final KafkaTemplate<String, OrderPlacedEvent> orderEventKafkaTemplate;
	
	@Value(value = "${message.topic.name}")
	private String topicName;
	
	@Override
	public void publishMessage(String queueOrTopicName, OrderPlacedEvent message)
	{
		log.info("[Kafka] Sending 'OrderPlacedEvent' message to '"+queueOrTopicName+"' topic.");
		
		orderEventKafkaTemplate.send(queueOrTopicName, message);
	}

	@Override
	public void publishMessage(OrderPlacedEvent message)
	{
		publishMessage(topicName, message);
	}

}
