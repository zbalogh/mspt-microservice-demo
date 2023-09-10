package com.programming.techie.orderservice.domain.appservice.outputports.api;

import com.programming.techie.orderservice.domain.core.entity.Order;

public interface NotificationApiService {

	public void sendOrderPlacedNotification(Order order);
	
}
