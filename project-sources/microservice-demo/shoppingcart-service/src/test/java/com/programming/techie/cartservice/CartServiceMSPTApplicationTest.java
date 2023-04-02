package com.programming.techie.cartservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.programming.techie.cartservice.model.ShoppingCart;
import com.programming.techie.cartservice.model.ShoppingCartItem;
import com.programming.techie.cartservice.repository.ShoppingCartRepository;

import lombok.extern.slf4j.Slf4j;

//Spring Boot will be started with Spring context during the JUnit test running.
@SpringBootTest(
		classes = CartServiceMSPTApplication.class
)
@Slf4j
public class CartServiceMSPTApplicationTest
{	
	@Autowired
	protected ShoppingCartRepository shoppingCartRepository;

	
	@Test
	void contextLoads()
	{
		try {
			createShoppingCarts();
			
			listAllShoppingCarts();
			
			deleteAllShoppingCarts();
		}
		catch (Exception ex) {
			log.error("Exception", ex);
		}
	}
	
	/**
	 * TEST method to create some test data
	 */
	protected void createShoppingCarts()
	{
		List<ShoppingCartItem> cartItems = new ArrayList<>();
		
		ShoppingCartItem cartItem1 = ShoppingCartItem.builder()
													.productId("ac373289-6092-43d1-a455-522bdec9f43b")
													.productName("Samsung A32")
													.skuCode("samsung_a32")
													.price(new BigDecimal(750))
													.quantity(2)
												.build();
		cartItems.add(cartItem1);
		
		ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
													.productId("5c6e20b9-492a-4e0c-8057-66cf6278a90e")
													.productName("iPhone 13")
													.skuCode("iphone_13")
													.price(new BigDecimal(1200))
													.quantity(1)
												.build();
		cartItems.add(cartItem2);
		
		ShoppingCart cart1 = ShoppingCart.builder()
										.userid("spring-testuser")
										.lastUpdatedAt(new Date())
										.cartItems(cartItems)
									.build();
		
		shoppingCartRepository.save(cart1);
		log.info("ShoppingCart: Saved item in the REDIS cache: " + cart1);
	}
	
	/**
	 * TEST method to list the test data
	 */
	protected void listAllShoppingCarts()
	{
		Iterable<ShoppingCart> carts = shoppingCartRepository.findAll();
		
		Stream<ShoppingCart> stream = StreamSupport.stream(carts.spliterator(), false);
		
		stream
		.filter(c -> c != null)
		.forEach(c -> {
			log.info("ShoppingCart: Got item from REDIS cache: userId=" + c.getUserid() + ", " + c.getLastUpdatedAt() + " | CartItems: " + c.getCartItems());
		});
	}
	
	protected void deleteAllShoppingCarts()
	{
		shoppingCartRepository.deleteAll();
	}

}
