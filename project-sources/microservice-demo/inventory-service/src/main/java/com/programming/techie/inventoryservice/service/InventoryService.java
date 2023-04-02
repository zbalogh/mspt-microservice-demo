package com.programming.techie.inventoryservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programming.techie.inventoryservice.dto.InventoryResponse;
import com.programming.techie.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService
{
	private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes)
    {
    	log.info("InventoryService.isInStock() method is invoked.");
    	
    	return inventoryRepository.findBySkuCodeIn(skuCodes)
        		.stream()
        		.map(inventory -> {
        			 return InventoryResponse.builder()
        				.skuCode(inventory.getSkuCode())
        				.isInStock(inventory.getQuantity() > 0)
        			.build();
        		})
        		.collect(Collectors.toList());
    }
    
}
