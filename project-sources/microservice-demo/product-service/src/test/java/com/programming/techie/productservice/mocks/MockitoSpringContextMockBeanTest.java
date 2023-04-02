package com.programming.techie.productservice.mocks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.programming.techie.productservice.model.Product;
import com.programming.techie.productservice.repository.ProductRepository;
import com.programming.techie.productservice.service.ProductService;

/**
 * Just a simple example test for mockito usage
 * 
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MockitoSpringContextMockBeanTest
{
	@MockBean
	private ProductRepository productRepository;
	
	@Autowired
	private ProductService productService;

	
	public void init()
	{
		List<Product> products = new ArrayList<>();
		
		products.add( Product.builder().id("1").name("Test1").build() );
		products.add( Product.builder().id("2").name("Test2").build() );
		
		Mockito.when( productRepository.findAll() ).thenReturn( products );
	}
	
	@Test
	public void testAppWithMockito1()
	{
		init();
		
		assertTrue(productService.getAllProducts().size() == 2, "Invalid data size.");
	}
	
	@Test
	public void testAppWithMockito2()
	{
		init();
		
		assertTrue(productRepository.findAll().size() == 2, "Invalid data size.");
	}

}
