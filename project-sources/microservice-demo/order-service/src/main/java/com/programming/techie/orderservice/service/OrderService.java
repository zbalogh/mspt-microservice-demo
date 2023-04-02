package com.programming.techie.orderservice.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programming.techie.orderservice.apiclient.InventoryServiceFeignClient;
import com.programming.techie.orderservice.apiclient.InventoryServiceRestTemplateClient;
import com.programming.techie.orderservice.apiclient.InventoryServiceWebClient;
import com.programming.techie.orderservice.dto.InventoryResponse;
import com.programming.techie.orderservice.dto.OrderLineItemsDto;
import com.programming.techie.orderservice.dto.OrderRequest;
import com.programming.techie.orderservice.exception.ProductNotAvailableInStockException;
import com.programming.techie.orderservice.model.Order;
import com.programming.techie.orderservice.model.OrderLineItems;
import com.programming.techie.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService
{
	private final OrderRepository orderRepository;
	
	private final InventoryServiceWebClient inventoryServiceWebClient;
	
	private final InventoryServiceFeignClient inventoryServiceFeignClient;
	
	private final InventoryServiceRestTemplateClient inventoryServiceRestTemplateClient;
	
	private final Tracer tracer;
	
	private final NotificationApiService notificationService;
	
	/**
	 * Place order service method to save order in the database.
	 */
	public Order placeOrder(OrderRequest orderRequest) throws IllegalArgumentException
	{	
		// create Order object
		final Order order = createOrderFromOrderRequestDto(orderRequest);
		
		// Start a new trace or a span within an existing trace representing an operation
		 Span span = tracer.nextSpan()
				 			.name("inventorylookup")
				 			.start();
		 
		 // Put the span in "scope" so that downstream code such as loggers can see trace IDs
		 // Using try-with-resources to auto-close the 'ws' scope resource. ---> https://www.baeldung.com/java-try-with-resources
		 try (Tracer.SpanInScope ws = tracer.withSpan(span))
		 {
			 // Call Inventory Service to check if the product is available in the stock
			boolean requestResult = callInventoryServiceWithWebClient(order);
			
			// if result is true, then we save the order
			if (requestResult)
			{
				log.info("Saving order in the repository.");
				
				Order responseOrder = orderRepository.save(order);
				
				log.info("Sending message to Notification Service.");
				
				notificationService.sendOrderPlacedNotification(responseOrder);
				
				return responseOrder;
			}
			else
			{
				log.error("Product is not in the stock. Cancel the order process by throwing an exception.");
				
				throw new ProductNotAvailableInStockException("Product is not in the stock, please try again later.");
			}
		 }
		 finally
		 {
			 // note the scope is independent of the span. Always finish a span.
			 span.end();
		 }
	}
	
	/**
	 * Find orders for the given userId. If no orders found, then it returns empty list.
	 * 
	 * @param userId
	 * @return
	 */
	public List<Order> getOrdersByUserId(String userId)
	{
		if (userId == null || userId.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<Order> orders = orderRepository.findByUserid(userId);
		
		if (orders == null) {
			return Collections.emptyList();
		}
		
		return orders;
	}
	
	
	private Order createOrderFromOrderRequestDto(OrderRequest orderRequest)
	{
		// create Order object
		final Order order = Order.builder()
				.orderNumber(UUID.randomUUID().toString())
				.orderCreatedAt(new Date())
				.userid(orderRequest.getUserid())
		.build();
		
		// get order line items from the DTO
		final List<OrderLineItems> items = orderRequest.getOrderLineItemsDtoList()
			.stream()
			.map(dto -> mapFromDto(dto))
			.map(item -> {
				item.setOrder(order);
				return item;
			})
			.collect(Collectors.toList());

		// add order items list
		order.setOrderLineItemsList(items);
		
		return order;
	}

	private OrderLineItems mapFromDto(OrderLineItemsDto dto)
	{
		return OrderLineItems.builder()
				.price(dto.getPrice())
				.skuCode(dto.getSkuCode())
				.quantity(dto.getQuantity())
		.build();
	}
	
	protected boolean callInventoryServiceWithWebClient(Order order)
	{
		// collect skuCodes from all order line items
		List<String> skuCodes = order.getOrderLineItemsList()
				.stream()
				.map(i -> i.getSkuCode())
				.collect(Collectors.toList());
		
		// Call Inventory Service to check if each product is available in the stock
		List<InventoryResponse> inventoryResponseList = inventoryServiceWebClient.isInStock(skuCodes);
		
		// check if all products are available in the inventory ('isInStock') 
		boolean allProductsInStock = inventoryResponseList.stream()
				.allMatch(inventoryResponse -> inventoryResponse.isInStock());
		
		return allProductsInStock;
	}
	
	protected boolean callInventoryServiceWithFeign(Order order)
	{
		// collect skuCodes from all order line items
		List<String> skuCodes = order.getOrderLineItemsList()
				.stream()
				.map(i -> i.getSkuCode())
				.collect(Collectors.toList());
		
		// call Inventory Service API by using FeignClient
		List<InventoryResponse> inventoryResponseList = inventoryServiceFeignClient.isInStock(skuCodes);
		
		// check if all products are available in the inventory ('isInStock') 
		boolean allProductsInStock = inventoryResponseList.stream()
				.allMatch(inventoryResponse -> inventoryResponse.isInStock());
		
		return allProductsInStock;
	}
	
	protected boolean callInventoryServiceWithRestTemplate(Order order)
	{
		// collect skuCodes from all order line items
		List<String> skuCodes = order.getOrderLineItemsList()
				.stream()
				.map(i -> i.getSkuCode())
				.collect(Collectors.toList());
		
		// call Inventory Service API by using FeignClient
		List<InventoryResponse> inventoryResponseList = inventoryServiceRestTemplateClient.isInStock(skuCodes);
		
		// check if all products are available in the inventory ('isInStock') 
		boolean allProductsInStock = inventoryResponseList.stream()
				.allMatch(inventoryResponse -> inventoryResponse.isInStock());
		
		return allProductsInStock;
	}
	
}
