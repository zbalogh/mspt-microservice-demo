package com.programming.techie.productservice.infrastructure.dataaccess.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.programming.techie.productservice.domain.appservice.outputports.repository.ProductDataAccess;
import com.programming.techie.productservice.domain.core.entity.Product;
import com.programming.techie.productservice.infrastructure.dataaccess.entity.ProductEntity;
import com.programming.techie.productservice.infrastructure.dataaccess.mapper.ProductDataAccessMapper;
import com.programming.techie.productservice.infrastructure.dataaccess.repository.ProductMongoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class ProductDataAccessAdapterImpl implements ProductDataAccess {

	private final ProductMongoRepository productMongoRepository;
	
	private final ProductDataAccessMapper productDataAccessMapper;
	
	@Override
	public Product create(Product product)
	{
		ProductEntity entity = productDataAccessMapper.mapToProductEntity(product);
		
		entity = productMongoRepository.save(entity);
		
		return productDataAccessMapper.mapFromProductEntity(entity);
	}
	
	@Override
	public Product update(Product product)
	{
		Optional<ProductEntity> optional = productMongoRepository.findById(product.getId());
		
		if (!optional.isPresent()) {
			throw new IllegalArgumentException("Product not found.");
		}
		
		ProductEntity entity = optional.get();
		
		entity = productDataAccessMapper.updateProductEntityAttributes(entity, product);
		
		entity = productMongoRepository.save(entity);
		
		return productDataAccessMapper.mapFromProductEntity(entity);
	}

	@Override
	public Optional<Product> findById(String id)
	{
		Optional<ProductEntity> optional = productMongoRepository.findById(id);
		
		if (!optional.isPresent()) {
			throw new IllegalArgumentException("Product not found.");
		}
		
		return optional.map( productDataAccessMapper::mapFromProductEntity );
	}

	@Override
	public void delete(String id)
	{
		Optional<ProductEntity> optional = productMongoRepository.findById(id);
		
		if (!optional.isPresent()) {
			throw new IllegalArgumentException("Product not found.");
		}
		
		ProductEntity entity = optional.get();
		
		productMongoRepository.delete(entity);
	}
	
	@Override
	public void delete(Product product)
	{
		this.delete(product.getId());
	}

	@Override
	public List<Product> findAll()
	{
		List<ProductEntity> productEntities = productMongoRepository.findAll();
		
		return productEntities.stream()
			.map( productDataAccessMapper::mapFromProductEntity )
			.collect(Collectors.toList());
	}

}
