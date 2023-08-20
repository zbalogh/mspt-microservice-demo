package com.programming.techie.productservice.domain.appservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse
{
	private String id;
	
	private String name;
	
	private String description;
	
	private BigDecimal price;
	
	private String skuCode;
}
