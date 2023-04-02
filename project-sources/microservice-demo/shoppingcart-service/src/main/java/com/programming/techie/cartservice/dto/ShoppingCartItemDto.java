package com.programming.techie.cartservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShoppingCartItemDto {

	private String productId;
	
	private String productName;
	
	private String skuCode;
	
	private BigDecimal price;
	
	private Integer quantity;

}
