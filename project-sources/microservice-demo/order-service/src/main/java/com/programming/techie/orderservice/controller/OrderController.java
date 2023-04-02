package com.programming.techie.orderservice.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programming.techie.orderservice.dto.OrderCreatedResponse;
import com.programming.techie.orderservice.dto.OrderLineItemsDto;
import com.programming.techie.orderservice.dto.OrderRequest;
import com.programming.techie.orderservice.dto.OrderResponse;
import com.programming.techie.orderservice.exception.ProductNotAvailableInStockException;
import com.programming.techie.orderservice.model.Order;
import com.programming.techie.orderservice.model.OrderLineItems;
import com.programming.techie.orderservice.service.OrderService;
import com.programming.techie.orderservice.util.JwtUtil;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController
{
	private final OrderService orderService;
	
	@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackPlaceOrder")
	@TimeLimiter(name = "inventory")
	//@PostMapping(value = "", produces = "text/plain;charset=UTF-8")
	@PostMapping(
			value = "",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public CompletableFuture<ResponseEntity<OrderCreatedResponse>> placeOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request)
	{
		// @TimeLimiter annotation requires "CompletableFuture" as response type because it uses 'asnyc' operation (see MVC async).
		CompletableFuture<ResponseEntity<OrderCreatedResponse>> future = CompletableFuture.supplyAsync(() ->
		{
			try {
				// read user from the bearer token
				String userNameFromToken = JwtUtil.getUserNameFromBearerToken(request);
				log.info("Username from the bearer token: " + userNameFromToken);
				
				if (userNameFromToken == null || userNameFromToken.isEmpty()) {
					// no user from the bearer token
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
				
				// override userId in the Order Request (we prefer user from the bearer token)
				orderRequest.setUserid(userNameFromToken);
				
				// place order
				Order order = orderService.placeOrder(orderRequest);
				
				// build order created response
				OrderCreatedResponse orderCreatedResponse = OrderCreatedResponse.builder()
																				.orderNumber(order.getOrderNumber())
																				.build();
				
				// if no error, then we return the order number (UUID)
				return new ResponseEntity<>(orderCreatedResponse, HttpStatus.CREATED);
			}
			// we want to handle this special type of exception because we want to send back message that no product available in the stock (inventory)
			catch (ProductNotAvailableInStockException ex)
			{
				log.error("Order is unsuccessful because no product available with the given quantity in the stock. | " + ex.getMessage());
				// error occurred while placing the order: no product is available in the stock (inventory)
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		});
		
		return future;
	}
	
	/**
	 * This fallback method is called if the circuit breaker is OPEN.
	 * It means when calling API service is failed (due to network or any other errors.)
	 */
	public CompletableFuture<ResponseEntity<OrderCreatedResponse>> fallbackPlaceOrder(OrderRequest orderRequest, HttpServletRequest request, Exception ex)
	{
		log.error("Error while calling Order API. We return error message with 503 status code.", ex);
		
		return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE) );
	}
	
	
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderResponse>> getMyOrders(HttpServletRequest request)
	{
		// read user from the bearer token
		String userNameFromToken = JwtUtil.getUserNameFromBearerToken(request);
		log.info("Username from the bearer token: " + userNameFromToken);
		
		if (userNameFromToken == null || userNameFromToken.isEmpty()) {
			// no user from the bearer token
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		List<Order> order = orderService.getOrdersByUserId(userNameFromToken);
		
		List<OrderResponse> orderListDto = order.stream()
												.map(this::mapToOrderResponse)
												.sorted(dateComparator)
												.collect(Collectors.toList());
		
		// return response
		return new ResponseEntity<>(orderListDto, HttpStatus.OK);
	}
	
	
	/**
	 * Helper method to transform Order entity to DTO object along with its order items.
	 * 
	 * @param order
	 * @return
	 */
	private OrderResponse mapToOrderResponse(Order order)
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
