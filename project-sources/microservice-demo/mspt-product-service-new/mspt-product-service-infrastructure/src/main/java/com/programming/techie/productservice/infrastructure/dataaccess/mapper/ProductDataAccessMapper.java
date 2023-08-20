package com.programming.techie.productservice.infrastructure.dataaccess.mapper;

import org.springframework.stereotype.Component;

import com.programming.techie.productservice.domain.core.entity.Product;
import com.programming.techie.productservice.infrastructure.dataaccess.entity.ProductEntity;

@Component
public class ProductDataAccessMapper {

	public ProductEntity mapToProductEntity(Product p)
	{
		return ProductEntity.builder()
				.id(p.getId())
				.name(p.getName())
				.description(p.getDescription())
				.price(p.getPrice())
				.skuCode(p.getSkuCode())
		.build();
	}
	
	public Product mapFromProductEntity(ProductEntity productEntity)
	{
		return Product.builder()
				.id(productEntity.getId())
				.name(productEntity.getName())
				.description(productEntity.getDescription())
				.price(productEntity.getPrice())
				.skuCode(productEntity.getSkuCode())
		.build();
	}
	
	public ProductEntity updateProductEntityAttributes(ProductEntity productEntity, Product product)
	{
		// skip ID field because we use this function to update an existing Product Entity
		productEntity.setName(product.getName());
		productEntity.setDescription(product.getDescription());
		productEntity.setPrice(product.getPrice());
		productEntity.setSkuCode(product.getSkuCode());
		
		return productEntity;
	}

}
