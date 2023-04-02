package com.programming.techie.orderservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.techie.orderservice.dto.OrderLineItemsDto;
import com.programming.techie.orderservice.dto.OrderRequest;

/*
 * Full Spring Boot Application test with PostgreSQL database connectivity!
 */

// start up the whole Spring Boot Application and initialize the Spring Test Context
@SpringBootTest(classes = OrderServiceMSPTApplication.class)

// Enable and auto-configure MockMvc to mock the HTTP/MVC layer without starting up real HTTP server
@AutoConfigureMockMvc
public class OrderServiceSpringBootTest
{
	// inject the MockMvc that is created by the @AutoConfigureMockMvc annotation
	@Autowired
	private MockMvc mockMvc;
	
	// JSON object converter
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	public void init() {}
	
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
