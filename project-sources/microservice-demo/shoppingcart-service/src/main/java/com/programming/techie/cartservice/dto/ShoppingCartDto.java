package com.programming.techie.cartservice.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShoppingCartDto {

	private Date lastUpdatedAt;
	
	private List<ShoppingCartItemDto> cartItems;

}
