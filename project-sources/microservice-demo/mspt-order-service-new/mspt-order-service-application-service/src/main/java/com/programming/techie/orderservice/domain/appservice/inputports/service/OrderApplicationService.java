package com.programming.techie.orderservice.domain.appservice.inputports.service;

import java.util.List;

import com.programming.techie.orderservice.domain.appservice.dto.OrderCreatedResponse;
import com.programming.techie.orderservice.domain.appservice.dto.OrderRequest;
import com.programming.techie.orderservice.domain.appservice.dto.OrderResponse;

public interface OrderApplicationService {

	public OrderCreatedResponse placeOrder(OrderRequest orderRequest);
	
	public List<OrderResponse> getOrdersByUserId(String userId);
	
}
