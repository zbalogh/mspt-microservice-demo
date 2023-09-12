package com.programming.techie.orderservice.domain.appservice.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.programming.techie.orderservice.domain.appservice.dto.OrderLineItemsDto;
import com.programming.techie.orderservice.domain.appservice.dto.OrderRequest;
import com.programming.techie.orderservice.domain.appservice.dto.OrderResponse;
import com.programming.techie.orderservice.domain.core.entity.Order;
import com.programming.techie.orderservice.domain.core.entity.OrderLineItems;

@Component
public class OrderDomainMapper {

	/**
	 * Helper method to map OrderRequest to Order object along with its order items.
	 * 
	 * @param orderRequest
	 * @return
	 */
	public Order mapFromOrderRequest(OrderRequest orderRequest)
	{
		// create Order object
		final Order order = Order.builder()
				//.orderNumber(UUID.randomUUID().toString())
				//.orderCreatedAt(new Date())
				.userid(orderRequest.getUserid())
		.build();
		
		// get order line items from the DTO
		final List<OrderLineItems> items = orderRequest.getOrderLineItemsDtoList()
			.stream()
			.map(dto -> mapFromOrderLineItemsDto(dto))
			.collect(Collectors.toList());

		// add order items list
		order.setOrderLineItemsList(items);
		
		return order;
	}

	private OrderLineItems mapFromOrderLineItemsDto(OrderLineItemsDto dto)
	{
		return OrderLineItems.builder()
				.price(dto.getPrice())
				.skuCode(dto.getSkuCode())
				.quantity(dto.getQuantity())
		.build();
	}
	
	/**
	 * Helper method to map Order to OrderResponse DTO object along with its order items.
	 * 
	 * @param order
	 * @return
	 */
	public OrderResponse mapToOrderResponse(Order order)
	{
		OrderResponse orderDto = OrderResponse.builder()
									.id(order.getId())
									.orderNumber(order.getOrderNumber())
									.userid(order.getUserid())
									.orderCreatedAt(order.getOrderCreatedAt())
								.build();
		
		List<OrderLineItems> orderItems = order.getOrderLineItemsList();
		
		if (orderItems == null || orderItems.isEmpty()) {
			// no order items, set empty list
			orderDto.setOrderLineItemsDtoList(Collections.emptyList());
		}
		else {
			// map order items to list of DTO
			List<OrderLineItemsDto> orderItemsDto = orderItems.stream()
				.map(item -> {
					return OrderLineItemsDto.builder()
						.id(item.getId())
						.skuCode(item.getSkuCode())
						.price(item.getPrice())
						.quantity(item.getQuantity())
					.build();
				})
				.collect(Collectors.toList());
			// set order items
			orderDto.setOrderLineItemsDtoList(orderItemsDto);
		}
		
		return orderDto;
	}

}
