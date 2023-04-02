package com.programming.techie.cartservice.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartItem implements Serializable {
	
	private static final long serialVersionUID = 1993431833872514541L;

	private String productId;
	
	private String productName;
	
	private String skuCode;
	
	private BigDecimal price;
	
	private Integer quantity;

}
