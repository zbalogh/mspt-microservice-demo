package com.programming.techie.orderservice.infrastructure.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.programming.techie.common.messaging.core.IMessageBusSender;
import com.programming.techie.orderservice.domain.appservice.outputports.api.NotificationApiService;
import com.programming.techie.orderservice.domain.core.entity.Order;
import com.programming.techie.orderservice.domain.core.events.OrderPlacedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class NotificationApiServiceImpl implements NotificationApiService {

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
