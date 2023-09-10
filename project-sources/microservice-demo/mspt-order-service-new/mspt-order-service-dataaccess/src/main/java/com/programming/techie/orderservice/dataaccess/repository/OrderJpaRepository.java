package com.programming.techie.orderservice.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programming.techie.orderservice.dataaccess.entity.OrderEntity;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

	List<OrderEntity> findByUserid(String userid);
	
}
