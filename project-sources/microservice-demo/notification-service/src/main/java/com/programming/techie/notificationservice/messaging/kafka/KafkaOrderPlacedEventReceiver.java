package com.programming.techie.notificationservice.messaging.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.programming.techie.notificationservice.messaging.events.OrderPlacedEvent;
import com.programming.techie.notificationservice.service.OrderPlacedEventProcessor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaOrderPlacedEventReceiver {
	
	private final OrderPlacedEventProcessor orderPlacedEventProcessor;

	@KafkaListener(
			topics = "${message.topic.name}",
			containerFactory = "orderEventKafkaListenerContainerFactory"
	)
    public void handleOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent)
    {
		orderPlacedEventProcessor.submit(orderPlacedEvent);
    }
	
}
