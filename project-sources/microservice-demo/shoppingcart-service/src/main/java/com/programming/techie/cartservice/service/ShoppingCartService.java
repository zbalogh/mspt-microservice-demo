package com.programming.techie.cartservice.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.programming.techie.cartservice.dto.ShoppingCartDto;
import com.programming.techie.cartservice.mapper.ShoppingCartMapper;
import com.programming.techie.cartservice.model.ShoppingCart;
import com.programming.techie.cartservice.repository.ShoppingCartRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartService {

	private final ShoppingCartRepository shoppingCartRepository;
	
	private final ShoppingCartMapper shoppingCartMapper;
	
	/**
	 * It returns the shopping cart for the given user.
	 * If no cart found, then it returns an empty cart.
	 * 
	 * @param userid
	 * @return
	 */
	public ShoppingCartDto getShoppingCart(String userid)
	{
		log.info("getMyShoppingCart() called for user " + userid);
		
		// find shopping cart for the given key (userid)
		Optional<ShoppingCart> cartOptional = shoppingCartRepository.findById(userid);
		
		if (cartOptional.isPresent()) {
			// found shopping cart
			ShoppingCart cart = cartOptional.get();
			
			// map Cart to DTO
			return shoppingCartMapper.mapShoppingCartToDto(cart);
		}
		else {
			// no shopping cart found for the user, so we return an empty cart
			return shoppingCartMapper.createEmptyShoppingCartDto();
		}
	}
	
	/**
	 * Save the Shopping Cart for the given user.
	 * 
	 * @param cartDto
	 * @param userid
	 * @return
	 */
	public ShoppingCartDto saveShoppingCart(ShoppingCartDto cartDto, String userid)
	{
		log.info("saveShoppingCart() called for user " + userid);
		
		ShoppingCart cart = shoppingCartMapper.mapShoppingCartFromDto(cartDto);

		// set user and lastUpdate
		cart.setUserid(userid);
		cart.setLastUpdatedAt(new Date());
		
		// save the shopping cart
		cart = shoppingCartRepository.save(cart);
		
		// map Cart to DTO
		return shoppingCartMapper.mapShoppingCartToDto(cart);
	}
	
	public void deleteShoppingCart(String userid)
	{
		log.info("deleteShoppingCart() called for user " + userid);
		
		// delete the shopping cart for the user
		shoppingCartRepository.deleteById(userid);
	}

}
