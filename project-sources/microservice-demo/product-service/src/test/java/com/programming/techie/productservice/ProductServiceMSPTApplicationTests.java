package com.programming.techie.productservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.techie.productservice.dto.ProductRequest;
import com.programming.techie.productservice.repository.ProductRepository;


//Spring Boot will be started with Spring context during the JUnit test running.
@SpringBootTest(classes = ProductServiceMSPTApplication.class)

//The test containers extension finds all fields that are annotated with Container and calls their container lifecycle methods
@Testcontainers(disabledWithoutDocker = true)

//it enables and configures MockMvc by using auto-configuration.
@AutoConfigureMockMvc
public class ProductServiceMSPTApplicationTests
{
	// --------------------------------------------------------------------------------------------------------------------
	// https://spring.io/guides/gs/testing-web/   (This is very useful!)
	//
	// https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework
	//
	// https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/
	//
	// https://igorski.co/mockmvc-test-spring-boot/
	// --------------------------------------------------------------------------------------------------------------------
	
	// we use MongoDB docker container for testing.
	// it will initialize and create a temporary container inside docker.
	// this temporary container will be removed after the test finished.
	@Container
	private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	
	// mock the Spring MVC/Servlet: SpringBootTest will not start real HTTP server, instead it will mock it. 
	@Autowired
	private MockMvc mockMvc;
	
	// JSON object converter
	@Autowired
	private ObjectMapper objectMapper;
	
	// MongoDB repository to access products
	@Autowired
	private ProductRepository productRepository;
	
	
	// Must be static method. It dynamically configures the application properties.
	@DynamicPropertySource
	private static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry)
	{
		// add mongoDB connection URI to the properties (application context)
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getReplicaSetUrl());
	}
	
	
	@Test
	public void shouldCreateProduct() throws Exception
	{
		// create ProductRequest for POST request
		ProductRequest productrequest = getproductRequest();
		
		// convert ProductRequest to JSON data
		String content = objectMapper.writeValueAsString(productrequest);
		
		// perform POST request against our REST Controller
		// no real HTTP running. Instead, it uses the mocked Spring MVC
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
		)
		// the response HTTP status should be CREATED (201)
		.andExpect(MockMvcResultMatchers.status().isCreated());
		
		// check if there is entry in the MongoDB, so get entries.
		assertEquals(1, productRepository.findAll().size());
	}
	
	private ProductRequest getproductRequest()
	{
		return ProductRequest.builder()
			.name("Samsung A52")
			.description("Samsung A52 smart phone")
			.price(BigDecimal.valueOf(1250))
		.build();
	}

}
