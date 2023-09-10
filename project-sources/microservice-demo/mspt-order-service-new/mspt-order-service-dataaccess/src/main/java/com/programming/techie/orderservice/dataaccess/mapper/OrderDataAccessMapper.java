package com.programming.techie.orderservice.dataaccess.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.programming.techie.orderservice.dataaccess.entity.OrderEntity;
import com.programming.techie.orderservice.dataaccess.entity.OrderLineItemsEntity;
import com.programming.techie.orderservice.domain.core.entity.Order;
import com.programming.techie.orderservice.domain.core.entity.OrderLineItems;

@Component
public class OrderDataAccessMapper {

	/**
	 * Map the given Order domain object to OrderEntity.
	 * 
	 * @param order
	 * @return
	 */
	public OrderEntity mapToOrderEntity(Order order)
	{
		OrderEntity orderEntity = OrderEntity.builder()
				.id(order.getId())
				.orderNumber(order.getOrderNumber())
				.orderCreatedAt(order.getOrderCreatedAt())
				.userid(order.getUserid())
		.build();
		
		orderEntity.setOrderLineItemsList( mapToOrderLineItemsEntityList(order.getOrderLineItemsList(), orderEntity) );
		
		return orderEntity;
	}
	
	private List<OrderLineItemsEntity> mapToOrderLineItemsEntityList(List<OrderLineItems> orderLineItemsList, OrderEntity orderEntity)
	{
		if (orderLineItemsList == null) {
			// prefer using empty list
			return Collections.emptyList();
		}
		
		return orderLineItemsList.stream()
			.map(item -> mapToOrderLineItemsEntity(item, orderEntity) )
			.collect(Collectors.toList());
	}

	private OrderLineItemsEntity mapToOrderLineItemsEntity(OrderLineItems item, OrderEntity orderEntity)
	{
		return OrderLineItemsEntity.builder()
				.id(item.getId())
				.skuCode(item.getSkuCode())
				.price(item.getPrice())
				.quantity(item.getQuantity())
				.order(orderEntity)
		.build();
	}
	

	/**
	 * Map from OrderEntity to Order domain object.
	 * 
	 * @param orderEntity
	 * @return
	 */
	public Order mapFromOrderEntity(OrderEntity orderEntity)
	{
		Order order = Order.builder()
				.id(orderEntity.getId())
				.orderNumber(orderEntity.getOrderNumber())
				.orderCreatedAt(orderEntity.getOrderCreatedAt())
				.userid(orderEntity.getUserid())
		.build();
		
		order.setOrderLineItemsList( mapFromOrderLineItemsEntityList(orderEntity.getOrderLineItemsList()) );
		
		return order;
	}

	private List<OrderLineItems> mapFromOrderLineItemsEntityList(List<OrderLineItemsEntity> orderLineItemsList)
	{
		if (orderLineItemsList == null) {
			// prefer using empty list
			return Collections.emptyList();
		}
		
		return orderLineItemsList.stream()
			.map(item -> mapFromOrderLineItemsEntity(item) )
			.collect(Collectors.toList());
	}

	private OrderLineItems mapFromOrderLineItemsEntity(OrderLineItemsEntity item)
	{
		return OrderLineItems.builder()
				.id(item.getId())
				.skuCode(item.getSkuCode())
				.price(item.getPrice())
				.quantity(item.getQuantity())
		.build();
	}
	
}
