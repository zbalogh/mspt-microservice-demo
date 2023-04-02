package com.programming.techie.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.programming.techie.orderservice.messaging.core.IMessageBusSender;
import com.programming.techie.orderservice.messaging.events.OrderPlacedEvent;
import com.programming.techie.orderservice.model.Order;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationApiService {

	@Autowired
	@Qualifier("kafkaOrderPlacedEventSender")
	private IMessageBusSender<OrderPlacedEvent> orderPlacedEventSender;
	
	public void sendOrderPlacedNotification(Order order)
	{
		OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
		orderPlacedEvent.setOrderNumber(order.getOrderNumber());
		orderPlacedEvent.setUserid(order.getUserid());
		
		orderPlacedEventSender.publishMessage(orderPlacedEvent);
	}
	
}
