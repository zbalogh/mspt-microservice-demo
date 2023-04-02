package com.programming.techie.orderservice.apiclient;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.programming.techie.orderservice.dto.InventoryResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceRestTemplateClient {
	
	private final RestTemplate restTemplate;

	public List<InventoryResponse> isInStock(List<String> skuCodes)
	{
		// The 'mspt-inventory-service' is the service name as registered in the Eureka. The LoadBalancer will fetch URLs from Eureka.
		// Please see the 'WebClientConfig' class for details how we configure WebClient and LoadBalancer.
		// NOTE: we do not use Eureka in Kubernetes, so we use directly the service name and port.
		String url = "http://mspt-inventory-service:8082/api/inventory";
		
		// call Inventory Service REST API with the given query parameter (skuCode)
		// "skuCode" has multiple value, so we need to build URI with the given query parameter.
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
			    .queryParam("skuCode", (Object[]) skuCodes.toArray(new String[0]));
		
		// build URI
		URI uri = builder.build().encode().toUri();
		
		// Call Inventory Service to check if each product is available in the stock
		InventoryResponse[] inventoryResponseArray = restTemplate.getForObject(uri, InventoryResponse[].class);
		
		return Arrays.asList(inventoryResponseArray);
	}
	
}
