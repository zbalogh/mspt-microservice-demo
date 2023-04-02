package com.programming.techie.cartservice.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.programming.techie.cartservice.dto.ShoppingCartDto;
import com.programming.techie.cartservice.dto.ShoppingCartItemDto;
import com.programming.techie.cartservice.model.ShoppingCart;
import com.programming.techie.cartservice.model.ShoppingCartItem;

@Component
public class ShoppingCartMapper {

	public ShoppingCartDto mapShoppingCartToDto(ShoppingCart cart)
	{
		// Define the collection for CartItems
		List<ShoppingCartItemDto> cartItems = new ArrayList<>();
		
		
		if (cart.getCartItems() != null) {
			// create ShoppingCartItemDto list
			cartItems = cart.getCartItems()
					.stream()
					//.map(cartItem -> mapShoppingCartItemToDto(cartItem))
					.map(this::mapShoppingCartItemToDto)
					.collect(Collectors.toList());
		}
		
		// create ShoppingCartDto
		return ShoppingCartDto.builder()
				.lastUpdatedAt(cart.getLastUpdatedAt())
				.cartItems(cartItems)
		.build();
	}
	
	private ShoppingCartItemDto mapShoppingCartItemToDto(ShoppingCartItem cartItem)
	{
		return ShoppingCartItemDto.builder()
				.productId(cartItem.getProductId())
				.productName(cartItem.getProductName())
				.skuCode(cartItem.getSkuCode())
				.price(cartItem.getPrice())
				.quantity(cartItem.getQuantity())
		.build();
	}
	
	public ShoppingCartDto createEmptyShoppingCartDto()
	{
		return ShoppingCartDto.builder()
				.lastUpdatedAt(new Date())
				.cartItems(Collections.emptyList())
		.build();
	}
	
	
	public ShoppingCart mapShoppingCartFromDto(ShoppingCartDto dto)
	{
		// Define the collection for CartItems
		List<ShoppingCartItem> cartItems = new ArrayList<>();
		
		if (dto.getCartItems() != null) {
			// create ShoppingCartItem list
			cartItems = dto.getCartItems()
					.stream()
					//.map(cartItem -> mapShoppingCartItemFromDto(cartItem))
					.map(this::mapShoppingCartItemFromDto)
					.collect(Collectors.toList());
		}
		
		// create ShoppingCartDto
		return ShoppingCart.builder()
				.lastUpdatedAt(dto.getLastUpdatedAt())
				.cartItems(cartItems)
		.build();
	}
	
	private ShoppingCartItem mapShoppingCartItemFromDto(ShoppingCartItemDto cartItem)
	{
		return ShoppingCartItem.builder()
				.productId(cartItem.getProductId())
				.productName(cartItem.getProductName())
				.skuCode(cartItem.getSkuCode())
				.price(cartItem.getPrice())
				.quantity(cartItem.getQuantity())
		.build();
	}

}
