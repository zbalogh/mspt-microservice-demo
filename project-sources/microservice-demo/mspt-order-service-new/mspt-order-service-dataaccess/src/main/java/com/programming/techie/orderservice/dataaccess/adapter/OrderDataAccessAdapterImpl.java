package com.programming.techie.orderservice.dataaccess.adapter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.programming.techie.orderservice.dataaccess.entity.OrderEntity;
import com.programming.techie.orderservice.dataaccess.mapper.OrderDataAccessMapper;
import com.programming.techie.orderservice.dataaccess.repository.OrderJpaRepository;
import com.programming.techie.orderservice.domain.appservice.outputports.repository.OrderDataAccess;
import com.programming.techie.orderservice.domain.core.entity.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class OrderDataAccessAdapterImpl implements OrderDataAccess
{
	private final OrderJpaRepository orderJpaRepository;
	
	private final OrderDataAccessMapper orderDataAccessMapper;
	
	@Override
	public Order insert(Order order)
	{
		OrderEntity entity = orderDataAccessMapper.mapToOrderEntity(order);
		
		orderJpaRepository.save(entity);
		
		return orderDataAccessMapper.mapFromOrderEntity(entity);
	}

	@Override
	public List<Order> findByUserid(String userid)
	{
		List<OrderEntity> entities = orderJpaRepository.findByUserid(userid);
		
		if (entities == null) {
			// in that case we just return empty collection
			return Collections.emptyList();
		}
		
		return entities.stream()
				.map( orderDataAccessMapper::mapFromOrderEntity )
				.collect(Collectors.toList());
	}

	@Override
	public long count()
	{
		return orderJpaRepository.count();
	}

}
