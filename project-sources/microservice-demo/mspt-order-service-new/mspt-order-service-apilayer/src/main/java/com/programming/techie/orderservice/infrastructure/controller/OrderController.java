package com.programming.techie.orderservice.infrastructure.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programming.techie.common.jwt.JwtUtil;
import com.programming.techie.orderservice.domain.appservice.dto.OrderCreatedResponse;
import com.programming.techie.orderservice.domain.appservice.dto.OrderRequest;
import com.programming.techie.orderservice.domain.appservice.dto.OrderResponse;
import com.programming.techie.orderservice.domain.appservice.inputports.service.OrderApplicationService;
import com.programming.techie.orderservice.domain.core.exception.ProductNotAvailableInStockException;

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
	private final OrderApplicationService orderService;
	
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
				OrderCreatedResponse orderCreatedResponse = orderService.placeOrder(orderRequest);
				
				// if no error, then we return the order created response
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
		
		List<OrderResponse> orderListDto = orderService.getOrdersByUserId(userNameFromToken);
		
		// return response
		return new ResponseEntity<>(orderListDto, HttpStatus.OK);
	}
	
}
