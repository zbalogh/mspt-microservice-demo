package com.programming.techie.orderservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineItemsDto
{
	private Long id;
	
	private String skuCode;
	
	private BigDecimal price;
	
	private Integer quantity;
	
}
