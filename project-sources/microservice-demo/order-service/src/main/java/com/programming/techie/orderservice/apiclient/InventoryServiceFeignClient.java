package com.programming.techie.orderservice.apiclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.programming.techie.orderservice.dto.InventoryResponse;

/**
 * API Client for Inventory Service based on Spring Feign Client
 */

// If we specify "url" attribute then Feign uses this absolute URL without LoadBalancer.
@FeignClient(name = "mspt-inventory-service", url = "http://mspt-inventory-service:8082")

// If we do not specify "url" attribute then Feign uses Spring Cloud LoadBalancer to get service URL.
// The LoadBalancer cooperates with Eureka client which fetches the registered services.
// Therefore, the specified "name" must equal to the service name registered via Eureka. 
//@FeignClient(name = "mspt-inventory-service")
public interface InventoryServiceFeignClient
{
	@GetMapping(path = "/api/inventory", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<InventoryResponse> isInStock(@RequestParam("skuCode") List<String> skuCodes);
	
}
