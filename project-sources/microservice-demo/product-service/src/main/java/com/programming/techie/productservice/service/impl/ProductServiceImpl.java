package com.programming.techie.productservice.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.programming.techie.productservice.dto.ProductRequest;
import com.programming.techie.productservice.dto.ProductResponse;
import com.programming.techie.productservice.dto.ProductUpdateRequest;
import com.programming.techie.productservice.model.Product;
import com.programming.techie.productservice.repository.ProductRepository;
import com.programming.techie.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService
{
	// Auto-wired via constructor
	private final ProductRepository productRepository;
	
	@Override
	public ProductResponse createProduct(ProductRequest productRequest)
	{
		Product product = Product.builder()
				.name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.skuCode(productRequest.getSkuCode())
		.build();
		
		product = productRepository.save(product);
		
		log.info("Product {} has been created.", product.getId());
		
		return mapToProductResponse(product);
	}
	
	@Override
	public ProductResponse updateProduct(ProductUpdateRequest productUpdateRequest)
	{
		Optional<Product> p = productRepository.findById(productUpdateRequest.getId());
		
		if (!p.isPresent()) {
			throw new IllegalArgumentException("Product not found.");
		}
		
		Product product = p.get();
		
		product.setName(productUpdateRequest.getName());
		product.setDescription(productUpdateRequest.getDescription());
		product.setPrice(productUpdateRequest.getPrice());
		product.setSkuCode(productUpdateRequest.getSkuCode());
		
		product = productRepository.save(product);
		
		log.info("Product {} has been updated.", product.getId());
		
		return mapToProductResponse(product);
		
	}
	
	@Override
	public void deleteProduct(String id)
	{
		Optional<Product> p = productRepository.findById(id);
		
		if (!p.isPresent()) {
			throw new IllegalArgumentException("Product not found.");
		}
		
		Product product = p.get();
		
		productRepository.delete(product);
		
		log.info("Product {} has been deleted.", product.getId());
	}
	
	@Override
	public List<ProductResponse> getAllProducts()
	{
		log.info("getAllProducts() called.");
		
		List<Product> products = productRepository.findAll();
		
		return products.stream()
				//.map(p -> mapToProductResponse(p))
				.map(this::mapToProductResponse)
				.collect(Collectors.toList());
	}
	
	@Override
	public ProductResponse getProductById(String id)
	{
		log.info("getProductById() called with parameter id=" + id);
		
		Optional<Product> p = productRepository.findById(id);
		
		if (!p.isPresent()) {
			throw new IllegalArgumentException("Product not found.");
		}
		
		Product product = p.get();
		
		return mapToProductResponse(product);
	}

	private ProductResponse mapToProductResponse(Product p)
	{
		return ProductResponse.builder()
				.id(p.getId())
				.name(p.getName())
				.description(p.getDescription())
				.price(p.getPrice())
				.skuCode(p.getSkuCode())
		.build();
	}

}
