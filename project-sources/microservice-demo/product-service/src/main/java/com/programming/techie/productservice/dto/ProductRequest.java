package com.programming.techie.productservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

	private String name;
	
	private String description;
	
	private BigDecimal price;
	
	private String skuCode;
	
}
