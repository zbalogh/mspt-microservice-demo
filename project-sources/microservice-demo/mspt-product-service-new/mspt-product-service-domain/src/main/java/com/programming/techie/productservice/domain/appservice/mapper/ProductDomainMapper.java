package com.programming.techie.productservice.domain.appservice.mapper;

import org.springframework.stereotype.Component;

import com.programming.techie.productservice.domain.appservice.dto.ProductCreateRequest;
import com.programming.techie.productservice.domain.appservice.dto.ProductResponse;
import com.programming.techie.productservice.domain.appservice.dto.ProductUpdateRequest;
import com.programming.techie.productservice.domain.core.entity.Product;

@Component
public class ProductDomainMapper
{
	public ProductResponse mapToProductResponse(Product p)
	{
		return ProductResponse.builder()
				.id(p.getId())
				.name(p.getName())
				.description(p.getDescription())
				.price(p.getPrice())
				.skuCode(p.getSkuCode())
		.build();
	}
	
	public Product mapFromProductCreateRequest(ProductCreateRequest productCreateRequest)
	{
		Product product = Product.builder()
				.name(productCreateRequest.getName())
				.description(productCreateRequest.getDescription())
				.price(productCreateRequest.getPrice())
				.skuCode(productCreateRequest.getSkuCode())
		.build();
		
		return product;
	}
	
	public Product mapFromProductUpdateRequest(ProductUpdateRequest productUpdateRequest)
	{
		Product product = Product.builder()
				.id(productUpdateRequest.getId())
				.name(productUpdateRequest.getName())
				.description(productUpdateRequest.getDescription())
				.price(productUpdateRequest.getPrice())
				.skuCode(productUpdateRequest.getSkuCode())
		.build();
		
		return product;
	}

}
