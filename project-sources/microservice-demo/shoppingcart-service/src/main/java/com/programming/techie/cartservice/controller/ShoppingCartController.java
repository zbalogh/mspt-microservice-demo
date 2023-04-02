package com.programming.techie.cartservice.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programming.techie.cartservice.dto.ShoppingCartDto;
import com.programming.techie.cartservice.service.ShoppingCartService;
import com.programming.techie.cartservice.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartController {
	
	// Injected via constructor
	private final ShoppingCartService shoppingCartService;

	@GetMapping(
		value = "",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<ShoppingCartDto> getMyShoppingCart(HttpServletRequest request)
	{
		// read user from the bearer token
		String userNameFromToken = JwtUtil.getUserNameFromBearerToken(request);
		log.info("Username from the bearer token: " + userNameFromToken);
		
		if (userNameFromToken == null || userNameFromToken.isEmpty()) {
			// no user from the bearer token
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		// get the shopping cart for the logged in user.
		// NOTE: if no cart found, it returns an empty cart object.
		ShoppingCartDto cartDto = shoppingCartService.getShoppingCart(userNameFromToken);
		
		return new ResponseEntity<ShoppingCartDto>(cartDto, HttpStatus.OK);
	}
	
	@PostMapping(
			value = "",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<ShoppingCartDto> saveMyShoppingCart(@RequestBody ShoppingCartDto cartDto, HttpServletRequest request)
	{
		if (cartDto == null) {
			// invalid request
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// read user from the bearer token
		String userNameFromToken = JwtUtil.getUserNameFromBearerToken(request);
		log.info("Username from the bearer token: " + userNameFromToken);

		if (userNameFromToken == null || userNameFromToken.isEmpty()) {
			// no user from the bearer token
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		// save the shopping cart for the given user
		cartDto = shoppingCartService.saveShoppingCart(cartDto, userNameFromToken);
		
		return new ResponseEntity<ShoppingCartDto>(cartDto, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "")
	public ResponseEntity<Void> clearMyShoppingCart(HttpServletRequest request)
	{
		// read user from the bearer token
		String userNameFromToken = JwtUtil.getUserNameFromBearerToken(request);
		log.info("Username from the bearer token: " + userNameFromToken);

		if (userNameFromToken == null || userNameFromToken.isEmpty()) {
			// no user from the bearer token
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		// delete the shopping cart for the given user
		shoppingCartService.deleteShoppingCart(userNameFromToken);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
