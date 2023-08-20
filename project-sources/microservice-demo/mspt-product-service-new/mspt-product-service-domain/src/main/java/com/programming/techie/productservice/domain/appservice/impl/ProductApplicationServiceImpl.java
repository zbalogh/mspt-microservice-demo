package com.programming.techie.productservice.domain.appservice.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.programming.techie.productservice.domain.appservice.dto.ProductCreateRequest;
import com.programming.techie.productservice.domain.appservice.dto.ProductResponse;
import com.programming.techie.productservice.domain.appservice.dto.ProductUpdateRequest;
import com.programming.techie.productservice.domain.appservice.inputports.service.ProductApplicationService;
import com.programming.techie.productservice.domain.appservice.mapper.ProductDomainMapper;
import com.programming.techie.productservice.domain.appservice.outputports.repository.ProductDataAccess;
import com.programming.techie.productservice.domain.core.entity.Product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
class ProductApplicationServiceImpl implements ProductApplicationService
{
	private final ProductDataAccess productDataAccess;
	
	private final ProductDomainMapper productDomainMapper;
	
	@Override
	public ProductResponse createProduct(ProductCreateRequest productRequest)
	{
		Product product = productDomainMapper.mapFromProductCreateRequest(productRequest);
		
		product = productDataAccess.create(product);
		
		log.info("Product {} has been created.", product.getId());
		
		return productDomainMapper.mapToProductResponse(product);
	}
	
	@Override
	public ProductResponse updateProduct(ProductUpdateRequest productUpdateRequest)
	{
		Product product = productDomainMapper.mapFromProductUpdateRequest(productUpdateRequest);
		
		product = productDataAccess.update(product);
		
		log.info("Product {} has been updated.", product.getId());
		
		return productDomainMapper.mapToProductResponse(product);
		
	}
	
	@Override
	public void deleteProduct(String id)
	{
		productDataAccess.delete(id);
		
		log.info("Product {} has been deleted.", id);
	}
	
	@Override
	public List<ProductResponse> getAllProducts()
	{
		log.info("getAllProducts() called.");
		
		List<Product> products = productDataAccess.findAll();
		
		return products.stream()
				//.map(p -> productDomainMapper.mapToProductResponse(p))
				.map(productDomainMapper::mapToProductResponse)
				.collect(Collectors.toList());
	}
	
	@Override
	public ProductResponse getProductById(String id)
	{
		log.info("getProductById() called with parameter id=" + id);
		
		Optional<Product> p = productDataAccess.findById(id);
		
		if (!p.isPresent()) {
			throw new IllegalArgumentException("Product not found.");
		}
		
		Product product = p.get();
		
		return productDomainMapper.mapToProductResponse(product);
	}
	
}
