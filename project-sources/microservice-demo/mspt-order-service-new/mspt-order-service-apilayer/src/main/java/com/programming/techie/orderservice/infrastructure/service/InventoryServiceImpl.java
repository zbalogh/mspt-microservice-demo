package com.programming.techie.orderservice.infrastructure.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.programming.techie.orderservice.domain.appservice.dto.InventoryResponse;
import com.programming.techie.orderservice.domain.appservice.outputports.api.InventoryApiService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class InventoryServiceImpl implements InventoryApiService
{
	private final WebClient.Builder webClientBuilder;
	
	public List<InventoryResponse> isInStock(List<String> skuCodes)
	{
		// The 'mspt-inventory-service' is the service name as registered in the Eureka. The LoadBalancer will fetch URLs from Eureka.
		// Please see the 'WebClientConfig' class for details how we configure WebClient and LoadBalancer.
		// NOTE: we do not use Eureka in Kubernetes, so we use directly the service name and port.
		String url = "http://mspt-inventory-service:8082/api/inventory";

		// Call Inventory Service to check if each product is available in the stock
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
			// call Inventory Service REST API with the given query parameter (skuCode)
			.uri(url, uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			// Convert the HTTP result body into Mono with the type of array 
			.bodyToMono(InventoryResponse[].class)
			// wait for the result (synchronous invocation)
			.block();
		
		return Arrays.asList(inventoryResponseArray);
	}
	
}
