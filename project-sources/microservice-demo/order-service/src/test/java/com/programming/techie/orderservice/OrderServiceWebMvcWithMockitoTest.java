package com.programming.techie.orderservice;

import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.techie.orderservice.controller.OrderController;
import com.programming.techie.orderservice.dto.OrderLineItemsDto;
import com.programming.techie.orderservice.dto.OrderRequest;
import com.programming.techie.orderservice.model.Order;
import com.programming.techie.orderservice.repository.OrderRepository;
import com.programming.techie.orderservice.service.OrderService;

//Add Mockito Extension for JUnit
@ExtendWith(MockitoExtension.class)

// Initialize Spring Test Context only with the given classes
@ContextConfiguration(classes = {OrderController.class, OrderService.class})

//Do not start the whole Spring Boot Application.
//Instead, we only test the Web MVC with the given Controller(s).
// It also auto-configures the MockMvc in order to mock the HTTP/MVC layer without starting up real HTTP server.
@WebMvcTest( controllers = {OrderController.class} )
public class OrderServiceWebMvcWithMockitoTest
{
	// inject the MockMvc that is created by the @WebMvcTest annotation
	@Autowired
	private MockMvc mockMvc;
	
	// create Mock object and add it into the Spring Test Context
	@MockBean
	private OrderRepository orderRepository;
	
	// JSON object converter
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	public void init()
	{
		// when the OrderRepository.save() method called with any Order object
		// then we return/answer exactly this same object as return value.
		Mockito.when( orderRepository.save( any(Order.class) ))
			   .thenAnswer( i -> i.getArguments()[0] );
	}
	
	@Test
	public void test() throws Exception
	{
		mockMvc.perform(
			// create POST request with the order request content
			MockMvcRequestBuilders.post("/api/order")
			.contentType(MediaType.APPLICATION_JSON)
			.content(createOrderRequestJsonContent())
		)
		// the response HTTP status should be CREATED (201)
		.andExpect(MockMvcResultMatchers.status().isCreated());
	}
	
	private String createOrderRequestJsonContent() throws JsonProcessingException
	{
		List<OrderLineItemsDto> items = new ArrayList<>();
		
		OrderLineItemsDto item = OrderLineItemsDto.builder()
			.skuCode("L2300PT")
			.price(BigDecimal.valueOf(1000))
			.quantity(2)
		.build();
		
		items.add(item);
		
		OrderRequest order = OrderRequest.builder()
			.orderLineItemsDtoList(items)
		.build();
		
		return objectMapper.writeValueAsString(order);
	}

}
