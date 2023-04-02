package com.programming.techie.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programming.techie.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserid(String userid);
	
}
