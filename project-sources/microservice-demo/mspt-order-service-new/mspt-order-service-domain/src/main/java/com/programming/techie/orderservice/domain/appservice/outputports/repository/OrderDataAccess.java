package com.programming.techie.orderservice.domain.appservice.outputports.repository;

import java.util.List;

import com.programming.techie.orderservice.domain.core.entity.Order;

public interface OrderDataAccess {

	public Order insert(Order order);
	
	public List<Order> findByUserid(String userid);
	
	public long count();
	
}
