package com.programming.techie.orderservice.domain.appservice.outputports.api;

import java.util.List;

import com.programming.techie.orderservice.domain.appservice.dto.InventoryResponse;

public interface InventoryApiService {

	public List<InventoryResponse> isInStock(List<String> skuCodes);
	
}
