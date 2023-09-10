package com.programming.techie.orderservice.domain.appservice.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.programming.techie.orderservice.domain.appservice.dto.InventoryResponse;
import com.programming.techie.orderservice.domain.appservice.dto.OrderCreatedResponse;
import com.programming.techie.orderservice.domain.appservice.dto.OrderRequest;
import com.programming.techie.orderservice.domain.appservice.dto.OrderResponse;
import com.programming.techie.orderservice.domain.appservice.inputports.service.OrderApplicationService;
import com.programming.techie.orderservice.domain.appservice.mapper.OrderDomainMapper;
import com.programming.techie.orderservice.domain.appservice.outputports.api.InventoryApiService;
import com.programming.techie.orderservice.domain.appservice.outputports.api.NotificationApiService;
import com.programming.techie.orderservice.domain.appservice.outputports.repository.OrderDataAccess;
import com.programming.techie.orderservice.domain.core.entity.Order;
import com.programming.techie.orderservice.domain.core.exception.ProductNotAvailableInStockException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
class OrderApplicationServiceImpl implements OrderApplicationService {

	private final OrderDomainMapper orderDomainMapper;
	
	private final OrderDataAccess orderDataAccess;
	
	private final NotificationApiService notificationService;
	
	private final InventoryApiService inventoryService;

	@Override
	public OrderCreatedResponse placeOrder(OrderRequest orderRequest)
	{
		// create Order object from OrderRequest
		final Order order = orderDomainMapper.mapFromOrderRequest(orderRequest);
		
		// Call Inventory Service to check if the product is available in the stock
		boolean inventoryResult = checkInventoryForProducts(order);
		
		if (!inventoryResult) {
			log.error("Product is not in the stock. Cancel the order process by throwing an exception.");
			
			throw new ProductNotAvailableInStockException("Product is not in the stock, please try again later.");
		}
		
		// Validate and initialize the Order
		order.validateOrder();
		order.initializeOrder();
		
		log.info("Saving order in the repository.");
		
		Order responseOrder = orderDataAccess.insert(order);
		
		log.info("Sending message to Notification Service.");
		
		notificationService.sendOrderPlacedNotification(responseOrder);
		
		// build order created response
		OrderCreatedResponse orderCreatedResponse = OrderCreatedResponse.builder()
				.orderNumber(order.getOrderNumber())
				.build();
		
		return orderCreatedResponse;
	}
	
	private boolean checkInventoryForProducts(Order order)
	{
		// collect skuCodes from all order line items
		List<String> skuCodes = order.getOrderLineItemsList()
				.stream()
				.map(i -> i.getSkuCode())
				.collect(Collectors.toList());
		
		// Call Inventory Service to check if each product is available in the stock
		List<InventoryResponse> inventoryResponseList = inventoryService.isInStock(skuCodes);
		
		// check if all products are available in the inventory ('isInStock') 
		boolean allProductsInStock = inventoryResponseList.stream()
											.allMatch(inventoryResponse -> inventoryResponse.isInStock());
		
		return allProductsInStock;
	}

	@Override
	public List<OrderResponse> getOrdersByUserId(String userId)
	{
		if (userId == null || userId.isEmpty()) {
			//if no userId, then we just use empty list. We do not throw error.
			log.warn("No userId specified for getOrdersByUserId() method, so we return empty list.");
			return Collections.emptyList();
		}
		
		log.info("Getting the orders for user {}", userId);
		
		List<Order> orders = orderDataAccess.findByUserid(userId);
		
		if (orders == null) {
			// do not use null. Instead, we use empty list.
			return Collections.emptyList();
		}
		
		return maptoOrderResponseList(orders);
	}
	
	private List<OrderResponse> maptoOrderResponseList(List<Order> orders)
	{
		List<OrderResponse> orderListDto = orders.stream()
				.map(orderDomainMapper::mapToOrderResponse)
				.sorted(dateComparator)
				.collect(Collectors.toList());
		
		return orderListDto;
	}
	
	/**
	 * Compare orders by date
	 */
	private static Comparator<OrderResponse> dateComparator = (o1, o2) ->
	{
		if (o1.getOrderCreatedAt() == null || o2.getOrderCreatedAt() == null) {
			// null value detected for date field, so unable to compare
			return 0;
		}
		// reverse order: the newest/latest orders are above in the result.
		// if 'o1' order is older (before) than 'o2' order, then we return positive integer, otherwise negative.
		return o1.getOrderCreatedAt().before(o2.getOrderCreatedAt()) ? 1 : -1;
	};

}
